(ns battlenet.test.core
  (:use [battlenet.core])
  (:use [clojure.test]))

(def mock-json "{\"realms\":[{
      \"type\":\"pvp\",
      \"queue\":false,
      \"status\":true,
      \"population\":\"high\",
      \"name\":\"Aegwynn\",
      \"slug\":\"aegwynn\"
    }]}")
(def mock-map {:realms [{:type "pvp", :queue false, :status true, :population "high", :name "Aegwynn", :slug "aegwynn"}]})

(deftest test-join-params
  (is
    (.equals "realms=aegwynn,aerie-peak"
      (join-params ["realms" "aegwynn" "aerie-peak"]))))

(deftest test-create-url
  (is
    (.equals "https://eu.battle.net/api/wow/realm/status?realms=aegwynn"
      (create-url "eu" "wow" "/realm/status" "realms=aegwynn"))))

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

(deftest test-realm-is-online
  (is
    (= true
      (realm-is-online "eu" "aegwynn"))))

