(ns battlenet.network
  (:import (java.net URL URLConnection HttpURLConnection UnknownHostException)
           (java.io FileNotFoundException IOException)))

(defn read-url
  "Reads from an URL."
  [url]
  (try
    (slurp url)
    (catch UnknownHostException ex
      (.println *err* ex)
      nil)
    (catch FileNotFoundException ex
      (.println *err* ex)
      nil)
    (catch IOException ex
      (.println *err* ex)
      (try
        (slurp url)
        (catch IOException ex
          (.println *err* ex)
          nil)))))
