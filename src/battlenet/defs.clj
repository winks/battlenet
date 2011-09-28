(ns battlenet.defs)

;
; URL templates and paths
;
(def bn-baseurl         "https://{region}.battle.net/api/{game}{path}?{params}")
(def bn-path-realm      "/realm/status")
(def bn-path-character  "/character/{realm}/{name}")
(def bn-path-guild      "/guild/{realm}/{name}")
(def bn-path-perks      "/data/guild/perks")
(def bn-path-rewards    "/data/guild/rewards")
(def bn-path-classes    "/data/character/classes")
(def bn-path-races      "/data/character/races")
(def bn-path-item       "/item/{id}")

;
; additional fields for character lookups
;
(def bn-fields
  {:all "all",
   :stats "stats", :talents "talents", :items "items",
   :reputation "reputation", :titles "titles", :professions "professions",
   :appearance "appearance", :companions "companions", :mounts "mounts",
   :guild "guild", :quests "quests", :pets "pets",
   :progression "progression", :achievements "achievements"})

;
; WoW classes
;
(def bn-classes
  ["_zero" "Warrior" "Paladin" "Hunter" "Rogue" "Priest"
   "Death Knight" "Shaman" "Mage" "Warlock" "_ten" "Druid"])

;
; WoW races
;
(def bn-races
  ["_zero" "Human" "Orc" "Dwarf" "Night Elf" "Undead"
   "Tauren" "Gnome" "Troll" "Goblin" "Blood Elf"
   "Draenei" "_" "_" "_" "_" "_" "_" "_" "_" "_twenty" "" "Worgen"])
