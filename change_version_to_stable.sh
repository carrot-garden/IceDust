FROMVERSION=0.8.2
TOVERSION=0.8.2

grep -rEl --exclude=*/target/* --exclude=*/src-gen/* --include=*.{yaml,xml,MF} "${FROMVERSION}.qualifier" * | xargs sed -i "" "s/${FROMVERSION}.qualifier/${TOVERSION}/g"
grep -rEl --exclude=*/target/* --exclude=*/src-gen/* --include=*.{yaml,xml,MF} "${FROMVERSION}-SNAPSHOT" * | xargs sed -i "" "s/${FROMVERSION}-SNAPSHOT/${TOVERSION}/g"
