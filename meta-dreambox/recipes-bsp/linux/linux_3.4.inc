DESCRIPTION = "Linux kernel for ${MACHINE}"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit siteinfo

CMDLINE ?= ""
LOCALVERSION ?= ""

#kernel_conf_variable CMDLINE "\"${CMDLINE} ${CMDLINE_DEBUG}\""
kernel_conf_variable() {
    CONF_SED_SCRIPT="$CONF_SED_SCRIPT /CONFIG_$1[ =]/d;"
    if test "$2" = "n"
    then
        echo "# CONFIG_$1 is not set" >> ${S}/.config
    else
        echo "CONFIG_$1=$2" >> ${S}/.config
    fi
}

DEFCONFIG_IN_SRC_URI = "${@bb.utils.contains('SRC_URI', 'file://defconfig', 'true', 'false', d)}"

do_configure:prepend() {
    echo "" > ${S}/.config
    CONF_SED_SCRIPT=""

    if [ -n "${CMDLINE}" ]; then
        kernel_conf_variable CMDLINE "\"${CMDLINE}\""
    fi

    if [ -n "${LOCALVERSION}" ]; then
        kernel_conf_variable LOCALVERSION "\"${LOCALVERSION}\""
    fi

    kernel_conf_variable SYSFS_DEPRECATED_V2 n

    # Newer inits like systemd need cgroup support
    if [ "${KERNEL_ENABLE_CGROUPS}" = "1" ] ; then
        kernel_conf_variable CGROUP_SCHED y
        kernel_conf_variable CGROUPS y
        kernel_conf_variable CGROUP_NS y
        kernel_conf_variable CGROUP_FREEZER y
        kernel_conf_variable CGROUP_DEVICE y
        kernel_conf_variable CPUSETS y
        kernel_conf_variable PROC_PID_CPUSET y
        kernel_conf_variable CGROUP_CPUACCT y
        kernel_conf_variable RESOURCE_COUNTERS y
    fi

    sed -e "${CONF_SED_SCRIPT}" \
    < '${WORKDIR}/defconfig' >>'${S}/.config'

}

do_configure:append() {
    if test -e scripts/Makefile.fwinst ; then
        sed -i -e "s:-m0644:-m 0644:g" scripts/Makefile.fwinst
    fi
}

do_install:append() {
    oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}/src/linux-${LINUX_VERSION} ARCH=$ARCH
}

PACKAGES =+ "${KERNEL_PACKAGE_NAME}-headers"
FILES:${KERNEL_PACKAGE_NAME}-headers = "${exec_prefix}/src/linux*"

INSANE_SKIP:${PN} += "installed-vs-shipped"
