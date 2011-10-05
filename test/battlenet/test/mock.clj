(ns battlenet.test.mock)

(def mock-json "{\"realms\":[{
      \"type\":\"pvp\",
      \"queue\":false,
      \"status\":true,
      \"population\":\"high\",
      \"name\":\"Aegwynn\",
      \"slug\":\"aegwynn\"
    }]}")


(def mock-map-single {:realms 
               [{:type "pvp",
                 :queue false,
                 :status true,
                 :population "high",
                 :name "Aegwynn",
                 :battlegroup "Blutdurst",
                 :slug "aegwynn"}]})

(def mock-map-multiple {:realms 
               [{:type "pvp",
                 :queue false,
                 :status true,
                 :population "high",
                 :name "Aegwynn",
                 :battlegroup "Blutdurst",
                 :slug "aegwynn"},
                {:type "pve",
                 :queue false,
                 :status true,
                 :population "medium",
                 :name "Aerie Peak",
                 :battlegroup "Misery",
                 :slug "aerie-peak"}
                ]})

(def mock-char
  {:achievementPoints 1234,
   :lastModified 1311956422000,
   :gender 1,
   :class 4,
   :name "Humanrogue",
   :realm "Malygos",
   :thumbnail "malygos/1/12312312-avatar.jpg",
   :race 1,
   :level 85})

(def mock-char-prof
  {:achievementPoints 1234,
   :lastModified 1311956422000,
   :gender 1,
   :class 4,
   :name "Humanrogue",
   :realm "Malygos",
   :thumbnail "malygos/1/12312312-avatar.jpg",
   :race 1,
   :level 85,
   :professions
    {
     :primary 
     [{:id 171,
       :name "Alchemy",
       :icon "trade_alchemy",
       :rank 540,
       :max 525,
       :recipes [2329 2330]
       },
      {:id 165,
       :name "Leatherworking",
       :icon "trade_leatherworking",
       :rank 500
       :max 525,
       :recipes [2149 2152]
       }
      ],
     :secondary
     [{:id 129,
       :name "First Aid",
       :icon "spell_holy_sealofsacrifice",
       :rank 525,
       :max 525,
       :recipes [3275 3276]
       }]}})

(def mock-char-rep
  {:achievementPoints 1234,
   :lastModified 1311956422000,
   :gender 1,
   :class 4,
   :name "Humanrogue",
   :realm "Malygos",
   :thumbnail "malygos/1/12312312-avatar.jpg",
   :race 1,
   :level 85,
   :reputation
   [{:id 1098,
     :name "Knights of the Ebon Blade",
     :standing 7
     :value 999,
     :max 999}]})

(def mock-titles
  {:titles
   [{:id 81,
     :name "%s the Seeker",
     :selected true},
    {:id 74,
     :name "Elder %s"}]})

(def mock-titles-2
  {:titles
   [{:id 81,
     :name "%s the Seeker"},
    {:id 74,
     :name "Elder %s",
     :selected true}]})

(def mock-titles-empty
  {:titles []})

(def mock-char-talents
  {:achievementPoints 1234,
   :lastModified 1311956422000,
   :gender 1,
   :class 4,
   :name "Humanrogue",
   :realm "Malygos",
   :thumbnail "malygos/1/12312312-avatar.jpg",
   :race 1,
   :level 85,
   :talents [{:name "Assassination",
              :trees [{:total 31},{:total 2},{:total 8}],
              :icon "ability_rogue_eviscerate"},
             {:name "Combat",
              :trees [{:total 7},{:total 31},{:total 3}],
              :icon "ability_backstab"}]})