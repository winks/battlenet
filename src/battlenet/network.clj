(ns battlenet.network
  (:import (java.net URL URLConnection HttpURLConnection UnknownHostException)
           (java.io FileNotFoundException IOException)))

(defn read-url
  "Reads from an URL."
  [url]
  (try
    (slurp url)
    (catch UnknownHostException _
      "{}")
    (catch FileNotFoundException _
      "{}")
    (catch IOException _
      (try
        (slurp url)
        (catch IOException _
          "{}")))))
