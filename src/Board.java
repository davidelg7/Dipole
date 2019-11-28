
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board {

    private int [][] board= new int[8][8];
    private int BLACK=-1;
    private int WHITE=1;
    enum dir{N,NE,E,SE,SW,W,NW};
    public Board(){
        board[5][2]=1*BLACK;
        board[6][1]=2*WHITE;
        board[6][3]=2*WHITE;
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
        System.out.println(b);
        List<Move>moves= b.getPossibleMoves(b.BLACK);
        List<Move.Type> quali=new LinkedList<>(Arrays.asList(Move.Type.DEL));
        for (Move m:moves){
            //if (!quali.contains(m.getType()))continue;
            b.makeMove(m);
            System.out.println(m);
            System.out.println(b);
            b.undoMove(m);
            System.out.println(b);

            System.out.println("-------------------------");
        }
       /* moves= b.getPossibleMoves(b.BLACK);
        for (Move m:moves){
            b.makeMove(m);
            System.out.println(m);
            System.out.println(b);
            System.out.println("-------------------------");
            b.undoMove(m);
        }*/
        System.out.println(moves.size());
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
                        for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga - k >= 0; k += 2)
                            if(isPositionOfPlayer(riga-k,colonna,WHITE))
                                moves.add(new Move(riga, colonna, riga - k, colonna, k,WHITE ,Move.Type.MERGE));
                            else
                            if(isPositionOfPlayer(riga-k,colonna,BLACK)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga-k][colonna]))
                                moves.add(new Move(riga, colonna, riga - k, colonna, k,WHITE , Move.Type.CAPTURE));
                            else
                            if(board[riga-k][colonna]==0)

                                moves.add(new Move(riga, colonna, riga - k, colonna, k,WHITE , Move.Type.BASE));

                        //genero le mosse sulla diagonale principale per il bianco
                        for(int k=1;k<=Math.abs(board[riga][colonna])&&riga-k>=0&&colonna-k>=0;k++)
                            if(isPositionOfPlayer(riga-k,colonna-k,WHITE))
                                moves.add(new Move(riga,colonna,riga-k,colonna-k,k,WHITE , Move.Type.MERGE));
                            else
                            if(isPositionOfPlayer(riga-k,colonna-k,BLACK)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga-k][colonna-k]))
                                moves.add(new Move(riga,colonna,riga-k,colonna-k,k,WHITE , Move.Type.CAPTURE));
                            else
                            if(board[riga-k][colonna-k]==0)

                                moves.add(new Move(riga,colonna,riga-k,colonna-k,k,WHITE , Move.Type.BASE));

                        //genero le mosse sulla diagonale secondaria per il bianco
                        for(int k=1;k<=Math.abs(board[riga][colonna])&&riga-k>=0&&colonna+k<8;k++)
                            if(isPositionOfPlayer(riga-k,colonna+k,WHITE))
                                moves.add(new Move(riga,colonna,riga-k,colonna+k,k,WHITE , Move.Type.MERGE));
                            else
                            if(isPositionOfPlayer(riga-k,colonna+k,BLACK)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga-k][colonna+k]))
                                moves.add(new Move(riga,colonna,riga-k,colonna+k,k,WHITE , Move.Type.CAPTURE));
                            else
                            if(board[riga-k][colonna+k]==0)

                                moves.add(new Move(riga,colonna,riga-k,colonna+k,k,WHITE , Move.Type.BASE));

                        //genero le mosse CAPTURE all'indietro per il bianco
                        for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga +k<8; k += 2)
                            //se la cella generata contiene una pedina avversaria di dimensione minore o uguale alla mia, aggiungo la possibile mossa CAPTURE
                            if (isPositionOfPlayer(riga+k,colonna,BLACK)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga+k][colonna]))
                                moves.add(new Move(riga, colonna, riga + k, colonna, k,WHITE , Move.Type.CAPTURE));

                        //genero le mosse CAPTURE all'indietro sulla diagonale secondaria per il bianco
                        for(int k=1;k<=Math.abs(board[riga][colonna])&&riga+k<8&&colonna-k>=0;k++)
                            if (isPositionOfPlayer(riga+k,colonna-k,BLACK)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga+k][colonna-k]))
                                moves.add(new Move(riga,colonna,riga+k,colonna-k,k,WHITE , Move.Type.CAPTURE));

                        //genero le mosse CAPTURE all'indietro sulla diagonale principale per il bianco
                        for(int k=1;k<=Math.abs(board[riga][colonna])&&riga+k<8&&colonna+k<8;k++)
                            if (isPositionOfPlayer(riga+k,colonna+k,BLACK)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga+k][colonna+k]))
                                moves.add(new Move(riga,colonna,riga+k,colonna+k,k,WHITE , Move.Type.CAPTURE));

                        int minOut=Integer.MAX_VALUE;
                        int k=0;
                        //controllo in verticale quanto possa uscire
                        for (; k <= Math.abs(board[riga][colonna]) && riga - k >= 0; k += 1)
                            continue;
                        if(k>0)k--;
                        minOut=Math.min(k,minOut);

                        //controllo sulla diagonale secondaria quanto possa uscire

                        for (k=0; k <= Math.abs(board[riga][colonna]) && riga - k >=0&&colonna+k<8; k += 1)
                            continue;
                        if(k>0)k--;
                        minOut=Math.min(k,minOut);

                        //controllo sulla diagonale principale quanto possa uscire
                        for (k=0; k <= Math.abs(board[riga][colonna]) && riga - k >=0&&colonna-k>=0; k += 1)
                            continue;
                        if(k>0)k--;
                        minOut=Math.min(k,minOut);

                        //outV è il minimo numero di celle del percorso più vicino al bordo
                        //se non sono riuscito a muovere le pedine senza uscire dalla scacchiera significa che
                        //potenzialmente ho mosse che mi consentono di eliminarne
                        if(minOut!=board[i][j])
                       // System.out.println("PUOI ELIMINARE da"+(minOut+1)+"a "+board[i][j]);
                            for(int out=minOut+1;out<=board[i][j];out++)
                                moves.add(new Move(riga,colonna,riga,colonna,out,WHITE , Move.Type.DEL));


                    }
                    else if(player==BLACK&&isPositionOfPlayer(i,j,player)){
                        int riga = i, colonna = j;
                        //genero le mosse in verticale
                        for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga + k <8; k += 2)
                            if(isPositionOfPlayer(riga+k,colonna,BLACK))
                                moves.add(new Move(riga, colonna, riga + k, colonna, k,BLACK , Move.Type.MERGE));
                            else
                            if(isPositionOfPlayer(riga+k,colonna,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga+k][colonna]))
                                moves.add(new Move(riga, j, riga + k, colonna, k,BLACK , Move.Type.CAPTURE));
                            else
                            if(board[riga+k][colonna]==0)
                                moves.add(new Move(riga, j, riga + k, colonna, k,BLACK , Move.Type.BASE));


                        //genero le mosse sulla diagonale principale per il nero
                        for(int k=1;k<=Math.abs(board[i][j])&&riga+k<8&&colonna+k<8;k++)
                            if(isPositionOfPlayer(riga+k,colonna+k,BLACK))
                                moves.add(new Move(i,j,riga+k,colonna+k,k,BLACK , Move.Type.MERGE));
                            else
                            if(isPositionOfPlayer(riga+k,colonna+k,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga+k][colonna+k]))
                                moves.add(new Move(i,j,riga+k,colonna+k,k,BLACK , Move.Type.CAPTURE));
                            else
                            if(board[riga+k][colonna+k]==0)
                                moves.add(new Move(i,j,riga+k,colonna+k,k,BLACK , Move.Type.BASE));

                        //genero le mosse sulla diagonale secondaria per il nero
                        for(int k=1;k<=Math.abs(board[i][j])&&riga+k<8&&colonna-k>=0;k++)
                            if(isPositionOfPlayer(riga+k,colonna-k,BLACK))
                                moves.add(new Move(i,j,riga+k,colonna-k,k,BLACK , Move.Type.MERGE));
                            else
                            if(isPositionOfPlayer(riga+k,colonna-k,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga+k][colonna-k]))
                               moves.add(new Move(i,j,riga+k,colonna-k,k,BLACK , Move.Type.CAPTURE));
                            else
                                if(board[riga+k][colonna-k]==0)
                                moves.add(new Move(i,j,riga+k,colonna-k,k,BLACK , Move.Type.BASE));

                        //genero le mosse CAPTURE all'indietro per il nero
                        for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga - k >=0; k += 2)
                            //se la cella generata contiene una pedina avversaria di dimensione minore o uguale alla mia, aggiungo la possibile mossa CAPTURE
                            if (isPositionOfPlayer(riga-k,colonna,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga-k][colonna]))
                                moves.add(new Move(riga, colonna, riga - k, colonna, k,BLACK , Move.Type.CAPTURE));

                        //genero le mosse CAPTURE all'indietro sulla diagonale secondaria per il nero
                        for(int k=1;k<=Math.abs(board[riga][colonna])&&riga-k>=0&&colonna+k<8;k++)
                            if (isPositionOfPlayer(riga-k,colonna+k,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga-k][colonna+k]))
                                moves.add(new Move(riga,colonna,riga-k,colonna+k,k,BLACK , Move.Type.CAPTURE));


                        //genero le mosse CAPTURE all'indietro sulla diagonale principale per il nero
                        for(int k=1;k<=Math.abs(board[riga][colonna])&&riga-k>=0&&colonna-k>=0;k++)
                            if (isPositionOfPlayer(riga-k,colonna-k,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga-k][colonna-k]))
                                moves.add(new Move(riga,colonna,riga-k,colonna-k,k,BLACK , Move.Type.CAPTURE));



                        int minOut=Integer.MAX_VALUE;
                        int k=0;
                        //controllo in verticale quanto possa uscire
                        for (; k <= Math.abs(board[riga][colonna]) && riga + k <8; k += 1)
                            continue;
                        if(k>0)k--;
                        minOut=Math.min(k,minOut);

                        //controllo sulla diagonale secondaria quanto possa uscire

                        for (k=0; k <= Math.abs(board[riga][colonna]) && riga + k <8&&colonna-k>=0; k += 1)
                            continue;
                        if(k>0)k--;
                        minOut=Math.min(k,minOut);

                        //controllo sulla diagonale principale quanto possa uscire
                        for (k=0; k <= Math.abs(board[riga][colonna]) && riga + k <8&&colonna+k<8; k += 1)
                            continue;
                        if(k>0)k--;
                        minOut=Math.min(k,minOut);

                        //outV è il minimo numero di celle del percorso più vicino al bordo
                        //se non sono riuscito a muovere le pedine senza uscire dalla scacchiera significa che
                        //potenzialmente ho mosse che mi consentono di eliminarne
                        if(minOut!=board[i][j])
                            for (int out = minOut + 1; out <= Math.abs(board[i][j]); out++)
                                moves.add(new Move(riga, colonna, riga, colonna, out, BLACK, Move.Type.DEL));






                    }





                }



            }
        if(moves.isEmpty())
            moves.add(new Move(-1,-1,-1,-1,0,player, Move.Type.STALL));
        return moves;

    }


    private int get(int i, int j) {
        return board[i][j];
    }

    public void makeMove(Move m){
        switch (m.getType()){
            case BASE:{
                board[m.getFromI()][m.getFromJ()]-=m.getN()*m.getPlayerMover();
                board[m.getToI()][m.getToJ()]+=m.getN()*m.getPlayerMover();
                break;
            }
            case CAPTURE:{
                board[m.getFromI()][m.getFromJ()]-=m.getN()*m.getPlayerMover();
                m.setOldN(board[m.getToI()][m.getToJ()]*m.getPlayerMover());
                board[m.getToI()][m.getToJ()]=0;
                board[m.getToI()][m.getToJ()]+=m.getN()*m.getPlayerMover();
                break;
            }
            case MERGE:{
                board[m.getFromI()][m.getFromJ()]-=m.getN()*m.getPlayerMover();
                board[m.getToI()][m.getToJ()]+=m.getN()*m.getPlayerMover();
                break;
            }
            case DEL:{
                board[m.getFromI()][m.getFromJ()]-=m.getN()*m.getPlayerMover();
                break;
            }
        }

    }
    public void undoMove(Move m){
        switch (m.getType()){
            case BASE:{
                board[m.getFromI()][m.getFromJ()]+=m.getN()*m.getPlayerMover();
                board[m.getToI()][m.getToJ()]-=m.getN()*m.getPlayerMover();
                break;}
            case CAPTURE:{
                board[m.getFromI()][m.getFromJ()]+=m.getN()*m.getPlayerMover();
                board[m.getToI()][m.getToJ()]=m.getOldN()*m.getPlayerMover();
                break;
            }
            case MERGE:{
                board[m.getFromI()][m.getFromJ()]+=m.getN()*m.getPlayerMover();
                board[m.getToI()][m.getToJ()]-=m.getN()*m.getPlayerMover();
                break;}
            case DEL:{
                board[m.getFromI()][m.getFromJ()]+=m.getN()*m.getPlayerMover();
                break;
            }
        }

    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NERI -\n \t");
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
        sb.append("BIANCHI +");
        return sb.toString();
    }
}
