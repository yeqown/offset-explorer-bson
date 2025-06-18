#!/bin/bash

# 创建临时目录
mkdir -p temp

# 复制编译后的类
cp -r out/* temp/

# 解压依赖的JAR文件
cd temp
echo "Extracting dependencies..."
for jar in ../lib/*.jar; do
  jar xf "$jar"
done

# 删除META-INF签名文件（避免签名冲突）
rm -rf META-INF/MANIFEST.MF
rm -rf META-INF/*.SF
rm -rf META-INF/*.RSA
rm -rf META-INF/*.DSA

# 创建JAR文件
echo "Creating JAR file..."
jar cvf ../bson-decoder.jar .

# 返回上级目录并清理
cd ..
rm -rf temp

echo "JAR file created: bson-decoder.jar"
