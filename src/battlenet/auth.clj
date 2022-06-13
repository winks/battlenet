(ns battlenet.auth
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as cjson]))

(def oauth (atom {}))

(defn pp [s]
  (.println *err* s))

(defn get-token2 [url auth]
  ;(clj-http.client/post "http://localhost:9999" {:basic-auth ["a" "b"] :form-params {:grant_type "client_credentials"}}))
  (let [data  (clj-http.client/post url {:basic-auth auth :form-params {:grant_type "client_credentials"}})
        x2    (pp (str "# " (:body data)))
        json  (clojure.data.json/read-str (:body data))
        exp   (get json "expires_in")
        token (get json "access_token")]
    [token exp]))

(defn set-token [now oauth-config]
  (let [new (get-token2 (:url oauth-config) [(:client-id oauth-config) (:client-secret oauth-config)])]
    (pp (str "# set-token: " new))
    (do
      (swap! oauth assoc :access_token (first new))
      (swap! oauth assoc :expires_at (+ now (- (second new) 3600)))
      (pp (str "# set-token: " oauth))
      (first new))))

(defn get-token [oauth-config]
  (let [token-ext (:token-ext oauth-config)
        now (quot (System/currentTimeMillis) 1000)]
    (pp (str "# get-token: ext:[" token-ext "] oauth:[" oauth "] now: " now))
    (if (empty? token-ext)
      (if-let [exp (find @oauth :expires_at)]
        (if-let [token (find @oauth :access_token)]
          (if (< now (second exp)) ; not expired
            (second token)
            (set-token now oauth-config))
        (set-token now oauth-config)) ; :access_token not found in atom
      (set-token now oauth-config)) ; :expires_at not found in atom
    token-ext)))
