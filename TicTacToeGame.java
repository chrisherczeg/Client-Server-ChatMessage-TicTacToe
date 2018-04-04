package SourceCode;

public class TicTacToeGame {

    private static final char PLAYERX = 'X';     // Helper constant for X player
    private static final char PLAYERO = 'O';     // Helper constant for O player
    private static final char SPACE = ' ';       // Helper constant for spaces

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

    public TicTacToeGame(String otherPlayer, boolean isX){
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                this.box[i][j]=' ';
            }
        }

    }

    public void printbox(){

        System.out.println(" "+box[0][0]+" | "+box[0][1]+" | "+box[0][2]+"\n-----------\n "+box[1][0]+" | "+box[1][1]+" | "+box[1][2]+"\n-----------\n "+box[2][0]+" | "+box[2][1]+" | "+box[2][2]);
    }

    public int takeTurn(int index){

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

    public char getWinner(){

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

    public int isTied(){
       if(tie){
           return 0;
       }else{
           char winner=getWinner();
           return 1;
       }
    }

    public char getSpace(int index){
    return SPACE;
    }

    public String toString() {
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
}