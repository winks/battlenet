(ns battlenet.model)

; BRealm
;
; A WoW realm
;
; type       -> string
; queue      -> boolean
; status     -> boolean
; population -> string
; name       -> string
; battlegroup -> string
; slug       -> string
; region     -> string
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

(defrecord BItem
  [requiredLevel quality requiredSkill minFactionId itemClass
   bonusStats sellPrice isAuctionable icon inventoryType
   maxCount name maxDurability buyPrice itemSource
   containerSlots stackable itemLevel hasSockets disenchantingSkillRank
   requiredSkillRank itemSpells minReputation baseArmor itemSubClass
   id description equippable itemBind])