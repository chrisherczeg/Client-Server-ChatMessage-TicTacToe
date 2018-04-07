package SourceCode;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

final class ChatServer {
    private static int uniqueId = 0;
    // Data structure to hold all of the connected clients
    private final List<ClientThread> clients = new ArrayList<>();
    private final int port;			// port the server is hosted on

    /**
     * ChatServer constructor
     * @param port - the port the server is being hosted on
     */
    private ChatServer(int port) {
        this.port = port;
    }

    private ChatServer() {
        this.port = 1500;
    }
    /*
     * This is what starts the ChatServer.
     * Right now it just creates the socketServer and adds a new ClientThread to a list to be handled
     */
    private void start(ChatServer client) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (true) {
                    System.out.println("<Server waiting for connection on port: " + this.port + ">");
                    Socket socket = serverSocket.accept();
                    Runnable r = new ClientThread(socket, uniqueId++, client);
                    Thread t = new Thread(r);
                    clients.add((ClientThread) r);
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    String dateMsg = dateFormat.format(date);
                    System.out.println(dateMsg + " " + ((ClientThread) r).username + " just connected");
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private boolean checkExistingUser(String username){
        int count = 0;
        for (int i = 0; i < clients.size() ; i++) {
            if(clients.get(i).username.equals(username)){
                count++;
            }
        }
        if(count > 1){
            return true;
        }
        return false;
    }

    /**
     *	Sample code to use as a reference for Tic Tac Toe
     *
     * directMessage - sends a message to a specific username, if connected
     * @param  - the string to be sent
     * @param  - the user the message will be sent to
     */
    /*private synchronized void directMessage(String message, String username) {
        String time = sdf.format(new Date());
        String formattedMessage = time + " " + message + "\n";
        System.out.print(formattedMessage);

        for (ClientThread clientThread : clients) {
            if (clientThread.username.equalsIgnoreCase(username)) {
                clientThread.writeMsg(formattedMessage);
            }
        }
    }*/


    /*
     *  > java ChatServer
     *  > java ChatServer portNumber
     *  If the port number is not specified 1500 is used
     */
    public static void main(String[] args) {
        ChatServer server = new ChatServer(1500);
        server.start(server);
    }

    /*
     * This is a private class inside of the ChatServer
     * A new thread will be created to run this every time a new client connects.
     */
    private final class ClientThread implements Runnable {
        Socket socket;                  // The socket the client is connected to
        ObjectInputStream sInput;       // Input stream to the server from the client
        ObjectOutputStream sOutput;     // Output stream to the client from the server
        String username;                // Username of the connected client
        ChatMessage cm;                 // Helper variable to manage messages
        int id;
        private ChatServer server;

        /*
         * socket - the socket the client is connected to
         * id - id of the connection
         */
        private ClientThread(Socket socket, int id, ChatServer server) {
            this.id = id;
            this.socket = socket;
            this.server = server;
            try {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                username = (String) sInput.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /*
         * This is what the client thread actually runs.
         */
        @Override
        public void run() {
            boolean notAllowedToConnect = false;
            if(server.checkExistingUser(username)){
                clients.get(id).writeMessage("Sorry a username: " + clients.get(id) + " already exists" , false);
                notAllowedToConnect = true;
            }
            // Read the username sent to you by client
            while (!notAllowedToConnect){
                try {
                    cm = (ChatMessage) sInput.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    //e.printStackTrace();
                    System.out.println("A Client forced closed without logging out, logging out for them");
                    break;
                }
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String dateMsg = dateFormat.format(now);
                int indexOf = cm.getMessage().indexOf(" ");
                //HACKY AF
                System.out.println(dateMsg + " " +  username + "" + cm.getMessage().substring(indexOf));

            // Send message back to the client
            String toUser = cm.getUserNameOfRecipient();
            int action = cm.getTypeOfMessage();
            String messageToBeSent = cm.getMessage();
            /**
             * Send Functionality for DM
             */
            if (action == cm.DM) {
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).username.equals(toUser)) {
                        //System.out.println("<ATTEMPTING SEND TO " + toUser + ">");
                        clients.get(i).writeMessage(messageToBeSent, false);
                    }//close if
                }//close for
            } else if (action == cm.MESSAGE) {
                messageToBeSent = username + ": " + messageToBeSent;
                server.broadcast(messageToBeSent);

            } else if (action == cm.LOGOUT) {
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).username.equals(toUser)) {
                        try {
                            clients.get(i).sInput.close();
                            clients.get(i).sOutput.close();
                            clients.get(i).socket.close();
                        } catch (IOException e) {
                            System.out.println("Logging out");
                        }
                        clients.remove(i);
                    }
                }
                System.out.print("Logged out");
                break;
            } else if (action == cm.LIST) {
                messageToBeSent = "User List: \n";
                int userSendIndex = 0;
                System.out.println("Listing: " + clients.size());
                for (int i = 0; i < clients.size(); i++) {
                    if (!(clients.get(i).username.equals(toUser))) {
                        messageToBeSent = messageToBeSent + clients.get(i).username + "\n";
                    } else {
                        userSendIndex = i;
                    }
                }
                System.out.println("Printing list for: " + username);
                clients.get(userSendIndex).writeMessage(messageToBeSent, false);

            } else if (action == cm.TICTACTOE) {
                //TODO tic tac toe
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).username.equals(toUser)) {
                        //TicTacToeGame game = new TicTacToeGame(toUser, false);//maybe we could make an array list of all the current tictactoe games
                        //System.out.print(game.toString());
                        System.out.println("<ATTEMPTING SEND TO " + toUser + ">");
                        System.out.println("Success: " + clients.get(i).writeMessage(messageToBeSent,false));
                        //todo: send a started game message to the other client
                        //this.writeMessage("Started game with " + clients.get(i).username, false);
                        //so now its told both clients that they have started the game
                        //I need to get the next string that this client inputs
                        /*while(true) { //need a different way to say while game is still going, do this(maybe put a method in tictactoe
                            ChatMessage nextMove = null;//server should send a message to the client to tell it that a game has started
                            ChatMessage nextMove2 = null;
                            ObjectInputStream oppInput;
                                try {
                                    oppInput = new ObjectInputStream(clients.get(i).socket.getInputStream());
                                }
                                catch(IOException e){
                                    return;
                                }

                            try {
                                nextMove = (ChatMessage) sInput.readObject();//right now it only takes input from user who started the game
                                //so to implement all the game stuff here, would need to get the socket for the other client, and make a new input
                                //stream
                            } catch (IOException | ClassNotFoundException e) { //so you could have it so that if another user tries to start a game with someone they are already in a game with, then it just takes it as if they are making a turn?
                                e.printStackTrace();
                            }
                                try{
                                    nextMove2 = (ChatMessage) oppInput.readObject();
                                    game.takeTurn(Integer.parseInt(nextMove2.getMessage().substring(3, 4)));
                                }catch(IOException | ClassNotFoundException e){
                                    e.printStackTrace();
                                }
                            game.takeTurn(Integer.parseInt(nextMove.getMessage().substring(3, 4)));//that substring only works for two letter client names
                            this.writeMessage(game.printbox(), false);
                            clients.get(i).writeMessage(game.printbox(), false);
                        }*/

                    }//close if
                }//close for

            }
        }
            if(notAllowedToConnect){
                try {
                    clients.get(id).sInput.close();
                    clients.get(id).sOutput.close();
                    clients.get(id).socket.close();
                } catch (IOException e) {
                    System.out.println("Closing connection");
                }
                clients.remove(id);
            }
        }//close run
        private boolean writeMessage(String msg, boolean broadcast){
            if(!(socket.isConnected())) {
                return false;
            }
            if(broadcast) {
                try {
                    sOutput.writeObject("> " + msg + "\n");
                    sOutput.flush();
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }else{
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String nonBroadcast = dateFormat.format(now) + " " + msg;
                try {
                    sOutput.writeObject("> " + nonBroadcast + "\n");
                    sOutput.flush();
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        }
    }

    private synchronized void broadcast(String message){
        //TODO
        //1. Iterate client list
        //2. write message using writeMessage method
        //3. Add date when broadcasting message SimpleDateFormat "HH:mm:ss"
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        message = dateFormat.format(now) + " "  + message;

        for (int i = 0; i < clients.size() ; i++) {
            clients.get(i).writeMessage(message, true);
        }
        System.out.println(message);
    }



    private synchronized void remove(int id){
        clients.remove(id);
    }

    private static void close(ChatClient client){

    }
}