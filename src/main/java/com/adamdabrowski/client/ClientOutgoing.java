package com.adamdabrowski.client;

import com.adamdabrowski.Message;
import com.adamdabrowski.MessageType;
import com.adamdabrowski.AlgorithmType;

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
                System.out.print("\n(optimize|visualize) (genetic|annealing) f(x,y) = ...\n");

                input = scanner.nextLine();
                String[] arr = input.split(" ", 3);

                if (arr.length != 3) {
                    continue;
                }

                String action = arr[0].toUpperCase();
                String algorithm = arr[1].toUpperCase();
                String function = arr[2];

                MessageType messageType;
                AlgorithmType algorithmType;

                switch (function.toUpperCase()) {
                    case "ROSENBROCK":
                        function = "f(x,y) = (1 - x)^2 + 100*(y - x^2)^2";
                        break;
                }

                switch (algorithm) {
                    case "GENETIC":
                        algorithmType = AlgorithmType.GENETIC;
                        break;
                    case "ANNEALING":
                        algorithmType = AlgorithmType.ANNEALING;
                        break;
                    default:
                        continue;
                }

                switch (action) {
                    case "OPTIMIZE":
                        messageType = MessageType.OPTIMIZE;
                        break;
                    case "VISUALIZE":
                        messageType = MessageType.VISUALIZE;
                        break;
                    default:
                        continue;
                }

                output.writeObject(new Message(messageType, algorithmType, function));
            } while (!Thread.interrupted());

            output.writeObject(new Message(MessageType.BYE, AlgorithmType.NONE, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
