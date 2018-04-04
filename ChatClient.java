package SourceCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;


final class ChatClient {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;

    private final String server;
    private final String username;
    private final int port;
    private String messageSender;
    private String user;
    private boolean loggedOut = false;

    /* ChatClient constructor
     * @param server - the ip address of the server as a string
     * @param port - the port number the server is hosted on
     * @param username - the username of the user connecting
     */
    private ChatClient(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    private ChatClient(int port, String username) {
        this.server = "local host";
        this.port = port;
        this.username = username;
    }
    private ChatClient(String username) {
        this.server = "local host";
        this.port = 1500;
        this.username = username;
    }
    /**
     * Attempts to establish a connection with the server
     *
     * @return boolean - false if any errors occur in startup, true if successful
     */
    private boolean start(ChatClient ccc) {
        // Create a socket
        try {
            socket = new Socket(server, port);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Attempt to create output stream
        try {
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Attempt to create input stream
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Create client thread to listen from the server for incoming messages
        Runnable r = new ListenFromServer();
        Thread t = new Thread(r);
        Runnable k = new KeyInput(ccc);
        Thread keyboard = new Thread(k);
        t.start();
        keyboard.start();

        // After starting, send the clients username to the server.
        try {
            sOutput.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /*
     * Sends a string to the server
     * @param msg - the message to be sent
     */
        private void sendMessage(ChatMessage msg) {
            if(!loggedOut) {
                try {
                    sOutput.writeObject(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }


    /*
     * To start the Client use one of the following command
     * > java ChatClient
     * > java ChatClient username
     * > java ChatClient username portNumber
     * > java ChatClient username portNumber serverAddress
     *
     * If the portNumber is not specified 1500 should be used
     * If the serverAddress is not specified "localHost" should be used
     * If the username is not specified "Anonymous" should be used
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Get proper arguments and override defaults
        // Create your client and start it
        System.out.printf("Enter your username to sign into the Chat client: ");
        String user = in.nextLine();
        ChatClient client = new ChatClient("localhost", 1500, user);
        client.start(client);

        // Send an empty message to the server
        //client.sendMessage(new ChatMessage());

        System.out.println("Which would you like to do: \n/ttt play tictactoe" +
                "\n/msg Broadcast a message\n/logout to logout\n/DM to direct message");

    }
    private int userDecision(String userInput){
        userInput = userInput.toLowerCase();
        if(userInput.length() < 3){
            return -1;
        }
        else if(userInput.substring(0,3).toLowerCase().equals("/dm")){
            return 2;
        }else if(userInput.substring(0,4).toLowerCase().equals("/ttt")){
            return 4;
        }else if(userInput.substring(0,4).toLowerCase().equals("/msg")){
            return 0;
        } else if(userInput.substring(0,5).toLowerCase().equals("/list")){
            return 3;
        }else{
            return 1;
        }
    }

    private String messageRebuilder(String [] arr, int decision){
        String message = "";
        int c = 1;
        if(decision == 2){
            c = 2;
        }
        for (int i = c; i < arr.length ; i++) {
            message  = message + arr[i] + " ";
        }
        return message;
    }
    /*
     * This is a private class inside of the ChatClient
     * It will be responsible for listening for messages from the ChatServer.
     * ie: When other clients send messages, the server will relay it to the client.
     */
    private final class ListenFromServer implements Runnable {
        public void run() {
            while (true) {
                try {
                    String msg = (String) sInput.readObject();
                    System.out.print(msg);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.print("Logging out");
                    loggedOut = true;
                    break;
                }
            }
        }
    }

    private final class KeyInput implements Runnable {
        Scanner in = new Scanner(System.in);
        private ChatClient client;

        public KeyInput(ChatClient client) {
            this.client = client;
        }

        public void run() {
            //System.out.println("Waiting for response");
            while (true) {
                if (in.hasNext()) {
                    String[] userInputs = in.nextLine().split(" ");
                    int decision = client.userDecision(userInputs[0]);
                    if(userInputs.length >=2) {
                        String userName = userInputs[1];
                        /**
                         * 0. General Message (Believe that means broadcast)
                         1. Logout
                         2. DM
                         3. List
                         4. TicTacToe
                         */
                        if (decision == ChatMessage.MESSAGE) {
                            String str = client.messageRebuilder(userInputs, 0);
                            client.sendMessage(new ChatMessage(decision, str, " "));
                            continue;
                        }
                        if (decision != ChatMessage.LOGOUT && decision != ChatMessage.LIST) {
                            if (userInputs.length > 2) {
                                String message = client.messageRebuilder(userInputs, 1);
                                client.sendMessage(new ChatMessage(decision, message, userName));
                                continue;
                            }
                        }
                    }
                        //Handle Logout
                        else if (decision == ChatMessage.LOGOUT) {
                            client.sendMessage(new ChatMessage(1, " ", client.username));
                            try{
                                client.sInput.close();
                                client.sOutput.close();
                                client.socket.close();
                                System.out.println("See you soon <3");
                                break;
                            }catch(IOException e){
                                e.printStackTrace();
                            }


                        }//Handle List
                        else if (decision == ChatMessage.LIST) {
                        try {
                            client.sendMessage(new ChatMessage(3, " ", client.username));
                            continue;
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        }
                    }
                }
        }
    }
}
