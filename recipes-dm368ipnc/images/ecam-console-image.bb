# Based on: https://github.com/gumstix/meta-gumstix-extras/blob/dylan/recipes-images/gumstix/gumstix-console-image.bb
DESCRIPTION = "An image with e-CAM56 37x GSTIX and TI DSP drivers"
LICENSE = "MIT"
PR = "r0"

IMAGE_FEATURES += "package-management ssh-server-openssh tools-sdk"
IMAGE_LINGUAS = "en-us"

inherit core-image

# Gumstix machines individually RDEPEND on the firware they need but we repeat
# it here as we might want to use the same image on multiple different machines.
FIRMWARE_INSTALL = " \
linux-firmware-sd8686 \
linux-firmware-sd8787 \
"

TOOLS_INSTALL = " \
alsa-utils \
systemd-analyze \
cpufrequtils \
gdbserver \
grep \
gzip \
iputils \
iw \
memtester \
nano \
ntp \
openssh-sftp \
openssh-sftp-server \
sudo \
tar \
tslib \
u-boot-mkimage \
u-boot-fw-utils \
vim \
wget \
zip \
media-ctl \
yavta \
v4l-utils \
mtd-utils-ubifs \
coreutils \
diffutils \
findutils \
less \
"

# List of [interesting] gst-plugins-good-* PACKAGES_DYNAMIC:
#   find ~/yocto/build/tmp/work/*/gst-plugins-good-*/packages-split/ -maxdepth 1 -type d | \
#      grep -Ev -- "-(dev|locale)" | sed 's#.*/##' | sort

ECAM_INSTALL = " \
xauth \
xserver-xorg \
ecam-driver \
ecam-services \
libgles-omap3 \
libgles-omap3-dev \
libgles-omap3-dbg \
libgles-omap3-es5 \
libgles1-mesa \
libgles1-mesa-dev \
libgles2-mesa \
libgles2-mesa-dev \
omap3-sgx-modules \
gstreamer-ti \
gst-ffmpeg \
gst-plugins-base-app \
gst-plugins-base-app-dev \
gst-plugins-good \
gst-plugins-good-avi \
gst-plugins-good-avi-dev \
gst-plugins-good-jpeg \
gst-plugins-good-jpeg-dev \
gst-plugins-good-multifile \
gst-plugins-good-multifile-dev \
gst-plugins-good-multipart \
gst-plugins-good-multipart-dev \
gst-plugins-good-video4linux2 \
gst-plugins-good-video4linux2-dev \
gst-meta-video \
gst-meta-audio \
gst-meta-debug \
opencv \
opencv-dev \
opencv-apps \
"

IMAGE_INSTALL += " \
packagegroup-cli-tools \
packagegroup-cli-tools-debug \
${FIRMWARE_INSTALL} \
${TOOLS_INSTALL} \
${ECAM_INSTALL} \
"

add_custom_smart_config() {
    smart --data-dir=${IMAGE_ROOTFS}/var/lib/smart channel --add gumstix type=rpm-md name="Gumstix Package Repository" baseurl=http://package-cache.s3-website-us-west-2.amazonaws.com/dev/ -y
}

set_gumstix_user() {
    echo "gumstix:x:500:" >> "${IMAGE_ROOTFS}/etc/group"
    # Password is "overo"
    echo "gumstix:VQ43An5F8LYqc:500:500:Gumstix User,,,:/home/gumstix:/bin/bash"  >> "${IMAGE_ROOTFS}/etc/passwd"

    install -d "${IMAGE_ROOTFS}/home/gumstix"
    cp -f "${IMAGE_ROOTFS}/etc/skel/.bashrc" "${IMAGE_ROOTFS}/etc/skel/.profile" "${IMAGE_ROOTFS}/home/gumstix"
    chown gumstix:gumstix -R "${IMAGE_ROOTFS}/home/gumstix"

    echo "%gumstix ALL=(ALL) ALL" >> "${IMAGE_ROOTFS}/etc/sudoers"
    chmod 0440 "${IMAGE_ROOTFS}/etc/sudoers"
    chmod u+s "${IMAGE_ROOTFS}/usr/bin/sudo"
}

enable_services() {
    install -d "${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants"

    ln -sf "/lib/systemd/system/ecam-driver.service" \
        "${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/ecam-driver.service"
    ln -sf "/lib/systemd/system/gstti-init.service" \
        "${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/gstti-init.service"
    ln -sf "/lib/systemd/system/usb-networking.service" \
        "${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/usb-networking.service"

    # First unmask it
    rm -f "${IMAGE_ROOTFS}/etc/systemd/system/networking.service"
    ln -sf "/lib/systemd/system/networking.service" \
        "${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/networking.service"

    # Enable Xorg forwarding with ssh
    sed -i 's/#X11Forwarding no/X11Forwarding yes/' "${IMAGE_ROOTFS}/etc/ssh/sshd_config"
}

create_rcS() {
    install -d "${IMAGE_ROOTFS}/etc/default"

    cat <<EOF > "${IMAGE_ROOTFS}/etc/default/rcS"
#
#	Defaults for the boot scripts in /etc/rcS.d. While this is for
#	sysvint, many services fail when this file does not exist.
#

# Time files in /tmp are kept in days.
TMPTIME=0
# Set to yes if you want sulogin to be spawned on bootup
SULOGIN=no
# Set to no if you want to be able to login over telnet/rlogin
# before system startup is complete (as soon as inetd is started)
DELAYLOGIN=no
# Assume that the BIOS clock is set to UTC time (recommended)
UTC=yes
# Set VERBOSE to "no" if you would like a more quiet bootup.
VERBOSE=no
# Set EDITMOTD to "no" if you don't want /etc/motd to be edited automatically
EDITMOTD=no
# Whether to fsck root on boot
ENABLE_ROOTFS_FSCK=no
# Set FSCKFIX to "yes" if you want to add "-y" to the fsck at startup.
FSCKFIX=yes
# Set TICKADJ to the correct tick value for this specific machine
#TICKADJ=10000
# Enable caching in populate-volatile.sh
VOLATILE_ENABLE_CACHE=yes
# Indicate whether the rootfs is intended to be read-only or not.
# Setting ROOTFS_READ_ONLY to yes and rebooting will give you a read-only rootfs.
# Normally you should not change this value.
ROOTFS_READ_ONLY=no
EOF
}

ROOTFS_POSTPROCESS_COMMAND =+ "set_gumstix_user ; add_custom_smart_config ; enable_services ; create_rcS ; "
