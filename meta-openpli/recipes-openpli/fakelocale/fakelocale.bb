SUMMARY = "LC_TIME locale support"
LICENSE = "CLOSED"
SECTION = "base"
PRIORITY = "required"
MAINTAINER = "OpenPli team"

SRC_URI = "file://lctimelocales.tar.gz file://locale.alias file://SYS_LC_MESSAGES"

S = "${WORKDIR}/locales"

inherit allarch

LOCALEDIR = "${libdir}/locale"
LOCALEDIR2 = "${datadir}/locale"

LANGUAGES = "ar_AE bg_BG ca_AD cs_CZ da_DK de_DE el_GR en_EN es_ES et_EE fa_IR fi_FI \
	fr_FR fy_NL he_IL hr_HR hu_HU id_ID is_IS it_IT lt_LT lv_LV nl_NL no_NO pl_PL pt_BR pt_PT \
	ru_RU sk_SK sl_SI sr_YU sv_SE th_TH tr_TR uk_UA"

RPROVIDES:${PN} = "virtual-locale-ar virtual-locale-bg virtual-locale-ca virtual-locale-cs \
	virtual-locale-da virtual-locale-de virtual-locale-el virtual-locale-en virtual-locale-es \
	virtual-locale-et virtual-locale-fa virtual-locale-fi virtual-locale-fr virtual-locale-fy \
	virtual-locale-he virtual-locale-hr virtual-locale-hu virtual-locale-is virtual-locale-it \
	virtual-locale-lt virtual-locale-lv virtual-locale-nl virtual-locale-no virtual-locale-pl \
	virtual-locale-pt virtual-locale-ru virtual-locale-sk virtual-locale-sl virtual-locale-id \
	virtual-locale-sr virtual-locale-sv virtual-locale-th virtual-locale-tr virtual-locale-uk"

do_install() {
	install -d ${D}${LOCALEDIR2}
	install ${WORKDIR}/locale.alias ${D}${LOCALEDIR2}

	install -d ${D}${LOCALEDIR}
	cp -r --preserve=mode,links ${S}/* ${D}/${LOCALEDIR}

	install -d ${D}${LOCALEDIR}/fake/LC_MESSAGES
	install ${WORKDIR}/SYS_LC_MESSAGES ${D}${LOCALEDIR}/fake/LC_MESSAGES/

	for lang in ${LANGUAGES}; do
		ln -s ../fake/LC_MESSAGES ${D}${LOCALEDIR}/${lang}/LC_MESSAGES
	done
}

FILES:${PN} = "${LOCALEDIR} ${LOCALEDIR2}"
