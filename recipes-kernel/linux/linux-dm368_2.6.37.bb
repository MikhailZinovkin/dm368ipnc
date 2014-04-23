DESCRIPTION = "Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
KERNEL_IMAGETYPE = "uImage"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "dm368ipnc"

S = "${WORKDIR}/git"

SRCREV = "f2b152564afdf9c9917c17d1c41c1082c82067bd"
SRC_URI = " \
    git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-2.6.37.y \
    file://defconfig \
"

# do_configure_prepend() {
#     install -m 0644 ${S}/arch/${ARCH}/configs/${KERNEL_DEFCONFIG} ${WORKDIR}/defconfig || die "No default configuration for ${MACHINE} / ${KERNEL_DEFCONFIG} available."
# }

