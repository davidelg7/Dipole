import javafx.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
public class Algorithms {

        private static int maximizer;

        private static void setMaximizer(int p){
            maximizer=p;
        }

        public static int Maximizer() {
            return maximizer;
        }
        private static ExecutorService es= Executors.newFixedThreadPool(5);
        public static void stopEcecutorService(){
            es.shutdown();
        }
        /**
         *
         * @param b Board sulla quale lavorare
         * @param opt giocatore che fa da max
         * @param h euristica considerata
         * @param depthMax massima profondità alla quale arrivare
         * @return coppia i,j di posizione ottimale secondo l'algoritmo
         */
        public static Move AlphaBetaAlg(Board b, int opt, Heuristics h, int depthMax){

            setMaximizer(opt);

            Move best=null;

            int currOpt=Integer.MIN_VALUE;
            List<FutureTask<Pair<Integer,Move>>> tasks=new LinkedList<>();
            List<Move> moves=b.getPossibleMoves(opt);
            for(Move p :b.getPossibleMoves(opt)){
                FutureTask<Pair<Integer,Move>> f=future(b.copy(),b.otherPlayer(opt),p,h,0,depthMax,Integer.MIN_VALUE,Integer.MAX_VALUE);
                es.submit(f);
                tasks.add(f);
            }
            Iterator<Move>it=moves.iterator();
            Move m=null;
            Move be=null;
            for (FutureTask<Pair<Integer,Move>> f:tasks){
                Pair<Integer,Move>O=null;
                try {
                    O= f.get();
                    m=it.next();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(currOpt<O.getKey()){
                    best=O.getValue();
                    currOpt=O.getKey();
                    be=m;
                }
            }

            return be;
        }
        private static FutureTask<Pair<Integer,Move>> future(Board b, int opt,Move mov, Heuristics h, int currDepth, int depthMax, int alpha , int beta){
            return new FutureTask<>(() -> AlphaBeta(b,opt,mov,h,currDepth,depthMax,alpha,beta));
        }
        private static Pair<Integer,Move> AlphaBeta(Board b, int opt,Move mov, Heuristics h, int currDepth, int depthMax, int alpha , int beta){

            List<Move> moves=b.getPossibleMoves(opt);

            if(currDepth==depthMax||moves.isEmpty()){ return new Pair(h.eval(b,opt),mov);}


            if(opt==Maximizer()) {
                int val = Integer.MIN_VALUE;
                Move best=null;
                for(Move m:moves) {
                    b.makeMove(m);
                    Pair<Integer,Move> v=AlphaBeta(b,b.otherPlayer(opt),m,h,currDepth+1,depthMax,alpha,beta);

                    if(v.getKey()>val){
                        val=v.getKey();
                        best=v.getValue();
                    }
                    b.undoMove(m);
                    alpha=Math.max(alpha,val);
                    if(alpha>=beta)
                        break;
                }
                return new Pair(val,best);
            }
            else{
                int val =Integer.MAX_VALUE;
                Move best=null;

                for(Move m:moves) {
                    b.makeMove(m);
                    Pair<Integer,Move> v=AlphaBeta(b,b.otherPlayer(opt),m,h,currDepth+1,depthMax,alpha,beta);

                    if(v.getKey()<val){
                        val=v.getKey();
                        best=v.getValue();
                    }
                    b.makeMove(m);
                    beta=Math.min(beta,val);
                    if(alpha>=beta)
                        break;
                }
                return new Pair(val,best);
            }
        }
    }

