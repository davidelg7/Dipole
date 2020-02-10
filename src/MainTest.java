
public class MainTest {
    public static void main(String[] args){
        int nGames=1000;
        int W=0;
        int B=0;
        Heuristics WHITE= new H_anto2();
        Heuristics BLACK= new H_anto();
        for (int i = 0; i <nGames; i++) {
            int winner= winner(WHITE,BLACK);
            if (winner==Board.WHITE)W++;
            if(winner==Board.BLACK)B++;
        }
        System.out.println("Vinte da H_WHITE "+W);
        System.out.println("Vinte da H_BLACK "+B);



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
            bestMove=TimerAlphaBeta.IterativeDeepeningAlphaBeta(b,player,4);
            b.makeMove(bestMove);
            player=b.otherPlayer(player);
            winner=b.checkWinner();
        }

        return winner;
    }
}
