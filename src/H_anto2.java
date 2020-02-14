import java.util.List;
import java.util.Random;

public class H_anto2 extends Heuristics {
    double[] attack = new double[]{0.1, 8.0, 9.5, 13.0 , 12.5, 11.0, 8, 4.0, 4.9, 3.0, 3.1, 3.0, 2.1};

    private int getNumPedine(int player, Board b){
        return player==Board.WHITE?b.getWhiteNow():b.getBlackNow();
    }
    private int getNumPedineAvversario(int player, Board b){
        return player==Board.WHITE?b.getBlackNow():b.getWhiteNow();
    }
    @Override
    double eval(Board b, Move m, int maximizer, int depth) {
        boolean isMaximizer=m.getPlayerMover()==maximizer;
        if(b.checkWinner()==m.getPlayerMover()&&isMaximizer)return Double.POSITIVE_INFINITY;
        if(b.checkWinner()==m.getPlayerMover()&&!isMaximizer) return Double.NEGATIVE_INFINITY;

        if (b.checkWinner()==b.otherPlayer(m.getPlayerMover())&&isMaximizer)return Double.NEGATIVE_INFINITY;
        if (b.checkWinner()==b.otherPlayer(m.getPlayerMover())&&!isMaximizer)return Double.POSITIVE_INFINITY;

        int player = m.getPlayerMover();
        int numPedine = getNumPedine(player,b);

        double sign=1.;
        if (!isMaximizer)sign=-1.;

        double sum = 0.0;

        switch (m.getType()){
            case CAPTURE:
                sum = sign*(
                        18
                        - (totCatchable(b,player)*4)
                        + (killer(b,player)*2)
                );

                break;
            case MERGE:
                sum = sign*(  12
//                        +m.getN()
//                        - (numMoves(m.getFromI(),m.getFromJ(),m.getToI(),m.getToJ())/5 )
//                        - (catchable(player,board_tmp)[0]*3+1)
                          - (totCatchable(b,player)*5)

                        - ((sidedsAndTopBottom(b,player)[0]>0?1:0)*2)
                        + (killer(b,player)*3)
                        );
//                        - (catchable(player,b)[1]*(0.7*Math.abs(numPedine))) );//+ (catchable(player,board_tmp)*(0.7*Math.abs(numPedine)))    );
                break;
            case BASE:
                //catchable: se la mossa base implica lo spostamento della pedina su una posizione in cui verrà mangiata
                //          allora dagli un guadagno minore.
                sum = sign* (  12/*+ neighbors(player,b) */
//                        + m.getN()
//                        - (catchable(player,board_tmp)[0]*4+1)
                        - (totCatchable(b,player)*4)
                        - ((sidedsAndTopBottom(b,player)[0]>0?1:0)*2)
                        + (adversaryInFirstPositions(b, player)*2)
                        + (neighbors(player, b)*(1-totCatchable(b,player)))
                        + (killer(b,player)*3)


                );

//                        -(catchable(player,board_tmp)[0]*(0.7*Math.abs(numPedine)))
//                        - (numMoves(m.getFromI(),m.getFromJ(),m.getToI(),m.getToJ())/5) );//;+ (neighbors(player,b)+(0.7*Math.abs(numPedine)))
//                        - (catchable(player,b)[1]*(0.7*Math.abs(numPedine))) );
                break;
            case DEL:
                sum= - sign*( (Math.abs(getNumPedine(player,b)) - Math.abs(getNumPedine(player,b)))  );
                break;
            case STALL:
                break;
        }
//        System.out.println("H_a");
        return sum;
    }



    private double adversaryInFirstPositions(Board b, int currPlayer){
//        int adversary = b.otherPlayer(currPlayer);
        int adversary = currPlayer==1?-1:1;
        int[][] board = b.getBoard();

        for(int i=3; i< 8; i++){
            for(int j=0;j<8; j++){
                if(adversary==Board.WHITE) {
                    if (board[7 - i][j] > 0) {
                        return 0.;
                    }
                }
                else {
                    if (board[i][j] < 0)
                        return 0.;
                }
            }
        }
        return 1.;
    }

    private double numMoves(int fromI, int fromJ, int toI, int toJ) {
        //mossa base in verticale (nord o sud)
        if (fromJ == toJ) {
            return Math.abs(fromI - toI);
        }
        //mossa capture in orizzontale
        else if (fromI == toI) {
            return Math.abs(fromJ - toJ);
        }
        //mossa in diagonale
        else {
            return Math.abs(fromI - toI);
        }

    }
    private int totCatchable(Board b,int currPlayer){

        int adversary= b.otherPlayer(currPlayer);

        List<Move> capture=b.getAllOfType(adversary, Move.Type.CAPTURE);
//        return capture.stream().mapToInt(m->m.getN()).sum();
        return capture.size()>0?1:0;
    }

    private int killer(Board b,int currPlayer){

        List<Move> capture=b.getAllOfType(currPlayer, Move.Type.CAPTURE);
        try {
//            return capture.stream().max((m1,m2)->m1.getN()-m2.getN()).get().getN();}
            return capture.size() > 0 ? 1 : 0;
        }
        catch (Exception e){}
        return 0;
    }

    /**
     *
     * @param player
     * @param b
     * @return
     */
    private int[] catchable(int player, Board b) {
        int[][] board = b.getBoard();
        int probablyDead = 0;
        int killer = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {

                if (b.getBoard()[i][j] > 0) { //se sono il giocatore rosso e la cella ha una mia pedina
                    for (int x = 0; x < board.length; x++) {
                        for (int y = 0; y < board.length; y++) {

                            int distance = 0;

                            if (b.getBoard()[x][y] < 0) {
                                if(i==x) distance = Math.abs(j-y);
                                else distance = Math.abs(i-x);
//                                int distance = distanzaTraDuePunti(i,j,x,y).intValue();
                                if ((distance >= Math.abs(b.getBoard()[i][j])) && (distance <= Math.abs(b.getBoard()[x][y])))
                                    probablyDead++;
                                if ((distance <= Math.abs(b.getBoard()[i][j])) && (distance >= Math.abs(b.getBoard()[x][y])) && (Math.abs(b.getBoard()[x][y]) < Math.abs(b.getBoard()[i][j])) ){
                                    killer++;
//                                    System.out.println("killer");
                                }

//                                    if (b.getBoard()[i][j] > probablyDead) probablyDead=b.getBoard()[i][j];
                            }
//                            System.out.println(b.getBoard()[x][y]+" prova "+i+","+j+"->"+x+","+y+" ---- "+probablyDead);

                        }

                    }
                }

                if (player == Board.BLACK && b.getBoard()[i][j] < 0) {
                    for (int x = 0; x < board.length; x++) {
                        for (int y = 0; y < board.length; y++) {
                            int distance = 0;

                            if (b.getBoard()[x][y] > 0) {
                                if (i == x) distance = Math.abs(j - y);
                                else distance = Math.abs(i - x);
//                                int distance = distanzaTraDuePunti(i,j,x,y).intValue();
                                if ((distance >= Math.abs(b.getBoard()[i][j])) && (distance <= Math.abs(b.getBoard()[x][y])))
                                    probablyDead++;
                                if ((distance <= Math.abs(b.getBoard()[i][j])) && (distance >= Math.abs(b.getBoard()[x][y])) && (Math.abs(b.getBoard()[x][y]) < Math.abs(b.getBoard()[i][j])) ){
                                    killer++;
//                                    System.out.println("killer");
                                }
                                //                                   if (b.getBoard()[i][j] > probablyDead) probablyDead=b.getBoard()[i][j];

                            }
//                            System.out.println(b.getBoard()[x][y]+" prova "+i+","+j+"->"+x+","+y+" ---- "+probablyDead);

                        }
                    }
                }
            }
        }
        int val1 = probablyDead>0?1:0;
        int val2 = killer>0?1:0;
        return new int[]{val1,val2};
//        return probablyDead>3?1.5:0.8;
    }

    private int[] sidedsAndTopBottom(Board b, int player){
        int lati=0;
        for (int i = 0; i <8 ; i++)
                if (b.isPositionOfPlayer(i,0,player))
                    lati+=Math.abs(b.get(i,0));
        for (int i = 0; i <8 ; i++)
            if (b.isPositionOfPlayer(i,7,player))
                lati+=Math.abs(b.get(i,7));


        int topBottom=0;

        //SCONSIGLIABILE PER IL BIANCO
        if(player==Board.WHITE)
            for (int j = 0; j < 8; j++) {
                if (b.isPositionOfPlayer(0, j, player))
                    topBottom += Math.abs(b.get(0, j));
            }
        else
            //RIGA SCONSIGLIABILE PER IL NERO
                    for (int j = 0; j < 8; j++) {
                        if (b.isPositionOfPlayer(7, j, player))
                            topBottom+= Math.abs(b.get(7, j));
                    }
       return new int[]{lati,topBottom};
    }
    private int neighbors(int player, Board b){
        int[][] board = b.getBoard();
        for (int i=0; i < board.length; i++){
            int neighbor = 0;
            for (int j=0; j< board.length; j++){
                if(player == 1){
                    if(board[i][j]>0) neighbor++;

                }
                if(player == -1){
                    if(board[i][j]<0) neighbor++;
                }
            }
            if(neighbor>1) return 1;
        }
        return 0;
    }
}
