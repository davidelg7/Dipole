
public class MainTest {
    public static void main(String[] args){


        TimerAlphaBeta tab = new TimerAlphaBeta();
        Board b= new Board(new H3());

        int player=Board.WHITE;
        int winner=0;
        while (winner==0){
            System.out.println("GIOCATORE "+(player==Board.WHITE?"WHITE":"BLACK"));
            Move bestMove=null;
            bestMove=player==Board.WHITE?tab.IterativeDeepeningAlphaBeta(b,player,2):tab.IterativeDeepeningAlphaBeta(b,player,3);

            System.out.println(bestMove);
            b.makeMove(bestMove);
            player=b.otherPlayer(player);
            System.out.println(b);
            System.out.println("---------------------------------------------------");
            winner=b.checkWinner();
        }
        System.out.println("WINNER IS "+(winner==Board.WHITE?"WHITE":"BLACK"));
        Algorithms.stopEcecutorService();





    }
}
