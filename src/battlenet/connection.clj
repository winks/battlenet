(def baseurl "https://{region}.battle.net/api/{game}{path}?{params}")
(def xrealms ["realms" "aggramar" "arathor"])

(defn create-url
  "builds a request URL"
  [region game path params]
    (.replace
      (.replace
        (.replace
          (.replace baseurl "{region}" region)
          "{game}" game)
        "{path}" path)
    "{params}" params))

(defn joinparams
  "joins url parameters"
  [params]
  (str (first params) "=" (apply str (interpose \, (nthnext params 1)))))

(println (create-url "eu" "wow" "/realm/status" "realms=aggramar"))
(println (joinparams xrealms))
(println (create-url "eu" "wow" "/realm/status" (joinparams xrealms)))
