DESCRIPTION = "Multi Stalker enigma2 plugin OE2.0 OE2.5"
HOMEPAGE = "https://www.dreamosat-forum.com"
PRIORITY = "optional"
LICENSE = "LGPL-2.1-only & GPL-3.0-only"

require conf/license/license-gplv2.inc

PV = "1.3"
PKGV = "v${PV}"
BB_STRICT_CHECKSUM = "0"

SRC_URI = " \
	https://github.com/ziko-ZR1/Multi-Stalker-install/raw/main/Downloads/py2/${TARGET_ARCH}/multistalker${PV}-py2-${TARGET_ARCH}.tar.gz;name=tarball \
	https://github.com/ziko-ZR1/Multi-Stalker-install/raw/main/Downloads/stalker-conf.tar.gz;name=tarball \
	"

S = "${WORKDIR}"

FILES:${PN} = "\
	/home/* \
	${libdir}/enigma2/python/Plugins/Extensions/MultiStalker/* \
	"

do_install() {
	install -d ${D}/home
	install -d ${D}${libdir}/enigma2/python/Plugins/Extensions/MultiStalker
	cp -rd ${S}${libdir}/* ${D}${libdir}/
	cp -rd ${S}/home/* ${D}/home/
}

PACKAGES = "enigma2-plugin-extensions-multistalker"
PROVIDES = "${PACKAGES}"

