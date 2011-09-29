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
(def bn-media-icon
  "http://{region}.media.blizzard.com/{game}/icons/{size}/{icon}.jpg")

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
  [nil "Warrior" "Paladin" "Hunter" "Rogue" "Priest"
   "Death Knight" "Shaman" "Mage" "Warlock" nil
   "Druid"])

;
; WoW races
;
(def bn-races
  [nil "Human" "Orc" "Dwarf" "Night Elf" "Undead"
   "Tauren" "Gnome" "Troll" "Goblin" "Blood Elf"
   "Draenei" nil nil nil nil
   nil nil nil nil nil
   nil "Worgen"])

;
; WoW quality
;
(def bn-quality
  [nil "Common" "Uncommon" "Rare" "Epic" "Legendary"])

;
; WoW factions
;
(def bn-factions
  ["Alliance" "Horde"])

;
; WoW races to factions mapping
;
(def bn-races-factions
  [nil "Alliance" "Horde" "Alliance" "Alliance" "Horde"
   "Horde" "Alliance" "Horde" "Horde" "Horde"
   "Alliance" nil nil nil nil
   nil nil nil nil nil
   nil "Alliance"])

;
; WoW standings
;
(def bn-reputation-standing
  ["Hated" "Hostile" "Unfriendly" "Neutral" "Friendly" "Honored" "Revered" "Exalted"])