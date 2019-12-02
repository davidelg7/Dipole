import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class TimerAlphaBeta extends Thread{

    private long startingTime;
    private int msLimit=900;
    private ExecutorService es = Executors.newFixedThreadPool(4);
    private static final double MIN_SHUFFLE=0.98;
    private static final double MAX_SHUFFLE=0.98;




    private FutureTask<Pair<Integer,Move>> alphaBetaPruningConcurrent(Board b, int player, Heuristics h, int depthMax){
        List<Pair<Integer,Move>> bests=new LinkedList<>();
       return null;



    }
    public Move AlphaBetaAlg(Board b, int player, Heuristics h, int depthMax){
        startingTime=System.currentTimeMillis();
        return alphaBetaPruning(player,player,null,b,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,0,depthMax,h).getValue();


    }
    private  Pair<Integer,Move> alphaBetaPruning (int player,int turn,Move m ,Board board,double alpha, double beta, int currentPly,int maxPly,Heuristics h) {
        if (currentPly++ == maxPly ||getCurrentTime()>msLimit) {
            return new Pair<>(h.eval(board,m),m);
        }

        if (turn == player) {
            return getMax(player,turn ,board,alpha, beta, currentPly,maxPly,h);
        } else {
            return getMin(player,turn ,board,alpha, beta, currentPly,maxPly,h);
        }
    }

    private  long getCurrentTime() {
        return System.currentTimeMillis()-startingTime;
    }

    private  Pair<Integer,Move> getMax (int player,int turn, Board board,double alpha, double beta, int currentPly,int maxPly,Heuristics h) {
        Move indexOfBestMove=null;
        List<Move> moves=board.getPossibleMoves(turn);
        if(Math.random()>MIN_SHUFFLE) Collections.shuffle(moves);
        for (Move theMove : moves) {

            Board modifiedBoard = board.copy();
            modifiedBoard.makeMove(theMove);
            Pair<Integer,Move> min= alphaBetaPruning(player,board.otherPlayer(turn),theMove, modifiedBoard, alpha, beta, currentPly,maxPly,h);
            int score=min.getKey();
            if (score > alpha) {
                alpha = score;
                indexOfBestMove = theMove;
            }

            // Pruning.
            if (alpha >= beta) {
                break;
            }
        }


        return new Pair((int) alpha,indexOfBestMove);
    }


    private  Pair<Integer,Move> getMin (int player,int turn, Board board ,double alpha, double beta, int currentPly,int maxPly,Heuristics h) {
        Move indexOfBestMove = null;

        List<Move>moves=board.getPossibleMoves(turn);
        if(Math.random()>MAX_SHUFFLE) Collections.shuffle(moves);
        for (Move theMove : moves) {

            Board modifiedBoard = board.copy();
            modifiedBoard.makeMove(theMove);

            Pair<Integer,Move> max= alphaBetaPruning(player,board.otherPlayer(turn),theMove, modifiedBoard, alpha, beta, currentPly,maxPly,h);
            int score=max.getKey();

            if (score < beta) {
                beta = score;
                indexOfBestMove = theMove;
            }

            // Pruning.
            if (alpha >= beta) {
                break;
            }
        }


        return new Pair((int) beta,indexOfBestMove);
    }



}
