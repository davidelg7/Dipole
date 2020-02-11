import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainTest {
    public static void main(String[] args) throws InterruptedException {
        int nGames=100;

        AtomicInteger W= new AtomicInteger();

        AtomicInteger B= new AtomicInteger();

        Heuristics H1= new H3();
        Heuristics H2= new H_anto2();

        List<Integer> l= new LinkedList<>();
        for (int i = 0; i <nGames; i++)
            l.add(i);
        // ANDATA
        l.stream().forEach(i->{

            int winner = winner(H1, H2);

            if (winner == Board.WHITE) W.getAndIncrement();
            if (winner == Board.BLACK) B.getAndIncrement();
        });
        System.out.println("ANDATA :");
        System.out.println("Vinte da H_WHITE "+W);
        System.out.println("Vinte da H_BLACK "+B);
        System.out.println("________________");

        AtomicInteger W2= new AtomicInteger();

        AtomicInteger B2= new AtomicInteger();

        // >RITORNO
        l.stream().forEach(i->{
            int winner = winner(H2, H1);

            if (winner == Board.WHITE) W2.getAndIncrement();
            if (winner == Board.BLACK) B2.getAndIncrement();
        });

        System.out.println("RITORNO :");
        System.out.println("Vinte da H_WHITE "+W2);
        System.out.println("Vinte da H_BLACK "+B2);
        System.out.println("________________");
    }

    private static int winner(Heuristics WHITE_H, Heuristics BLACK_H){
        TimerAlphaBeta tab = new TimerAlphaBeta();
        Board b= new Board(null);

        int player=Board.WHITE;
        int winner=0;
        while (winner==0){
            Move bestMove=null;
            Heuristics turn=null;

            turn=   player==Board.WHITE?WHITE_H:BLACK_H;
            b.setH(turn);

            bestMove=TimerAlphaBeta.IterativeDeepeningAlphaBeta(b,player,5);
            b.makeMove(bestMove);
            player=b.otherPlayer(player);
            winner=b.checkWinner();
        }

        return winner;
    }
}
