(ns battlenet.model)

; BRealm
;
; A WoW realm
;
; type        -> string
; queue       -> boolean
; status      -> boolean
; population  -> string
; name        -> string
; battlegroup -> string
; slug        -> string
; region      -> string
(defrecord BRealm
  [type queue status population name battlegroup slug region])

; BCharacter
;
; A WoW character
;
; name         -> string
; realm        -> BRealm
; class        -> integer
; race         -> integer
; gender       -> integer
; level        -> integer
; achPoints    -> integer
; thumbnail    -> string
; lastModified -> integer
(defrecord BCharacter
  [name realm class race gender level achPoints thumbnail lastModified])

; BGuild
;
; A WoW guild
;
; name         -> string
; realm        -> BRealm
; level        -> integer
; side         -> integer
; achPoints    -> integer
; emblem       -> map
; lastModified -> integer
(defrecord BGuild
  [name realm level side achPoints emblem lastModified])

; BItem
;
; A WoW item
;
; name                   -> string
; id                     -> integer
; description            -> string
; requiredLevel          -> integer
; quality                -> integer
; requiredSkill          -> integer
; minFactionId           -> integer
; itemClass              -> integer
; bonusStats             -> vector
; sellPrice              -> integer
; isAuctionable          -> boolean
; icon                   -> string
; inventoryType          -> integer
; maxCount               -> integer
; maxDurability          -> integer
; buyPrice               -> integer
; itemSource             -> map
; containerSlots         -> integer
; stackable              -> integer
; itemLevel              -> integer
; hasSockets             -> boolean
; disenchantingSkillRank -> integer
; requiredSkillRank      -> integer
; itemSpells             -> vector
; minReputation          -> integer
; baseArmor              -> integer
; itemSubClass           -> integer
; equippable             -> boolean
; itemBind               -> integer

(defrecord BItem
  [requiredLevel quality requiredSkill minFactionId itemClass
   bonusStats sellPrice isAuctionable icon inventoryType
   maxCount name maxDurability buyPrice itemSource
   containerSlots stackable itemLevel hasSockets disenchantingSkillRank
   requiredSkillRank itemSpells minReputation baseArmor itemSubClass
   id description equippable itemBind])