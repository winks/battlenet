(ns battlenet.test.tools
  (:require [battlenet.tools :as to])
  (:use [battlenet.test.mock])
  (:use [clojure.test]))

;;;;;;;;;;;;;
; URL stuff
;;;;;;;;;;;;;

(deftest test-create-url-1
  (is
    (.equals "https://eu.api.battle.net/wow/realm/status?realms=aegwynn"
             (to/create-url "eu" "wow" "/realm/status" "realms=aegwynn"))))

(deftest test-create-url-2
  (is
    (.equals "https://eu.api.battle.net/wow/realm/status?realms=aegwynn,aerie-peak"
             (to/create-url "eu" "wow" "/realm/status" "realms=aegwynn,aerie-peak"))))

(deftest test-media-url-icon
  (is
    (.equals
      "http://eu.media.blizzard.com/wow/icons/18/inv_bracer_leatherraidrogue_i_01.jpg"
      (to/media-url-icon "eu" "wow" "small" "inv_bracer_leatherraidrogue_i_01"))))

(deftest test-media-url-avatar
  (is
    (.equals
      "http://eu.battle.net/static-render/eu/foo.jpg"
      (to/media-url-avatar "eu" "foo.jpg"))))
