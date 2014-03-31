DESCRIPTION = "Autostart systemd services"
# Useful link for OTG stuff. A "homepage" is required for a package.
HOMEPAGE = "http://jumpnowtek.com/index.php?option=com_content&view=article&id=53&Itemid=65"
SECTION = "networking"
LICENSE = "GPLv2"
# Just a blank license file.
LIC_FILES_CHKSUM = "file://LICENSE;md5=d41d8cd98f00b204e9800998ecf8427e"
PR = "r1"

SRC_URI = "file://usb-networking.service \
           file://usb-networking.sh \
           file://networking.service \
           file://LICENSE"

S = "${WORKDIR}"

do_install() {
    install -d "${D}/usr/share"
    install -d "${D}/lib/systemd/system"

    # OTG port
    install -m 0755 "${WORKDIR}/usb-networking.sh" "${D}/usr/share/usb-networking.sh"
    install -m 0644 "${WORKDIR}/usb-networking.service" "${D}/lib/systemd/system/usb-networking.service"
    
    # Ethernet
    install -m 0644 "${WORKDIR}/networking.service" "${D}/lib/systemd/system/networking.service"
}

FILES_${PN} += "/lib/systemd/system/* /usr/share/*"
