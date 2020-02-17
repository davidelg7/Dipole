import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ServerCom extends Thread {

    //*********** CONFIGURATION ***************
    private int DEPTH = 5;

    static {
        TimerAlphaBeta.MAX_SHUFFLE = .4;
        TimerAlphaBeta.MIN_SHUFFLE = .4;
        TimerAlphaBeta.MAX_MS = 900;
        TimerAlphaBeta.MAX_BREADTH = 15;
    }
    //*****************************************

    private Board b = new Board(new H());
    ;
    private int player;
    private Socket s;
    private String address;
    private int port;
    private Scanner sc;
    private PrintWriter pw;

    public ServerCom(String address, int port) throws IOException {
        this.s = new Socket(address, port);
        sc = new Scanner(s.getInputStream());
        pw = new PrintWriter(s.getOutputStream());
        this.address = address;
        this.port = port;
    }

    public Move traduciMossa(String fromCoord, String direction, int numMoves, int player) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('A', 0);
        map.put('B', 1);
        map.put('C', 2);
        map.put('D', 3);
        map.put('E', 4);
        map.put('F', 5);
        map.put('G', 6);
        map.put('H', 7);

        Map<String, Pair<Integer, Integer>> dir = new HashMap<>();
        dir.put("N", new Pair<>(-1, 0));
        dir.put("S", new Pair<>(1, 0));
        dir.put("E", new Pair<>(0, 1));
        dir.put("W", new Pair<>(0, -1));
        dir.put("NE", new Pair<>(-1, 1));
        dir.put("NW", new Pair<>(-1, -1));
        dir.put("SE", new Pair<>(1, 1));
        dir.put("SW", new Pair<>(1, -1));

        int fromI = map.get(fromCoord.charAt(0));
        int fromJ = Integer.parseInt(fromCoord.charAt(1) + "") - 1;

        int toI = fromI + dir.get(direction).getKey() * numMoves;
        int toJ = fromJ + dir.get(direction).getValue() * numMoves;

        return new Move(fromI, fromJ, toI, toJ, numMoves, player, null);

    }

    @Override
    public void run() {
        int i=0;
        while (!s.isClosed()) {
            String line = sc.nextLine();
            String[] split = line.split(" ");
            if (split[0].contains("WELCOME")) {
                player = line.split(" ")[1].equals("White") ? Board.WHITE : Board.BLACK;
            }

            if (split[0].contains("YOUR_TURN")) {
                i++;
                Move m = TimerAlphaBeta.AlphaBeta(b, player, DEPTH+i/8);
                System.out.println(i/10+DEPTH);
                b.makeMove(m);
                pw.println(new Message(m.getFromI(), m.getFromJ(), m.getToI(), m.getToJ()).message);
                pw.flush();
            }
            if (split[0].contains("OPPONENT_MOVE")) {
                i++;
                String[] m = split[1].split(",");
                Move tmp = traduciMossa(m[0], m[1], Integer.parseInt(m[2]), b.otherPlayer(player));
                List<Move> list = b.getPossibleMoves(b.otherPlayer(player));
                tmp = list.get(list.indexOf(tmp));
                b.makeMove(tmp);
            }
            if (line.contains("ILLEGAL_MOVE") || line.contains("TIMEOUT") || line.contains("VICTORY") || line.contains("TIE") || line.contains("DEFEAT")) {
                System.exit(0);
            }


        }


    }

    private static String getIp(List<String> args) {
        try {
            for (int i = 0; i < args.size(); i++) {
                if (args.get(i).toLowerCase().equals("-ip"))
                    return args.get(i + 1);
            }
        } catch (Exception e) {
        }
        return "localhost";
    }
    private static int getPort(List<String> args) {
        try {
            for (int i = 0; i < args.size(); i++) {
                if (args.get(i).toLowerCase().equals("-port"))
                    return Integer.parseInt(args.get(i + 1));
            }
        } catch (Exception e) {
        }
        return 8901;
    }


    public static void main(String... args) throws IOException {

        System.out.println(getIp(Arrays.asList(args)) + " " + getPort(Arrays.asList(args)));
        new ServerCom(getIp(Arrays.asList(args)), getPort(Arrays.asList(args))).start();

    }

}
