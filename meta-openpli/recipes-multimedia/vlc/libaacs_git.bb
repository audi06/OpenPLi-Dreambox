SUMMARY = "Open implementation of the AACS specification"
SECTION = "libs/multimedia"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=4b54a1fd55a448865a0b32d41598759d"

DEPENDS = "libgcrypt libgpg-error bison-native flex-native"
PV = "0.11.0+git${SRCPV}"
PKGV = "0.11.0+git${GITPKGV}"

# make the origin overridable from OE config, for local mirroring
SRC_ORIGIN ?= "git://code.videolan.org/videolan/libaacs.git;protocol=https;branch=master"
SRC_URI := "\
    ${SRC_ORIGIN} \
    file://libgcrypt-gpg-error-use-pkgconfig.patch \
    "

S = "${WORKDIR}/git"

inherit gitpkgv autotools-brokensep lib_package pkgconfig
