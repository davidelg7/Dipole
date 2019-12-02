public class H2 extends Heuristics {

     int eval(Board board, Move move) {
       return (int) ((Math.random()-0.5)*100000);
    }


}