grep -rEl --exclude=*/target/* --exclude=*/src-gen/* --include=*.{yaml,xml,MF} "0.4.3(.qualifier|-SNAPSHOT)" * | xargs sed -i "" "s/0.4.3/0.5.0/g"