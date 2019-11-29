

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class Board {

    private int [][] board= new int[8][8];
    public static final int BLACK=-1;
    public static final int WHITE=1;
    private int N_THREADS=1;

    public int otherPlayer(int opt) {
        return opt==WHITE?BLACK:opt==BLACK?WHITE:0;
    }

    public Board copy() {
        Board b= new Board();
        for (int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                b.board[i][j]=board[i][j];
            return b;
    }

    public int checkWinner() {
        int b=0;
        int w=0;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(board[i][j]*WHITE>0)w++;
                if(board[i][j]*BLACK>0)b++;
            }
        }
        if(b>0&&w>0)return 0;
        if (b>0)return BLACK;
        return WHITE;
    }

    enum dir{N,NE,E,SE,SW,W,NW};
    public Board(){
        board[0][3]=12*BLACK;
        board[7][4]=12*WHITE;
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
        List<Future<List<Move>>> futures=new LinkedList<>();
        ExecutorService es = Executors.newFixedThreadPool(N_THREADS);
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                Future<List<Move>> futureTask=es.submit(getMoves(board,i,j,player));
                futures.add(futureTask);
                }
            }
        for (Future<List<Move>> f:futures){
            try {
                moves.addAll(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

            es.shutdown();

        if(moves.isEmpty())
            moves.add(new Move(-1,-1,-1,-1,0,player, Move.Type.STALL));
        Collections.sort(moves);
        return moves;

    }
    private Callable<List<Move>> getMoves(int[][] board,int i,int j,int player){
        return new Callable<List<Move>>() {
            @Override
            public List<Move> call() throws Exception {

                List<Move> moves= new LinkedList<>();
                //se la cella appartiene al giocatore considerato
                //considero il giocatore bianco
                if(player==WHITE&&isPositionOfPlayer(i,j,WHITE)) {
                    int riga = i, colonna = j;
                    //genero le mosse in verticale

                    //k rappresenta il numero di celle di cui mi sposto
                    //fin tanto che mi sposto al massimo del numero di celle che ho, e non vado fuori dalla Board
                    for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga - k >= 0; k += 2)
                        if(k>=Math.abs(board[riga-k][colonna]))
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
                        if(k>=Math.abs(board[riga-k][colonna-k]))
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
                        if(k>=Math.abs(board[riga-k][colonna+k]))
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
                        if(k>=Math.abs(board[riga+k][colonna]))
                            //se la cella generata contiene una pedina avversaria di dimensione minore o uguale alla mia, aggiungo la possibile mossa CAPTURE
                        if (isPositionOfPlayer(riga+k,colonna,BLACK)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga+k][colonna]))
                            moves.add(new Move(riga, colonna, riga + k, colonna, k,WHITE , Move.Type.CAPTURE));

                    //genero le mosse CAPTURE all'indietro sulla diagonale secondaria per il bianco
                    for(int k=1;k<=Math.abs(board[riga][colonna])&&riga+k<8&&colonna-k>=0;k++)
                        if(k>=Math.abs(board[riga+k][colonna-k]))
                            if (isPositionOfPlayer(riga+k,colonna-k,BLACK)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga+k][colonna-k]))
                            moves.add(new Move(riga,colonna,riga+k,colonna-k,k,WHITE , Move.Type.CAPTURE));

                    //genero le mosse CAPTURE all'indietro sulla diagonale principale per il bianco
                    for(int k=1;k<=Math.abs(board[riga][colonna])&&riga+k<8&&colonna+k<8;k++)
                        if(k>=Math.abs(board[riga+k][colonna+k]))
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
                else if(player==BLACK&&isPositionOfPlayer(i,j,BLACK)){
                    int riga = i, colonna = j;
                    //genero le mosse in verticale
                    for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga + k <8; k += 2)
                        if(k>=Math.abs(board[riga+k][colonna]))
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
                        if(k>=Math.abs(board[riga+k][colonna+k])) {
                            if (isPositionOfPlayer(riga + k, colonna + k, BLACK))
                                moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.MERGE));
                            else if (isPositionOfPlayer(riga + k, colonna + k, WHITE) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga + k][colonna + k]))
                                moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.CAPTURE));
                            else if (board[riga + k][colonna + k] == 0)
                                moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.BASE));
                        }
                    //genero le mosse sulla diagonale secondaria per il nero
                    for(int k=1;k<=Math.abs(board[i][j])&&riga+k<8&&colonna-k>=0;k++)
                        if(k>=Math.abs(board[riga+k][colonna-k])) {

                            if (isPositionOfPlayer(riga + k, colonna - k, BLACK))
                                moves.add(new Move(i, j, riga + k, colonna - k, k, BLACK, Move.Type.MERGE));
                            else if (isPositionOfPlayer(riga + k, colonna - k, WHITE) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga + k][colonna - k]))
                                moves.add(new Move(i, j, riga + k, colonna - k, k, BLACK, Move.Type.CAPTURE));
                            else if (board[riga + k][colonna - k] == 0)
                                moves.add(new Move(i, j, riga + k, colonna - k, k, BLACK, Move.Type.BASE));
                        }
                    //genero le mosse CAPTURE all'indietro per il nero
                    for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga - k >=0; k += 2)
                        if(k>=Math.abs(board[riga-k][colonna]))
                            //se la cella generata contiene una pedina avversaria di dimensione minore o uguale alla mia, aggiungo la possibile mossa CAPTURE
                        if (isPositionOfPlayer(riga-k,colonna,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga-k][colonna]))
                            moves.add(new Move(riga, colonna, riga - k, colonna, k,BLACK , Move.Type.CAPTURE));

                    //genero le mosse CAPTURE all'indietro sulla diagonale secondaria per il nero
                    for(int k=1;k<=Math.abs(board[riga][colonna])&&riga-k>=0&&colonna+k<8;k++)
                        if(k>=Math.abs(board[riga-k][colonna+k]))
                            if (isPositionOfPlayer(riga-k,colonna+k,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga-k][colonna+k]))
                            moves.add(new Move(riga,colonna,riga-k,colonna+k,k,BLACK , Move.Type.CAPTURE));


                    //genero le mosse CAPTURE all'indietro sulla diagonale principale per il nero
                    for(int k=1;k<=Math.abs(board[riga][colonna])&&riga-k>=0&&colonna-k>=0;k++)
                        if(k>=Math.abs(board[riga-k][colonna-k]))
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
                return moves;
            }
        };
    }

    public int get(int i, int j) {
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
