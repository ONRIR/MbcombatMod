#!/usr/bin/env python3
import zipfile
import os

os.makedirs('build/libs', exist_ok=True)

with zipfile.ZipFile('build/libs/bannerlordcombat-1.0.0-sources.jar', 'w', zipfile.ZIP_DEFLATED) as jar:
    for root, dirs, files in os.walk('src/main/java'):
        for file in files:
            if file.endswith('.java'):
                filepath = os.path.join(root, file)
                arcname = filepath.replace('src/main/java/', '')
                jar.write(filepath, arcname)
    
    for root, dirs, files in os.walk('src/main/resources'):
        for file in files:
            filepath = os.path.join(root, file)
            arcname = filepath.replace('src/main/resources/', '')
            jar.write(filepath, arcname)

print("Created bannerlordcombat-1.0.0-sources.jar")
