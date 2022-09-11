DESCRIPTION = "Enigma2 motor setting by chveneburi (30W-100.5E)"
MAINTAINER = "audi06_19 <audi06_19@hotmail.com>"
HOMEPAGE = "https://www.dreamosat-forum.com"
LICENSE = "LGPL-2.1-only & GPL-3.0-only"

require conf/license/license-gplv2.inc

inherit gitpkgv allarch

PKGV = "${DATE}"

# required in bitbake 1.32-
PKGV[vardepsexclude] += "DATE"

S = "${WORKDIR}"

DEPENDS = ""

PACKAGES = "${PN}"

CONF_FILES = "/home/dreamosat/DemonEditor/data"

SRC_URI += "\
    file://${CONF_FILES}/dm900 \
    "

FILES:${PN} = "${sysconfdir}/enigma2/"

do_install() {
	install -d ${D}${sysconfdir}/enigma2
	#for i in (ls ${S}/*.tv) ; do
	#	cp -r ${S}/${i} ${D}${sysconfdir}/enigma2/$i
	#done
	cp -r ${S}${CONF_FILES}/dm900/* ${D}${sysconfdir}/enigma2
	rm -rf ${D}${sysconfdir}/enigma2/satellites.xml*
}

