package SourceCode;
import java.io.Serializable;

final class ChatMessage implements Serializable {
    private static final long serialVersionUID = 6898543889087L;

    // Types of messages
    static final int MESSAGE = 0, LOGOUT = 1, DM = 2, LIST = 3, TICTACTOE = 4;

    // Here is where you should implement the chat message object.
    // Variables, Constructors, Methods, etc.
    private String userNameOfRecipient;
    private String message;
    private int typeOfMessage;

    public ChatMessage(int typeOfMessage, String message, String userNameOfRecipient){
        this.userNameOfRecipient = userNameOfRecipient;
        this.message = message;
        this.typeOfMessage = typeOfMessage;
    }

    public String getMessage() {
        return message;
    }

    public int getTypeOfMessage() {
        return typeOfMessage;
    }

    public String getUserNameOfRecipient() {
        return userNameOfRecipient;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTypeOfMessage(int typeOfMessage) {
        this.typeOfMessage = typeOfMessage;
    }

    public void setUserNameOfRecipient(String userNameOfRecipient) {
        this.userNameOfRecipient = userNameOfRecipient;
    }

}