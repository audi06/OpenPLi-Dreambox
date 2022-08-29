SUMMARY = "A complete, cross-platform solution to record, convert and stream audio and video."
DESCRIPTION = "FFmpeg is the leading multimedia framework, able to decode, encode, transcode, \
               mux, demux, stream, filter and play pretty much anything that humans and machines \
               have created. It supports the most obscure ancient formats up to the cutting edge."
HOMEPAGE = "https://www.ffmpeg.org/"
SECTION = "libs"

LICENSE = "GPL-2.0-or-later & LGPL-2.1-or-later & ISC & MIT & BSD-2-Clause & BSD-3-Clause & IJG"
LICENSE:${PN} = "GPL-2.0-or-later"
LICENSE:libavcodec = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavdevice = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavfilter = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavformat = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavresample = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libavutil = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libpostproc = "GPL-2.0-or-later"
LICENSE:libswresample = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE:libswscale = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPL-2.0-or-later', 'LGPL-2.1-or-later', d)}"
LICENSE_FLAGS = "commercial"

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=bd7a443320af8c812e4c18d1b79df004 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

GIT_SITE = "${@ 'git://gitlab.com/jack2015' if d.getVar('CODEWEBSITE') else 'git://gitee.com/jackgee2021'}"

SRC_URI = "${GIT_SITE}/FFmpeg.git;protocol=https;branch=release/4.4 \
           file://0001-libavutil-include-assembly-with-full-path-from-sourc.patch \
           file://0002-fix-mpegts.patch \
           file://0003-allow-to-choose-rtmp-impl-at-runtime.patch \
           file://0004-hls-replace-key-uri.patch \
           file://0005-mips64-cpu-detection.patch \
           file://0006-optimize-aac.patch \
           file://0007-increase-buffer-size.patch \
           file://0008-recheck-discard-flags.patch \
           file://0009-ffmpeg-fix-edit-list-parsing.patch \
           file://0011-rtsp.patch \
           file://0012-dxva2.patch \
           "

S = "${WORKDIR}/git"

# Build fails when thumb is enabled: https://bugzilla.yoctoproject.org/show_bug.cgi?id=7717
ARM_INSTRUCTION_SET:armv4 = "arm"
ARM_INSTRUCTION_SET:armv5 = "arm"
ARM_INSTRUCTION_SET:armv6 = "arm"

# Should be API compatible with libav (which was a fork of ffmpeg)
# libpostproc was previously packaged from a separate recipe
PROVIDES = "libav libpostproc"

DEPENDS = "nasm-native libxml2"

PV = "4.4.2"

inherit autotools pkgconfig gitpkgv

PACKAGECONFIG ??= "avdevice avfilter avcodec avformat swresample swscale postproc avresample \
                   alsa bzlib gpl libass libbluray libdav1d libfreetype librtmp libv4l2 libvorbis \
                   lzma mp3lame openjpeg openssl pic pthreads shared theora vpx x264 x265 zlib \
                   ${@bb.utils.contains('AVAILTUNES', 'mips32r2', 'mips32r2', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xv xcb', '', d)} \
"

PACKAGECONFIG[avdevice] = "--enable-avdevice,--disable-avdevice"
PACKAGECONFIG[avfilter] = "--enable-avfilter,--disable-avfilter"
PACKAGECONFIG[avcodec] = "--enable-avcodec,--disable-avcodec"
PACKAGECONFIG[avformat] = "--enable-avformat,--disable-avformat"
PACKAGECONFIG[swresample] = "--enable-swresample,--disable-swresample"
PACKAGECONFIG[swscale] = "--enable-swscale,--disable-swscale"
PACKAGECONFIG[postproc] = "--enable-postproc,--disable-postproc"
PACKAGECONFIG[avresample] = "--enable-avresample,--disable-avresample"
PACKAGECONFIG[alsa] = "--enable-alsa,--disable-alsa,alsa-lib"
PACKAGECONFIG[bzlib] = "--enable-bzlib,--disable-bzlib,bzip2"
PACKAGECONFIG[gpl] = "--enable-gpl,--disable-gpl"
PACKAGECONFIG[libass] = "--enable-libass,--disable-libass,libass"
PACKAGECONFIG[libbluray] = "--enable-libbluray --enable-protocol=bluray,--disable-libbluray,libbluray"
PACKAGECONFIG[libdav1d] = "--enable-libdav1d,--disable-libdav1d,libdav1d"
PACKAGECONFIG[libfreetype] = "--enable-libfreetype,--disable-libfreetype,freetype"
PACKAGECONFIG[librtmp] = "--enable-librtmp,--disable-librtmp,librtmp rtmpdump"
PACKAGECONFIG[libv4l2] = "--enable-libv4l2,--disable-libv4l2,v4l-utils"
PACKAGECONFIG[libvorbis] = "--enable-libvorbis,--disable-libvorbis,libvorbis"
PACKAGECONFIG[lzma] = "--enable-lzma,--disable-lzma,xz"
PACKAGECONFIG[mp3lame] = "--enable-libmp3lame,--disable-libmp3lame,lame"
PACKAGECONFIG[openjpeg] = "--enable-libopenjpeg,--disable-libopenjpeg,openjpeg"
PACKAGECONFIG[openssl] = "--enable-openssl,--disable-openssl,openssl"
PACKAGECONFIG[pic] = "--enable-pic"
PACKAGECONFIG[pthreads] = "--enable-pthreads,--disable-pthreads"
PACKAGECONFIG[shared] = "--enable-shared"
PACKAGECONFIG[theora] = "--enable-libtheora,--disable-libtheora,libtheora libogg"
PACKAGECONFIG[vpx] = "--enable-libvpx,--disable-libvpx,libvpx"
PACKAGECONFIG[x264] = "--enable-libx264,--disable-libx264,x264"
PACKAGECONFIG[x265] = "--enable-libx265,--disable-libx265,x265"
PACKAGECONFIG[zlib] = "--enable-zlib,--disable-zlib,zlib"
PACKAGECONFIG[mips32r2] = ",--disable-mipsdsp --disable-mipsdspr2"
PACKAGECONFIG[xcb] = "--enable-libxcb,--disable-libxcb,libxcb"
PACKAGECONFIG[xv] = "--enable-outdev=xv,--disable-outdev=xv,libxv"

# Check codecs that require --enable-nonfree
USE_NONFREE = "${@bb.utils.contains_any('PACKAGECONFIG', [ 'openssl' ], 'yes', '', d)}"

def cpu(d):
    for arg in (d.getVar('TUNE_CCARGS') or '').split():
        if arg.startswith('-mcpu='):
            return arg[6:]
    return 'generic'

EXTRA_OECONF = " \
    ${@bb.utils.contains('USE_NONFREE', 'yes', '--enable-nonfree', '', d)} \
    \
    --cross-prefix=${TARGET_PREFIX} \
    \
    --ld='${CCLD}' \
    --cc='${CC}' \
    --cxx='${CXX}' \
    --arch=${TARGET_ARCH} \
    --target-os='linux' \
    --enable-cross-compile \
    --extra-cflags='${CFLAGS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}' \
    --extra-ldflags='${LDFLAGS}' \
    --sysroot='${STAGING_DIR_TARGET}' \
    ${EXTRA_FFCONF} \
    --libdir=${libdir} \
    --shlibdir=${libdir} \
    --datadir=${datadir}/ffmpeg \
    --cpu=${@cpu(d)} \
    --pkg-config=pkg-config \
"

EXTRA_OECONF:append:linux-gnux32 = " --disable-asm"

EXTRA_OECONF += "${@bb.utils.contains('TUNE_FEATURES', 'mipsisa64r6', '--disable-mips64r2 --disable-mips32r2', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('TUNE_FEATURES', 'mipsisa64r2', '--disable-mips64r6 --disable-mips32r6', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('TUNE_FEATURES', 'mips32r2', '--disable-mips64r6 --disable-mips32r6', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('TUNE_FEATURES', 'mips32r6', '--disable-mips64r2 --disable-mips32r2', '', d)}"
EXTRA_OECONF:append:mips = " --extra-libs=-latomic --disable-mips32r5 --disable-mipsdsp --disable-mipsdspr2 \
                             --disable-loongson2 --disable-loongson3 --disable-mmi --disable-msa --disable-msa2"
EXTRA_OECONF:append:riscv32 = " --extra-libs=-latomic"
EXTRA_OECONF:append:armv5 = " --extra-libs=-latomic"

# gold crashes on x86, another solution is to --disable-asm but thats more hacky
# ld.gold: internal error in relocate_section, at ../../gold/i386.cc:3684

LDFLAGS:append:x86 = "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', ' -fuse-ld=bfd ', '', d)}"

EXTRA_OEMAKE = "V=1"

do_configure() {
    ${S}/configure ${EXTRA_OECONF}
}

# patch out build host paths for reproducibility
do_compile:prepend:class-target() {
        sed -i -e "s,${WORKDIR},,g" ${B}/config.h
}

PACKAGES =+ "libavcodec \
             libavdevice \
             libavfilter \
             libavformat \
             libavresample \
             libavutil \
             libpostproc \
             libswresample \
             libswscale"

FILES:libavcodec = "${libdir}/libavcodec${SOLIBS}"
FILES:libavdevice = "${libdir}/libavdevice${SOLIBS}"
FILES:libavfilter = "${libdir}/libavfilter${SOLIBS}"
FILES:libavformat = "${libdir}/libavformat${SOLIBS}"
FILES:libavresample = "${libdir}/libavresample${SOLIBS}"
FILES:libavutil = "${libdir}/libavutil${SOLIBS}"
FILES:libpostproc = "${libdir}/libpostproc${SOLIBS}"
FILES:libswresample = "${libdir}/libswresample${SOLIBS}"
FILES:libswscale = "${libdir}/libswscale${SOLIBS}"

# ffmpeg disables PIC on some platforms (e.g. x86-32)
INSANE_SKIP:${MLPREFIX}libavcodec = "textrel"
INSANE_SKIP:${MLPREFIX}libavdevice = "textrel"
INSANE_SKIP:${MLPREFIX}libavfilter = "textrel"
INSANE_SKIP:${MLPREFIX}libavformat = "textrel"
INSANE_SKIP:${MLPREFIX}libavutil = "textrel"
INSANE_SKIP:${MLPREFIX}libavresample = "textrel"
INSANE_SKIP:${MLPREFIX}libswscale = "textrel"
INSANE_SKIP:${MLPREFIX}libswresample = "textrel"
INSANE_SKIP:${MLPREFIX}libpostproc = "textrel"

MIPSFPU = "${@bb.utils.contains('TARGET_FPU', 'soft', '--disable-mipsfpu', '--enable-mipsfpu', d)}"

EXTRA_FFCONF = " \
	--prefix=${prefix} \
	--disable-runtime-cpudetect \
	--disable-ffplay \
	--enable-ffprobe \
	\
	--disable-doc \
	--disable-htmlpages \
	--disable-manpages \
	--disable-podpages \
	--disable-txtpages \
	\
	--disable-altivec \
	--disable-amd3dnow \
	--disable-amd3dnowext \
	--disable-mmx \
	--disable-mmxext \
	--disable-sse \
	--disable-sse2 \
	--disable-sse3 \
	--disable-ssse3 \
	--disable-sse4 \
	--disable-sse42 \
	--disable-avx \
	--disable-xop \
	--disable-fma3 \
	--disable-fma4 \
	--disable-avx2 \
	--disable-inline-asm \
	--disable-yasm \
	--disable-x86asm \
	--disable-fast-unaligned \
	\
	--disable-dxva2 \
	--disable-vaapi \
	--disable-vdpau \
	\
	--disable-muxers \
	--enable-muxer=apng \
	--enable-muxer=flac \
	--enable-muxer=mp3 \
	--enable-muxer=h261 \
	--enable-muxer=h263 \
	--enable-muxer=h264 \
	--enable-muxer=h265 \
	--enable-muxer=hevc \
	--enable-muxer=image2 \
	--enable-muxer=image2pipe \
	--enable-muxer=m4v \
	--enable-muxer=matroska \
	--enable-muxer=mjpeg \
	--enable-muxer=mp4 \
	--enable-muxer=mpeg1video \
	--enable-muxer=mpeg2video \
	--enable-muxer=mpegts \
	--enable-muxer=ogg \
	\
	--disable-parsers \
	--enable-parser=aac \
	--enable-parser=aac_latm \
	--enable-parser=ac3 \
	--enable-parser=dca \
	--enable-parser=dvbsub \
	--enable-parser=dvd_nav \
	--enable-parser=dvdsub \
	--enable-parser=flac \
	--enable-parser=h264 \
	--enable-parser=h265 \
	--enable-parser=hevc \
	--enable-parser=mjpeg \
	--enable-parser=mpeg4video \
	--enable-parser=mpegvideo \
	--enable-parser=mpegaudio \
	--enable-parser=png \
	--enable-parser=vc1 \
	--enable-parser=vorbis \
	--enable-parser=vp8 \
	--enable-parser=vp9 \
	\
	--disable-encoders \
	--enable-encoder=aac \
	--enable-encoder=h261 \
	--enable-encoder=h263 \
	--enable-encoder=h263p \
	--enable-encoder=jpeg2000 \
	--enable-encoder=jpegls \
	--enable-encoder=ljpeg \
	--enable-encoder=libx264 \
	--enable-encoder=mjpeg \
	--enable-encoder=mpeg1video \
	--enable-encoder=mpeg2video \
	--enable-encoder=mpeg4 \
	--enable-encoder=png \
	--enable-encoder=rawvideo \
	\
	--disable-decoders \
	--enable-decoder=aac \
	--enable-decoder=aac_latm \
	--enable-decoder=adpcm_ct \
	--enable-decoder=adpcm_g722 \
	--enable-decoder=adpcm_g726 \
	--enable-decoder=adpcm_g726le \
	--enable-decoder=adpcm_ima_amv \
	--enable-decoder=adpcm_ima_oki \
	--enable-decoder=adpcm_ima_qt \
	--enable-decoder=adpcm_ima_rad \
	--enable-decoder=adpcm_ima_wav \
	--enable-decoder=adpcm_ms \
	--enable-decoder=adpcm_sbpro_2 \
	--enable-decoder=adpcm_sbpro_3 \
	--enable-decoder=adpcm_sbpro_4 \
	--enable-decoder=adpcm_swf \
	--enable-decoder=adpcm_yamaha \
	--enable-decoder=alac \
	--enable-decoder=ape \
	--enable-decoder=atrac1 \
	--enable-decoder=atrac3 \
	--enable-decoder=atrac3p \
	--enable-decoder=ass \
	--enable-decoder=cook \
	--enable-decoder=dca \
	--enable-decoder=dsd_lsbf \
	--enable-decoder=dsd_lsbf_planar \
	--enable-decoder=dsd_msbf \
	--enable-decoder=dsd_msbf_planar \
	--enable-decoder=dvbsub \
	--enable-decoder=dvdsub \
	--enable-decoder=eac3 \
	--enable-decoder=evrc \
	--enable-decoder=flac \
	--enable-decoder=g723_1 \
	--enable-decoder=g729 \
	--enable-decoder=h261 \
	--enable-decoder=h263 \
	--enable-decoder=h263i \
	--enable-decoder=h264 \
	--enable-decoder=h265 \
	--enable-decoder=hevc \
	--enable-decoder=iac \
	--enable-decoder=imc \
	--enable-decoder=jpeg2000 \
	--enable-decoder=jpegls \
	--enable-decoder=mace3 \
	--enable-decoder=mace6 \
	--enable-decoder=metasound \
	--enable-decoder=mjpeg \
	--enable-decoder=mlp \
	--enable-decoder=movtext \
	--enable-decoder=mp1 \
	--enable-decoder=mp2 \
	--enable-decoder=mp3 \
	--enable-decoder=mp3adu \
	--enable-decoder=mp3on4 \
	--enable-decoder=mpeg1video \
	--enable-decoder=mpeg2video \
	--enable-decoder=mpeg4 \
	--enable-decoder=nellymoser \
	--enable-decoder=opus \
	--enable-decoder=pcm_alaw \
	--enable-decoder=pcm_bluray \
	--enable-decoder=pcm_dvd \
	--enable-decoder=pcm_f32be \
	--enable-decoder=pcm_f32le \
	--enable-decoder=pcm_f64be \
	--enable-decoder=pcm_f64le \
	--enable-decoder=pcm_lxf \
	--enable-decoder=pcm_mulaw \
	--enable-decoder=pcm_s16be \
	--enable-decoder=pcm_s16be_planar \
	--enable-decoder=pcm_s16le \
	--enable-decoder=pcm_s16le_planar \
	--enable-decoder=pcm_s24be \
	--enable-decoder=pcm_s24daud \
	--enable-decoder=pcm_s24le \
	--enable-decoder=pcm_s24le_planar \
	--enable-decoder=pcm_s32be \
	--enable-decoder=pcm_s32le \
	--enable-decoder=pcm_s32le_planar \
	--enable-decoder=pcm_s8 \
	--enable-decoder=pcm_s8_planar \
	--enable-decoder=pcm_u16be \
	--enable-decoder=pcm_u16le \
	--enable-decoder=pcm_u24be \
	--enable-decoder=pcm_u24le \
	--enable-decoder=pcm_u32be \
	--enable-decoder=pcm_u32le \
	--enable-decoder=pcm_u8 \
	--enable-decoder=pcm_zork \
	--enable-decoder=pgssub \
	--enable-decoder=png \
	--enable-decoder=qcelp \
	--enable-decoder=qdm2 \
	--enable-decoder=ra_144 \
	--enable-decoder=ra_288 \
	--enable-decoder=ralf \
	--enable-decoder=s302m \
	--enable-decoder=sipr \
	--enable-decoder=shorten \
	--enable-decoder=sonic \
	--enable-decoder=srt \
	--enable-decoder=ssa \
	--enable-decoder=subrip \
	--enable-decoder=subviewer \
	--enable-decoder=subviewer1 \
	--enable-decoder=tak \
	--enable-decoder=text \
	--enable-decoder=truehd \
	--enable-decoder=truespeech \
	--enable-decoder=tta \
	--enable-decoder=vorbis \
	--enable-decoder=wmalossless \
	--enable-decoder=wmapro \
	--enable-decoder=wmav1 \
	--enable-decoder=wmav2 \
	--enable-decoder=wmavoice \
	--enable-decoder=wavpack \
	--enable-decoder=xsub \
	\
	--disable-demuxers \
	--enable-demuxer=aac \
	--enable-demuxer=ac3 \
	--enable-demuxer=apng \
	--enable-demuxer=ass \
	--enable-demuxer=avi \
	--enable-demuxer=dts \
	--enable-demuxer=dash \
	--enable-demuxer=ffmetadata \
	--enable-demuxer=flac \
	--enable-demuxer=flv \
	--enable-demuxer=h264 \
	--enable-demuxer=h265 \
	--enable-demuxer=hls \
	--enable-demuxer=image2 \
	--enable-demuxer=image2pipe \
	--enable-demuxer=image_bmp_pipe \
	--enable-demuxer=image_jpeg_pipe \
	--enable-demuxer=image_jpegls_pipe \
	--enable-demuxer=image_png_pipe \
	--enable-demuxer=m4v \
	--enable-demuxer=matroska \
	--enable-demuxer=mjpeg \
	--enable-demuxer=mov \
	--enable-demuxer=mp3 \
	--enable-demuxer=mpegts \
	--enable-demuxer=mpegtsraw \
	--enable-demuxer=mpegps \
	--enable-demuxer=mpegvideo \
	--enable-demuxer=mpjpeg \
	--enable-demuxer=ogg \
	--enable-demuxer=pcm_s16be \
	--enable-demuxer=pcm_s16le \
	--enable-demuxer=realtext \
	--enable-demuxer=rawvideo \
	--enable-demuxer=rm \
	--enable-demuxer=rtp \
	--enable-demuxer=rtsp \
	--enable-demuxer=srt \
	--enable-demuxer=vc1 \
	--enable-demuxer=wav \
	--enable-demuxer=webm_dash_manifest \
	\
	--disable-filters \
	--enable-filter=scale \
	--enable-filter=drawtext \
	--enable-filter=overlay \
	\
	--enable-zlib \
	--enable-bzlib \
	--enable-openssl \
	--enable-libass \
	--enable-bsfs \
	--disable-xlib \
	--disable-libxcb \
	--disable-libxcb-shm \
	--disable-libxcb-xfixes \
	--disable-libxcb-shape \
	\
	--enable-shared \
	--enable-network \
	--enable-nonfree \
	--enable-small \
	--enable-stripping \
	--disable-static \
	--disable-debug \
	--disable-runtime-cpudetect \
	--enable-pic \
	--enable-pthreads \
	--enable-hardcoded-tables \
	\
	${@bb.utils.contains("TARGET_ARCH", "mipsel", "${MIPSFPU} --extra-libs=-latomic --disable-mips32r5 --disable-mipsdsp --disable-mipsdspr2 \
			--disable-loongson2 --disable-loongson3 --disable-mmi --disable-msa --disable-msa2", "", d)} \
	${@bb.utils.contains("TARGET_ARCH", "arm", "--enable-armv6 --enable-armv6t2 --enable-vfp --enable-neon", "", d)} \
	${@bb.utils.contains("TUNE_FEATURES", "aarch64", "--enable-armv8 --enable-vfp --enable-neon", "", d)} \
"
