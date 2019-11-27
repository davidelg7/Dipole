
import java.util.LinkedList;
import java.util.List;

public class Board {

    private int [][] board= new int[8][8];
    private int BLACK=-1;
    private int WHITE=1;
    enum dir{N,NE,E,SE,SW,W,NW};
    public Board(){
        board[7][4]=12;
        board[0][3]=-12;
    }
    private boolean isPositionOfPlayer(int i, int j, int player){
        //se player =-1 e board[i][j]=4 -> board[i][j]*player=-4 quindi la cella non appartiene al giocatore
        if(board[i][j]*player<=0)return false;
        //se player =1 e board[i][j]=4 -> board[i][j]*player=4 quindi la cella appartiene al giocatore
        return board[i][j]*player>0;
    }
    public static void main(String...args){
        System.out.println("TEST GENERA MOSSE");
        Board b= new Board();

        List<Move>moves= b.getPossibleMoves(b.WHITE);
        for (Move m:moves){
            b.makeMove(m);
            System.out.println(m);
            System.out.println(b);
            System.out.println("-------------------------");
            b.undoMove(m);
        }
        moves= b.getPossibleMoves(b.BLACK);
        for (Move m:moves){
            b.makeMove(m);
            System.out.println(m);
            System.out.println(b);
            System.out.println("-------------------------");
            b.undoMove(m);
        }
        System.out.println();
    }
    public List<Move> getPossibleMoves(int player){
        List<Move>moves= new LinkedList<>();

        //per ogni riga
        for (int i=0;i<8;i++){
            //per ogni colonna
            for (int j=0;j<8;j++){
                //se la cella appartiene al giocatore considerato
                    //considero il giocatore bianco
                    if(player==WHITE&&isPositionOfPlayer(i,j,player)) {
                        int riga = i, colonna = j;
                        //genero le mosse in verticale

                        //k rappresenta il numero di celle di cui mi sposto
                        //fin tanto che mi sposto al massimo del numero di celle che ho, e non vado fuori dalla Board
                        for (int k = 2; k <= Math.abs(board[i][j]) && riga - k >= 0; k += 2)
                            moves.add(new Move(i, j, riga - k, colonna, k, Move.Type.BASE));

                        //genero le mosse sulla diagonale principale per il bianco
                        for(int k=1;k<=Math.abs(board[i][j])&&riga-k>=0&&colonna-k>=0;k++)
                            moves.add(new Move(i,j,riga-k,colonna-k,k, Move.Type.BASE));

                        //genero le mosse sulla diagonale principale per il bianco
                        for(int k=1;k<=Math.abs(board[i][j])&&riga-k>=0&&colonna+k<8;k++)
                            moves.add(new Move(i,j,riga-k,colonna+k,k, Move.Type.BASE));




                    }
                    else if(player==BLACK&&isPositionOfPlayer(i,j,player)){
                        int riga = i, colonna = j;
                        //genero le mosse in verticale

                        //k rappresenta il numero di celle di cui mi sposto
                        //fin tanto che mi sposto al massimo del numero di celle che ho, e non vado fuori dalla Board
                        for (int k = 2; k <= Math.abs(board[i][j]) && riga + k <8; k += 2)
                            moves.add(new Move(i, j, riga + k, colonna, -k, Move.Type.BASE));


                        //genero le mosse sulla diagonale principale per il nero
                        for(int k=1;k<=Math.abs(board[i][j])&&riga+k<8&&colonna+k<8;k++)
                            moves.add(new Move(i,j,riga+k,colonna+k,-k, Move.Type.BASE));

                        //genero le mosse sulla diagonale secondaria per il nero
                        for(int k=1;k<=Math.abs(board[i][j])&&riga+k<8&&colonna-k>=0;k++)
                            moves.add(new Move(i,j,riga+k,colonna-k,-k, Move.Type.BASE));


                    }





                }


            }






        return moves;

    }
    public void makeMove(Move m){

        board[m.getFromI()][m.getFromJ()]-=m.getN();
        board[m.getToI()][m.getToJ()]+=m.getN();
    }
    public void undoMove(Move m){

        board[m.getFromI()][m.getFromJ()]+=m.getN();
        board[m.getToI()][m.getToJ()]-=m.getN();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" \t");
        for (int i=0;i<8;i++)
            sb.append(i+"\t");
        sb.append("\n");
        for (int i=0;i<8;i++) {
            sb.append(i+":\t");
            for (int j = 0; j < 7; j++) {
                sb.append(board[i][j]+"\t|");
            }
            sb.append(board[i][7]+"\n");
            sb.append("\t");

            for (int j = 0; j < 8; j++) {
                sb.append("----");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
