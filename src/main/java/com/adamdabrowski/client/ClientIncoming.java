package com.adamdabrowski.client;

import com.adamdabrowski.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ClientIncoming implements Runnable {
    private ObjectInputStream input;

    ClientIncoming(ObjectInputStream input) {
        this.input = input;
    }

    @Override
    public void run() {
        do {
            try {
                Object message = input.readObject();
                if (message instanceof Message) {
                    Message msg = (Message) message;
                    System.out.printf("Response: %s | %s\n\n", msg.type, msg.payload);
                } else {
                    System.out.printf("Response: %s\n\n", message.toString());
                }
            } catch (SocketException | EOFException e) {
                return; // Socket closed
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } while (!Thread.interrupted());
    }
}
