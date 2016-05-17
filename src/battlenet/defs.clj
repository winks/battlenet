(ns battlenet.defs)

;
; URL templates and paths
;
(def bn-baseurl         "https://{region}.api.battle.net/{game}{path}{qp}{params}")
(def bn-media-icon
  "http://{region}.media.blizzard.com/{game}/icons/{size}/{icon}.jpg")
(def bn-media-avatar
  "http://{region}.battle.net/static-render/{region}/{thumbnail}")

(def bn-game-wow "wow")
(def bn-game-d3 "d3")

(def bn-genders
  {0 "Male",
   1 "Female"})
