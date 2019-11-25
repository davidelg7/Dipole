
import java.util.LinkedList;
import java.util.List;

public class Board {

    private int [][] board= new int[8][8];
    private int BLACK=-1;
    private int WHITE=1;
    enum dir{N,NE,E,SE,SW,W,NW};
    private boolean isPositionOfPlayer(int i, int j, int player){
        //se player =-1 e board[i][j]=4 -> board[i][j]*player=-4 quindi la cella non appartiene al giocatore
        if(board[i][j]*player<=0)return false;
        //se player =1 e board[i][j]=4 -> board[i][j]*player=4 quindi la cella appartiene al giocatore
        return board[i][j]*player>0;
    }
    public static void main(String...args){
        System.out.println(new Board());
    }
    public List<Move> getPossibleMoves(int player){
        List<Move>moves= new LinkedList<>();

        //per ogni riga
        for (int i=0;i<8;i++){
            //per ogni colonna
            for (int j=0;j<8;j++){
                //se la cella appartiene al giocatore considerato
                if(isPositionOfPlayer(i,j,player)){
                    //considero il giocatore bianco
                    if(player==WHITE){}
                        int riga=i,colonna=j;
                    //genero le mosse in verticale

                    //k rappresenta il numero di celle di cui mi sposto
                    //fin tanto che mi sposto al massimo del numero di celle che ho, e non vado fuori dalla Board
                        for(int k=0;k<=Math.abs(board[i][j])&&riga>=0;k+=2,riga-=2) {
                            moves.add(new Move(i,j,riga,colonna,k,0));
                        }






                }


            }

        }





        return null;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<8;i++) {
            sb.append(i+"\t");
            for (int j = 0; j < 7; j++) {
                sb.append(board[i][j]+"\t|");
            }
            sb.append(board[i][7]);
            for (int j = 0; j < 7; j++) {
                sb.append(board[i][j]+"__  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
