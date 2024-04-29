package Shared;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private int port;
    private Socket socket;
    private List<Notification> notifications;

    public Client(int port, Socket socket) {
        this.port = port;
        this.socket = socket;
        this.notifications = new ArrayList<>();
    }

    public Client addNotification(Notification notification) {
        this.notifications.add(notification);
        return this;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public Client getClientByPort(int port) {
        return this.port == port ? this : null;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
