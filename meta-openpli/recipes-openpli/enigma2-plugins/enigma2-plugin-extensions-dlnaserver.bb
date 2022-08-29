DESCRIPTION = "VU+ DLNA Server plugin"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c9e255efa454e0155c1fd758df7dcaf3"

S = "${WORKDIR}/git"
SRC_URI = " git://gitee.com/jackgee2021/dvbapp.git;protocol=https;branch=vuplus_experimental \
	file://01-minidlna.patch;striplevel=1;apply=yes \
	file://02-readymedia.patch;striplevel=1;apply=yes \
"

inherit gitpkgv
PV = "git${SRCPV}"
PKGV = "git${GITPKGV}"

RDEPENDS:${PN} = "readymedia"
FILES:${PN} = "${libdir}/enigma2/python/Plugins/Extensions/DLNAServer/*"
PACKAGES = "${PN}"

do_install() {
	install -d ${D}${libdir}/enigma2/python/Plugins/Extensions/DLNAServer
	install -m 0644 ${S}/lib/python/Plugins/Extensions/DLNAServer/*.py ${D}${libdir}/enigma2/python/Plugins/Extensions/DLNAServer
	python2 -O -m compileall ${D}${libdir}/enigma2/python/Plugins/
}
