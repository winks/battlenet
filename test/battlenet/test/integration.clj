(ns battlenet.test.integration
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:use [battlenet.network])
  (:use [clojure.test]))

(deftest test-check-url-int
  (is
    (= 1 (check-url "https://eu.battle.net/api/wow/realm/status"))))

(deftest test-read-url-int
  (is
    (.startsWith
      (read-url "https://eu.battle.net/api/wow/realm/status?realms=aegwynn") "{")))

(deftest test-realm-is-online-int
  (is
    (= true
      (realm-is-online "eu" "aegwynn"))))

(deftest test-get-type-int
  (is
    (= "pvp"
      (realm-get-type "eu" "aegwynn"))))

(deftest test-get-names-int
  (is
    (.equals ["Aerie Peak" "Aegwynn"]
             (map get-name 
                  (get (read-remote-rmap "eu" "aegwynn,aerie-peak") :realms)))))