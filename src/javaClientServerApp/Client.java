package javaClientServerApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {

        final int[] team = {0};

        JFrame frame = new JFrame();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        JProgressBar progressBar;

//        int n = JOptionPane.showConfirmDialog(
//                frame,
//                "Wybierz drużynę: ",
//                "Wybierz drużynę",
//                JOptionPane.YES_NO_OPTION);


        //=====GUI=================================================================================================

        //=====PANELS=====
        panel1.setBackground(Color.DARK_GRAY);
        panel2.setBackground(Color.DARK_GRAY);
        panel3.setBackground(Color.LIGHT_GRAY);
        panel4.setBackground(Color.GRAY);

//        panel1.setPreferredSize(new Dimension(100, 100));
//        panel2.setPreferredSize(new Dimension(100, 50));
        panel3.setPreferredSize(new Dimension(100, 50));
//        panel4.setPreferredSize(new Dimension(100, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets.top = 10; // Top margin
        gbc.insets.bottom = 10; // Bottom margin
        gbc.gridy = GridBagConstraints.RELATIVE;

        panel3.setLayout(new GridBagLayout());
        panel4.setLayout(new GridBagLayout());

        //=====LABELS=====
        JLabel titleLabel = new JLabel("PRZECIAGANIE LINY - JAVA GAME");
        titleLabel.setHorizontalTextPosition(JLabel.CENTER);
        titleLabel.setVerticalTextPosition(JLabel.CENTER);
        Font titleFont = new Font("Times New Roman", Font.BOLD, 24);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);

        JLabel teamLabel = new JLabel("<team>");
        Font teamFont = new Font("Times New Roman", Font.BOLD, 16);
        teamLabel.setForeground(Color.WHITE);
        teamLabel.setFont(teamFont);

        //=====PROGRESS_BAR=====
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(50);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(250, progressBar.getPreferredSize().height));
        progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, 50));

        //=====BUTTONS=====
        JButton pullButton = new JButton("W prawo!");
        JButton pushButton = new JButton("W lewo!");
        JButton exitButton = new JButton("Zamknij");

//        pushButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });

        //=====FRAME=====
        frame.setTitle("PRZECIAGANIE LINY - JAVA GAME");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setSize(500, 250);

        //=====ADDS=====

        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.add(panel3, BorderLayout.EAST);
        frame.add(panel4, BorderLayout.CENTER);

        panel1.add(titleLabel);
        panel4.add(progressBar);
        panel3.add(teamLabel, gbc);
        panel3.add(pullButton, gbc);
        panel3.add(pushButton, gbc);
        panel3.add(exitButton, gbc);

        ImageIcon imageIcon = new ImageIcon("src/logo_java.png");
        frame.setIconImage(imageIcon.getImage());

//        frame.setVisible(true);
//        frame.repaint();
//        frame.revalidate();

        //=====TEAM_FRAME=====
        JFrame teamFrame = new JFrame();
        teamFrame.setTitle("WYBIERZ DRUŻYNĘ - JAVA GAME");
        teamFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        teamFrame.setLayout(new GridBagLayout());
        teamFrame.setResizable(false);
        teamFrame.setSize(400, 200);

        JPanel teamPanel = new JPanel();

        JButton teamRightButton = new JButton("PRAWO");
        JButton teamLeftButton = new JButton("LEWO");

        teamFrame.add(teamPanel);
        teamPanel.add(teamLeftButton);
        teamPanel.add(teamRightButton);

        teamFrame.setVisible(true);
        teamFrame.repaint();
        teamFrame.revalidate();

        //=========================================================================================================

        String hostName = "localhost";
        int portNumber = 7777;
        final boolean[] isRunning = {true};

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {

            String fromServer;
            final String[] fromUser = new String[1];

            //====TEAM_BUTTONS_ACTIONS====
            teamLeftButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    synchronized (fromUser) {
                        fromUser[0] = "/left";
                        fromUser.notify();
                    }
                    System.out.println(fromUser[0]);
                    team[0] = -1;
                    teamLabel.setText("LEWO");
                    teamFrame.setVisible(false);
                    frame.setVisible(true);
                    frame.repaint();
                    frame.revalidate();
                }
            });

            teamRightButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    synchronized (fromUser) {
                        fromUser[0] = "/right";
                        fromUser.notify();
                    }
                    System.out.println(fromUser[0]);
                    team[0] = 1;
                    teamLabel.setText("PRAWO");
                    teamFrame.setVisible(false);
                    frame.setVisible(true);
                    frame.repaint();
                    frame.revalidate();
                }
            });

            //====BUTTONS_ACTIONS====
            pullButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    synchronized (fromUser) {
                        fromUser[0] = "/pull";
                        fromUser.notify();
                    }
                    System.out.println(fromUser[0]);
                }
            });

            pushButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    synchronized (fromUser) {
                        fromUser[0] = "/push";
                        fromUser.notify();
                    }
                    System.out.println(fromUser[0]);
                }
            });

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    synchronized (fromUser) {
                        fromUser[0] = "/exit";
                        fromUser.notify();
                    }
                    System.out.println(fromUser[0]);
                }
            });

            // watek wysylajacy wiadomosci fromUser do serwera
            Thread writerThread = new Thread(() -> {

                    while (isRunning[0]) {
                        synchronized (fromUser) {
                            try {
                                while (fromUser[0] == null && isRunning[0]) {
                                    fromUser.wait();
                                }
                                if (fromUser[0] != null) {
                                    out.println(fromUser[0]);
                                    if (fromUser[0].equals("/exit")) {
                                        isRunning[0] = false;
                                        System.exit(1);
                                    }
                                    fromUser[0] = null;
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.err.println("Watek wysylajacy przerwany: " + e.getMessage());
                            }
                        }
                    }

            });
            writerThread.start();

            // odczytywanie fromServer
            while ((fromServer = in.readLine()) != null && isRunning[0]) {
                System.out.println("[SERVER]: " + fromServer);
                String rawFromServer = fromServer;
//                String[] partsFromServer = rawFromServer.split(":");
//                String partNumber = partsFromServer[1].trim();
//                int number = Integer.parseInt(partNumber);
                try {
                    int number = Integer.parseInt(rawFromServer);
                    if (number == -1) {
                        socket.close();
                        System.out.println("ZWYCIESTWO PRAWYCH");
                        JOptionPane.showMessageDialog(frame,
                                "ZWYCIESTWO PRAWYCH");
                    }
                    if (number == -2) {
                        socket.close();
                        System.out.println("ZWYCIESTWO LEWYCH");
                        JOptionPane.showMessageDialog(frame,
                                "ZWYCIESTWO LEWYCH");
                    }
                    progressBar.setValue(number);
                } catch (NumberFormatException e) {
                    System.err.println("Otrzymano nieprawidlowa wartosc od serwera: " + fromServer);
                }
            }

            writerThread.join();

        } catch (UnknownHostException e) {
            System.err.println("Nieznamy host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Brak komunikacji z hostem: " + hostName);
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Watek wysylajacy przerwany: " + e.getMessage());
            System.exit(1);
        }

    }

}