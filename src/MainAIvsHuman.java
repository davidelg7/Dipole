import java.util.List;
import java.util.Scanner;

public class MainAIvsHuman {
    public static void main(String[] args){



        Board b= new Board(new H3());

        int player=Board.WHITE;
        int mover=Board.WHITE;
        TimerAlphaBeta tab = new TimerAlphaBeta();
        int winner=0;
        while (winner==0) {
            if (mover == player) {
                System.out.println("GIOCATORE " + (player == Board.WHITE ? "WHITE" : "BLACK"));
                Move bestMove = null;
                bestMove = TimerAlphaBeta.IterativeDeepeningAlphaBeta(b, player, 7);

                System.out.println(bestMove);
                b.makeMove(bestMove);
                mover = b.otherPlayer(mover);
                System.out.println(b);
                System.out.println("---------------------------------------------------");
            }
            else{
                Move bestMove = null;
                bestMove = getMove(b,player);

                System.out.println(bestMove);
                b.makeMove(bestMove);
                mover = b.otherPlayer(mover);
                System.out.println(b);
                System.out.println("---------------------------------------------------");
            }
            winner = b.checkWinner();
        }
        System.out.println("WINNER IS "+(winner==Board.WHITE?"WHITE":"BLACK"));
        Algorithms.stopEcecutorService();





    }
    private static Move getMove(Board b,int player){
        Scanner sc= new Scanner(System.in);
        System.out.println("Inserisci fromI");
        int fromI=sc.nextInt();
        System.out.println("Inserisci fromJ");
        int fromJ=sc.nextInt();
        System.out.println("Inserisci toI");
        int toI=sc.nextInt();
        System.out.println("Inserisci toJ");
        int toJ=sc.nextInt();
        System.out.println("Inserisci n");
        int n=sc.nextInt();
        Move oppMove= new Move(fromI,fromJ,toI,toJ,n,b.otherPlayer(player),null);
        List<Move> l=b.getPossibleMoves(b.otherPlayer(player));
        oppMove=l.get(l.indexOf(oppMove));
        System.out.println(oppMove);
        return oppMove;

    }
}
