package com.kafkatool.external;

import java.util.Map;

public class MinimalBsonDecorator implements ICustomMessageDecorator2 {
    
    public MinimalBsonDecorator() {
        // Empty constructor
    }
    
    @Override
    public String getDisplayName() {
        return "Minimal BSON";
    }
    
    @Override
    public String decorate(String zookeeperHost, String brokerHost, String topic, long partitionId, long offset, 
                          byte[] msg, Map<String, byte[]> headers, Map<String, String> reserved) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("BSON Message:\n");
            sb.append("Length: ").append(msg.length).append(" bytes\n");
            
            // 显示前20个字节的十六进制表示
            sb.append("Hex: ");
            for (int i = 0; i < Math.min(20, msg.length); i++) {
                sb.append(String.format("%02X ", msg[i]));
            }
            
            return sb.toString();
        } catch (Throwable t) {
            return "Error: " + t.getMessage();
        }
    }
}
