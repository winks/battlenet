(ns battlenet.d3.defs
  (:use [battlenet.defs :as defs]))

(def bn-path-account    "/account/{account}/{num}")
(def bn-path-hero       "/hero/{hero}")

(def bn-fields-account
  {:all "all",
   :account "account",
   :heroes "heroes",
   :artisan "artisan",
   :progression "progression",
   :kills "kills",
   :time-played "time-played",
   :last-modified "last_modified"})
