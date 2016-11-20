TARGET?=/tmp
FILENAME?=battlenet-wowexample.html
UPD := $(shell date)

chars:
	cat resources/head.html > ${TARGET}/${FILENAME}.tmp
	lein run >> ${TARGET}/${FILENAME}.tmp
	cat resources/foot.html | sed "s,</div></body>,<div class="update">Last update: $(UPD)</div>\n</div></body>," >> ${TARGET}/${FILENAME}.tmp
	mv -f ${TARGET}/${FILENAME}.tmp ${TARGET}/${FILENAME}
	cp -f resources/main.css ${TARGET}/main.css

rep:
	cat resources/head-rep.html > ${TARGET}/rep-${FILENAME}.tmp
	lein run rep >> ${TARGET}/rep-${FILENAME}.tmp
	cat resources/foot.html | sed "s,</div></body>,<div class="update">Last update: $(UPD)</div>\n</div></body>," >> ${TARGET}/rep-${FILENAME}.tmp
	mv -f ${TARGET}/rep-${FILENAME}.tmp ${TARGET}/rep-${FILENAME}
	cp -f resources/main.css ${TARGET}/main.css

.PHONY: chars
