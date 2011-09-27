(ns battlenet.test.tools
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:use [clojure.test]))

(def mock-map {:realms 
               [{:type "pvp",
                 :queue false,
                 :status true,
                 :population "high",
                 :name "Aegwynn",
                 :slug "aegwynn"}]})

(deftest test-create-url
  (is
    (.equals "https://eu.battle.net/api/wow/realm/status?realms=aegwynn"
      (create-url "eu" "wow" "/realm/status" "realms=aegwynn"))))

(deftest test-join-params
  (is
    (.equals "realms=aegwynn,aerie-peak"
      (join-params ["realms" "aegwynn" "aerie-peak"]))))

;(deftest test-check-url
;  (is
;    (= 1 (read-url "http://www.heise.de/"))))

(deftest test-read-url
  (is
    (.startsWith
      (read-url "https://eu.battle.net/api/wow/realm/status?realms=aegwynn") "{")))

(deftest test-get-realm-map
  (is
    (.equals "Aegwynn"
      (get (first (get mock-map :realms)) :name))))

(deftest test-realm-map-to-model
  (is
    (.equals "Aegwynn"
      (get (realm-map-to-model mock-map) :name))))