import java.util.Collections;
import java.util.List;

public class Algorithms2 {

    public static Move AlphaBetaAlg(Board b, int player, Heuristics h, int depthMax){

        return alphaBetaPruning(player,player,null,b,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,0,depthMax, h).getValue();


    }
    private static Pair<Integer,Move> alphaBetaPruning (int player,int turn,Move m ,Board board,double alpha, double beta, int currentPly,int maxPly,Heuristics h) {
        if (currentPly++ == maxPly ) {
            return new Pair<>(h.eval(board,m),m);
        }

        if (turn == player) {
            return getMax(player,turn ,board,alpha, beta, currentPly,maxPly,h);
        } else {
            return getMin(player,turn ,board,alpha, beta, currentPly,maxPly,h);
        }
    }

    private static Pair<Integer,Move> getMax (int player,int turn, Board board,double alpha, double beta, int currentPly,int maxPly,Heuristics h) {
        Move indexOfBestMove=null;
        List<Move>moves=board.getPossibleMoves(turn);
        if(Math.random()>0.75) Collections.shuffle(moves);
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


    private static Pair<Integer,Move> getMin (int player,int turn, Board board ,double alpha, double beta, int currentPly,int maxPly,Heuristics h) {
        Move indexOfBestMove = null;

        List<Move>moves=board.getPossibleMoves(turn);
        if(Math.random()>0.75) Collections.shuffle(moves);
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
