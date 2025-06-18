# BSON Decoder Plugin for Offset Explorer - Makefile

# 变量定义
PLUGIN_NAME = bson-decoder
PLUGIN_JAR = $(PLUGIN_NAME).jar
SRC_DIR = src
OUT_DIR = out
LIB_DIR = lib
TEMP_DIR = temp
KAFKA_TOOL_PLUGINS_DIR = /Applications/Offset\ Explorer\ 3.app/Contents/Resources/app/plugins

# MongoDB BSON库版本
BSON_VERSION = 4.9.1
BSON_JAR = bson-$(BSON_VERSION).jar
BSON_URL = https://repo1.maven.org/maven2/org/mongodb/bson/$(BSON_VERSION)/$(BSON_JAR)

# 默认目标
all: compile package

# 创建必要的目录
init:
	@echo "Creating directories..."
	@mkdir -p $(OUT_DIR)
	@mkdir -p $(LIB_DIR)
	@mkdir -p $(TEMP_DIR)

# 下载依赖
deps: init
	@echo "Downloading dependencies..."
	@if [ ! -f $(LIB_DIR)/$(BSON_JAR) ]; then \
		echo "Downloading MongoDB BSON library..."; \
		curl -L -o $(LIB_DIR)/$(BSON_JAR) $(BSON_URL); \
	else \
		echo "MongoDB BSON library already exists."; \
	fi

# 编译源代码
compile: deps
	@echo "Compiling source code..."
	@javac -d $(OUT_DIR) -sourcepath $(SRC_DIR) -cp $(LIB_DIR)/$(BSON_JAR) $(SRC_DIR)/com/kafkatool/external/BsonDecorator.java
	@echo "Compilation complete."

# 打包为JAR文件
package: compile
	@echo "Packaging JAR file..."
	@rm -rf $(TEMP_DIR)/*
	@cp -r $(OUT_DIR)/* $(TEMP_DIR)/
	@cd $(TEMP_DIR) && \
	for jar in ../$(LIB_DIR)/*.jar; do \
		echo "Extracting $$jar..."; \
		jar xf "$$jar"; \
	done
	@cd $(TEMP_DIR) && \
	rm -rf META-INF/MANIFEST.MF META-INF/*.SF META-INF/*.RSA META-INF/*.DSA
	@cd $(TEMP_DIR) && \
	jar cvf ../$(PLUGIN_JAR) .
	@echo "JAR file created: $(PLUGIN_JAR)"

# 安装插件到Offset Explorer
install: package
	@echo "Installing plugin to Offset Explorer..."
	@cp $(PLUGIN_JAR) "/Applications/Offset Explorer 3.app/Contents/Resources/app/plugins/"
	@echo "Plugin installed. Please restart Offset Explorer to use the plugin."

# 清理生成的文件
clean:
	@echo "Cleaning up..."
	@rm -rf $(OUT_DIR)
	@rm -rf $(TEMP_DIR)
	@rm -f $(PLUGIN_JAR)
	@echo "Clean complete."

# 完全清理（包括依赖）
distclean: clean
	@echo "Removing dependencies..."
	@rm -rf $(LIB_DIR)
	@echo "All cleaned up."

# 帮助信息
help:
	@echo "BSON Decoder Plugin for Offset Explorer - Makefile Help"
	@echo ""
	@echo "Available targets:"
	@echo "  all        - Default target: compile and package the plugin"
	@echo "  init       - Create necessary directories"
	@echo "  deps       - Download dependencies"
	@echo "  compile    - Compile the source code"
	@echo "  package    - Create the plugin JAR file"
	@echo "  install    - Install the plugin to Offset Explorer"
	@echo "  clean      - Remove generated files"
	@echo "  distclean  - Remove all generated files including dependencies"
	@echo "  help       - Display this help message"
	@echo ""
	@echo "Usage examples:"
	@echo "  make             - Compile and package the plugin"
	@echo "  make install     - Install the plugin to Offset Explorer"
	@echo "  make clean       - Clean up generated files"

.PHONY: all init deps compile package install clean distclean help
