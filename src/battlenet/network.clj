(ns battlenet.network
  (:import (java.net URL URLConnection HttpURLConnection UnknownHostException)
           (java.io FileNotFoundException)))

(defn read-url
  "Reads from an URL."
  [url]
  (try
    (slurp url)
    (catch UnknownHostException _
      "{}")
    (catch FileNotFoundException _
      "{}")))
