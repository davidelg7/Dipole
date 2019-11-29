
public class MainTest {
    public static void main(String[] args){



        Board b= new Board();

        int player=Board.WHITE;
        int winner=0;
        while (winner==0){
            System.out.println("GIOCATORE "+(player==Board.WHITE?"WHITE":"BLACK"));
            Move bestMove=null;
            bestMove=player==Board.WHITE?Algorithms.AlphaBetaAlg(b,player,new H2(),2):Algorithms.AlphaBetaAlg(b,player,new H2(),4);

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
