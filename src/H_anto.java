public class H_anto extends Heuristics {

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
        Board board_tmp = b.copy();
        board_tmp.undoMove(m);
        int player = m.getPlayerMover();
        int numPedine = getNumPedine(player, b);

        int sign=1;
        if (!isMaximizer)sign=-1;
        double sum = 0;
//        System.out.println("H_anto "+numPedine);
        switch (m.getType()){
            case CAPTURE:
                sum+= sign*( -Math.pow(Math.abs(numPedine),7) *
                        (Math.abs(getNumPedineAvversario(player, board_tmp))-Math.abs(getNumPedineAvversario(player, b)))   );
//                System.out.println(Math.abs(getNumPedineAvversario(player, board_tmp))-Math.abs(getNumPedineAvversario(player, b))+" ANTO");

                break;
            case MERGE:
                sum+= sign*(  Math.abs(numPedine) + (-catchable(player ,b)[0]*(0.5*Math.abs(numPedine))) );//+ (catchable(player,b)[1]+(0.7*Math.abs(numPedine))));//+ (catchable(player,board_tmp)*(0.7*Math.abs(numPedine)))    );
                break;
            case BASE:
                //catchable: se la mossa base implica lo spostamento della pedina su una posizione in cui verrà mangiata
                //          allora dagli un guadagno minore.
                sum+= sign* ( Math.abs(numPedine) + (-catchable(player,b)[0]*(0.7*Math.abs(numPedine)))+(catchable(player,board_tmp)[0]*(0.7*Math.abs(numPedine)))  );// );//;+ (neighbors(player,b)+(0.7*Math.abs(numPedine))) + (catchable(player,b)[1]*(0.7*Math.abs(numPedine)))
                break;
            case DEL:
                sum-= sign*( (Math.abs(getNumPedine(player,board_tmp)) - Math.abs(getNumPedine(player,b)))  );
                break;
            case STALL:
                break;
        }
//        System.out.println("H_a");
        return sum;
    }

    private int[] catchable(int player, Board b) {
        int[][] board = b.getBoard();
        int probablyDead = 0;
        int killer = 0;
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
                                if ((distance >= Math.abs(b.getBoard()[i][j])) && (distance <= Math.abs(b.getBoard()[x][y])))
                                    probablyDead++;
                                if ((distance < Math.abs(b.getBoard()[i][j])) && (distance >= Math.abs(b.getBoard()[x][y]))) {
                                    killer++;
                                    System.out.println("killer");
                                }

//                                    if (b.getBoard()[i][j] > probablyDead) probablyDead=b.getBoard()[i][j];
                            }
//                            System.out.println(b.getBoard()[x][y]+" prova "+i+","+j+"->"+x+","+y+" ---- "+probablyDead);

                        }

                    }
                }

                if (player == -1 && b.getBoard()[i][j] < 0) {
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
                                if ((distance < Math.abs(b.getBoard()[i][j])) && (distance >= Math.abs(b.getBoard()[x][y]))) {
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
