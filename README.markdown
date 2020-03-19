battlenet
=========

Clojure library for Blizzard's Community Platform API.

2020-03 Update
--------------

Unfortunately, Blizzard has yet again changed their API and I've not had the
time and motivation to fix this.
What I usually do is create a list of my chars to have an overview of Level,
Professions, and Reputations.

I started a small rewrite in `examples/w2/` which works with the API as of
March 2020, but the professions API is still being migrated by Blizzard.

The actual library, unfortunately will not do a lot, for now.

Infos
-----
This is far from stable and just some remotely useful project while trying
to dig into Clojure. It's probably ugly or even just plain wrong. Also outdated.

Use these to inject your own api key/locale setting for api calls:
```
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
