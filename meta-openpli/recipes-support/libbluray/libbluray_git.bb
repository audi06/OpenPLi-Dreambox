SUMMARY  = "Library to access Blu-Ray disk"
SECTION = "misc"
HOMEPAGE = "http://videolan.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM="file://COPYING;md5=435ed639f84d4585d93824e7da3d85da"

DEPENDS = "libaacs libdca libdvdcss libxml2"
RDEPENDS:${PN} = "libaacs libdca libdvdcss"

# make the origin overridable from OE config, for local mirroring
SRC_ORIGIN ?= "gitsm://code.videolan.org/videolan/libbluray.git;protocol=https;branch=master"
SRC_URI := "${SRC_ORIGIN} "

inherit gitpkgv setuptools autotools-brokensep pkgconfig

PV = "v1.3.0+git${SRCPV}"
PKGV = "v1.3.0+git${GITPKGV}"

S="${WORKDIR}/git"

EXTRA_OECONF = " \
	--disable-bdjava-jar \
	--disable-doxygen-doc \
	--disable-doxygen-dot \
	--without-freetype \
	--without-fontconfig \
	"
