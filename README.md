# battlenet

Clojure library to read WoW characters from Blizzard's Battle.net API.

Blizzard changed their API in 2020 yet again and I've not had the
time and motivation to fix this properly, so v1 is a partial rewrite
of version 0.5, which is still sitting in a branch named `v0.5`.

## howto

Step 1: get a token (should be valid for 24h)

```
# either install curl + sed + jq and do this, or somehow put a token into a file

cp Makefile.example Makefile
vi Makefile
# edit the "x:y" part

make token
```

step 2: run

```
cp src/battlenet/config.clj.example src/battlenet/config.clj
vi src/battlenet/config.clj

make test
```

## web

```
lein ring server-headless
```