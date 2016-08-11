TARGET?=/tmp
FILENAME?=battlenet-wowexample.html

chars:
	cat resources/head.html > ${TARGET}/${FILENAME}.tmp
	lein run >> ${TARGET}/${FILENAME}.tmp
	cat resources/foot.html >> ${TARGET}/${FILENAME}.tmp
	mv -f ${TARGET}/${FILENAME}.tmp ${TARGET}/${FILENAME}
	cp -f resources/main.css ${TARGET}/main.css


.PHONY: chars
