import java.util.*;
import java.util.stream.Collectors;

public class TimerAlphaBeta {

    public static long startingTime;
    public static int MAX_MS = 800;
    public static double MIN_SHUFFLE = 0.4;
    public static double MAX_SHUFFLE = 0.4;
    public static int MAX_BREADTH = 12;


    private static double AlphaBeta(Board b, Move move, int maxPlayer, int currPlayer, int currDepth, int maxDepth, double alpha, double beta) {
        if (getCurrentTime() > MAX_MS) {
            return b.eval(move, maxPlayer);
        }
        if (currDepth == maxDepth || b.checkWinner() != 0) {
            return b.eval(move, maxPlayer);
        }
        if (currPlayer == maxPlayer) {
            double best = Double.NEGATIVE_INFINITY;
            List<Move> moves = b.getPossibleMoves(currPlayer);
            shuffleWithProb(MAX_SHUFFLE, moves);
            moves=limit(MAX_BREADTH,moves);
            for (Move m : moves) {
                Board copy = b.copy();
                copy.makeMove(m);
                double val = AlphaBeta(copy, m, maxPlayer, currPlayer * -1, currDepth + 1, maxDepth, alpha, beta);
                best = Math.max(best, val);
                alpha = Math.max(alpha, best);
                if (alpha >= beta) {
                    break;
                }
            }
            return best;
        } else {
            double best = Double.POSITIVE_INFINITY;
            List<Move> moves = b.getPossibleMoves(currPlayer);
            shuffleWithProb(MIN_SHUFFLE, moves);
            moves=limit(MAX_BREADTH,moves);
            for (Move m : moves) {
                Board copy = b.copy();
                copy.makeMove(m);
                double val = AlphaBeta(copy, m, maxPlayer, currPlayer * -1, currDepth + 1, maxDepth, alpha, beta);
                best = Math.min(best, val);
                beta = Math.min(beta, best);
                if (alpha >= beta) {
                    break;
                }

            }
            return best;
        }
    }

    public static synchronized Move AlphaBeta(Board b, int player, int depthMax) {
        startingTime = System.currentTimeMillis();
        Pair<Move, Double> val = AlphaBetaAlg2(b.copy(), player, depthMax);
        return val.getKey();
    }

    private static synchronized Pair<Move, Double> AlphaBetaAlg2(Board b, int player, int depthMax) {

        List<Move> moves = b.getPossibleMoves(player);
        moves=limit(MAX_BREADTH,moves);
        List<Double> max = moves.parallelStream().map(m -> {
            Board copy = b.copy();
            copy.makeMove(m);
            double d = AlphaBeta(copy, m, player, player * -1, 0, depthMax, Integer.MIN_VALUE, Integer.MAX_VALUE);
            return d;
        }).collect(Collectors.toList());
        int indexOfMax = max.indexOf(max.stream().max(Double::compareTo).get());
        List<Move> bestMoves = new LinkedList<>();
        for (int i = 0; i < moves.size(); i++) {
            if (max.get(i) == max.get(indexOfMax))
                bestMoves.add(moves.get(i));
        }
        int rndIndex = new Random().nextInt(bestMoves.size());
        return new Pair<>(bestMoves.get(rndIndex), max.get(indexOfMax));
    }

    private static void shuffleWithProb(double prob, List<Move> moves) {
        if (Math.random() < prob)
            Collections.shuffle(moves);
    }

    private static List<Move> limit(int n, List<Move> moves) {
        moves = moves.subList(0, Math.min(n, moves.size()));
        return moves;
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis() - startingTime;
    }
}