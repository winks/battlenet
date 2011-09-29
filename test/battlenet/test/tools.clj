(ns battlenet.test.tools
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.model])
  (:use [battlenet.tools])
  (:use [battlenet.network])
  (:use [clojure.test])
  (:use [clojure.walk]))

(def mock-map-single {:realms 
               [{:type "pvp",
                 :queue false,
                 :status true,
                 :population "high",
                 :name "Aegwynn",
                 :battlegroup "Blutdurst",
                 :slug "aegwynn"}]})

(def mock-map-multiple {:realms 
               [{:type "pvp",
                 :queue false,
                 :status true,
                 :population "high",
                 :name "Aegwynn",
                 :battlegroup "Blutdurst",
                 :slug "aegwynn"},
                {:type "pve",
                 :queue false,
                 :status true,
                 :population "medium",
                 :name "Aerie Peak",
                 :battlegroup "Misery",
                 :slug "aerie-peak"}
                ]})

(def mock-char
  {:achievementPoints 1234,
   :lastModified 1311956422000,
   :gender 1,
   :class 4,
   :name "Humanrogue",
   :realm "Malygos",
   :thumbnail "malygos/1/12312312-avatar.jpg",
   :race 1,
   :level 85})


(deftest test-join-params-1
  (is
    (.equals "realms=aegwynn"
             (join-params ["realms" "aegwynn"]))))

(deftest test-join-params-2
  (is
    (.equals "realms=aegwynn,aerie-peak"
             (join-params ["realms" "aegwynn" "aerie-peak"]))))

(deftest test-create-url-1
  (is
    (.equals "https://eu.battle.net/api/wow/realm/status?realms=aegwynn"
             (create-url "eu" "wow" "/realm/status" "realms=aegwynn"))))

(deftest test-create-url-2
  (is
    (.equals "https://eu.battle.net/api/wow/realm/status?realms=aegwynn,aerie-peak"
             (create-url "eu" "wow" "/realm/status" "realms=aegwynn,aerie-peak"))))

(deftest test-create-url-item-1
  (is
    (.equals "https://eu.battle.net/api/wow/item/1234?"
             (create-url-item "eu" "wow" "/item/{id}" 1234))))

(deftest test-create-url-item-2
  (is
    (.equals "https://eu.battle.net/api/wow/item/1234?"
             (create-url-item "eu" "wow" "/item/{id}" "1234"))))

(deftest test-create-url-character
  (is
    (.equals "https://eu.battle.net/api/wow/character/aegwynn/asdf?"
             (create-url-character "eu"
                                   "wow"
                                   "/character/{realm}/{name}"
                                   "aegwynn"
                                   "asdf"))))

(deftest test-create-url-guild
  (is
    (.equals "https://eu.battle.net/api/wow/guild/aegwynn/asdf?"
             (create-url-guild "eu"
                               "wow"
                               "/guild/{realm}/{name}"
                               "aegwynn"
                               "asdf"))))

(deftest test-media-url-icon
  (is
    (.equals
      "http://eu.media.blizzard.com/wow/icons/18/inv_bracer_leatherraidrogue_i_01.jpg"
      (media-url-icon "eu" "wow" "small" "inv_bracer_leatherraidrogue_i_01"))))

(deftest test-access-realm-map
  (is
    (.equals "Aegwynn"
             (access-rmap mock-map-single :name))))

(deftest test-rmap-to-brealm
  (is
    (.equals "Aegwynn"
             (get (rmap-to-brealm mock-map-single) :name))))

(deftest test-get-names-1
  (is
    (.equals "Aegwynn"
             (get-name (first (get mock-map-multiple :realms))))))

(deftest test-get-names-2
  (is
    (.equals ["Aegwynn" "Aerie Peak"]
             (map get-name (get mock-map-multiple :realms)))))

(deftest test-cmap-to-bcharacter-1
  (is
    (.equals "Humanrogue"
             (:name (cmap-to-bcharacter mock-char)))))


(deftest test-cmap-to-bcharacter-2
  (is
    (.equals "Rogue"
             (nth bn-classes (:class (cmap-to-bcharacter mock-char))))))

(deftest test-cmap-to-bcharacter-3
  (is
    (.equals "Human"
             (nth bn-races (:race (cmap-to-bcharacter mock-char))))))