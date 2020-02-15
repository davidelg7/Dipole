import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainTest {
    //=======================
    //CONFIGURATION

    static int nGames=10 ;

    static int DEPTH=4;

    //=======================
    public static void main(String[] args) throws InterruptedException {

        AtomicInteger W= new AtomicInteger();

        AtomicInteger B= new AtomicInteger();

        Heuristics H1= new H_anto2();

        Heuristics H2= new H4();

        List<Integer> l= new LinkedList<>();
        for (int i = 0; i <nGames; i++)
            l.add(i);
        // ANDATA
        l.stream().forEach(i->{
            int winner = winner(H1, H2);
            if (winner == Board.WHITE) W.getAndIncrement();
            if (winner == Board.BLACK) B.getAndIncrement();
        });
        String Winner=W.get() >B.get()?H1.toString().split("@")[0]:W.get() <B.get()?H2.toString().split("@")[0]:"EVEN";
        System.out.println("VINCITORE ANDATA : "+Winner+String.format(" %1.2f",Math.max(W.get(),B.get())/(nGames*.01))+"%");
        System.out.println("WHITE: "+H1.toString().split("@")[0]+" "+W);
        System.out.println("BLACK: "+H2.toString().split("@")[0]+" "+B);
        System.out.println("________________");

        AtomicInteger W2= new AtomicInteger();

        AtomicInteger B2= new AtomicInteger();

        // >RITORNO
        l.stream().forEach(i->{


            int winner = winner(H2, H1);

            if (winner == Board.WHITE) W2.getAndIncrement();
            if (winner == Board.BLACK) B2.getAndIncrement();
        });

        Winner=W2.get() >B2.get()?H2.toString().split("@")[0]:W2.get() <B2.get()?H1.toString().split("@")[0]:"EVEN";
        System.out.println("VINCITORE RITORNO : "+Winner+String.format(" %1.2f",Math.max(W2.get(),B2.get())/(nGames*.01))+"%");
        System.out.println("WHITE: "+H2.toString().split("@")[0]+" "+W2);
        System.out.println("BLACK: "+H1.toString().split("@")[0]+" "+B2);
        System.out.println("________________");
    }

    private static int winner(Heuristics WHITE_H, Heuristics BLACK_H){
        Board b= new Board(null);

        int player=Board.WHITE;
        int winner=0;
        while (winner==0){
            Move bestMove=null;
            Heuristics turn=null;
            turn=   player==Board.WHITE?WHITE_H:BLACK_H;
            b.setH(turn);
            bestMove=TimerAlphaBeta.IterativeDeepeningAlphaBeta(b,player,DEPTH);
            b.makeMove(bestMove);
            player=b.otherPlayer(player);
            winner=b.checkWinner();
        }

        return winner;
    }
}
