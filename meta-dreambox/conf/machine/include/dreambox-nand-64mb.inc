DREAMBOX_ERASE_BLOCK_SIZE = "0x4000"
DREAMBOX_FLASH_SIZE = "0x4000000"
DREAMBOX_SECTOR_SIZE = "512"
MKUBIFS_ARGS = "-m 512 -e 15KiB -c 3798 -x favor_lzo -X 1 -F -j 4MiB"
UBINIZE_ARGS = "-m 512 -p 16KiB -s 512"

STB_PLATFORM = "dm1gen"

MACHINE_FEATURES += "modem dvb-c blindscan-dvbc usbconsole smallflash SCART"
