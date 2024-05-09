package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Shared.Client;

public class Server {

    private static List<Client> connectedClients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) { // try-with-resources
            serverSocket.setReuseAddress(true);
            System.out.println("Server is running on port 8080");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // blocking call
                System.out.println("New client connected:" + clientSocket.getPort());

                Client newClient = new Client(clientSocket.getPort(), clientSocket);
                
                connectedClients.add(newClient);

                ClientHandler clientHandler = new ClientHandler(newClient);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
