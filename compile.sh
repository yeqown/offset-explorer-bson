#!/bin/bash

# 创建目录
mkdir -p lib
mkdir -p out

# 下载MongoDB BSON库依赖
echo "Downloading MongoDB BSON dependencies..."
curl -L -o lib/bson-4.9.1.jar https://repo1.maven.org/maven2/org/mongodb/bson/4.9.1/bson-4.9.1.jar

# 编译代码
echo "Compiling..."
javac -d out -sourcepath src -cp lib/bson-4.9.1.jar src/com/kafkatool/external/BsonDecorator.java

echo "Compilation complete."
