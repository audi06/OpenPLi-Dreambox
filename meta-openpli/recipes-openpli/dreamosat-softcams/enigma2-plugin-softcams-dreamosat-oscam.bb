DESCRIPTION = "DreamOSat camManager for ${CAMNAME} svn-${REVISION} Open Source Softcam"
SUMMARY = "${CAMNAME} - Open Source Softcam - svn-${REVISION}"
MAINTAINER = "audi06_19 <audi06_19@hotmail.com>"
HOMEPAGE = "https://www.dreamosat-forum.com"
DESCRIPTION = "Combining the benefits of\n \
- latest trunk\n \
- standart interface\n \
- openssl support\n \
- libusb support\n \
- emu support\n \
- config support\n \
"
LICENSE = "CLOSED"
PACKAGES = "enigma2-plugin-softcams-dreamosat-oscam"
PROVIDES="${PACKAGES}"

require conf/license/license-gplv2.inc
inherit cmake gitpkgv

# no docs, locales etc.
PACKAGES = "${PN}"

EXTRA_OECMAKE += "\
	-DOSCAM_SYSTEM_NAME=Tuxbox \
	-DWEBIF=1 \
	-DWEBIF_LIVELOG=1 \
	-DWEBIF_JQUERY=1 \
	-DWITH_STAPI=0 \
	-DHAVE_LIBUSB=1 \
	-DSTATIC_LIBUSB=0 \
	-DWITH_SSL=1 \
	-DIPV6SUPPORT=1 \
	-DCLOCKFIX=0 \
	-DHAVE_PCSC=1 \
	-DCARDREADER_SMARGO=1 \
	-DCARDREADER_PCSC=1 \
	-DCW_CYCLE_CHECK=1 \
	-DCS_CACHEEX=1 \
	-DMODULE_CONSTCW=1 \
	-DLCDSUPPORT=1 \
	"

S = "${WORKDIR}/git"

DEPENDS = "libusb openssl"

RDEPENDS:${PN} += "\
	enigma2-plugin-extensions-dreamosat-cammanager \
	"

INSANE_SKIP:${PN} += "already-stripped"

SRCREV = "${AUTOREV}"
PV = "1.20+git${SRCPV}"
PKGV = "1.20+git${GITPKGV}"
SRC_URI = "git://repo.or.cz/oscam.git;protocol=https;branch=master \
	file://oZeL.patch;striplevel=0 \
	file://${CONF_FILES}/oscam.conf \
	file://${CONF_FILES}/oscam.ccache \
	file://${CONF_FILES}/oscam.dvbapi \
	file://${CONF_FILES}/oscam.services \
	file://${CONF_FILES}/oscam.server \
	file://${CONF_FILES}/oscam.user \
	"

B = "${S}"
CAMNAME = "OSCam"
REVISION = "11712"

CONF_FILES = "/home/dreamosat/Genel/Emulator/EMU_BINAR/config/oscam/etc/tuxbox/config"

CONFFILES = "\
	${sysconfdir}/tuxbox/config/oscam.conf \
	${sysconfdir}/tuxbox/config/oscam.ccache \
	${sysconfdir}/tuxbox/config/oscam.dvbapi \
	${sysconfdir}/tuxbox/config/oscam.services \
	${sysconfdir}/tuxbox/config/oscam.server \
	${sysconfdir}/tuxbox/config/oscam.user \
	"

FILES:${PN} = "\
	${bindir}/${CAMNAME}_${REVISION} \
	${sysconfdir}/tuxbox/config/* \
	${prefix}/camscript/${CAMNAME}_${REVISION}.sh \
	"

do_compile:prepend() {
	revision=`(svnversion -n . 2>/dev/null || printf 0) | sed 's/.*://; s/[^0-9]*$//; s/^$/0/'`
	export REVISION="${revision}"
	if [ "${revision}" = "0" ]
	then
		which git > /dev/null 2>&1 && revision=`git log -25 --pretty=%B | grep git-svn-id | head -n 1 | sed -n -e 's/^.*trunk@\([0-9]*\) .*$/\1/p'`
		export REVISION="${revision}"
	fi
}

# Generate a simplistic standard init script
# (sorry for the sleep 1, but start-stop-daemon -R does not work as advertised)
do_install:append () {
cat > ${S}/${CAMNAME}_${REVISION}.sh << EOF
#!/bin/sh
#### "*******************************************";
#### "*          ..:: A U T H O R ::..          *";
#### "*             << Audi06_19 >>             *";
#### "*  ..:: https://dreamosat-forum.com ::..  *";
#### "*******************************************";

OSD="${CAMNAME}_${REVISION}"
CAM="${CAMNAME}_${REVISION}"
PID=\`pidof \${CAM}\`
Action=\$1

[ -n "\${CAM}" ] || exit 1;
[ -x ${bindir}/\${CAM} ] || exit 1;

RED='\033[31m';
GREEN='\033[32m';
YELLOW='\033[33m';
BLUE='\033[34m';
PURPLE='\033[35m';
CYAN='\033[36m';
END='\033[m';

PIDFILE="/var/run/\${CAM}.pid";
DESC="\${YELLOW}Softcam service\${END} \${CYAN}\${CAM}\${CYAN}";
DAEMON="${bindir}/\${CAM}";

cam_clean () { rm -rf /tmp/*.info* > /dev/null 2>&1;
        rm -rf /tmp/.*cam* > /dev/null 2>&1;
        rm -rf /tmp/*.pid > /dev/null 2>&1;
        rm -rf /tmp/*.log > /dev/null 2>&1;
        rm -rf \${PIDFILE} > /dev/null 2>&1;}

cam_handle () { if test -z "\${PID}" ; then
            \$0 cam_up;
        else
            \$0 cam_down;
        fi;}

cam_down () { kill \`cat \${PIDFILE} 2> /dev/null\` 2> /dev/null;
    RETVAL=\$?;
    if [ "\${RETVAL}" -eq "0" ]; then
        echo -e "\${BLUE}Stopping\${END} \${DESC} : \${GREEN}OK\${END}";
    else
        echo -e "\${BLUE}Stopping\${END} \${DESC} : \${RED}FAILED\${END}";
    fi;
    sleep 1;
    killall \${CAM} 2>/dev/null;
    sleep 2;
    cam_clean;}

cam_up () { if [ -e \${PIDFILE} ]; then
        PIDDIR=/proc/\$(cat \${PIDFILE})
        if [ -d \${PIDDIR} ] && [[ \$(readlink -f \${PIDDIR}/exe) == \${DAEMON} ]]; then
            echo -e "\${DESC} \${PURPLE}already started; not starting.\${END}";
            exit 1;
        else
            rm -rf \${PIDFILE} > /dev/null 2>&1;
        fi;
    fi;
    ulimit -s 1024;
    \${DAEMON} --wait 60 --config-dir ${sysconfdir}/tuxbox/config --daemon --pidfile \${PIDFILE} --restart 2 --utf8 2 | grep -v "UTF-8 mode";
    sleep 0.5;
    RETVAL=1;
    [ -e \${PIDFILE} ] && RETVAL=0;
    if [ "\${RETVAL}" -eq "0" ]; then
        echo -e "\${BLUE}Starting\${END} \${DESC} : \${GREEN}OK\${END}";
    else
        echo -e "\${BLUE}Starting\${END} \${DESC} : \${RED}FAILED\${END}";
    fi;}

if test "\${Action}" = "cam_startup" ; then
    if test -z "\${PID}" ; then
        cam_down;
        cam_up;
    else
        echo -e "\${CAM} \${PURPLE}already running, exiting...\${END}";
    fi;
elif test "\${Action}" = "cam_res" ; then
    cam_down;
    cam_up;
elif test "\${Action}" = "cam_down" ; then
    cam_down;
elif test "\${Action}" = "cam_up" ; then
    cam_up;
else
    cam_handle;
fi;
exit 0;
EOF
}

# Install routine, should be ok for most cams.
do_install() {
	install -d ${D}${sysconfdir}/tuxbox/config
	install -m 0644 ${WORKDIR}${CONF_FILES}/* ${D}${sysconfdir}/tuxbox/config
	install -d ${D}${bindir}
	install -m 0755 ${B}/oscam ${D}${bindir}/${CAMNAME}_${REVISION}
}

# Install routine, should be ok for most cams.
do_install:append () {
	install -d ${D}${prefix}/camscript
	install -m 755 ${S}/${CAMNAME}_${REVISION}.sh ${D}${prefix}/camscript/${CAMNAME}_${REVISION}.sh
}


pkg_prerm:${PN} () {
#!/bin/sh
###########################################################
# Script by audi06_19
# Support: www.dreamosat-forum.com
# E-Mail: admin@dreamosat-forum.com
###########################################################
PSVER=$(ps --version 2>&1 | awk '{print $4}' | cut -d "." -f1)
FIN="=================================================="
echo ${FIN}
if [ -f /etc/apt/apt.conf ] ; then
    if ! [ $(ps -ax | grep -Fi ${CAMNAME} | grep -v grep | wc -l) == 0 ] ; then
        echo -e "OE2.5/6 SoftCam Stop...";
        if [ $(systemctl list-units --type=service --state=running | grep -Fi ${CAMNAME} | grep -v grep | wc -l) == 1 ] ; then
            systemctl stop `systemctl list-units --type=service --state=running | grep -Fi ${CAMNAME} | grep -v grep | awk {'print $1'}` > /dev/null 2>&1;
        else
python <<'EOF'
import subprocess, signal
import os
p = subprocess.Popen(['ps', '-A'], stdout=subprocess.PIPE)
out, err = p.communicate()
for line in out.splitlines():
    if "${CAMNAME}" in line.decode('utf-8'):
        pid = int(line.split(None, 1)[0])
        os.kill(pid, signal.SIGKILL)
EOF
            #kill -9 `ps -ax | grep -Fi ${CAMNAME} | grep -v grep | awk {'print $1'}` &> /dev/null 2>&1;
        fi
    fi
elif [ $(python --version 2>&1 | awk '{print $2}' | cut -d "." -f1) == 3 ] ; then
    if ! [ $(ps -ax | grep -Fi ${CAMNAME} | grep -v grep | wc -l) == 0 ] ; then
        echo -e "OE2.0 Python 3 SoftCam Stop...";
        kill -9 `ps -ax | grep -Fi ${CAMNAME} | grep -v grep | awk {'print $1'}` &> /dev/null 2>&1;
    fi
elif [ ${PSVER} == 3 ] ; then
    if ! [ $(ps -ax | grep -Fi ${CAMNAME} | grep -v grep | wc -l) == 0 ] ; then
        echo -e "OE2.0 PS ver 3 SoftCam Stop...";
python <<'EOF'
import subprocess, signal
import os
p = subprocess.Popen(['ps', '-A'], stdout=subprocess.PIPE)
out, err = p.communicate()
for line in out.splitlines():
    if "${CAMNAME}" in line.decode('utf-8'):
        pid = int(line.split(None, 1)[0])
        os.kill(pid, signal.SIGKILL)
EOF
        #kill -9 `ps -ax | grep -Fi ${CAMNAME} | grep -v grep | awk {'print $1'}` &> /dev/null 2>&1;
    fi
else
    if ! [ $(ps | grep -Fi ${CAMNAME} | grep -v grep | wc -l) == 0 ] ; then
        echo -e "OE2.0 SoftCam Stop...";
        kill -9 `ps | grep -Fi ${CAMNAME} | grep -v grep | awk {'print $1'}` &> /dev/null 2>&1;
    fi
fi;
if [ "x$D" == "x" ]; then
    rm -rf ${bindir}/${CAMNAME}_${REVISION} > /dev/null 2>&1
    rm -rf ${prefix}/camscript/${CAMNAME}_${REVISION}.sh > /dev/null 2>&1
fi
exit 0
}

pkg_postinst:${PN} () {
#!/bin/sh
###########################################################
# Script by audi06_19
# Support: www.dreamosat-forum.com
# E-Mail: admin@dreamosat-forum.com
###########################################################
if [ "x$D" == "x" ]; then
FIN="=================================================="
echo $FIN
FREEsize=$(df -k /usr/ | grep [0-9]% | tr -s " " | cut -d " " -f 4)
if [ -f /etc/apt/apt.conf ] ; then
    if [ -s /usr/lib/dpkg/info/enigma2-plugin-softcams-dreamosat-oscam-emu.list ] ||
    [ -s /var/lib/dpkg/info/enigma2-plugin-softcams-dreamosat-oscam-emu.list ]; then
        echo "DreamOSat Softcams ${CAMNAME} exist. No freesize check"
    else
        if [ 3484 -gt "${FREEsize}" ]; then
            echo -e "Paketsize 3484kb tis to big for your Flashsize ${FREEsize}kb"
            echo -e "Eklenti boyutu 3484kb Cihazınızda yeterli hafıza yok. Flashsize:${FREEsize}kb"
            echo -e "Abort Installation \nKurulum İptal Edildi"
            killall -9 dpkg install 2> /dev/null
        fi
    fi
else
    if [ -s /usr/lib/opkg/info/enigma2-plugin-softcams-dreamosat-oscam-emu.list ] ||
    [ -s /var/lib/opkg/info/enigma2-plugin-softcams-dreamosat-oscam-emu.list ]; then
        echo "DreamOSat Softcams ${CAMNAME} exist. No freesize check"
    else
        if [ 3484 -gt "${FREEsize}" ]; then
            echo -e "Paketsize 3484kb tis to big for your Flashsize ${FREEsize}kb"
            echo -e "Eklenti boyutu 3484kb Cihazınızda yeterli hafıza yok. Flashsize:${FREEsize}kb"
            echo -e "Abort Installation \nKurulum İptal Edildi"
            killall -9 opkg install 2> /dev/null
        fi
    fi
fi
fi
exit 0
}


