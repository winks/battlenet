# battlenet

Clojure library to read WoW characters from Blizzard's Battle.net API.

Blizzard changed their API in 2020 yet again and I've not had the
time and motivation to fix this properly, so v1 is a partial rewrite
of version 0.5, which is still sitting in a branch named `v0.5`.

As of TWW the versioning now follows this schema:

  * `MAJOR.MINOR.WOW_VERSION.PATCH`
  * e.g. the first TWW release was `1.0.11.0`
  * the first Midnight release might be `1.0.12.0`

## howto

Step 1: get a Blizzard API token (should be valid for 24h)

```
# either install `curl` + `sed` + `jq` and do this, or somehow put a token into a file

cp Makefile.example Makefile
vi Makefile
# edit the "xxx:yyy" part

make token
```

Step 2: run it

```
# prepare config

cp src/battlenet/config.clj.example src/battlenet/config.clj
vi src/battlenet/config.clj

# prepare input
mkdir data/input
echo "SERVERNAME;CHARNAME" > data/input/my-list.txt

# run
export BLIZZARD_APIKEY=$(cat /path/to/token/from/step/1)
make my-list
```

Step 3:

Your output will be in `resources/public/`

## web

```
lein ring server-headless
```
