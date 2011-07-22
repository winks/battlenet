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
; slug       -> string
; region     -> string
(defrecord BRealm
  [type queue status population name slug region])

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
; lastModified -> string
(defrecord BCharacter
  [name realm class race gender level achPoints thumbnail lastModified])
