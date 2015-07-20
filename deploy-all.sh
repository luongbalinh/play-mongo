#!/usr/bin/env bash
orig="$PWD"
cd ..
ws="$PWD"
if [ ! -z "$1" -a "$1" != " " ]; then
  target="-DredmartRepo=$1"
fi
modules=("play-reactivemongo" "mp-models" "mp-utils" "mp-finance-core")
for module in "${modules[@]}"; do
  cd "$ws/$module"
  sbt -DpublishDoc=false -DpublishSrc=false ${target} publish
done
cd "$orig"