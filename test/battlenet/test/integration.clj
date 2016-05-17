(ns battlenet.test.integration
  (:use [battlenet.wow.core])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:require [battlenet.wow.tools :as wto])
  (:require [battlenet.network :as net])
  (:require [battlenet.wow.network :as wnet])
  (:use [battlenet.test.mock])
  (:use [clojure.test]))

(def test-params "locale=en_GB&apikey=XXX")

(deftest test-read-url-int-success
  (is
    (.startsWith
      (net/read-url (str "https://eu.api.battle.net/wow/realm/status?realms=aegwynn&" test-params))
      "{")))

(deftest test-read-url-int-error
  (is
    (.contains
      (net/read-url (str "https://eu.api.battle.net/wow/realm/statuswhateverinvalidurl?" test-params))
      "page not found")))

(deftest test-realm-is-online-int
  (is
    (instance? Boolean
      (realm-is-online "eu" "aegwynn"))))

(deftest test-realm-is-offline-int
  (is
    (instance? Boolean
      (realm-is-offline "eu" "aegwynn"))))

(deftest test-get-type-int
  (is
    (= "pvp"
      (realm-get-type "eu" "aegwynn"))))

(deftest test-get-names-int
  (is
    (=
      (set (map wto/get-name (get (wnet/read-remote-realms "eu" "aegwynn,aerie-peak") :realms)))
      (set ["Aerie Peak" "Aegwynn"]))))

(deftest test-realm-get-info-int
  (is
    (.equals "pvp"
             (:type (first( realm-get-info "eu" "aegwynn"))))))

(deftest test-realm-has-queue-int
  (is
    (instance? Boolean
       (realm-has-queue "eu" "aegwynn"))))

(deftest test-get-realm-names-int
  (is
    (.equals ["Aerie Peak" "Agamaggan"]
             (let [g (get-realm-names "eu")] (conj (conj nil (nth g 2)) (nth g 1))))))

