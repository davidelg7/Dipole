import java.util.Arrays;
import java.util.Random;

public class Values {
    private static double learningRate=.5;
public static void main(String...args){
    Structure s= new Structure();

    double dw=0;
    double db=0;
    double ddW=0;
    double ddb=0;
    int nPlays=10;
    int lastW=nPlays/2;
    int lastB=nPlays/2;
    TimerAlphaBeta tab= new TimerAlphaBeta();

    for(int j=0;j<10;j++){

     Structure mutated=makeMutation(s,j);

    int white=0;
    int black=0;
    int startPlayer=j%2==0?1:-1;

        for (int i = 0; i <nPlays ; i++) {

        Board b= new Board(new H3(null,null,mutated.baseW));
        int winner=0;
        int opt=startPlayer;
        winner=play(b,tab,startPlayer);
        System.out.println(winner);
        if(winner==Board.WHITE)white++;
        if(winner==Board.BLACK)black++;
    }

    if (isBeneficial(startPlayer,white,black)){
        s=mutated;
        learningRate=.5;}
    else {
        learningRate+=.5;
    }

        System.out.println("==========ITERATION "+j+" ====================");
        System.out.println("WHITE WIN: "+white);
        System.out.println("BLACK WIN: "+black);
        System.out.println("MUTATION :");

        System.out.println(Arrays.toString(mutated.baseW));
        System.out.println(Arrays.toString(mutated.attack));


        lastB=black;
        lastW=white;

    }


    System.out.println("==========FINAL====================");

    System.out.println(Arrays.toString(s.baseW));
    System.out.println(Arrays.toString(s.baseW));


}
    private static int play(Board b,TimerAlphaBeta tab,int player){
        int depth=2;
        while (b.checkWinner()==0) {
            Move bestMove = TimerAlphaBeta.IterativeDeepeningAlphaBeta(b, player,4);
            b.makeMove(bestMove);
            player = b.otherPlayer(player);
            if (player==Board.WHITE)depth=2;
            if (player==Board.BLACK)depth=2;

        }
        return b.checkWinner();
    }
    private static boolean isBeneficial(int player,double w,double b){
    if (player==Board.WHITE) return w>b;
    if(player==Board.BLACK) return b>w;
    return false;
    }

    private static Structure makeMutation(Structure s, int pos) {
    s=s.copy();

    Random r = new Random();
    int rB=r.nextInt(s.attack.length);
    s.attack[rB]+=learningRate;
    return s;
    }


}
class Structure{

    double[] baseW = new double[]{  0.0, 1.5, 9.0, 13.0, 11.5, 11.0, 7.0, 3.0, 4.5, 2.1, 2.2, 2.2, 2.0};
    double[] attack = new double[]{0.1, 5.0, 6.0, 7.0, 8.2, 9.0, 5.0, 4.0, 4.1, 3.0, 3.1, 3.0, 2.1};

    Structure copy(){
        Structure s= new Structure();
        s.baseW=Arrays.copyOf(this.baseW,baseW.length);
        s.attack=Arrays.copyOf(this.attack,attack.length);
        return s;
    }
}