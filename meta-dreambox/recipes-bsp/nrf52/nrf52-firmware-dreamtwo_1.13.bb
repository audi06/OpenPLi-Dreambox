RDEPENDS:${PN} += "flash-nrf52"

CURRENT_FW = "central-two-noreset-200814-1.13.hex"

include nrf52-firmware.inc

COMPATIBLE_MACHINE = "^(dreamtwo)$"
