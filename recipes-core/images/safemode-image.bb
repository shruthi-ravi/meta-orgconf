SUMMARY = "Safemode image for older nilrt"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "\
	file://grubenv_non_ni_target \
	file://unicode.pf2 \
"

RDEPENDS_${PN} += "grub-efi grub"
COMPATIBLE_MACHINE = "x64"

inherit build-services
EXPORTS_TO_FETCH = "\
	 nilinux/os-common/export/7.0/7.0.0d46/standard_x64_safemode.tar.gz \
"

export SAFEMODE_PAYLOAD_PATH

do_install() {
	mkdir -p ${D}/payload/fonts

	SAFEMODE_PAYLOAD="${SAFEMODE_PAYLOAD_PATH:-${BS_EXPORT_DATA}}/standard_x64_safemode.tar.gz"

	echo SAFEMODE_PAYLOAD_PATH = ${SAFEMODE_PAYLOAD_PATH}
	echo SAFEMODE_PAYLOAD = ${SAFEMODE_PAYLOAD}

	tar -xf ${SAFEMODE_PAYLOAD} -C ${D}/payload

	cp ${WORKDIR}/grubenv_non_ni_target	${D}/payload
	cp ${WORKDIR}/unicode.pf2		${D}/payload/fonts

	GRUB_VERSION=$(echo ${GRUB_BRANCH} | cut -d "/" -f 2)

	echo "BUILD_IDENTIFIER=${BUILD_IDENTIFIER}" > ${D}/payload/imageinfo
	echo "GRUB_VERSION=${GRUB_VERSION}.0" >> ${D}/payload/imageinfo
}

# always invalidate the sstate-cache for do_install as we have the SAFEMODE_PAYLOAD_PATH
# var which is identical across builds
do_install[nostamp] = "1"

FILES_${PN} = "\
	/payload \
"
