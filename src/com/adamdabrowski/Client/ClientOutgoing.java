package com.adamdabrowski.Client;

import com.adamdabrowski.Message;
import com.adamdabrowski.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class ClientOutgoing implements Runnable {
    private ObjectOutputStream output;

    ClientOutgoing(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        try {
            do {
                System.out.print("\n(optimize|visualize) f(x,y) = ...\n");

                input = scanner.nextLine();
                String[] arr = input.split(" ", 2);

                if (arr.length != 2) {
                    continue;
                }

                String action = arr[0].toUpperCase();
                String function = arr[1];

                switch (action) {
                    case "OPTIMIZE":
                        output.writeObject(new Message(MessageType.NYI, function));
                        break;
                    case "VISUALIZE":
                        output.writeObject(new Message(MessageType.NYI, function));
                        break;
                    default:
                        break;
                }
            } while (!Thread.interrupted());

            output.writeObject(new Message(MessageType.BYE, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
