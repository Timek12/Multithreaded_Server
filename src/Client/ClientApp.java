package Client;

import Client.Exceptions.InvalidInputException;
import Shared.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientApp {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("Enter the message: ");
                String message = sc.nextLine();
                if (message.equals("exit")) {
                    break;
                }
                System.out.println("Enter the delay in seconds: ");
                try {
                    String delay = sc.nextLine();
                    int delayInSeconds = Integer.parseInt(delay);
                    if (delayInSeconds < 0) {
                        throw new InvalidInputException("Delay in seconds cannot be negative");
                    }

                    Notification notification = new Notification(message, delayInSeconds,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    out.println(notification.toJson());

                    System.out.println("Message sent, waiting for server response...");
                    // block and wait for response from server
                    String response = in.readLine();
                    Notification responseNotification = Notification.fromJson(response);
                    
                    System.out.printf("[%s]: %s\n", responseNotification.getDateTimeAtCompleted(), responseNotification.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Delay in seconds should be an integer");
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
