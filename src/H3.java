public class H3 extends Heuristics{
    public int eval(Board b, Move m){
        b.makeMove(m);
        if(b.checkWinner()==m.getPlayerMover())return Integer.MAX_VALUE;
        if (b.checkWinner()==b.otherPlayer(m.getPlayerMover()))return Integer.MIN_VALUE;
        int sum=0;

        switch (m.getType()){
            case CAPTURE:{
                sum+=Math.pow(10,m.getN()+1);
                break;
            }
            case MERGE:
                sum+=100*m.getN();
                break;
            case BASE:
                sum-=10*m.getN();
                break;
            case DEL:
                sum-=Math.pow(10,m.getN());
                break;
        }
        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                if (b.get(i,j)==m.getPlayerMover())
                    sum+=10*Math.abs(b.get(i,j));
                else
                    if(b.get(i,j)==b.otherPlayer(m.getPlayerMover()))
                    sum-=10*Math.abs(b.get(i,j));

        b.undoMove(m);
        //System.out.println(sum);
        return sum;

    }
}
