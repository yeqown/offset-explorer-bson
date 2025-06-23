package com.offsetexplorer.external;

import org.bson.BsonBinaryReader;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BsonDecorator implements ICustomMessageDecorator2 {

    private static final JsonWriterSettings JSON_WRITER_SETTINGS =
            JsonWriterSettings.builder()
                    .outputMode(JsonMode.RELAXED)
                    .build();
    private static final CodecRegistry CODEC_REGISTRY = CodecRegistries.fromRegistries(
            com.mongodb.MongoClientSettings.getDefaultCodecRegistry()
    );
    private static final org.bson.codecs.DocumentCodec DOCUMENT_CODEC = new org.bson.codecs.DocumentCodec(CODEC_REGISTRY);

    public BsonDecorator() {
        // 打印调试信息，确认插件已加载
        System.out.println("BSON Decoder plugin initialized successfully!");
    }

    @Override
    public String getDisplayName() {
        return "BSON";
    }

    @Override
    public String decorate(String zookeeperHost, String brokerHost, String topic, long partitionId, long offset, 
                          byte[] msg, Map<String, byte[]> headers, Map<String, String> reserved) {
        try {
            if (msg == null || msg.length == 0) {
                return "Empty message";
            }

            // 使用MongoDB的BSON解析器
            BsonBinaryReader reader = new BsonBinaryReader(ByteBuffer.wrap(msg));
            Document document = DOCUMENT_CODEC.decode(reader, org.bson.codecs.DecoderContext.builder().build());
            return document.toJson(JSON_WRITER_SETTINGS);
        } catch (Exception e) {
            // 如果解析失败，返回错误信息, 记录错误日志，返回原始消息
            System.err.println("Failed to decode BSON message: " + e.getMessage());
            return (new String(msg, StandardCharsets.UTF_8));
        }
    }
}
