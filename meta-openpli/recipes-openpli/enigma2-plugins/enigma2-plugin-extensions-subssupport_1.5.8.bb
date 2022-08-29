inherit gitpkgv autotools-brokensep

SUMMARY = "Collection of enigma2 subtitles plugins"
HOMEPAGE = "https://github.com/mx3L/subssupport"
AUTHOR = "Maroš Ondrášek <mx3ldev@gmail.com>"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS:${PN} = "python-requests python-xmlrpc python-compression python-codecs python-zlib python-difflib unrar"

SRC_URI = "git://gitlab.com/jack2015/subssupport.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

FILES:${PN} = "${libdir}/enigma2/python/Plugins/Extensions/SubsSupport \
${localstatedir}/lib/subssupport"

do_install:append() {
    install -d ${D}${localstatedir}/lib/subssupport
}

