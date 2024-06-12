package javaClientServerApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {
                outputLine = inputLine;
                if (outputLine.equals("/pull")) {
                    Server.modifyRopeValue(5);
                }
                if (outputLine.equals("/push")) {
                    Server.modifyRopeValue(-5);
                }
                if (outputLine.equals("/exit")) {
                    break;
                }

            }

        } catch (IOException e) {
            System.err.println("[SERVER]: Blad polaczenia: " + e.getMessage());
        } finally {
            try {
                socket.close();
                out.close();
                in.close();
            } catch (IOException e) {
                System.err.println("[SERVER]: Blad zamykania polaczenia soketu: " + e.getMessage());
            }
            Server.removeServerThread(this);
            System.out.println("[DEBUG]: Usunieto serverThread");
        }

    }

    public synchronized void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            out.flush();
        }
    }
}
