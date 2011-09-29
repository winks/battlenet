(ns battlenet.tools
  (:use [battlenet.defs])
  (:import [battlenet.model BRealm BCharacter BGuild BProfession BItem BReputation]))

(defn join-params
  "Joins URL parameters."
  [params]
  (str (first params) "=" (apply str (interpose \, (nthnext params 1)))))

(defn create-url
  "Builds a request URL."
  [region game path params]
    (.replace
      (.replace
        (.replace
          (.replace bn-baseurl "{region}" region)
          "{game}" game)
        "{path}" path)
    "{params}"  params))

(defn create-url-item
  "Builds a request URL for item requests."
  [region game path itemid]
   (.replace
     (create-url region game path "") "{id}" 
     (if (integer? itemid) (Integer/toString itemid) itemid)))

(defn create-url-character
  "Builds a request URL for character requests."
  ([region game path realm charname]
    (.replace
      (.replace
        (create-url region game path "") "{realm}" realm)
      "{name}" charname))
  ([region game path realm charname params]
    (.replace
      (.replace
        (create-url region game path params) "{realm}" realm)
      "{name}" charname)))

(defn create-url-guild
  "Builds a request URL for guild requests."
  ([region game path realm guildname]
    (create-url-character region game path realm guildname))
  ([region game path realm guildname params]
    (create-url-character region game path realm guildname params)))

(defn media-url-icon
  "Builds an icon URL."
  [region game size icon]
  (.replace
    (.replace
      (.replace
       (.replace bn-media-icon "{region}" region)
       "{game}" game)
      "{size}" (if (.equals "small" size) "18" "56"))
    "{icon}" icon))

(defn access-rmap
  "Access a member of a realmsmap"
  [rsmap crit]
  (get (first (get rsmap :realms)) crit))

(defn get-name
  "Get a name from a rmap."
  [rmap]
  (get rmap :name))

(defn rmap-to-brealm
  "Convert a map of a realm to a BRealm."
  [rmap]
  (BRealm.
    (access-rmap rmap :type)
    (access-rmap rmap :queue)
    (access-rmap rmap :status)
    (access-rmap rmap :population)
    (access-rmap rmap :name)
    (access-rmap rmap :battlegroup)
    (access-rmap rmap :slug)
    ""))

(defn cmap-to-bcharacter
  "Convert a map of a character to a BCharacter."
  [cmap]
  (BCharacter.
    (:name cmap)
    ""
    (:class cmap)
    (:race cmap)
    (:gender cmap)
    (:level cmap)
    (:achPoints cmap)
    (:thumbnail cmap)
    (:lastModified cmap)))

(defn pmap-to-bprofession
  "Convert a map of a profession to a BProfession."
  [prmap]
  (BProfession.
    (:name prmap)
    (:id prmap)
    (:icon prmap)
    (:rank prmap)
    (:max prmap)
    (:recipes prmap)))

(defn repmap-to-breputation
  "Convert a map of reputation data to a BReputation."
  [repmap]
  (BReputation.
    (:name repmap)
    (:id repmap)
    (:standing repmap)
    (:value repmap)
    (:max repmap)))
