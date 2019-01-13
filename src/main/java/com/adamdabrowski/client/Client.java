package com.adamdabrowski.client;

import com.adamdabrowski.Config;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket client = new Socket(Config.HOST, Config.PORT);
             ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream())) {

            Thread incoming = new Thread(new ClientIncoming(inputStream));
            Thread outgoing = new Thread(new ClientOutgoing(outputStream));

            incoming.start();
            outgoing.start();

            outgoing.join();
            incoming.interrupt();
        } catch (ConnectException e) {
            System.err.print("Server is not available.\n");
        } catch (InterruptedException | IOException e) {
            System.err.printf("Exception caught: %s\n", e);
            System.exit(1);
        }
    }
}
