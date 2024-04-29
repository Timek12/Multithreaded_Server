package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Shared.Client;
import Shared.Notification;

public class ClientHandler implements Runnable {
    private final Client client;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Client client) {
        this.client = client;
        try {
            out = new PrintWriter(client.getSocket().getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("history")) {
                    sendNotifications();
                    continue;
                }

                if(line.equals("exit")) {
                    break;
                }
                
                Notification notification = Notification.fromJson(line);
                client.addNotification(notification);
                sendNotification(notification);
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNotification(Notification notification) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                notification.setDateTimeAtCompleted(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                System.out.println("Sending message: " + notification.getMessage());
                out.println(notification.toJson());
            }
        }, notification.getDelayInSeconds() * 1000);
    }

    public void sendNotifications() {
        List<Notification> notifications = client.getNotifications();
        for (Notification notification : notifications) {
            out.println(notification.toJson());
        }
        out.println("end");
    }

}
