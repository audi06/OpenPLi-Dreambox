SUMMARY = "tuxbox tuxtxt 32bit framebuffer for enigma2"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "freetype libtuxtxt"

inherit autotools pkgconfig gitpkgv

SRC_URI = "git://gitlab.com/jack2015/tuxtxt.git;protocol=https;branch=master \
	file://0002-Use-separate-transparency-for-menu-and-teletext.patch \
	${@bb.utils.contains('DISTRO_FEATURES', 'tuxtxtfhd', 'file://tuxtxt_FHD.patch', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'tuxtxtfhd', ' \
	file://tuxtxt.ttf \
	file://tuxtxt_nonbold.ttf \
	file://EXP_MODE \
	file://TTF_FHD \
	file://TTF_HD \
	file://TTF_SD \
	file://X11_SD \
	', '', d)} \
"

S = "${WORKDIR}/git/tuxtxt"

PV = "2.0+git${SRCPV}"
PKGV = "2.0+git${GITPKGV}"
PR = "r3"

EXTRA_OECONF = "--with-boxtype=generic --with-configdir=/etc \
	${@bb.utils.contains("MACHINE_FEATURES", "textlcd", "--with-textlcd" , "", d)} \
	DVB_API_VERSION=5 \
	"

do_configure:prepend() {
    touch ${S}/NEWS
    touch ${S}/README
    touch ${S}/AUTHORS
    touch ${S}/ChangeLog
}

do_install:append() {
    # remove unused .pyc files
    find ${D}${libdir}/enigma2/python/ -name '*.pyc' -exec rm {} \;
    if ${@bb.utils.contains('DISTRO_FEATURES', 'tuxtxtfhd', 'true', 'false', d)}; then
        install -d ${D}/usr/share/fonts
        rm -f ${D}/usr/share/fonts/tuxtxt.ttf
        rm -f ${D}/usr/share/fonts/tuxtxt_nonbold.ttf
        cp -f ${WORKDIR}/tuxtxt.ttf ${D}/usr/share/fonts/tuxtxt.ttf
        cp -f ${WORKDIR}/tuxtxt_nonbold.ttf ${D}/usr/share/fonts/tuxtxt_nonbold.ttf
        cp -f ${WORKDIR}/EXP_MODE ${D}${sysconfdir}/tuxtxt
        cp -f ${WORKDIR}/TTF_FHD ${D}${sysconfdir}/tuxtxt
        cp -f ${WORKDIR}/TTF_HD ${D}${sysconfdir}/tuxtxt
        cp -f ${WORKDIR}/TTF_SD ${D}${sysconfdir}/tuxtxt
        cp -f ${WORKDIR}/X11_SD ${D}${sysconfdir}/tuxtxt
    fi
}

PACKAGES = "${PN}-src ${PN}-dbg ${PN}-dev ${PN}"
FILES:${PN}-src = "/usr/src ${libdir}/enigma2/python/Plugins/Extensions/Tuxtxt/*.py"
FILES:${PN} = "${libdir}/libtuxtxt32bpp.so.* ${datadir}/fonts ${libdir}/enigma2/python/Plugins/Extensions/Tuxtxt/*.pyo ${sysconfdir}/tuxtxt"
CONFFILES:${PN} = "${sysconfdir}/tuxtxt/tuxtxt2.conf"
