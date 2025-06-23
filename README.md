# BSON Decoder Plugin for Offset Explorer (Kafka Tool)

这个插件允许Offset Explorer (Kafka Tool) 解析和显示BSON格式的Kafka消息。

## 功能

- 将BSON二进制消息解析为可读的JSON格式
- 支持格式化输出，便于阅读
- 提供错误处理和调试信息

## 安装步骤

### 使用Makefile（推荐）

项目包含一个Makefile，可以方便地编译、打包和安装插件：

1. 编译和打包插件：
   ```
   make
   ```

2. 安装插件到Offset Explorer：
   ```
   make install
   ```

3. 重启Offset Explorer

### 手动安装

如果您不想使用Makefile，也可以手动执行以下步骤：

1. 编译插件：
   ```
   chmod +x compile.sh
   ./compile.sh
   ```

2. 打包插件：
   ```
   chmod +x buildJar.sh
   ./buildJar.sh
   ```

3. 将生成的`bson-decoder.jar`文件复制到Offset Explorer安装目录下的`plugins`文件夹中：
   ```
   cp bson-decoder.jar /Applications/Offset\ Explorer\ 3.app/Contents/Resources/app/plugins/
   ```

4. 重启Offset Explorer

## 使用方法

1. 启动Offset Explorer
2. 导航到包含BSON格式消息的Kafka主题
3. 在"Content Type"下拉菜单中选择"BSON Decoder"
4. 点击"Update"按钮
5. 消息将以格式化的JSON形式显示

## Makefile 命令

项目的Makefile提供了以下命令：

- `make` 或 `make all` - 编译和打包插件
- `make compile` - 仅编译源代码
- `make package` - 将编译后的代码打包为JAR文件
- `make install` - 安装插件到Offset Explorer
- `make clean` - 清理生成的文件
- `make distclean` - 完全清理（包括依赖）
- `make help` - 显示帮助信息

## 依赖项

- MongoDB BSON库 (4.9.1)

## 故障排除

如果插件无法正确解析消息，它将显示错误信息和消息的前几个字节的十六进制表示，以帮助调试。

## 开发

如果您想修改或扩展这个插件，主要的实现代码在 `src/com/offsetexplorer/external/BsonDecorator.java` 文件中。
