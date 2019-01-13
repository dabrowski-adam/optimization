package com.adamdabrowski;

import java.io.Serializable;

public class Message implements Serializable {
    public MessageType type;
    public String payload;

    public Message(MessageType type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return type.toString() + " | " + payload;
    }
}
