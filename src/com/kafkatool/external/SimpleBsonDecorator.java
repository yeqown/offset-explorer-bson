package com.kafkatool.external;

import java.util.Map;

public class SimpleBsonDecorator implements ICustomMessageDecorator2 {

    static {
        System.out.println("SimpleBsonDecorator class loaded!");
    }

    public SimpleBsonDecorator() {
        System.out.println("SimpleBsonDecorator constructor called!");
    }

    @Override
    public String getDisplayName() {
        return "Simple BSON Decoder";
    }

    @Override
    public String decorate(String zookeeperHost, String brokerHost, String topic, long partitionId, long offset, 
                          byte[] msg, Map<String, byte[]> headers, Map<String, String> reserved) {
        if (msg == null || msg.length == 0) {
            return "Empty message";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Message length: ").append(msg.length).append(" bytes\n");
        sb.append("First 20 bytes (hex): ");
        
        for (int i = 0; i < Math.min(msg.length, 20); i++) {
            sb.append(String.format("%02X ", msg[i]));
        }
        
        return sb.toString();
    }
}
