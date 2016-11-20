TARGET?=/tmp
FILENAME?=battlenet-wowexample.html
UPD := $(shell date)

chars:
	cat resources/head.html > ${TARGET}/${FILENAME}.tmp
	lein run >> ${TARGET}/${FILENAME}.tmp
	cat resources/foot.html | sed "s,</div></body>,<div class="update">Last update: $(UPD)</div>\n</div></body>," >> ${TARGET}/${FILENAME}.tmp
	mv -f ${TARGET}/${FILENAME}.tmp ${TARGET}/${FILENAME}
	cp -f resources/main.css ${TARGET}/main.css


.PHONY: chars
