(ns battlenet.test.integration
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:use [battlenet.network])
  (:use [clojure.test]))

(deftest test-check-url
  (is
    (= 1 (check-url "https://eu.battle.net/api/wow/realm/status"))))

(deftest test-read-url
  (is
    (.startsWith
      (read-url "https://eu.battle.net/api/wow/realm/status?realms=aegwynn") "{")))

(deftest test-realm-is-online
  (is
    (= true
      (realm-is-online "eu" "aegwynn"))))

(deftest test-get-type
  (is
    (= "pvp"
      (realm-get-type "eu" "aegwynn"))))