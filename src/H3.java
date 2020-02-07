import java.util.List;
import java.util.stream.Collectors;

public class H3 extends Heuristics {

    double[] attack = new double[]{0.1, 8.0, 9.5, 13.0 , 12.5, 11.0, 8, 4.0, 4.9, 3.0, 3.1, 3.0, 2.1};
    double[] attacked = new double[]{0.1, 8.5, 10, 13.5 , 13.5, 12.0, 7.8, 4.0, 4.9, 3.0, 3.1, 3.0, 2.1};
    double[] base = new double[]{ 0.0, 1.5, 9.0, 11.0, 11.5, 11.0, 7.0, 3.0, 4.5, 2.1, 2.2, 2.2, 2.0};
    double[] merge = new double[]{ 0.0, 8, 3, 4, 3, 3, 3.0, 3.0, 4.5, 2.1, 2.2, 2.2, 2.0};
    public H3(){}

    public H3(double[]attack,double[]merge,double[]base){
//        this.attack=attack;
//        this.merge=merge;
        this.base=base;
    }
    public double eval(Board b, Move m,int maximizer,int depth) {
        boolean isMaximizer=m.getPlayerMover()==maximizer;
        if(b.checkWinner()==m.getPlayerMover()&&isMaximizer)return Double.POSITIVE_INFINITY;
        if(b.checkWinner()==m.getPlayerMover()&&!isMaximizer) return Double.NEGATIVE_INFINITY;

        if (b.checkWinner()==b.otherPlayer(m.getPlayerMover())&&isMaximizer)return Double.NEGATIVE_INFINITY;
        if (b.checkWinner()==b.otherPlayer(m.getPlayerMover())&&!isMaximizer)return Double.POSITIVE_INFINITY;
        double sum = 0;

        int sign=1;
        if (!isMaximizer)sign=-1;

        int moverPieceBefore=b.getPlayerBeforeMove(m.getPlayerMover());
        int moverPieceAfter=b.getPlayerNow(m.getPlayerMover());

        int otherPieceBefore=b.getPlayerBeforeMove(b.otherPlayer(m.getPlayerMover()));
        int otherPieceAfter=b.getPlayerNow(b.otherPlayer(m.getPlayerMover()));

        if(moverPieceAfter<moverPieceBefore){
            double exp=attacked[moverPieceBefore-moverPieceAfter];
            double val=(sign*Math.pow(2,exp));
            sum-=val;
        }else

        if(otherPieceAfter<otherPieceBefore) {
            double exp=attack[otherPieceBefore-otherPieceAfter];
            double val=(sign*Math.pow(2,exp));
            sum+=val;
        }

//        int pos=m.getPlayerMover()==Board.WHITE?4:5;
//
//        int xFromCenter=pos-Math.abs(m.getToJ()-pos);
//        int yFromCenter=pos-Math.abs(m.getToI()-pos);
////        System.out.println("AGGIUNGO "+(Math.pow(2,xFromCenter+yFromCenter)));
//
//        sum+=sign*Math.pow(2,xFromCenter+yFromCenter*.5);


//
//        for (int i = 0; i < 8; i++)
//            for (int j = 0; j < 8; j++)
//
//                if (b.isPositionOfPlayer(i,j,m.getPlayerMover()))
//                        sum+=sign*Math.pow(2,base[Math.abs(b.get(i,j))]);
////                else
////                    if  (b.isPositionOfPlayer(i,j,b.otherPlayer(m.getPlayerMover())))
////                        sum-=sign*Math.pow(2,base[Math.abs(b.get(i,j))]);
        System.out.println("H3");
        switch (m.getType()){
            case CAPTURE:
                sum+=sign*Math.pow(2,attack[m.getN()]);//-(distance(m.getPlayerMover(),b));
                break;
            case MERGE:
//                sum+=sign*Math.pow(2,merge[m.getN()]);
                break;
            case BASE:
                //distance: se la mossa base implica lo spostamento della pedina su una posizione in cui verrà mangiata
                //          allora dagli un guadagno minore.
                sum+=sign*2*m.getN();//+(distance(m.getPlayerMover(),b));
                break;
            case DEL:
                sum-=sign*Math.pow(3,attacked[m.getN()]);//+distance(m.getPlayerMover(),b);
                break;
            case STALL:
                break;
        }
//        System.out.println(sum);
        return sum;
    }

    private Double distanzaTraDuePunti(int x1, int x2, int y1, int y2){
        return Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2), 2));
    }

    private void print(Board b) {
        int[][] board = b.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

//    private Double distance2 (Board b, Move m) {
//        int toI = Math.abs(m.getToI()-1);
//        int toJ = Math.abs(m.getToJ()-1);
//        if(toJ>7) return -1.;
//        for (int i = 0; i < b.getBoard().length; i++) {
//            for (int j = 0; j < b.getBoard().length; j++) {
//                int probablyDead = 0;
//                int distance = 0;
//                if (m.getPlayerMover() == 1) {
//                    if (i == toI) distance = Math.abs(j - toJ);
//                    else distance = Math.abs(i - toI);
////                                int distance = distanzaTraDuePunti(i,j,x,y).intValue();
////                    System.out.println(toI +""+toJ);
//                    if ((distance >= Math.abs(b.getBoard()[i][j])) && (distance <= Math.abs(b.getBoard()[toI][toJ])))
//                        probablyDead++;
//
//                    System.out.println(b.getBoard()[toI][toJ]+" prova "+i+","+j+"->"+toI+","+toJ+" ---- "+probablyDead);
//
//
//                }
//            }
//
//        }
//        return 0.;
//    }


    private Double distance(int player, Board b) {
        int[][] board = b.getBoard();
        int probablyDead = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (player == 1 && b.getBoard()[i][j] > 0) { //se sono il giocatore rosso e la cella ha una mia pedina
//                    print(b);
                    for (int x = 0; x < board.length; x++) {
                        for (int y = 0; y < board.length; y++) {
                            int distance = 0;

                            if (b.getBoard()[x][y] < 0) {
                                if(i==x) distance = Math.abs(j-y);
                                else distance = Math.abs(i-x);
//                                int distance = distanzaTraDuePunti(i,j,x,y).intValue();
                                if ((distance >= Math.abs(b.getBoard()[i][j])) && (distance <= Math.abs(b.getBoard()[x][y]))) probablyDead++;
                            }
//                            System.out.println(b.getBoard()[x][y]+" prova "+i+","+j+"->"+x+","+y+" ---- "+probablyDead);

                        }

                    }
                }

                if (player == -1 && b.getBoard()[i][j] < 0) { //se sono il giocatore rosso e la cella ha una mia pedina
//                    print(b);
                    for (int x = 0; x < board.length; x++) {
                        for (int y = 0; y < board.length; y++) {
                            int distance = 0;

                            if (b.getBoard()[x][y] > 0) {
                                if (i == x) distance = Math.abs(j - y);
                                else distance = Math.abs(i - x);
//                                int distance = distanzaTraDuePunti(i,j,x,y).intValue();
                                if ((distance >= Math.abs(b.getBoard()[i][j])) && (distance <= Math.abs(b.getBoard()[x][y])))
                                    probablyDead++;
                            }
//                            System.out.println(b.getBoard()[x][y]+" prova "+i+","+j+"->"+x+","+y+" ---- "+probablyDead);

                        }
                    }
                }
            }
        }
        return probablyDead>0?5.:0;
    }
//    private Double distance(int player, Board b){
//
//        int[][] board = b.getBoard();
//        for (int i=0; i< board.length; i++){
//            for(int j = 0; j<board.length; j++){
//                if(player == 1 && b.getBoard()[i][j] > 0){ //se sono il giocatore rosso e la cella ha una mia pedina
//                    for (int x=0; x<board.length; x++){
//                        for(int y = 0; y<board.length; y++) {
//                            int probablyDead=0;
//                            int distance = 0;
//                            if(b.getBoard()[x][y] < 0){
//
//                                if(i==x) distance = Math.abs(j-y);
//                                else distance = Math.abs(i-x);
////                                int distance = distanzaTraDuePunti(i,j,x,y).intValue();
//                                if ((distance >= Math.abs(b.getBoard()[i][j])) && (distance <= Math.abs(b.getBoard()[x][y]))) probablyDead++;
//                            }
//                            System.out.println(board[x][y]+" prova "+i+","+j+"->"+x+","+y+" ---- "+probablyDead);
//                        }
//                        System.out.println("Fine");
//
//                    }
////                    print(b);
//                }
//
//                if(player == -1 && board[i][j] < 0){ //se sono il giocatore nero e la cella ha una mia pedina
//                    for (int x=0; x< board.length; x++){
//                        for(int y = 0; y<board.length; y++) {
//                            int probablyDead=0;
//                            if (board[x][y] > 0) {
//                                int distance = distanzaTraDuePunti(i, j, x, y).intValue();
//                                if ((distance >= Math.abs(board[i][j])) && (distance <= Math.abs(board[x][y])))
//                                    probablyDead++;
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//
////        return probablyDead>3?0.:5.;
//        return -1.;
//    }

//    public static void main(String... args){
//        H3 prova = new H3();
//        prova.distance(1,new Board(new H3()));
//    }

}
