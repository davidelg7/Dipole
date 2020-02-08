import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.BufferPoolMXBean;
import java.net.Socket;
import java.util.*;

public class ServerCom2 extends Thread{
    //    private Board b= new Board(new H3( new double[]{0.0, 3.0, 6.0, 7.0, 8.0, 9.0, 5.0, 4.0, 4.0, 1.5, 3.0, 3.0, 2.0},
//            new double[]{0.0, 5.0, 4.0, 7.0, 6.5, 6.0, 5.0, 4.0, 4.0},
//            new double[]{0.0, 3.0, 6.0, 7.0, 6.5, 6.0, 5.0, 4.0, 4.0}));
    private Board b= new Board(new H_anto());
    private int player;
    private Socket s;
    private String address;
    private int port;
    private Scanner sc;
    private PrintWriter pw;
    public ServerCom2(String address, int port) throws IOException {
        this.player = player;
        this.s = new Socket(address,port);
        sc= new Scanner(s.getInputStream());
        pw= new PrintWriter(s.getOutputStream());
        this.address = address;
        this.port = port;
    }
    public Move traduciMossa(String fromCoord, String direction, int numMoves,int player) {
        Map<Character,Integer>map= new HashMap<>();
        map.put('A',0);
        map.put('B',1);
        map.put('C',2);
        map.put('D',3);
        map.put('E',4);
        map.put('F',5);
        map.put('G',6);
        map.put('H',7);

        Map<String,Pair<Integer,Integer>> dir= new HashMap<>();
        dir.put("N",new Pair<>(-1,0));
        dir.put("S",new Pair<>(1,0));
        dir.put("E",new Pair<>(0,1));
        dir.put("W",new Pair<>(0,-1));
        dir.put("NE",new Pair<>(-1,1));
        dir.put("NW",new Pair<>(-1,-1));

        dir.put("SE",new Pair<>(1,1));
        dir.put("SW",new Pair<>(1,-1));

        int fromI=map.get(fromCoord.charAt(0));
        int fromJ=Integer.parseInt(fromCoord.charAt(1)+"")-1;

        int toI=fromI+dir.get(direction).getKey()*numMoves;
        int toJ=fromJ+dir.get(direction).getValue()*numMoves;

        return new Move(fromI,fromJ,toI,toJ,numMoves,player,null);

    }
    @Override
    public void run() {
        int i=1;
        TimerAlphaBeta tab= new TimerAlphaBeta();
        while (!s.isClosed()){
//            System.out.println("TURN"+i);
//            System.out.println(player);
            String line=sc.nextLine();
//            System.out.println(line);
            String[] split=line.split(" ");
            if(split[0].contains("WELCOME")){
                player=line.split(" ")[1].equals("White")? Board.WHITE:Board.BLACK;
            }
            if(split[0].contains("YOUR_TURN")){

                Move m = tab.IterativeDeepeningAlphaBeta(b,player,4);

                b.makeMove(m);

                pw.println(new Message(m.getFromI(),m.getFromJ(),m.getToI(),m.getToJ()).message);
                pw.flush();


                i++;
            }
            if (split[0].contains("OPPONENT_MOVE")){
                String[] m=split[1].split(",");
                Move tmp=traduciMossa(m[0],m[1],Integer.parseInt(m[2]),b.otherPlayer(player));
                List<Move> list=b.getPossibleMoves(b.otherPlayer(player));
                tmp=list.get(list.indexOf(tmp));
                b.makeMove(tmp);
                i++;

            }
            if(line.contains("ILLEGAL_MOVE")||line.contains("TIMEOUT")||line.contains("VICTORY")||line.contains("TIE")||line.contains("TIE")||line.contains("DEFEAT")) {
                System.exit(0);
            }







        }







    }
    public static void main(String...args) throws IOException {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new ServerCom2("localhost",8901).start();

    }

}
