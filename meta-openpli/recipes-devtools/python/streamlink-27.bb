SUMMARY = "Streamlink[-27] is a fork of the Streamlink project which continues to support python 2.7"
DESCRIPTION = "The project extension [-27] indicates that this project continues to support python 2.7, \
                as the streamlink project has discontinued python 2.7 support as of version 1.7.0"
HOMEPAGE = "https://billy2011.github.io/streamlink-27"
SECTION = "devel/python"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c0be52291b7252b878da806d185b1d1"

RDEPENDS:${PN} = "python-core \
    python-backports-functools-lru-cache \
    python-backports-shutil-get-terminal-size \
    python-backports-shutil-which \
    python-ctypes \
    python-futures \
    python-isodate \
    python-lxml \
    python-misc \
    python-pkgutil \
    python-pycryptodome \
    python-pycountry \
    python-pysocks \
    python-requests \
    python-shell \
    python-singledispatch \
    python-subprocess \
    python-typing \
    python-websocket-client \
    "

inherit gittag setuptools python-dir

SRCREV = "39f3855b59c2ef01f3d6513a1cc972968494a35c"
PV = "git${SRCPV}"
PKGV = "${GITPKGVTAG}"

GIT_SITE = "${@ 'git://gitlab.com/jack2015' if d.getVar('CODEWEBSITE') else 'git://gitee.com/jackgee2021'}"

SRC_URI = "${GIT_SITE}/streamlink-27.git;protocol=https;branch=master"

FILESEXTRAPATHS:prepend := "${THISDIR}/streamlink-27:"

SRC_URI += " \
    file://0001-added-files.patch \
    "
S = "${WORKDIR}/git"

do_install:append() {
    rm -rf ${D}${bindir}
    rm -rf ${D}${libdir}/${PYTHON_DIR}/site-packages/streamlink_cli
}

PACKAGES = "${PN}-src ${PN}"

FILES:${PN} = " \
    ${libdir}/${PYTHON_DIR}/site-packages/streamlink/*.pyo \
    ${libdir}/${PYTHON_DIR}/site-packages/streamlink/*/*.pyo \
    ${libdir}/${PYTHON_DIR}/site-packages/streamlink/*/*/*.pyo \
    "

FILES:${PN}-src += " \
    ${libdir}/${PYTHON_DIR}/site-packages/streamlink-*.egg-info/* \
    ${libdir}/${PYTHON_DIR}/site-packages/streamlink/plugins/.removed \
    ${libdir}/${PYTHON_DIR}/site-packages/streamlink/*.py \
    ${libdir}/${PYTHON_DIR}/site-packages/streamlink/*/*.py \
    ${libdir}/${PYTHON_DIR}/site-packages/streamlink/*/*/*.py \
    "
