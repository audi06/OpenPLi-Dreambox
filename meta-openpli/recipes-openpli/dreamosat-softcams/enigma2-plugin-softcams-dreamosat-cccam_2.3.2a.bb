DESCRIPTION = "DreamOSat camManager for ${CAMNAME} rev-${PV} Open Source Softcam"
SUMMARY = "${CAMNAME} - Open Source Softcam - rev-${PV}"
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
PACKAGES = "enigma2-plugin-softcams-dreamosat-cccam"
PROVIDES="${PACKAGES}"

require conf/license/license-gplv2.inc

CAMNAME = "CCcam"
EMUVERSION = "1"
S = "${WORKDIR}"
B = "${S}"

RDEPENDS:${PN} += "\
	libxcrypt-compat \
	enigma2-plugin-extensions-dreamosat-cammanager \
	"

SRC_URI[softcam.md5sum] = "80c05deecd7cea0d8b930caef399a4f5"
SRC_URI[softcam.sha256sum] = "1c130bb76b06f836a4ed8936cd205f01d90c2645cd13814e4c487a7296f04cdb"
SRC_URI = "http://downloads.openpli.org/softcams/CCcam-${PV}.zip;name=softcam \
    file://${CONF_FILES}/CCcam.cfg \
    file://${CONF_FILES}/CCcam.prio \
    "

CONF_FILES = "/home/dreamosat/Genel/Emulator/EMU_BINAR/config/cccam/etc"

CONFFILES = "\
	${sysconfdir}/CCcam.cfg \
	${sysconfdir}/CCcam.prio \
	"

FILES:${PN} = "\
	${bindir}/${CAMNAME}_${PV}-r${EMUVERSION} \
	${sysconfdir}/CCcam.cfg \
	${sysconfdir}/CCcam.prio \
	${prefix}/camscript/${CAMNAME}_${PV}-r${EMUVERSION}.sh \
	"
INHIBIT_PACKAGE_STRIP = "1"


# Generate a simplistic standard init script
# (sorry for the sleep 1, but start-stop-daemon -R does not work as advertised)
do_install:append () {
cat > ${S}/${CAMNAME}_${PV}-r${EMUVERSION}.sh << EOF
#!/bin/sh
#### "*******************************************";
#### "*          ..:: A U T H O R ::..          *";
#### "*             << Audi06_19 >>             *";
#### "*  ..:: https://dreamosat-forum.com ::..  *";
#### "*******************************************";

OSD="${CAMNAME}_${PV}-r${EMUVERSION}"
CAM="${CAMNAME}_${PV}-r${EMUVERSION}"
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
    killall -9 \${CAM} 2>/dev/null
    # exec start-stop-daemon -K -R 2 -x \${DAEMON}
    # exec start-stop-daemon -K -o -R TERM/5/KILL/2 -x \${DAEMON}
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
    \${DAEMON} -C /etc/CCcam.cfg &
    #exec start-stop-daemon -S -x \${DAEMON}
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

do_install() {
	install -d ${D}/usr/bin
	install -m 0755 ${S}/CCcam.${TARGET_ARCH} ${D}${bindir}/${CAMNAME}_${PV}-r${EMUVERSION}
	install -d ${D}/etc
	install -m 0644 ${S}${CONF_FILES}/CCcam.cfg ${D}/etc/CCcam.cfg
	install -m 0644 ${S}${CONF_FILES}/CCcam.prio ${D}/etc/CCcam.prio
}

# Install routine, should be ok for most cams.
do_install:append () {
	install -d ${D}${prefix}/camscript
	install -m 755 ${S}/${CAMNAME}_${PV}-r${EMUVERSION}.sh ${D}${prefix}/camscript/${CAMNAME}_${PV}-r${EMUVERSION}.sh
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
    rm -rf ${bindir}/${CAMNAME}_${PV}-r${EMUVERSION} > /dev/null 2>&1
    rm -rf ${prefix}/camscript/${CAMNAME}_${PV}-r${EMUVERSION}.sh > /dev/null 2>&1
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
    if [ -s /usr/lib/dpkg/info/enigma2-plugin-softcams-dreamosat-cccam.list ] ||
    [ -s /var/lib/dpkg/info/enigma2-plugin-softcams-dreamosat-cccam.list ]; then
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
    if [ -s /usr/lib/opkg/info/enigma2-plugin-softcams-dreamosat-cccam.list ] ||
    [ -s /var/lib/opkg/info/enigma2-plugin-softcams-dreamosat-cccam.list ]; then
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

INSANE_SKIP:${PN} = "file-rdeps already-stripped"

