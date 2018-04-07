package SourceCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
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
    private ArrayList<String> clientsInTTTGame = new ArrayList<String>(); //array list for clients who are in the game
    private ArrayList<TTT> games = new ArrayList<TTT>(); //array list for threads that have the game

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
            //e.printStackTrace();
            return false;
        }

        // Attempt to create output stream
        try {
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }

        // Attempt to create input stream
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }

        // Create client thread to listen from the server for incoming messages
        System.out.println("Connection accepted " + ccc.server +"/" + ccc.port);
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
        if(!(client.start(client))){
            System.out.println("Could not start the client, check that a Server is started.");
        }else if (!client.socket.isConnected()) {
            System.out.println("Sorry Connection closed");
        }
            //System.out.println("Which would you like to do: \n/ttt play tictactoe" +
                   // "\n/msg Broadcast a message\n/logout to logout\n/DM to direct message");


        // Send an empty message to the server
        //client.sendMessage(new ChatMessage());



    }
    private int userDecision(String userInput){
        userInput = userInput.toLowerCase();
        if(userInput.equals("/msg")){
            return ChatMessage.DM;
        }else if(userInput.equals("/ttt")){
            return ChatMessage.TICTACTOE;
        } else if(userInput.equals("/list")){
            return ChatMessage.LIST;
        }else if(userInput.equals("/logout")){
            return ChatMessage.LOGOUT;
        }else{
            return ChatMessage.MESSAGE;
        }
    }

    private String messageRebuilder(String [] arr, int decision){
        String message = "";
        int c = 1;
        if(decision == 0){
            c = 0;
        }
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
    private final class ListenFromServer implements Runnable { //todo: listen for a chat message here?
        public void run() {
            while (true) {
                try {
                    Object input = sInput.readObject();
                    if (input instanceof ChatMessage) {
                        if(((ChatMessage) input).getTypeOfMessage() == ChatMessage.START_GAME){
                            clientsInTTTGame.add(((ChatMessage) input).getMessage());
                            System.out.println(((ChatMessage) input).getMessage());
                            games.add(new TTT(((ChatMessage) input).getMessage(), false));
                            continue;
                        }
                        if(((ChatMessage) input).getTypeOfMessage() == ChatMessage.TICTACTOE){
                            //set the correct TTT box to be equal to the box just sent to it
                            continue;
                        }
                    }
                    String msg = (String) input;
                    System.out.print(msg);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Server has closed the connection");
                    loggedOut = true;
                    break;
                }
            }
            System.exit(0); //If we want reprompt instead of close take this out
        }
    }

    private final class TTT{//todo: this
        String opponent;
        boolean didStart;
        int move;
        int moves;
        TicTacToeGame game;
        private TTT(String opponent, boolean didStart, int move){
            this.opponent = opponent;
            this.didStart = didStart;
            this.move = move;
        }

        private TTT(String opponent, boolean didStart){
            this.opponent = opponent;
            this.didStart = didStart;
            if(didStart){
                game = new TicTacToeGame(username, true);
            }
            else{
                game = new TicTacToeGame(username, false);
            }
        }

        public String getBox(){
            return game.printbox();
        }

        public void playGame(int move){
           game.takeTurn(move);
           System.out.println(game.printbox());
        }
    }

    private final class KeyInput implements Runnable {
        Scanner in = new Scanner(System.in);
        private ChatClient client;

        public KeyInput(ChatClient client) {
            this.client = client;
        }

        public void run() {
            if(!(client.socket.isConnected())){
                System.out.println("Server has closed the connection");
            }
            //System.out.println("Waiting for response");
            while (true) {
                if (in.hasNext()) {
                    String[] userInputs = in.nextLine().split(" ");
                    int decision = client.userDecision(userInputs[0]);
                    if (decision == ChatMessage.MESSAGE) {
                        String str = client.messageRebuilder(userInputs, 0);
                        client.sendMessage(new ChatMessage(decision, str, " "));
                        continue;
                    }
                    if(userInputs.length >=2) {
                        String userName = userInputs[1];
                        /**
                         * 0. General Message (Believe that means broadcast)
                         1. Logout
                         2. DM
                         3. List
                         4. TicTacToe
                         */
                        //if (decision != ChatMessage.LOGOUT && decision != ChatMessage.LIST) {
                        if(decision == ChatMessage.DM) {
                            if (userInputs.length > 2) {
                                String message = client.messageRebuilder(userInputs, 2);
                                client.sendMessage(new ChatMessage(decision, client.username  + " -> " + userName + ": " + message, userName));
                                Date now = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                String datemsg = dateFormat.format(now);
                                System.out.println(datemsg + " " +client.username  + " -> " + userName + ": " + message);
                                continue;
                            }
                        }else if(decision == ChatMessage.TICTACTOE){
                            if(userInputs.length > 2){
                                //todo: start TTT thread and send message to make other client start thread | go to thread where the game is already happening
                                boolean clientInGame = false;
                                for(int i = 0; i < clientsInTTTGame.size(); i++){ //iterate through array
                                    if(userName.equals(clientsInTTTGame.get(i))) { //test if the client is already in a game with this client
                                        //todo: make it go to the thread that is already carrying this game
                                        clientInGame = true;
                                        String message  = client.messageRebuilder(userInputs, 2);
                                        //int index = message.indexOf(' ');
                                        if(message.length() == 0){
                                            System.out.println("print box"); //todo: make it print the box for the current game
                                        }
                                        else {
                                            try {
                                                int move = Integer.parseInt(message.substring(0, 1));
                                                games.get(i).playGame(move);
                                                client.sendMessage(new ChatMessage(decision, games.get(i).getBox(), userName));
                                            }
                                            catch(IllegalArgumentException e){
                                                System.out.println(message);
                                                System.out.println("Please enter a number between 0 and 9 to make a move");
                                            }
                                        }
                                    }
                                }
                                if(!clientInGame){//if client is not in the game with this client
                                    TTT game = new TTT(userName, true); //todo: make it start a TTT thread
                                    clientsInTTTGame.add(userName);
                                    games.add(game);
                                   //todo: send start game message to the server
                                    client.sendMessage(new ChatMessage(5, "Started TicTacToe with " + client.username, userName));
                                    System.out.println("Started TicTacToe with " + userName);
                                }
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
