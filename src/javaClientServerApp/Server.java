package javaClientServerApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ArrayList<ServerThread> serverThreads = new ArrayList<>();

    private static final int portNumber = 7777;
    private static int ropeValue = 50;

    public static void main(String[] args) {

        //watek odpowiedzialny za cykliczne wysylanie wiadomosci broakcast do polaczonych klientow
        boolean isRunning = false;
        Thread broadcastThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    broadcastRopeValue();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println("Watek broadcast przerwany: " + e.getMessage());
                    }
                }
            }
        });
        broadcastThread.start();
        isRunning = true;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber)
                ) {
            System.out.println("[SERVER]: Server uruchomiony!");
            if (isRunning) {
                System.out.println("[SERVER]: Watek broadcast uruchomiony!");
            } else {
                System.err.println("[SERVER]: Watek broadcast nie dziala poprawnie!");
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER]: Polaczono z klientem");
                ServerThread serverThread = new ServerThread(clientSocket);
                synchronized (serverThread) {
                    serverThreads.add(serverThread);
                }
                System.out.println("[DEBUG]: Dodano do arraylist");
                new Thread(serverThread).start();

            }
        } catch (IOException e) {
            System.err.println("Nie mozna sluchac na porcie: " + portNumber + "!");
            System.exit(-1);
        }

    }

    public static synchronized void removeServerThread(ServerThread serverThread) {
        serverThreads.remove(serverThread);
    }

    public static synchronized void modifyRopeValue(int value) {
        ropeValue += value;
        System.out.println("[SERVER]: Zmodyfikowano wartosc liny na: " + ropeValue);
        if (ropeValue >= 100) {
            for (ServerThread serverThread : serverThreads) {
                serverThread.sendMessage(String.valueOf(-1));
            }
        }
        if (ropeValue <= 0) {
            for (ServerThread serverThread : serverThreads) {
                serverThread.sendMessage(String.valueOf(-2));
            }
        }
    }

    private static synchronized void broadcastRopeValue() {
//        String message = "Aktualna wartosc liny: " + ropeValue;
        int message = ropeValue;
//        System.out.println(message);
        for (ServerThread serverThread : serverThreads) {
            serverThread.sendMessage(String.valueOf(message));
        }
    }

}
