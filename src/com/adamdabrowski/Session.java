package com.adamdabrowski;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;

public class Session implements Runnable, AutoCloseable {
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    Session(Socket client) throws IOException {
        this.client = client;
        this.input = new ObjectInputStream(client.getInputStream());
        this.output = new ObjectOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        String id = client.getInetAddress().toString() + "@" + LocalTime.now().toString();

        try {
            loop: for (Message msg = readMessage(); msg != null; msg = readMessage()) {
                System.out.printf("%s :: %s | %s\n", id, msg.type, msg.payload);
                switch (msg.type) {
                    case HELLO:
                        sendMessage(new Message(MessageType.NYI, ""));
                        break;
                    case BYE:
                        sendMessage(new Message(MessageType.NYI, ""));
                        break loop;
                    case ERROR:
                        sendMessage(new Message(MessageType.NYI, ""));
                        break;
                    case TEXT:
                        sendMessage(new Message(MessageType.NYI, ""));
                        break;
                }

                if (Thread.interrupted()) {
                    break;
                }
            }
        } catch (EOFException e) {
            // Connection dropped.
        } catch (IOException|ClassNotFoundException e) {
            System.err.printf("Exception caught: %s\n", e);
        } finally {
            System.out.printf("%s :: Connection ended.\n", id);
            try {
                client.close();
            } catch (Exception e) {
                System.err.printf("Exception caught: %s\n", e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    private Message readMessage() throws IOException, ClassNotFoundException {
        Object message = input.readObject();
        if (message instanceof Message) {
            return (Message) message;
        }
        return null;
    }

    private void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    }
}
