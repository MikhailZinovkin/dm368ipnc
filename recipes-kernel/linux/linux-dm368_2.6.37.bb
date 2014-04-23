require recipes-kernel/linux/linux-yocto.inc

KERNEL_IMAGETYPE = "uImage"

COMPATIBLE_MACHINE = "dm368ipnc"

BOOT_SPLASH ?= "logo_linux_clut224-generic.ppm"

S = "${WORKDIR}/git"

SRCREV = "e396c9d8699c95d52b2abcc2d4d5f9616e839734"
SRC_URI = " \
    git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-2.6.37.y \
    file://defconfig \
    file://${BOOT_SPLASH} \
"


