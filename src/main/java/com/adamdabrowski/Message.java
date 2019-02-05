package com.adamdabrowski;

import java.io.Serializable;

public class Message implements Serializable {
    public MessageType type;
    public AlgorithmType algorithm;
    public String payload;

    public Message(MessageType type, AlgorithmType algorithm, String payload) {
        this.type = type;
        this.algorithm = algorithm;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return type.toString() + " | " + payload;
    }
}
