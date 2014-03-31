DESCRIPTION = "Camera driver for e-CAM56 37x GSTIX"
HOMEPAGE = "http://www.e-consystems.com/5MP-Gumstix-Camera.asp"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://inc_header.h;beginline=1;endline=7;md5=b816030b90f9adc8249d6d5d15f9aaef"
PR = "r1"

SRC_URI = "http://roadnarrows.com/distro/e-con/e-CAM56_37x_GSTIX/Software/e-CAM56_37x_GSTIX_LINUX_REL_2.0.zip \
    file://ecam-driver.service \
    file://load.sh \
    file://0001-Add-modules_install-target.patch"
SRC_URI[md5sum] = "4a4db65442b3473a1a360212eedb3a8d"
SRC_URI[sha256sum] = "18b77bfda359ef8796226fad42cdc6e62dc27f9fef1533c130a99ef2408c8a89"

S = "${WORKDIR}/e-CAM56_37x_GSTIX_LINUX_REL_2.0/Driver/Source"

inherit module

do_install_append() {
    install -d "${D}/usr/share/ecam"
    install -d "${D}/lib/systemd/system"
    install -m 0755 "${WORKDIR}/load.sh" "${D}/usr/share/ecam/load.sh"
    install -m 0644 "${WORKDIR}/ecam-driver.service" "${D}/lib/systemd/system/ecam-driver.service"
}

FILES_${PN} += "/lib/systemd/system/* /usr/share/ecam/*"
