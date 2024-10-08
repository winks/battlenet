(ns battlenet.defs)

;
; URL templates and paths
;
(def bn-wow-char-url    "https://worldofwarcraft.com/{locale}/character/{region}/{realm}/{name}")
(def bn-wow-guild-url   "https://worldofwarcraft.com/{locale}/guild/{region}/{realm}/{name}")

(def bn-d3-profile-url  "https://{region}.diablo3.com/en/profile/{account}/")

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
   12 "Demon Hunter"
   13 "Evoker"})

;
; WoW races
;
(def bn-races
  {0 "Unknown",
   1 "Human",
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
   31 "Zandalari Troll",
   32 "Kul Tiran",
   34 "Dark Iron Dwarf",
   35 "Vulpera",
   36 "Mag'har Orc",
   37 "Mechagnome",
   52 "Dracthyr"
   70 "Dracthyr"
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
   35 "Horde",
   36 "Horde",
   37 "Alliance",
   52 "Alliance"
   70 "Hordex"
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

(def bn-power-types
  {0 "Mana"
   1 "Rage"
   2 "Focus"
   3 "Energy"
   4 "Combo Points"
   5 "Runes"
   6 "Runic Power"
   7 "Soul Shards"
   8 "Lunar Power"
   9 "Holy Power"
   10 "Alternate"
   11 "Maelstrom"
   12 "Chi"
   16 "Arcane Charges"
   17 "Fury"
   18 "Pain"
   13 "Insanity"
   19 "Unk"})

(def bn-expansions
  {0 (:wow "World of Warcraft"),
   1 (:bc "The Burning Crusade"),
   2 (:lk "Wrath of the Lich King"),
   3 (:cata "Cataclysm"),
   4 (:mop "Mists of Pandaria"),
   5 (:wod "Warlords of Draenor"),
   6 (:legion "Legion"),
   7 (:bfa "Battle for Azeroth"),
   8 (:sl "Shadowlands"),
   9 (:df "Dragonflight"),
   10 (:tww "The War Within"),
   })

(def bn-raids-old
  {:wow '(2717 2677 3429 3428),
   :bc '(3457 3836 3923 3607 3845 3606 3959 4075),
   :lk '(4603 3456 4493 4500 4273 2159 4722 4812 4987),
   :cata '(5600 5094 5334 5638 5723 5892),
   :mop '(6125 6297 6067)})

(def bn-professions-order
  [:khazalgar :dragonisles :shadowlands :kultiran :legion :draenor :pandaria :cataclysm :northrend :outland nil])

(def bn-professions-index
  {164 "Blacksmithing"
   165 "Leatherworking"
   171 "Alchemy"
   182 "Herbalism"
   185 "Cooking"
   186 "Mining"
   197 "Tailoring"
   202 "Engineering"
   333 "Enchanting"
   356 "Fishing"
   393 "Skinning"
   755 "Jewelcrafting"
   773 "Inscription"
   794 "Archaeology"
   2777 "Soul Cyphering"
   2787 "Abominable Stitching"
   2791 "Ascension Crafting"})

(def bn-professions-secondary
  {:archaeology {
     :archaeology         {:id 794 :name "Archaeology"}}
   :cooking {
     :khazalgarcooking    {:id 2873 :name "Khaz Algar Cooking"}
     :dragonislescooking  {:id 2824 :name "Dragon Isles Cooking"}
     :shadowlandscooking  {:id 2752 :name "Shadowlands Cooking"}
     :kultirancooking     {:id 2541 :name "Kul Tiran Cooking"}
     :legioncooking       {:id 2542 :name "Legion Cooking"}
     :draenorcooking      {:id 2543 :name "Draenor Cooking"}
     :pandariacooking     {:id 2544 :name "Pandaria Cooking"}
     :cataclysmcooking    {:id 2545 :name "Cataclysm Cooking"}
     :northrendcooking    {:id 2546 :name "Northrend Cooking"}
     :outlandcooking      {:id 2547 :name "Outland Cooking"}
     :cooking             {:id 2548  :name "Cooking"}}
   :fishing {
     :khazalgarfishing    {:id 2876 :name "Khaz Algar Fishing"}
     :dragonislesfishing  {:id 2826 :name "Dragon Isles Fishing"}
     :shadowlandsfishing  {:id 2754 :name "Shadowlands Fishing"}
     :kultiranfishing     {:id 2585 :name "Kul Tiran Fishing"}
     :legionfishing       {:id 2586 :name "Legion Fishing"}
     :draenorfishing      {:id 2587 :name "Draenor Fishing"}
     :pandariafishing     {:id 2588 :name "Pandaria Fishing"}
     :cataclysmfishing    {:id 2589 :name "Cataclysm Fishing"}
     :northrendfishing    {:id 2590 :name "Northrend Fishing"}
     :outlandfishing      {:id 2591 :name "Outland Fishing"}
     :fishing             {:id 2592 :name "Fishing"}}})

(def bn-professions-primary
  {:alchemy {
     :khazalgaralchemy    {:id 2871 :name "Khaz Algar Alchemy"}
     :dragonislesalchemy  {:id 2823 :name "Dragon Isles Alchemy"}
     :shadowlandsalchemy  {:id 2750 :name "Shadowlands Alchemy"}
     :kultiranalchemy     {:id 2478 :name "Kul Tiran Alchemy"}
     :legionalchemy       {:id 2479 :name "Legion Alchemy"}
     :draenoralchemy      {:id 2480 :name "Draenor Alchemy"}
     :pandariaalchemy     {:id 2481 :name "Pandaria Alchemy"}
     :cataclysmalchemy    {:id 2482 :name "Cataclysm Alchemy"}
     :northrendalchemy    {:id 2483 :name "Northrend Alchemy"}
     :outlandalchemy      {:id 2484 :name "Outland Alchemy"}
     :alchemy             {:id 2485 :name "Alchemy"}}
   :blacksmithing {
     :khazalgarblacksmithing    {:id 2872 :name "Khaz Algar Blacksmithing"}
     :dragonislesblacksmithing  {:id 2822 :name "Dragon Isles Blacksmithing"}
     :shadowlandsblacksmithing  {:id 2751 :name "Shadowlands Blacksmithing"}
     :kultiranblacksmithing     {:id 2437 :name "Kul Tiran Blacksmithing"}
     :legionblacksmithing       {:id 2454 :name "Legion Blacksmithing"}
     :draenorblacksmithing      {:id 2472 :name "Draenor Blacksmithing"}
     :pandariablacksmithing     {:id 2473 :name "Pandaria Blacksmithing"}
     :cataclysmblacksmithing    {:id 2474 :name "Cataclysm Blacksmithing"}
     :northrendblacksmithing    {:id 2475 :name "Northrend Blacksmithing"}
     :outlandblacksmithing      {:id 2476 :name "Outland Blacksmithing"}
     :blacksmithing             {:id 2477 :name "Blacksmithing"}}
   :enchanting {
     :khazalgarenchanting    {:id 2874 :name "Khaz Algar Enchanting"}
     :dragonislesenchanting  {:id 2825 :name "Dragon Isles Enchanting"}
     :shadowlandsenchanting  {:id 2753 :name "Shadowlands Enchanting"}
     :kultiranenchanting     {:id 2486 :name "Kul Tiran Enchanting"}
     :legionenchanting       {:id 2487 :name "Legion Enchanting"}
     :draenorenchanting      {:id 2488 :name "Draenor Enchanting"}
     :pandariaenchanting     {:id 2489 :name "Pandaria Enchanting"}
     :cataclysmenchanting    {:id 2491 :name "Cataclysm Enchanting"}
     :northrendenchanting    {:id 2492 :name "Northrend Enchanting"}
     :outlandenchanting      {:id 2493 :name "Outland Enchanting"}
     :enchanting             {:id 2494 :name "Enchanting"}}
   :engineering {
     :khazalgarengineering    {:id 2875 :name "Khaz Algar Engineering"}
     :dragonislesengineering  {:id 2827 :name "Dragon Isles Engineering"}
     :shadowlandsengineering  {:id 2755 :name "Shadowlands Engineering"}
     :kultiranengineering     {:id 2499 :name "Kul Tiran Engineering"}
     :legionengineering       {:id 2500 :name "Legion Engineering"}
     :draenorengineering      {:id 2501 :name "Draenor Engineering"}
     :pandariaengineering     {:id 2502 :name "Pandaria Engineering"}
     :cataclysmengineering    {:id 2503 :name "Cataclysm Engineering"}
     :northrendengineering    {:id 2504 :name "Northrend Engineering"}
     :outlandengineering      {:id 2505 :name "Outland Engineering"}
     :engineering             {:id 2506 :name "Engineering"}}
   :herbalism {
     :khazalgarherbalism    {:id 2877 :name "Khaz Algar Herbalism"}
     :dragonislesherbalism  {:id 2832 :name "Dragon Isles Herbalism"}
     :shadowlandsherbalism  {:id 2760 :name "Shadowlands Herbalism"}
     :kultiranherbalism     {:id 2549 :name "Kul Tiran Herbalism"}
     :legionherbalism       {:id 2550 :name "Legion Herbalism"}
     :draenorherbalism      {:id 2551 :name "Draenor Herbalism"}
     :pandariaherbalism     {:id 2552 :name "Pandaria Herbalism"}
     :cataclysmherbalism    {:id 2553 :name "Cataclysm Herbalism"}
     :northrendherbalism    {:id 2554 :name "Northrend Herbalism"}
     :outlandherbalism      {:id 2555 :name "Outland Herbalism"}
     :herbalism             {:id 2556 :name "Herbalism"}}
   :inscription {
     :khazalgarinscription    {:id 2878 :name "Khaz Algar Inscription"}
     :dragonislesinscription  {:id 2828 :name "Dragon Isles Inscription"}
     :shadowlandsinscription  {:id 2756 :name "Shadowlands Inscription"}
     :kultiraninscription     {:id 2507 :name "Kul Tiran Inscription"}
     :legioninscription       {:id 2508 :name "Legion Inscription"}
     :draenorinscription      {:id 2509 :name "Draenor Inscription"}
     :pandariainscription     {:id 2510 :name "Pandaria Inscription"}
     :cataclysminscription    {:id 2511 :name "Cataclysm Inscription"}
     :northrendinscription    {:id 2512 :name "Northrend Inscription"}
     :outlandinscription      {:id 2513 :name "Outland Inscription"}
     :inscription             {:id 2514 :name "Inscription"}}
   :jewelcrafting {
     :khazalgarjewelcrafting    {:id 2879 :name "Khaz Algar Jewelcrafting"}
     :dragonislesjewelcrafting  {:id 2829 :name "Dragon Isles Jewelcrafting"}
     :shadowlandsjewelcrafting  {:id 2757 :name "Shadowlands Jewelcrafting"}
     :kultiranjewelcrafting     {:id 2517 :name "Kul Tiran Jewelcrafting"}
     :legionjewelcrafting       {:id 2518 :name "Legion Jewelcrafting"}
     :draenorjewelcrafting      {:id 2519 :name "Draenor Jewelcrafting"}
     :pandariajewelcrafting     {:id 2520 :name "Pandaria Jewelcrafting"}
     :cataclysmjewelcrafting    {:id 2521 :name "Cataclysm Jewelcrafting"}
     :northrendjewelcrafting    {:id 2522 :name "Northrend Jewelcrafting"}
     :outlandjewelcrafting      {:id 2523 :name "Outland Jewelcrafting"}
     :jewelcrafting             {:id 2524 :name "Jewelcrafting"}}
   :leatherworking {
     :khazalgarleatherworking    {:id 2880 :name "Khaz Algar Leatherworking"}
     :dragonislesleatherworking  {:id 2830 :name "Dragon Isles Leatherworking"}
     :shadowlandsleatherworking  {:id 2758 :name "Shadowlands Leatherworking"}
     :kultiranleatherworking     {:id 2525 :name "Kul Tiran Leatherworking"}
     :legionleatherworking       {:id 2526 :name "Legion Leatherworking"}
     :draenorleatherworking      {:id 2527 :name "Draenor Leatherworking"}
     :pandarialeatherworking     {:id 2528 :name "Pandaria Leatherworking"}
     :cataclysmleatherworking    {:id 2529 :name "Cataclysm Leatherworking"}
     :northrendleatherworking    {:id 2530 :name "Northrend Leatherworking"}
     :outlandleatherworking      {:id 2531 :name "Outland Leatherworking"}
     :leatherworking             {:id 2532  :name "Leatherworking"}}
   :mining {
     :khazalgarmining    {:id 2881 :name "Khaz Algar Mining"}
     :dragonislesmining  {:id 2833 :name "Dragon Isles Mining"}
     :shadowlandsmining  {:id 2761 :name "Shadowlands Mining"}
     :kultiranmining     {:id 2565 :name "Kul Tiran Mining"}
     :legionmining       {:id 2566 :name "Legion Mining"}
     :draenormining      {:id 2567 :name "Draenor Mining"}
     :pandariamining     {:id 2568 :name "Pandaria Mining"}
     :cataclysmmining    {:id 2569 :name "Cataclysm Mining"}
     :northrendmining    {:id 2570 :name "Northrend Mining"}
     :outlandmining      {:id 2571 :name "Outland Mining"}
     :mining             {:id 2572 :name "Mining"}}
   :skinning {
     :khazalgarskinning    {:id 2882 :name "Khaz Algar Skinning"}
     :dragonislesskinning  {:id 2834 :name "Dragon Isles Skinning"}
     :shadowlandsskinning  {:id 2762 :name "Shadowlands Skinning"}
     :kultiranskinning     {:id 2557 :name "Kul Tiran Skinning"}
     :legionskinning       {:id 2558 :name "Legion Skinning"}
     :draenorskinning      {:id 2559 :name "Draenor Skinning"}
     :pandariaskinning     {:id 2560 :name "Pandaria Skinning"}
     :cataclysmskinning    {:id 2561 :name "Cataclysm Skinning"}
     :northrendskinning    {:id 2562 :name "Northrend Skinning"}
     :outlandskinning      {:id 2563 :name "Outland Skinning"}
     :skinning             {:id 2564  :name "Skinning"}}
   :tailoring {
     :khazalgartailoring    {:id 2883 :name "Khaz Algar Tailoring"}
     :dragonislestailoring  {:id 2831 :name "Dragon Isles Tailoring"}
     :shadowlandstailoring  {:id 2759 :name "Shadowlands Tailoring"}
     :kultirantailoring     {:id 2533 :name "Kul Tiran Tailoring"}
     :legiontailoring       {:id 2534 :name "Legion Tailoring"}
     :draenortailoring      {:id 2535 :name "Draenor Tailoring"}
     :pandariatailoring     {:id 2536 :name "Pandaria Tailoring"}
     :cataclysmtailoring    {:id 2537 :name "Cataclysm Tailoring"}
     :northrendtailoring    {:id 2538 :name "Northrend Tailoring"}
     :outlandtailoring      {:id 2539 :name "Outland Tailoring"}
     :tailoring             {:id 2540 :name "Tailoring"}}})

(def bn-reputations {
  :tww [
  ]
  :df [
    2503 "Maruuk Centaur"
    2506 "Dragonflight"
    2507 "Dragonscale Expedition"
    2510 "Valdrakken Accord"
    2511 "Iskaara Tuskarr"
    2517 "Wrathion"
    2518 "Sabellian"
    2523 "Dark Talons"
    2524 "Obsidian Warders"
    2526 "Winterpelt Furbolg"
    2544 "Artisan's Consortium - Dragon Isles Branch"
    2550 "Cobalt Assembly"
    2552 "Valdrakken Accord Paragon"
  ]
  :sl [
    2413 "Court of Harvesters"
    2464 "Court of Night"
    2462 "Stitchmasters"
    2407 "The Ascended"
    2439 "The Avowed"
    2410 "The Undying Army"
    2465 "The Wild Hunt"
    2478 "The Enlightened"
    2572 "The Archivists' Codex"
    2470 "Death's Advance"
    2461 "Plague Deviser Marileth"
    2460 "Stonehead"
    2458 "Kleia and Pelagos"
    2457 "Grandmaster Vole"
    2456 "Droman Aliothe"
    2455 "Cryptkeeper Kassir"
    2454 "Rendle and Cudgelface"
    2452 "Polemarch Adrestes"
    2451 "Hunt-Captain Korayn"
    2450 "Alexandros Mograine"
    2449 "The Countess"
    2448 "Mikanikos"
    2447 "Lady Moonberry"
    2446 "Baroness Vashj"
    2445 "The Ember Court"
    2414 "Shadowlands?"
    2432 "Ve'nari"]
  :bfa [
    2159 "7th Legion"
    2164 "Champions of Azeroth"
    2161 "Order of Embers"
    2160 "Proudmoore Admiralty"
    2415 "Rajani"
    2391 "Rustbolt Resistance"
    2162 "Storm's Wake"
    2156 "Talanji's Expedition"
    2157 "The Honorbound"
    2373 "The Unshackled"
    2163 "Tortollan Seekers"
    2417 "Uldum Accord"
    2158 "Voldunai"
    2400 "Waveblade Ankoan"
    2103 "Zandalari Empire"]
  :legion [
    2170 "Argussian Reach"
    2045 "Armies of Legionfall"
    2165 "Army of the Light"
    9997 "Chromie"
    1900 "Court of Farondis"
    1883 "Dreamweavers"
    1828 "Highmountain Tribe"
    1859 "The Nightfallen"
    1894 "The Wardens"
    1948 "Valarjar"]
  :legion2 [
    1947 "Illidari"
    1888 "Jandvik Vrykul"
    1899 "Moonguard"
    2018 "Talon's Vengeance"
    1984 "The First Responders"]
  :wod [
    1515 "Arakkoa Outcasts"
    1735 "Barracks Bodyguards"
    1731 "Council of Exarchs"
    1445 "Frostwolf Orcs"
    1847 "Hand of the Prophet"
    1708 "Laughing Skull Orcs"
    1849 "Order of the Awakened"
    1520 "Shadowmoon Exiles"
    1710 "Sha'tari Defense"
    1711 "Steamwheedle Preservation Society"
    1681 "Vol'jin's Spear"
    1850 "The Saberstalkers"
    1848 "Vol'jin's Headhunters"]
  :horde-only [
    2373
    2103 2156 2157 2158
    1445 1708 1681 1848]
  :alliance-only [
    2400
    2159 2160 2161 2162
    1520 1731 1847 1710]
  :horde [
      68 "Undercity"
      76 "Orgrimmar"
      81 "Thunder Bluff"
     530 "Darkspear Trolls"
     911 "Silvermoon City"
    1133 "Bilgewater Cartel"
    1352 "Huojin Pandaren"]
  :alliance [
      47 "Ironforge"
      54 "Gnomeregan"
      69 "Darnassus"
      72 "Stormwind"
     509 "The Leage of Arathor"
     930 "Exodar"
    1134 "Gilneas"
    1353 "Tushui Pandaren"]
})
