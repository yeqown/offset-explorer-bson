package com.kafkatool.external;

import org.bson.BsonBinaryReader;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;

import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.Map;

public class BsonDecorator implements ICustomMessageDecorator2 {

    private JsonWriterSettings jsonWriterSettings;
    
    // 静态初始化块，在类加载时执行
    static {
        try {
            System.out.println("BSON Decoder plugin class loaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BsonDecorator() {
        try {
            // 配置JSON输出设置，使用漂亮的打印格式
            this.jsonWriterSettings = JsonWriterSettings.builder()
                    .indent(true)
                    .indentCharacters("  ")
                    .newLineCharacters("\n")
                    .outputMode(JsonMode.EXTENDED)
                    .build();
            
            // 打印调试信息，确认插件已加载
            System.out.println("BSON Decoder plugin initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing BSON Decoder plugin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getDisplayName() {
        // 返回一个更明显的名称，以便在下拉菜单中容易识别
        return "BSON Decoder (Custom)";
    }

    @Override
    public String decorate(String zookeeperHost, String brokerHost, String topic, long partitionId, long offset, 
                          byte[] msg, Map<String, byte[]> headers, Map<String, String> reserved) {
        try {
            if (msg == null || msg.length == 0) {
                return "Empty message";
            }

            // 尝试解析BSON
            Document document = Document.parse(bsonToJson(msg));
            
            // 将Document转换为格式化的JSON字符串
            return documentToFormattedJson(document);
        } catch (Exception e) {
            // 如果解析失败，返回错误信息
            return "Failed to parse BSON: " + e.getMessage() + "\n" +
                   "Message length: " + msg.length + " bytes\n" +
                   "First few bytes (hex): " + bytesToHex(msg, 0, Math.min(msg.length, 20));
        }
    }

    /**
     * 将BSON字节数组转换为JSON字符串
     */
    private String bsonToJson(byte[] bsonBytes) {
        try {
            // 使用MongoDB的BSON解析器
            BsonBinaryReader reader = new BsonBinaryReader(ByteBuffer.wrap(bsonBytes));
            BsonDocumentCodec documentCodec = new BsonDocumentCodec();
            BsonDocument document = documentCodec.decode(reader, DecoderContext.builder().build());
            
            // 将BSON文档转换为JSON
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter, jsonWriterSettings);
            documentCodec.encode(jsonWriter, document, EncoderContext.builder().build());
            
            return stringWriter.toString();
        } catch (Exception e) {
            // 如果上面的方法失败，尝试另一种解析方式
            try {
                Document doc = new DocumentCodec().decode(
                    new BsonBinaryReader(ByteBuffer.wrap(bsonBytes)), 
                    DecoderContext.builder().build()
                );
                return doc.toJson(jsonWriterSettings);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to parse BSON with multiple methods", ex);
            }
        }
    }

    /**
     * 将Document对象转换为格式化的JSON字符串
     */
    private String documentToFormattedJson(Document document) {
        return document.toJson(jsonWriterSettings);
    }

    /**
     * 将字节数组转换为十六进制字符串表示
     */
    private String bytesToHex(byte[] bytes, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < offset + length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
    }
}
