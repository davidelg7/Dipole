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

        switch (m.getType()){
            case CAPTURE:
                    sum+=sign*Math.pow(2,attack[m.getN()]);
                break;
            case MERGE:
//                sum+=sign*Math.pow(2,merge[m.getN()]);
                break;
            case BASE:
                sum+=sign*2*m.getN();
                break;
            case DEL:
                sum-=sign*Math.pow(3,attacked[m.getN()]);
                break;
            case STALL:
                break;
        }
    return sum;
    }



}
