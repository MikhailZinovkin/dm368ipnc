require recipes-kernel/linux/linux-yocto.inc

KERNEL_IMAGETYPE = "uImage"

COMPATIBLE_MACHINE = "dm368ipnc"

BOOT_SPLASH ?= "logo_linux_clut224-generic.ppm"

S = "${WORKDIR}/git"

#SRCREV = "f2b152564afdf9c9917c17d1c41c1082c82067bd"
SRC_URI = " \
    git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-2.6.37.y \
    file://defconfig \
    file://${BOOT_SPLASH} \
"


