package SourceCode;
public class TicTacToeGame{

    private static final char PLAYERX = 'X';     // Helper constant for X player
    private static final char PLAYERO = 'O';     // Helper constant for O player
    private static final char SPACE = ' ';       // Helper constant for spaces
    public String player1;
    public String player2;

    /*
    Sample TicTacToe Board
      0 | 1 | 2
     -----------
      3 | 4 | 5
     -----------
      6 | 7 | 8
     */

    // TODO 4: Implement necessary methods to manage the games of Tic Tac Toe
    public char[][] box=new char[3][3];
    public  int playerturn=0;
    public boolean tie=true;

    public TicTacToeGame(){
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                this.box[i][j]=' ';
            }
        }
    }

    public TicTacToeGame(String player1, String player2){
        this.player1 = player1;
        this.player2 = player2;
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                this.box[i][j]=' ';
            }
        }

    }
    public synchronized String printbox(){
        return("Game Board\n "+box[0][0]+" | "+box[0][1]+" | "+box[0][2]+"\n-----------\n "+box[1][0]+" | "+box[1][1]+" | "+box[1][2]+"\n-----------\n "+box[2][0]+" | "+box[2][1]+" | "+box[2][2]);
    }

    public synchronized boolean inTurn(String user){
        if((playerturn == 0 || playerturn % 2 == 0) && user.equals(player1)){
            return true;
        }
        else if((playerturn == 1 || playerturn % 2 != 0) && user.equals(player2)){
            return true;
        }
        else{
            return false;
        }
    }

    public synchronized boolean makeMove(int index){
        if (index == 0) {
            if(box[0][0] == ' '){
                return true;
            }
            else{
                return false;
            }
        } else if (index == 1) {
            if(box[0][1] == ' '){
                return true;
            }
            else{
                return false;
            }
        } else if (index == 2) {
            if(box[0][2] == ' '){
                return true;
            }
            else{
                return false;
            }
        } else if (index == 3) {
            if(box[1][0] == ' '){
                return true;
            }
            else{
                return false;
            }
        } else if (index == 4) {
            if(box[1][1] == ' '){
                return true;
            }
            else{
                return false;
            }
        } else if (index == 5) {
            if(box[1][2] == ' '){
                return true;
            }
            else{
                return false;
            }
        } else if (index == 6) {
            if(box[2][0] == ' '){
                return true;
            }
            else{
                return false;
            }
        } else if (index == 7) {
            if(box[2][1] == ' '){
                return true;
            }
            else{
                return false;
            }
        } else if (index == 8) {
            if(box[2][2] == ' '){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public synchronized int takeTurn(int index){

        if(playerturn==0 || playerturn%2==0) {
            if (index == 0) {
                box[0][0] = 'X';
                playerturn++;
            } else if (index == 1) {
                box[0][1] = 'X';
                playerturn++;
            } else if (index == 2) {
                box[0][2] = 'X';
                playerturn++;
            } else if (index == 3) {
                box[1][0] = 'X';
                playerturn++;
            } else if (index == 4) {
                box[1][1] = 'X';
                playerturn++;
            } else if (index == 5) {
                box[1][2] = 'X';
                playerturn++;
            } else if (index == 6) {
                box[2][0] = 'X';
                playerturn++;
            } else if (index == 7) {
                box[2][1] = 'X';
                playerturn++;
            } else if (index == 8) {
                box[2][2] = 'X';
                playerturn++;
            }
        } else{
            if (index == 0) {
                box[0][0] = 'O';
                playerturn++;
            } else if (index == 1) {
                box[0][1] = 'O';
                playerturn++;
            } else if (index == 2) {
                box[0][2] = 'O';
                playerturn++;
            } else if (index == 3) {
                box[1][0] = 'O';
                playerturn++;
            } else if (index == 4) {
                box[1][1] = 'O';
                playerturn++;
            } else if (index == 5) {
                box[1][2] = 'O';
                playerturn++;
            } else if (index == 6) {
                box[2][0] = 'O';
                playerturn++;
            } else if (index == 7) {
                box[2][1] = 'O';
                playerturn++;
            } else if (index == 8) {
                box[2][2] = 'O';
                playerturn++;
            }

        }

        return 0;
    }

    public synchronized char getWinner(){

        if(box[0][0]=='X' && box[0][1]=='X' && box[0][2]=='X'){
            tie=false;
            return 'X';
        } else if(box[0][0]=='X' && box[1][0]=='X' && box[2][0]=='X'){
            tie=false;
            return 'X';
        } else if(box[0][2]=='X' && box[1][2]=='X' && box[2][2]=='X'){
            tie=false;
            return 'X';
        } else if(box[0][1]=='X' && box[1][1]=='X' && box[2][1]=='X'){
            tie=false;
            return 'X';
        } else if(box[2][0]=='X' && box[2][1]=='X' && box[2][2]=='X'){
            tie=false;
            return 'X';
        } else if(box[1][0]=='X' && box[1][1]=='X' && box[1][2]=='X'){
            tie=false;
            return 'X';
        } else if(box[0][0]=='X' && box[1][1]=='X' && box[2][2]=='X'){
            tie=false;
            return 'X';
        } else if(box[0][2]=='X' && box[1][1]=='X' && box[2][0]=='X'){
            tie=false;
            return 'X';
        } else if(box[0][0]=='O' && box[0][1]=='O' && box[0][2]=='O'){
            tie=false;
            return 'O';
        } else if(box[0][0]=='O' && box[1][0]=='O' && box[2][0]=='O'){
            tie=false;
            return 'O';
        } else if(box[0][2]=='O' && box[1][2]=='O' && box[2][2]=='O'){
            tie=false;
            return 'O';
        } else if(box[0][1]=='O' && box[1][1]=='O' && box[2][1]=='O'){
            tie=false;
            return 'O';
        } else if(box[2][0]=='O' && box[2][1]=='O' && box[2][2]=='O'){
            tie=false;
            return 'O';
        } else if(box[1][0]=='O' && box[1][1]=='O' && box[1][2]=='O'){
            tie=false;
            return 'O';
        } else if(box[0][0]=='O' && box[1][1]=='O' && box[2][2]=='O'){
            tie=false;
            return 'O';
        } else if(box[0][2]=='O' && box[1][1]=='O' && box[2][0]=='O'){
            tie=false;
            return 'O';
        } else {
            return ' ';
        }

    }

    public synchronized boolean gameOver(){
        if(this.getWinner() == 'O' || this.getWinner() == 'X'){
            return true;
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (box[i][j] == ' '){
                    return false;
                }
            }
        }
        return false;
    }


    public synchronized int isTied(){
        if(tie){
            return 0;
        }else{
            char winner=getWinner();
            return 1;
        }
    }

    public synchronized char getSpace(int index){
        return SPACE;
    }

    public synchronized String toString() {
        String win="";
        if(getWinner()==PLAYERX){
            win="Player 1 wins";
        } else if(getWinner()==PLAYERO){
            win="Player 2 wins";
        } else {
            win="Match Tied";
        }
        return win;
    }

    public synchronized String winner() {
        if(getWinner()==PLAYERX){
            return player1;
        } else if(getWinner()==PLAYERO){
            return player2;
        } else {
            return"No one. The game was tied.";
        }
    }

    public synchronized boolean equalTo(String player1, String player2){ //use to check if users are already in game together, regardless of order they are entered in
        if(this.player1.equals(player1)){
            if(this.player2.equals(player2)){
                return true;
            }
        }
        if(this.player2.equals(player1)){
            if(this.player1.equals(player2)){
                return true;
            }
        }
        return false;
    }

}