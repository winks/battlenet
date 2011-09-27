(ns battlenet.test.tools
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:use [battlenet.network])
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

(deftest test-access-realm-map
  (is
    (.equals "Aegwynn" 
      (access-realm-map mock-map :name))))

(deftest test-realm-map-to-model
  (is
    (.equals "Aegwynn"
      (get (realm-map-to-model mock-map) :name))))