package nio.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private final static ExecutorService CURRENT_THREAD = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        try {
            new Client().start();
        } finally {
            CURRENT_THREAD.shutdown();
        }
    }

    public void start() {
        CURRENT_THREAD.submit(() -> {
            try (Socket socket = new Socket("localhost", 9000);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                 BufferedReader reader = new BufferedReader(new InputStreamReader((socket.getInputStream())))) {

                System.out.println("Client started");

                while (true) {
                    Scanner in = new Scanner(System.in);
                    String input_message = in.nextLine();
                    writer.write(input_message);
                    writer.newLine();
                    writer.flush();
                    System.out.println("Echo message from server " + reader.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}