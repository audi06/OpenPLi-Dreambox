SUMMARY = "SABnzbd - The automated Usenet download tool"
DESCRIPTION = "SABnzbd is an Open Source Binary Newsreader written in Python."
HOMEPAGE = "https://sabnzbd.org"
MAINTAINER = "team@sabnzbd.org"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYRIGHT.txt;md5=6c2cd2089133de5067e13a6d4f75afef"

DEPENDS = "python"
RDEPENDS:${PN} = "\
    python-cheetah python-compression python-core python-crypt python-ctypes python-email python-html \
    python-misc python-multiprocessing python-sabyenc python-sqlite3 python-shell python-subprocess \
    "
RDEPENDS:${PN}-src = "python"

RRECOMMENDS:${PN} = "par2cmdline unrar"

SRCREV = "8f21533e76d64a3bc26643394d5e98dc01ece63e"

GIT_SITE = "${@ 'git://gitlab.com/jack2015' if d.getVar('CODEWEBSITE') else 'git://gitee.com/jackgee2021'}"

SRC_URI = "${GIT_SITE}/sabnzbd.git;protocol=https;branch=master \
    file://sabnzbd \
    file://sabnzbd.conf \
    file://init-functions \
    "

S = "${WORKDIR}/git"

INSTALLDIR = "${libdir}/${PN}"

PACKAGES = "${PN}-doc ${PN}-src ${PN}"

FILES:${PN}-src = "${INSTALLDIR}/*/*.py ${INSTALLDIR}/*/*/*.py"
FILES:${PN}-doc = "${INSTALLDIR}/*.txt ${INSTALLDIR}/licenses ${INSTALLDIR}/interfaces/*/licenses"
FILES:${PN} = "${INSTALLDIR} /etc/init.d/sabnzbd /etc/init.d/init-functions /etc/enigma2/sabnzbd.conf"

inherit update-rc.d
INITSCRIPT_NAME = "sabnzbd"
INITSCRIPT_PARAMS = "defaults"

do_compile() {
    python2 -O -m compileall .
}

do_install() {
	install -d ${D}${INSTALLDIR}
	cp -r . ${D}${INSTALLDIR}/
	rm -rf ${D}${INSTALLDIR}/.git
	install -d ${D}${sysconfdir}/init.d
	install -m 755 ${WORKDIR}/sabnzbd ${D}${sysconfdir}/init.d/sabnzbd
	install -m 755 ${WORKDIR}/init-functions ${D}${sysconfdir}/init.d/init-functions
	install -d ${D}${sysconfdir}/enigma2
	install -m 644 ${WORKDIR}/sabnzbd.conf ${D}/etc/enigma2/sabnzbd.conf
}
