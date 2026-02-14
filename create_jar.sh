#!/bin/bash
cd /home/claude/bannerlord_combat_mod
mkdir -p jar_contents/com/bannerlordcombat
mkdir -p jar_contents/META-INF/neoforge
mkdir -p jar_contents/assets/bannerlordcombat/lang

cp -r src/main/resources/META-INF/neoforge/* jar_contents/META-INF/neoforge/
cp -r src/main/resources/assets/bannerlordcombat/lang/* jar_contents/assets/bannerlordcombat/lang/

cat > jar_contents/META-INF/MANIFEST.MF << 'EOF'
Manifest-Version: 1.0
FML-Language: java
Automatic-Module-Name: bannerlordcombat

EOF

cd jar_contents
jar cf ../build/libs/bannerlordcombat-1.0.0.jar .
cd ..
