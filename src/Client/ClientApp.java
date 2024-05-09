package Client;

import Client.Exceptions.EmptyMessageException;
import Client.Exceptions.InvalidDurationException;
import Client.Exceptions.InvalidInputException;
import Shared.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
                displayMenu();
                int choice;
                String message = "";
                try {
                    String stringChoice = sc.nextLine();
                    choice = Integer.parseInt(stringChoice);
                    if (choice < 1 || choice > 3) {
                        throw new InvalidInputException("Invalid choice, please enter a number between 1 and 3");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice, please enter a number between 1 and 3");
                    continue;
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                switch (choice) {
                    case 1:
                        try {
                            System.out.print("Enter the message: ");
                            message = sc.nextLine();
                            if (message.equals("exit")) {
                                break;
                            }

                            if (message.isEmpty()) {
                                throw new EmptyMessageException("Message cannot be empty");
                            }
                        } catch (EmptyMessageException e) {
                            System.out.println(e.getMessage());
                            continue;
                        }

                        System.out.print("Enter the delay in seconds: ");
                        try {
                            String delay = sc.nextLine();
                            int delayInSeconds = Integer.parseInt(delay);
                            if (delayInSeconds < 0) {
                                throw new InvalidDurationException("Delay in seconds cannot be negative");
                            }

                            Notification notification = new Notification(message, delayInSeconds,
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                            out.println(notification.toJson());

                            System.out.println("Message sent, waiting for server response...");

                            // block and wait for response from server
                            String response = in.readLine();
                            Notification responseNotification = Notification.fromJson(response);

                            System.out.printf("[%s]: %s\n", responseNotification.getDateTimeAtCompleted(),
                                    responseNotification.getMessage());

                        } catch (NumberFormatException e) {
                            System.out.println("Delay in seconds should be an integer");
                        } 
                        catch (InvalidDurationException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 2:
                        out.println("history");
                        System.out.println("Fetching notification history...");

                        List<Notification> notifications = readNotifications(in);

                        displayNotifications(notifications);
                        break;
                    case 3:
                        out.println("exit");
                        System.out.println("Exiting...");
                        sc.close();
                        socket.close();
                        return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Notification> readNotifications(BufferedReader in) throws IOException {
        List<Notification> notifications = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals("end")) {
                break;
            }
            Notification notification = Notification.fromJson(line);
            notifications.add(notification);
        }
        return notifications;
    }

    public static void displayNotifications(List<Notification> notifications) {
        for (Notification notification : notifications) {
            System.out.printf("[%s]: %s\n", notification.getDateTimeAtCompleted(), notification.getMessage());
        }
    }

    public static void displayMenu() {
        System.out.printf("\n------ MENU ------\n");
        System.out.println("1. Send a notification");
        System.out.println("2. Display notification history");
        System.out.println("3. Exit");
    }
}
