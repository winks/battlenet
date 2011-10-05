(ns battlenet.test.tools
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:use [battlenet.network])
  (:use [battlenet.test.mock])
  (:use [clojure.test]))

;;;;;;;;;;;;;
; URL stuff
;;;;;;;;;;;;;

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

(deftest test-create-url-character-1
  (is
    (.equals "https://eu.battle.net/api/wow/character/aegwynn/asdf?"
             (create-url-character "eu"
                                   "wow"
                                   "/character/{realm}/{name}"
                                   "aegwynn"
                                   "asdf"))))

(deftest test-create-url-character-2
  (is
    (.equals "https://eu.battle.net/api/wow/character/aegwynn/asdf?fields=professions"
             (create-url-character "eu"
                                   "wow"
                                   "/character/{realm}/{name}"
                                   "aegwynn"
                                   "asdf"
                                   "fields=professions"))))

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

(deftest test-media-url-avatar
  (is
    (.equals
      "http://eu.battle.net/static-render/eu/foo.jpg"
      (media-url-avatar "eu" "foo.jpg"))))

;;;;;;;;
; misc
;;;;;;;;

(deftest test-access-rmap
  (is
    (.equals "Aegwynn"
             (access-rmap mock-map-single :name))))

(deftest test-get-names-1
  (is
    (.equals "Aegwynn"
             (get-name (first (get mock-map-multiple :realms))))))

(deftest test-get-names-2
  (is
    (.equals ["Aegwynn" "Aerie Peak"]
             (map get-name (get mock-map-multiple :realms)))))

(deftest test-get-title-1
  (is
    (.equals "%s the Seeker"
             (get-title mock-titles))))

(deftest test-get-title-2
  (is
    (.equals "Elder %s"
             (get-title mock-titles-2))))

(deftest test-get-title3
  (is
    (.equals "%s"
             (get-title mock-titles-empty))))

;;;;;;;;;;;;;;;
; professions
;;;;;;;;;;;;;;;

(deftest test-get-primary-professions-1
  (is
    (.equals [["Alchemy" 540] ["Leatherworking" 500]]
             (get-primary-professions (:professions mock-char-prof)))))

(deftest test-get-primary-professions-2
  (is
    (.equals [nil nil]
             (get-primary-professions (:professions mock-char)))))

(deftest test-get-secondary-profession-1
  (is
    (.equals "525"
             (get-secondary-profession (:professions mock-char-prof) "First Aid"))))

(deftest test-get-secondary-profession-2
  (is
    (nil?
      (get-secondary-profession (:professions mock-char-prof) "Cooking"))))

;;;;;;;;;;;;
; currency
;;;;;;;;;;;;

(deftest test-copper-to-gold-1
  (is
    (.equals "1 C"
             (copper-to-gold 1))))

(deftest test-copper-to-gold-2
  (is
    (.equals "1 S 23 C"
             (copper-to-gold 123))))

(deftest test-copper-to-gold-3
  (is
    (.equals "1 G 23 S 45 C"
             (copper-to-gold 12345))))

(deftest test-copper-to-gold-html
  (is
    (.equals (str "<span class=\"icon-gold\">1</span>"
                  "<span class=\"icon-silver\">23</span>"
                  "<span class=\"icon-copper\">45</span>")
             (copper-to-gold 12345 1))))

;;;;;;;;;
; lookup
;;;;;;;;;

(deftest test-lookup-class
  (is
    (.equals "Rogue"
             (lookup-class 4))))

(deftest test-lookup-race
  (is
    (.equals "Orc"
             (lookup-race 2))))

(deftest test-lookup-gender
  (is
    (.equals "Male"
             (lookup-gender 0))))

(deftest test-lookup-quality
  (is
    (.equals "Epic"
             (lookup-quality 4))))

(deftest test-lookup-faction
  (is
    (.equals "Horde"
             (lookup-faction 1))))

(deftest test-lookup-reputation
  (is
    (.equals "Exalted"
             (lookup-reputation 7))))

(deftest test-lookup-stat
  (is
    (.equals "Agility"
             (lookup-stat 3))))

(deftest test-lookup-inventory
  (is
    (.equals "Main Hand"
             (lookup-inventory 16))))

;;;;;;;;;;;
; talents
;;;;;;;;;;;

(deftest test-get-talent-numbers-1
  (is
    (.equals "31/2/8"
             (get-talent-numbers (:trees (first (:talents mock-char-talents)))))))

(deftest test-get-talent-numbers-2
  (is
    (.equals "7/31/3"
             (get-talent-numbers (:trees (nth (:talents mock-char-talents) 1))))))

(deftest test-get-talent-numbers-2
  (is
    (.equals ""
             (get-talent-numbers (:trees (nth (:talents mock-char) 1))))))

(deftest test-get-talent-spec-1
  (is
    (.equals [["Assassination" "31/2/8" "ability_rogue_eviscerate"]
              ["Combat" "7/31/3" "ability_backstab"]]
             (get-talent-spec (:talents mock-char-talents)))))

(deftest test-get-talent-spec-2
  (is
    (.equals [nil nil]
             (get-talent-spec (:talents mock-char)))))