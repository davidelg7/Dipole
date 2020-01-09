import java.util.*;
import java.util.stream.Collectors;

public class TimerAlphaBeta {

    private static long startingTime;
    private static int msLimit = 900;
    private static final double MIN_SHUFFLE = 0.0;
    private static final double MAX_SHUFFLE = 0.0;
    private static final double CUT = 0;
    private static final int DIM = 12;
    private static boolean timeOut= false;
    private static  int depth=3;
//    private static LinkedList<LinkedList<List<Number>>> levels= new LinkedList<>();
    public static double AlphaBeta(Board b,Move move, int maxPlayer, int currPlayer,int currDepth, int maxDepth, double alpha, double beta){
        if(getCurrentTime()>msLimit){
            timeOut=true;
//            levels.get(currDepth).getLast().add(b.eval(move,maxPlayer,1));
            return b.eval(move,maxPlayer,1);
        }
        if (currDepth==maxDepth||b.checkWinner()!=0){
            timeOut=false;
//            levels.get(currDepth).getLast().add(b.eval(move,maxPlayer,1));
            return b.eval(move,maxPlayer,1);}
        if(currPlayer==maxPlayer){
            double best=Double.NEGATIVE_INFINITY;
            List<Move> moves=b.getPossibleMoves(currPlayer);
            shuffleWithProb(MAX_SHUFFLE,moves);
            limit(DIM,moves);
//            levels.get(currDepth+1).add(new LinkedList<>());
            for (Move m:moves){
                Board copy= b.copy();copy.makeMove(m);
                double val = AlphaBeta(copy,m,maxPlayer,currPlayer*-1,currDepth+1,maxDepth,alpha,beta);
                best=Math.max(best,val);
                alpha=Math.max(alpha,best);
                if(alpha>=beta){break;}
            }
//            levels.get(currDepth).getLast().add(best);
            return best;
        }
        else{
            double best=Double.POSITIVE_INFINITY;
            List<Move> moves=b.getPossibleMoves(currPlayer);
            shuffleWithProb(MIN_SHUFFLE,moves);
            limit(DIM,moves);
//            levels.get(currDepth+1).add(new LinkedList<>());
            for (Move m:moves){
                Board copy= b.copy();copy.makeMove(m);
                double val = AlphaBeta(copy,m,maxPlayer,currPlayer*-1,currDepth+1,maxDepth,alpha,beta);
                best=Math.min(best,val);
                beta=Math.min(beta,best);
                if(alpha>=beta){break;}

            }
//            levels.get(currDepth).getLast().add(best);
            return best;
        }
    }
    public static synchronized Move IterativeDeepeningAlphaBeta(Board b, int player, int depthMax){
        startingTime=System.currentTimeMillis();
        Pair<Move,Double> best=AlphaBetaAlg2(b.copy(),player,1);

        for (int i = 2; i <=depthMax ; i++) {

            Pair<Move,Double> m=AlphaBetaAlg2(b.copy(),player,i);
            if (best.getValue()>m.getValue())break;
            else
                best=m;
        }
        System.out.println("Scelgo "+best);
        return best.getKey();
    }
    public static   Move AutoDeepeningAlphaBeta(Board b,int player){
        if(!timeOut)depth+=1;
        if (timeOut)depth-=1;
        Move m =IterativeDeepeningAlphaBeta(b,player,depth);
//        stampa();
        return m;
    }
//    private static void stampa(){
//        levels.stream().forEach(l-> System.out.println(l));
//    }
    private static synchronized Pair<Move,Double> AlphaBetaAlg2(Board b, int player, int depthMax){
//        levels= new LinkedList<>();
//            levels.add(new LinkedList<>(Arrays.asList(new LinkedList<>())));

        List<Move> moves= b.getLimitedPossibleMoves(player,DIM);
        List<Double>max=moves.stream().map(m->{
            Board copy= b.copy();
            copy.makeMove(m);
            return AlphaBeta(copy,m,player,player*-1,0,depthMax,Integer.MIN_VALUE,Integer.MAX_VALUE);
        }).collect(Collectors.toList());
        int indexOfMax=max.indexOf(max.stream().max(Double::compareTo).get());
        List<Move> bestMoves=new LinkedList<>();
        for (int i = 0; i <moves.size() ; i++) {
            if (max.get(i)==max.get(indexOfMax))
                bestMoves.add(moves.get(i));
        }
        int rndIndex=new Random().nextInt(bestMoves.size());
        return new Pair<>(bestMoves.get(rndIndex),max.get(indexOfMax));
    }
    private static void shuffleWithProb(double prob,List<Move> moves){
        if(Math.random()<prob)
        Collections.shuffle(moves);
    }
    private static void limit(int n, List<Move>moves){
        moves=moves.subList(0, Math.min(n,moves.size()));
    }
    private static long getCurrentTime() {
        return System.currentTimeMillis() - startingTime;
    }
}