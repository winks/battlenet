battlenet
=========

Clojure library for Blizzard's Community Platform API.

Infos
-----
This is far from stable and just some remotely useful project while trying
to dig into Clojure. It's probably ugly or even just plain wrong. Also outdated.

Use these to inject your own api key/locale setting for api calls:
```
export BATTLENET_LOCALE=en_GB
export BATTLENET_APIKEY=XXX
```

Setup
-----
core.clj has most of the useful stuff, with a few more in tools.clj.
Some API-reading functions in network.clj don't have wrappers yet.

There's an example how to read from the D3 api in `examples/d3example`,
run it with `lein run` in that folder.

Shouts
------
* [Blizzard Community API](http://us.battle.net/wow/en/forum/2626217/)
* Loosely based on [battlenet-python](https://github.com/vishnevskiy/battlenet)
* [This](http://java.ociweb.com/mark/clojure/article.html) and [this](http://moxleystratton.com/clojure/clojure-tutorial-for-the-non-lisp-programmer) helped me a lot
* [Leiningen](https://github.com/technomancy/leiningen) is simply awesome
* [Radagast](https://github.com/technomancy/radagast) told me that my coverage is not too bad.
