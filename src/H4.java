import java.util.List;
import java.util.stream.Collectors;

public class H4 extends Heuristics {

    double[] attack = new double[]{0.1, 10.0, 12.5, 13.0 , 12.5, 11.0, 8, 4.0, 4.9, 3.0, 3.1, 3.0, 2.1};
    double[] attacked = new double[]{0.1, 8.5, 10, 13.5 , 13.5, 12.0, 7.8, 4.0, 4.9, 3.0, 3.1, 3.0, 2.1};
    double[] base = new double[]{ 0.0, 1.5, 13.0, 11.0, 12, 11.0, 7.0, 3.0, 4.5, 2.1, 2.2, 2.2, 2.0};
    double[] merge = new double[]{ 0.0, 8, 3, 4, 3, 3, 3.0, 3.0, 4.5, 2.1, 2.2, 2.2, 2.0};

    public double eval(Board b, Move m,int maximizer,int depth) {
        boolean isMaximizer=m.getPlayerMover()==maximizer;
        if(b.checkWinner()==m.getPlayerMover()&&isMaximizer)return Double.POSITIVE_INFINITY;
        if(b.checkWinner()==m.getPlayerMover()&&!isMaximizer) return Double.NEGATIVE_INFINITY;

        if (b.checkWinner()==b.otherPlayer(m.getPlayerMover())&&isMaximizer)return Double.NEGATIVE_INFINITY;
        if (b.checkWinner()==b.otherPlayer(m.getPlayerMover())&&!isMaximizer)return Double.POSITIVE_INFINITY;
        double sum = 0;

        int sign=1;
        if (!isMaximizer)sign=-1;


        List<Move> dangerous= dangerousMovesInNextTurn(b,m.getPlayerMover(),m);

        for (Move danger: dangerous)
            sum-=sign*attack[danger.getN()];


        switch (m.getType()){
            case CAPTURE:
                sum+=sign*attack[m.getN()];
                break;
            case MERGE:
                sum+=sign*merge[b.get(m.getToI(),m.getToJ())*m.getPlayerMover()];
                break;
            case BASE:
                //distance: se la mossa base implica lo spostamento della pedina su una posizione in cui verrà mangiata
                //          allora dagli un guadagno minore.

                sum+=sign*2*base[b.get(m.getToI(),m.getToJ())*m.getPlayerMover()];
                break;
            case DEL:
                sum-=sign*attacked[m.getN()];//+distance(m.getPlayerMover(),b);
                break;
            case STALL:
                break;
        }
//        System.out.println(sum);
        return sum;
    }

    private List<Move> dangerousMovesInNextTurn(Board b,int currplayer,Move currMove){
        Board copy1=b.copy();
        Board copy2=b.copy();
        copy2.undoMove(currMove);
        int adversary= b.otherPlayer(currplayer);

        List<Move>lastTurnCaptureMoves=copy2.getAllOfType(adversary, Move.Type.CAPTURE);

        List<Move>nextTurnCaptureMoves=copy1.getAllOfType(adversary, Move.Type.CAPTURE);

        //se faccio la differenza di next e di last ho come risultato una lista di mosse capture dell'avversario
        //che dipendono solo dalla mossa che ho appena fatto. Se ho fatto una mossa che non va tanto bene le possibili capture dell'avversario
        //aumentano. Queste mosse capture che l'avversario può fare le restituisco.
        nextTurnCaptureMoves.removeAll(lastTurnCaptureMoves);

        return nextTurnCaptureMoves;
    }

}
