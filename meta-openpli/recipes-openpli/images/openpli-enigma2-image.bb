require openpli-image.bb

KERNEL_WIFI_DRIVERS = " \
	firmware-carl9170 \
	firmware-htc7010 \
	firmware-htc9271 \
	firmware-rt2870 \
	firmware-rt73 \
	firmware-rtl8712u \
	firmware-zd1211 \
	\
	kernel-module-ath9k-htc \
	kernel-module-carl9170 \
	kernel-module-r8712u \
	kernel-module-rt2500usb \
	kernel-module-rt2800usb \
	kernel-module-rt73usb \
	kernel-module-rtl8187 \
	kernel-module-zd1211rw \
	"

EXTERNAL_WIFI_DRIVERS = " \
	firmware-rtl8192eu \
	firmware-rtl8188eu \
	\
	rtl8188eu \
	rtl8192eu \
	"

ENIGMA2_PLUGINS = " \
	enigma2-plugin-extensions-dreamosat-cammanager \
	enigma2-plugin-extensions-dreamosat-downloader \
	enigma2-plugin-extensions-dreamosat-keyupdater \
	enigma2-plugin-extensions-dreamosat-script \
	enigma2-plugin-extensions-epgimport \
	enigma2-plugin-extensions-xstreamity \
	enigma2-plugin-extensions-xtraevent \
	enigma2-plugin-extensions-socketmmi \
	enigma2-plugin-extensions-subssupport \
	\
	enigma2-plugin-softcams-dreamosat-cccam \
	enigma2-plugin-softcams-dreamosat-ncam \
	enigma2-plugin-softcams-dreamosat-oscam \
	enigma2-plugin-softcams-dreamosat-oscam-emu \
	enigma2-plugin-softcams-dreamosat-oscam-smod \
	\
	enigma2-plugin-settings-dreamosat \
	\
	enigma2-plugin-extensions-audiosync \
	enigma2-plugin-extensions-autobackup \
	enigma2-plugin-extensions-bitrate \
	enigma2-plugin-extensions-cdinfo \
	enigma2-plugin-extensions-cutlisteditor \
	enigma2-plugin-extensions-dvdplayer \
	enigma2-plugin-extensions-filecommander \
	enigma2-plugin-extensions-foreca \
	enigma2-plugin-extensions-graphmultiepg \
	enigma2-plugin-extensions-mediaplayer \
	enigma2-plugin-extensions-mediascanner \
	enigma2-plugin-extensions-moviecut \
	enigma2-plugin-extensions-netcaster \
	enigma2-plugin-extensions-openwebif \
	enigma2-plugin-extensions-oscamstatus \
	enigma2-plugin-extensions-pictureplayer \
	enigma2-plugin-extensions-systemtools \
	enigma2-plugin-extensions-tmbd \
	enigma2-plugin-extensions-youtube \
	\
	enigma2-plugin-systemplugins-cablescan \
	enigma2-plugin-systemplugins-commoninterfaceassignment \
	enigma2-plugin-systemplugins-fastscan \
	enigma2-plugin-systemplugins-hdmicec \
	enigma2-plugin-systemplugins-hotplug \
	enigma2-plugin-systemplugins-networkbrowser \
	enigma2-plugin-systemplugins-osd3dsetup \
	enigma2-plugin-systemplugins-osdpositionsetup \
	enigma2-plugin-systemplugins-positionersetup \
	enigma2-plugin-systemplugins-satfinder \
	enigma2-plugin-systemplugins-softwaremanager \
	enigma2-plugin-systemplugins-videoenhancement \
	enigma2-plugin-systemplugins-videomode \
	enigma2-plugin-systemplugins-videotune \
	enigma2-plugin-systemplugins-wirelesslan \
	enigma2-plugin-systemplugins-serviceapp \
	"

DEPENDS += " \
	enigma2 \
	package-index \
	"

IMAGE_INSTALL += " \
	aio-grab \
	cdtextinfo \
	enigma2 \
	libavahi-client \
	ntpdate \
	settings-autorestore \
	tuxbox-common \
	wget \
	bash \
	bzip2 \
	ca-certificates \
	libcurl \
	ntfs-3g \
	openresolv \
	openssh-sftp \
	openssh-sftp-server \
	openvpn \
	perl \
	pigz \
	python3-audio \
	python3-compression \
	python3-email \
	python3-fcntl \
	python3-future \
	python3-html \
	python3-image \
	python3-imaging \
	python3-ipaddress  \
	python3-json \
	python3-mechanize \
	python3-misc \
	python3-mmap \
	python3-netclient \
	python3-netifaces \
	python3-numbers \
	python3-pkgutil \
	python3-pip \
	python3-pprint \
	python3-pycrypto \
	python3-pyopenssl \
	python3-pysmb \
	python3-requests \
	python3-shell \
	python3-six \
	python3-sqlite3 \
	python3-twisted-core \
	python3-twisted-web \
	python3-xml \
	tar \
	xz \
	${ENIGMA2_PLUGINS} \
	${KERNEL_WIFI_DRIVERS} \
	${EXTERNAL_WIFI_DRIVERS} \
	"

export IMAGE_BASENAME = "openpli-enigma2"
