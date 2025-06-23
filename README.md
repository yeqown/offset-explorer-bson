# BSON Decoder Plugin for Offset Explorer (Kafka Tool)

This plugin enables Offset Explorer (Kafka Tool) to parse and display Kafka messages in BSON format.

## Features

- Parse BSON binary messages into readable JSON format.
- Support formatted output for easy reading.
- Provide error handling and debugging information.

## Installation and Usage

### Prebuilt JAR

1. Download the prebuilt JAR file from the [Releases](URL_ADDRESS1. Download the prebuilt JAR file from the [Releases](https://github.com/yourusername/offset-explorer-bson-plugin/releases) page.
2. Place the JAR file in the `plugins` directory of Offset Explorer.
3. Restart Offset Explorer.
4. Change the content type of the topic to `BSON`.

### Manual Installation

If you do not want to use Makefile, you can manually execute the following steps:

1. Compile and install the plugin:
   ```
   make install
   ```
2. Restart Offset Explorer
3. Change the content type of the topic to `BSON`.
