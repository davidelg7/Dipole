


import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Board {

    private int [][] board= new int[8][8];
    private static int meanWidth=0;
    private static int invocation=1;
    public static final int BLACK=-1;
    public static final int WHITE=1;
    private boolean updated=true;
    private int wBefore=12;
    private int bBefore=12;
    private List<Move>lastPossiblesMoves;
    private static final int LARGHEZZA_MAX=15;
    private int N_THREADS=1;
    private Heuristics h;
    public static double getMeanWidth(){
        return meanWidth/invocation;
    }
    public int otherPlayer(int opt) {
        return opt==WHITE?BLACK:opt==BLACK?WHITE:0;
    }
    public Board(int [][] b ,Heuristics h){
        this.board=b;
        this.h=h;

    }


    public Board copy() {
        Board b= new Board(this.h);
        for (int i=0;i<8;i++)
            b.board[i]=Arrays.copyOf(board[i],board[i].length);

            return b;
    }

    public int checkWinner() {
        int b=0;
        int w=0;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(isPositionOfPlayer(i,j,WHITE)){
                    w+=board[i][j];}
                if(isPositionOfPlayer(i,j,BLACK)){
                    b-=board[i][j];}
            }
        }
        if (b>0&&w==0)return BLACK;
        if (w>0&&b==0)return WHITE;
        return 0;
    }

    public Board(Heuristics h){
        this.h=h;
        setBoard(0,3,BLACK,12);
        setBoard(7,4,WHITE,12);

    }
    public boolean isPositionOfPlayer(int i, int j, int player){

        return Math.signum(board[i][j])==Math.signum(player);
    }
    public static void main(String...args){
        System.out.println("TEST GENERA MOSSE");
        Board b= new Board(new H3());

        List<Move>moves= b.getPossibleMoves(b.BLACK);

        System.out.println(moves.size());
        System.out.println(moves);
    }
    public List<Move> getSampleForEach(int player){
        List<Move> moves= getPossibleMoves(player);
        return null;
    }
    public List<Move> getAllOfType(int player,Move.Type type){
        return getPossibleMoves(player).stream().filter(f->f.getType().equals(type)).collect(Collectors.toList());
    }
    public synchronized List<Move> getLimitedPossibleMoves(int player,int dim){
        return getPossibleMoves(player).subList(0, Math.min(dim,lastPossiblesMoves.size()));
    }
    public synchronized List<Move> getPossibleMoves(int player){


        List<Move>moves= new LinkedList<>();
        List<Future<List<Move>>> futures=new LinkedList<>();

        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                moves.addAll(allMoves(board,i,j,player));

//        ExecutorService es = Executors.newFixedThreadPool(N_THREADS);
//        for (int i=0;i<8;i++){
//            for (int j=0;j<8;j++){
//                Future<List<Move>> futureTask=es.submit(getMoves(board,i,j,player));
//                futures.add(futureTask);
//                }
//            }
//        for (Future<List<Move>> f:futures){
//            try {
//                moves.addAll(f.get());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//
//            es.shutdown();

        if(moves.isEmpty()){
            int i=0;
            int j=0;
            for (int k=0;k<8;k++)
                for (int s=0;s<8;s++)
                    if(board[k][s]*player>0){
                        j=s;
                        i=k;
                        break;}
            moves.add(new Move(i,j,i,j,0,player, Move.Type.STALL));
        }
        Collections.sort(moves);
        lastPossiblesMoves=moves;
        updated=false;
        invocation++;
        meanWidth+=moves.size();
//        System.out.println(moves);
        return moves;

    }
    public void setBoard(int i, int j,int player,int b){
        board[i][j]=b*player;
    }
    private List<Move> allMoves(int[][] board,int i , int j, int player){
        List<Move> moves= new LinkedList<>();
        //se la cella appartiene al giocatore considerato
        //considero il giocatore bianco
        if(player==WHITE&&isPositionOfPlayer(i,j,WHITE)) {
            int riga = i, colonna = j;
            //genero le mosse in verticale

            //k rappresenta il numero di celle di cui mi sposto
            //fin tanto che mi sposto al massimo del numero di celle che ho, e non vado fuori dalla Board
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga - k >= 0; k += 2)
            {
                if (isPositionOfPlayer(riga - k, colonna, WHITE))
                    moves.add(new Move(riga, colonna, riga - k, colonna, k, WHITE, Move.Type.MERGE));
                else
                if(k>=Math.abs(board[riga-k][colonna]))
                    if (isPositionOfPlayer(riga - k, colonna, BLACK) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga - k][colonna]))
                        moves.add(new Move(riga, colonna, riga - k, colonna, k, WHITE, Move.Type.CAPTURE));
                    else if (board[riga - k][colonna] == 0)
                        moves.add(new Move(riga, colonna, riga - k, colonna, k, WHITE, Move.Type.BASE));
            }
            //genero le mosse capture a destra per il bianco
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && colonna + k < 8; k += 2)
                if (isPositionOfPlayer(riga, colonna + k, BLACK) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga][colonna + k]) && k >= Math.abs(board[riga][colonna + k]))
                    moves.add(new Move(riga, colonna, riga, colonna + k, k, WHITE, Move.Type.CAPTURE));

            //genero le mosse capture a sinistra per il bianco
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && colonna - k >= 0; k += 2)
                if (isPositionOfPlayer(riga, colonna - k, BLACK) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga][colonna - k]) && k >= Math.abs(board[riga][colonna - k]))
                    moves.add(new Move(riga, colonna, riga, colonna - k, k, WHITE, Move.Type.CAPTURE));


            //genero le mosse sulla diagonale principale per il bianco
            for (int k = 1; k <= Math.abs(board[riga][colonna]) && riga - k >= 0 && colonna - k >= 0; k++)
                if (isPositionOfPlayer(riga - k, colonna - k, WHITE))
                    moves.add(new Move(riga, colonna, riga - k, colonna - k, k, WHITE, Move.Type.MERGE));
                else
                if(k>=Math.abs(board[riga-k][colonna-k]))

                    if (isPositionOfPlayer(riga - k, colonna - k, BLACK) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga - k][colonna - k]))
                        moves.add(new Move(riga, colonna, riga - k, colonna - k, k, WHITE, Move.Type.CAPTURE));
                    else if (board[riga - k][colonna - k] == 0)

                        moves.add(new Move(riga, colonna, riga - k, colonna - k, k, WHITE, Move.Type.BASE));

            //genero le mosse sulla diagonale secondaria per il bianco
            for(int k=1;k<=Math.abs(board[riga][colonna])&&riga-k>=0&&colonna+k<8;k++)
            {
                if (isPositionOfPlayer(riga - k, colonna + k, WHITE))
                    moves.add(new Move(riga, colonna, riga - k, colonna + k, k, WHITE, Move.Type.MERGE));

                else
                if(k>=Math.abs(board[riga-k][colonna+k]))
                    if (isPositionOfPlayer(riga - k, colonna + k, BLACK) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga - k][colonna + k]))
                        moves.add(new Move(riga, colonna, riga - k, colonna + k, k, WHITE, Move.Type.CAPTURE));
                    else if (board[riga - k][colonna + k] == 0)

                        moves.add(new Move(riga, colonna, riga - k, colonna + k, k, WHITE, Move.Type.BASE));
            }
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
            boolean v=false,D=false,d=false;
            int minOut=Integer.MAX_VALUE;
            int k=0;
            //controllo in verticale quanto possa uscire
            for (; k <= Math.abs(board[riga][colonna]) && riga - k >= 0; k += 1)
                continue;
            if(k>0)k--;
            if (minOut==Math.min(k,minOut))v=true;
            minOut=Math.min(k,minOut);

            //controllo sulla diagonale secondaria quanto possa uscire

            for (k=0; k <= Math.abs(board[riga][colonna]) && riga - k >=0&&colonna+k<8; k += 1)
                continue;
            if(k>0)k--;
            if (minOut==Math.min(k,minOut))d=true;
            minOut=Math.min(k,minOut);

            //controllo sulla diagonale principale quanto possa uscire
            for (k=0; k <= Math.abs(board[riga][colonna]) && riga - k >=0&&colonna-k>=0; k += 1)
                continue;
            if(k>0)k--;
            if (minOut==Math.min(k,minOut))D=true;
            minOut=Math.min(k,minOut);

            //outV è il minimo numero di celle del percorso più vicino al bordo
            //se non sono riuscito a muovere le pedine senza uscire dalla scacchiera significa che
            //potenzialmente ho mosse che mi consentono di eliminarne
            if(minOut!=board[i][j]){
                // System.out.println("PUOI ELIMINARE da"+(minOut+1)+"a "+board[i][j]);
                for(int out=minOut+1;out<=board[i][j];out++)
                    if (v&&riga-out<0)
                        moves.add(new Move(riga,colonna,riga-out,colonna,out,WHITE , Move.Type.DEL));
                    else {
                        if (d&&(riga-out<0||colonna+out>7))
                            moves.add(new Move(riga, colonna, riga - out, colonna + out, out, WHITE, Move.Type.DEL));
                        else if (D&&(riga-out<0||colonna-out<0))
                            moves.add(new Move(riga, colonna, riga - out, colonna - out, out, WHITE, Move.Type.DEL));
                    }}

        }
        else if(player==BLACK&&isPositionOfPlayer(i,j,BLACK)){
            int riga = i, colonna = j;
            //genero le mosse in verticale
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga + k <8; k += 2)
            {
                if (isPositionOfPlayer(riga + k, colonna, BLACK))
                    moves.add(new Move(riga, colonna, riga + k, colonna, k, BLACK, Move.Type.MERGE));
                else
                if(k>=Math.abs(board[riga+k][colonna]))
                    if (isPositionOfPlayer(riga + k, colonna, WHITE) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga + k][colonna]))
                        moves.add(new Move(riga, j, riga + k, colonna, k, BLACK, Move.Type.CAPTURE));
                    else if (board[riga + k][colonna] == 0)
                        moves.add(new Move(riga, j, riga + k, colonna, k, BLACK, Move.Type.BASE));
            }
            //genero le mosse capture a destra per il nero
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && colonna + k <8; k += 2)
                if(isPositionOfPlayer(riga,colonna+k,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga][colonna+k])&&k>=Math.abs(board[riga][colonna+k]))
                    moves.add(new Move(riga, colonna, riga , colonna+k, k,BLACK , Move.Type.CAPTURE));

            //genero le mosse capture a sinistra per il nero
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && colonna - k >= 0; k += 2)
                if(isPositionOfPlayer(riga,colonna-k,WHITE)&&Math.abs(board[riga][colonna])>=Math.abs(board[riga][colonna-k])&&k>=Math.abs(board[riga][colonna-k]))
                    moves.add(new Move(riga, colonna, riga , colonna-k, k,BLACK , Move.Type.CAPTURE));


            //genero le mosse sulla diagonale principale per il nero
            for(int k=1;k<=Math.abs(board[i][j])&&riga+k<8&&colonna+k<8;k++)
            {
                if (isPositionOfPlayer(riga + k, colonna + k, BLACK))
                    moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.MERGE));
                else
                if(k>=Math.abs(board[riga+k][colonna+k]))
                    if (isPositionOfPlayer(riga + k, colonna + k, WHITE) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga + k][colonna + k]))
                        moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.CAPTURE));
                    else if (board[riga + k][colonna + k] == 0)
                        moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.BASE));
            }
            //genero le mosse sulla diagonale secondaria per il nero
            for(int k=1;k<=Math.abs(board[i][j])&&riga+k<8&&colonna-k>=0;k++)
            {

                if (isPositionOfPlayer(riga + k, colonna - k, BLACK))
                    moves.add(new Move(i, j, riga + k, colonna - k, k, BLACK, Move.Type.MERGE));
                else
                if(k>=Math.abs(board[riga+k][colonna-k]))
                    if (isPositionOfPlayer(riga + k, colonna - k, WHITE) && Math.abs(board[riga][colonna]) >= Math.abs(board[riga + k][colonna - k]))
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


            boolean v=false,D=false,d=false;
            int minOut=Integer.MAX_VALUE;
            int k=0;
            //controllo in verticale quanto possa uscire
            for (; k <= Math.abs(board[riga][colonna]) && riga + k <8; k += 2)
                continue;
            if(k>0)k--;
            if (minOut==Math.min(k,minOut))v=true;
            minOut=Math.min(k,minOut);

            //controllo sulla diagonale secondaria quanto possa uscire

            for (k=0; k <= Math.abs(board[riga][colonna]) && riga + k <8&&colonna-k>=0; k += 1)
                continue;
            if(k>0)k--;
            if (minOut==Math.min(k,minOut))d=true;

            minOut=Math.min(k,minOut);

            //controllo sulla diagonale principale quanto possa uscire
            for (k=0; k <= Math.abs(board[riga][colonna]) && riga + k <8&&colonna+k<8; k += 1)
                continue;
            if(k>0)k--;
            if (minOut==Math.min(k,minOut))D=true;
            minOut=Math.min(k,minOut);

            //outV è il minimo numero di celle del percorso più vicino al bordo
            //se non sono riuscito a muovere le pedine senza uscire dalla scacchiera significa che
            //potenzialmente ho mosse che mi consentono di eliminarne
            if(minOut!=board[i][j]) {
                for (int out = minOut + 1; out <= Math.abs(board[i][j]); out++)
                    if (D&&riga+out>=8||colonna+out>=8)
                        moves.add(new Move(riga, colonna, riga + out, colonna + out, out, BLACK, Move.Type.DEL));
                    else{

                        if (d&&riga+out>=8||colonna-out<0 )
                            moves.add(new Move(riga, colonna, riga + out, colonna - out, out, BLACK, Move.Type.DEL));

                        else if (v&&riga+out>=8)
                            moves.add(new Move(riga, colonna, riga + out, colonna, out, BLACK, Move.Type.DEL));
                    }
            }

        }
        return moves;
    }
    private Callable<List<Move>> getMoves(int[][] board,int i,int j,int player){
        return new Callable<List<Move>>() {
            @Override
            public List<Move> call() throws Exception {
                return allMoves(board,i,j,player);

            }
        };
    }

    public int get(int i, int j) {
        return board[i][j];
    }

    public void makeMove(Move m){
        wBefore=getWhiteNow();
        bBefore=getBlackNow();
        updated=true;
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
        updated=true;
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
    public String toString2(){
        StringBuilder sb = new StringBuilder();
sb.append("\n");
        for (int i=0;i<8;i++) {

            for (int j = 0; j < 7; j++) {
                String s=board[i][j]>0?"W":board[i][j]<0?"B":"";

                sb.append(Math.abs(board[i][j])+s+" |");
            }
            String s=board[i][7]>0?"W":board[i][7]<0?"B":"";
            sb.append(Math.abs(board[i][7])+s+"\n");
            sb.append(" ");

            sb.append("\n");
        }
        return sb.toString();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("     ");
        for (int i=0;i<8;i++)
            sb.append(i+"   ");
        sb.append("\n");
        for (int i=0;i<8;i++) {

            sb.append(i+":  ");
            for (int j = 0; j < 7; j++) {

                sb.append(cell(i,j)+"|");
            }
            sb.append(cell(i,7)+"\n");
            sb.append("   ");

            for (int j = 0; j < 8; j++) {
                sb.append("----");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    private String cell(int i, int j){
        int l=3;
        String player=board[i][j]>0?"W":board[i][j]<0?"B":"";
        int n= Math.abs(board[i][j]);
        if(n>9){
            return n+""+player;
        }
        else
            if(n==0)
                return " 0 ";

            return " "+n+player;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Arrays.equals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    public int[][] getBoard() {
        return board;
    }
    public int[][] onlyPlayer(int player){
        int[][]res= new int[8][8];
        for (int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                if(isPositionOfPlayer(i,j,player))
                    res[i][j]=get(i,j)*player;
        return res;
    }

    public double eval(Move move, int player,int depth) {
        return h.eval(this,move,player,depth);
    }
    public void setH(Heuristics h){
        this.h=h;
    }
    public int getWhiteBeforeMove(){return wBefore;}
    public int getBlackBeforeMove(){return bBefore;}
    public int getWhiteNow(){
        int w=0;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(isPositionOfPlayer(i,j,WHITE)){
                    w+=board[i][j];}
            }
        }
    return w;
    }
    public int getBlackNow(){
        int b=0;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(isPositionOfPlayer(i,j,BLACK)){
                    b-=board[i][j];}
            }
        }
        return b;
    }
    public int getPlayerNow(int player){return player==WHITE?getWhiteNow():getBlackNow();}
    public int getPlayerBeforeMove(int player){return player==WHITE?getWhiteBeforeMove():getBlackBeforeMove();}
}
