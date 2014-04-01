DESCRIPTION = "Get packages from ftp.appropho.com (or mirrors)"
SECTION = "devel/ipnc-sdk"
HOMEPAGE = "ftp://ftp.appropho.com"

PR = "r1"

SRC_URI = "file:///proj/ipcam/yocto_kernel_2.6.37/IPNC_RDK_DM36x_V5.1.0-Linux-x86-Install.bin"

SRC_URI[md5sum] = "4a4db65442b3473a1a360212eedb3a8d"

S = "${WORKDIR}/${SRCNAME}-${PV}"


