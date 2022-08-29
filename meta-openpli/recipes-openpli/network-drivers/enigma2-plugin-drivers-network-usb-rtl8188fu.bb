SUMMARY = "WiFi devices for Realtek RTL8188FU/RTL8188FTV chipsets."
inherit allarch

require conf/license/license-gplv2.inc

RRECOMMENDS:${PN} = " \
        rt8188fu \
        firmware-rt8188fu \
"

PV = "1.0"
PR = "r1"

ALLOW_EMPTY:${PN} = "1"
