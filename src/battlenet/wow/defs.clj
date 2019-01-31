(ns battlenet.wow.defs)

;
; URL templates and paths
;
(def bn-wow-char-url    "https://worldofwarcraft.com/{locale}/character/{realm}/{name}")
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
  {1 "Warrior",
   2 "Paladin",
   3 "Hunter",
   4 "Rogue",
   5 "Priest",
   6 "Death Knight",
   7 "Shaman",
   8 "Mage",
   9 "Warlock",
   10 "Monk",
   11 "Druid"
   12 "Demon Hunter"})

;
; WoW races
;
(def bn-races
  {1 "Human",
   2 "Orc",
   3 "Dwarf",
   4 "Night Elf",
   5 "Undead",
   6 "Tauren",
   7 "Gnome",
   8 "Troll",
   9 "Goblin",
   10 "Blood Elf",
   11 "Draenei",
   22 "Worgen",
   24 "Pandaren",
   25 "Pandaren",
   26 "Pandaren",
   27 "Nightborne",
   28 "Highmountain Tauren",
   29 "Void Elf",
   30 "Lightforged Draenei",
   34 "Dark Iron Dwarf",
   36 "Mag'har Orc",
   })

;
; WoW quality
;

(def bn-quality
  {1 "Common",
   2 "Uncommon",
   3 "Rare",
   4 "Epic",
   5 "Legendary"})

;
; WoW factions
;
(def bn-factions
  {0 "Alliance",
   1 "Horde"})

;
; WoW races to factions mapping
;
(def bn-races-factions
  {1 "Alliance",
   2 "Horde",
   3 "Alliance",
   4 "Alliance",
   5 "Horde"
   6 "Horde",
   7 "Alliance",
   8 "Horde",
   9 "Horde",
   10 "Horde",
   11 "Alliance",
   22 "Alliance",
   24 "?",
   25 "Alliance",
   26 "Horde",
   27 "Horde",
   28 "Horde",
   29 "Alliance",
   30 "Alliancee",
   34 "Alliance",
   36 "Horde",
   })

;
; WoW standings
;
(def bn-reputation-standing
  {0 "Hated",
   1 "Hostile",
   2 "Unfriendly",
   3 "Neutral",
   4 "Friendly",
   5 "Honored",
   6 "Revered",
   7 "Exalted"})

;
; WoW InventorySlotId/InventoryType
;
(def bn-inventory
  ["Ammo" "Head" "Neck" "Shoulder" "Shirt" "Chest"
   "Belt" "Legs" "Feet" "Wrist" "Gloves"
   "Finger" "Finger" "Trinket" "Trinket" "Back"
   "Main Hand" "Off Hand" "Ranged" "Tabard"])

;
; WoW stats
;
(def bn-stats
  {3 "Agility",
   4 "Strength",
   5 "Intellect",
   6 "Spirit",
   7 "Stamina",
   13 "Dodge",
   14 "Parry",
   31 "Hit",
   32 "Crit",
   35 "Resilience",
   36 "Haste",
   37 "Expertise",
   45 "Spell Power",
   49 "Mastery"})

(def bn-expansions
  {0 (:wow "World of Warcraft"),
   1 (:bc "The Burning Crusade"),
   2 (:lk "Wrath of the Lich King"),
   3 (:cata "Cataclysm"),
   4 (:mop "Mists of Pandaria"),
   5 (:wod "Warlords of Draenor"),
   6 (:legion "Legion"),
   7 (:bfa "Battle for Azeroth"),
   })

(def bn-raids
  {:wow '(2717 2677 3429 3428),
   :bc '(3457 3836 3923 3607 3845 3606 3959 4075),
   :lk '(4603 3456 4493 4500 4273 2159 4722 4812 4987),
   :cata '(5600 5094 5334 5638 5723 5892),
   :mop '(6125 6297 6067)})

(def bn-professions-order
  [:kultiran :legion :draenor :pandaria :cataclysm :northrend :outland nil])

(def bn-professions-secondary
  {:archaeology {
     :archaeology      {:id 794 :name "Archaeology"}}
   :cooking {
     :kultirancooking  {:id 2541 :name "Kul Tiran Cooking"}
     :legioncooking    {:id 2542 :name "Legion Cooking"}
     :draenorcooking   {:id 2543 :name "DraenorCooking"}
     :pandariacooking  {:id 2544 :name "Pandaria Cooking"}
     :cataclysmcooking {:id 2545 :name "Cataclysm Cooking"}
     :northrendcooking {:id 2546 :name "Northrend Cooking"}
     :outlandcooking   {:id 2547 :name "Outland Cooking"}
     :cooking          {:id 185  :name "Cooking"}}
   :fishing {
     :kultiranfishing  {:id 2585 :name "Kul Tiran Fishing"}
     :legionfishing    {:id 2586 :name "Legion Fishing"}
     :draenorfishing   {:id 2587 :name "Draenor Fishing"}
     :pandariafishing  {:id 2588 :name "Pandaria Fishing"}
     :cataclysmfishing {:id 2589 :name "Cataclysm Fishing"}
     :northrendfishing {:id 2590 :name "Northrend Fishing"}
     :outlandfishing   {:id 2591 :name "Outland Fishing"}
     :fishing          {:id 356  :name "Fishing"}}})

(def bn-professions-primary
  {:alchemy        {:id 171 :name "Alchemy"}
   :blacksmithing  {:id 164 :name "Blacksmithing"}
   :enchanting     {:id 333 :name "Enchanting"}
   :engineering    {:id 202 :name "Engineering"}
   :herbalism      {:id 182 :name "Herbalism"}
   :inscription    {:id 773 :name "Inscription"}
   :jewelcrafting  {:id 755 :name "Jewelcrafting"}
   :leatherworking {:id 165 :name "Leatherworkingng"}
   :mining         {:id 186 :name "Mining"}
   :skinning       {:id 393 :name "Skinning"}
   :tailoring      {:id 197 :name "Tailoring"}})
