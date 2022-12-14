do_install:append() {
        if [ -n "${KERNEL_EXTRA_IMAGETYPE}" ] && [ "${KERNEL_OUTPUT}" != "${KERNEL_EXTRA_OUTPUT}" ] && [ -f "${KERNEL_EXTRA_OUTPUT}" ]; then
                if [ ${KERNEL_EXTRA_INSTALL} = "1" ]; then
                        install -m 0644 ${KERNEL_EXTRA_OUTPUT} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_EXTRA_IMAGETYPE}-${KERNEL_VERSION}
                fi
                install -m 0644 ${KERNEL_EXTRA_OUTPUT} ${D}${KERNEL_SRC_PATH}/${KERNEL_EXTRA_IMAGETYPE}
        fi
}

do_install:prepend() {
   if [ "${KERNEL_IMAGETYPE}" = "vmlinux.bin" ]; then
      install -m 0644 ${B}/arch/${ARCH}/boot/vmlinux.bin ${B}/
   fi
}

KERNEL_EXTRA_IMAGETYPE ?= "${KERNEL_ALT_IMAGETYPE}"
KERNEL_EXTRA_OUTPUT ?= "arch/${ARCH}/boot/${KERNEL_EXTRA_IMAGETYPE}"
KERNEL_EXTRA_IMAGE_BASE_NAME ?= "${KERNEL_EXTRA_IMAGETYPE}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
KERNEL_EXTRA_IMAGE_BASE_NAME[vardepsexclude] += "DATETIME"
KERNEL_EXTRA_IMAGE_SYMLINK_NAME ?= "${KERNEL_EXTRA_IMAGETYPE}-${MACHINE}"
KERNEL_EXTRA_INSTALL ?= "0"

do_deploy:append() {
        if [ -n "${KERNEL_EXTRA_IMAGETYPE}" ] && [ "${KERNEL_OUTPUT}" != "${KERNEL_EXTRA_OUTPUT}" ] && [ -f "${KERNEL_EXTRA_OUTPUT}" ]; then
                install -m 0644 ${KERNEL_EXTRA_OUTPUT} ${DEPLOYDIR}/${KERNEL_EXTRA_IMAGE_BASE_NAME}.bin
                ln -sf ${KERNEL_EXTRA_IMAGE_BASE_NAME}.bin ${DEPLOYDIR}/${KERNEL_EXTRA_IMAGE_SYMLINK_NAME}.bin
        fi
}
