package com.example.chat;

public enum ConnectionEnum {
    ServerIP("192.168.15.116");

    private String ip;
    private  String serverIP;

    ConnectionEnum(String s) {
        this.ip = s;
    }

    public String getIp() {
        return ip;
    }

    public String getServerIP() {
        return serverIP;
    }
}
