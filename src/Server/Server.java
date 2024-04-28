package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Shared.Client;

public class Server {
    //private static Map<String, Notification> clientNotifications = new HashMap<>();
    private static List<Client> connectedClients = new ArrayList<>();
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            serverSocket.setReuseAddress(true);
            System.out.println("Server is running on port 8080");

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("New client connected:" + client.getPort());

                connectedClients.add(new Client(client.getPort(), client));

                ClientHandler clientSock = new ClientHandler(connectedClients.get(connectedClients.size() - 1));
                new Thread(clientSock).start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
