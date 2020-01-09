import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class TestH {
    private static final double MIN_SHUFFLE = 0.8;
    private static final double MAX_SHUFFLE = 0.8;
    private static int[] lev= new int[2];
public static void writeToFile(String file,Heuristics h){
//    PrintWriter pw=null;
//    try {
//        pw = new PrintWriter(file);
//    } catch (FileNotFoundException e) {
//        e.printStackTrace();
//    }
    Board b= new Board(new H3());
    System.out.println(TimerAlphaBeta.AlphaBeta(b,null,1,1,0,1,-100,1000));
//    Move tmp= new Move(7,4,-1,12,4,1, Move.Type.DEL);
//    System.out.println("ORIGINALE");
//    System.out.println(b);
//    b.makeMove(tmp);
//    System.out.println("MAKE MOVE");
//    System.out.println(b);
//    b.undoMove(tmp);
//    System.out.println("UNDO MOVE");
//    System.out.println(b);
//    pw.close();

}




    public static void main(String...args){
        writeToFile("H3.txt",new H3());
    }


}
