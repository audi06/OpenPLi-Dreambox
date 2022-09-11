DESCRIPTION = "Extensions Plugins DreamOSat Downloader For Images"
SUMMARY = " Enigma2 Plugin DreamOSat Downloader For Images by audi06_19"
MAINTAINER = "audi06_19 <audi06_19@hotmail.com>"
HOMEPAGE = "https://www.dreamosat-forum.com"
LICENSE = "LGPL-2.1-only & GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

# inherit distutils-openplugins gettext
inherit allarch gitpkgv pkgconfig gettext
SRCREV = "${AUTOREV}"

# DEPENDS = "${PN}"
RDEPENDS:${PN} = "curl"
# RRECOMMENDS:${PN} = "${PN}"
PACKAGES = "enigma2-plugin-extensions-dreamosat-downloader"
PROVIDES = "${PACKAGES}"

PV = "git${SRCPV}"
PKGV = "git${GITPKGV}"

LOCAL_FILES = "DreamOSatDownloader"
SRC_URI = "git://git@github.com/audi06/DreamOSat_Plugins.git;branch=main;protocol=ssh"

S = "${WORKDIR}/git/${LOCAL_FILES}"

FILES:${PN} = "${libdir}"

do_compile:append() {
    python3 -m compileall -o2 -b ${S}
    # python2 -O -m compileall ${S}
}

do_install() {
	install -d ${D}${libdir}/enigma2/python/Plugins/Extensions/${LOCAL_FILES}
	cp -r --preserve=mode,links ${S}/* ${D}${libdir}/enigma2/python/Plugins/Extensions/${LOCAL_FILES}/
	# remove unused .py files
	find ${D}/ -name '*.py' -exec rm {} \;
	# remove unused .po files
	find ${D}/ -name '*.po' -exec rm {} \;
	# remove unused .pyo files
	find ${D}/ -name '*.pyo' -exec rm {} \;
	find ${D}/ -name '*.egg-info' -exec rm {} \;
	# make scripts executable
	find "${D}" -name '*.sh' -exec chmod a+x '{}' ';'
}
