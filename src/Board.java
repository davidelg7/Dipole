


import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Board {

    private int[][] board = new int[8][8];

    public static final int BLACK = -1;
    public static final int WHITE = 1;

    private Heuristics h;

    public int otherPlayer(int opt) {
        return opt == WHITE ? BLACK : opt == BLACK ? WHITE : 0;
    }

    public Board(int[][] b, Heuristics h) {
        this.board = b;
        this.h = h;

    }
    private static Board parse(String s){
        Board b = new Board(null);
        b.setBoard(0, 3, BLACK, 0);
        b.setBoard(7, 4, WHITE, 0);
        List<String> lines=Arrays.asList( s.split("\n")).subList(2,10);
        lines= lines.stream().map(l->l.substring("[A|  ".length()-1,"[A|  ---   04W   ---   ---   ---   ---   ---   ---".length())).collect(Collectors.toList());
        List<List<String>> board=lines.stream().map(l->Arrays.asList(l.split("   "))).collect(Collectors.toList());
        for (int i = 0; i < 8; i++) {
            List<String> row=board.get(i);
            for (int j = 0; j < 8; j++) {
                String pos= row.get(j);
                if(pos.contains("W"))
                    b.setBoard(i,j,Board.WHITE,Integer.parseInt(pos.split("W")[0]));
                if(pos.contains("B"))
                    b.setBoard(i,j,Board.BLACK,Integer.parseInt(pos.split("B")[0]));
            }
        }
        return b;
    }
    private static boolean test(int player,String s1,String s2){

        Board b=parse(s1);
        String objective=s2;
        List<Move> moves=b.getPossibleMoves(player);
//        moves.stream().forEach(m-> System.out.println(m));
        List<String> toMessage= moves.stream().map(m->new Message(m.getFromI(),m.getFromJ(),m.getToI(),m.getToJ()).message).collect(Collectors.toList());
//        toMessage.stream().forEach(l-> System.out.println(l));
        System.out.println(toMessage.contains(objective));
        return toMessage.contains(objective);


    }
    public static void main(String... args){

        boolean t1=test(Board.WHITE,"     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   05W   ---   ---   ---   ---   ---   ---  |A\n" +
                "B|  ---   ---   ---   ---   ---   ---   ---   ---  |B\n" +
                "C|  ---   ---   ---   ---   ---   ---   ---   ---  |C\n" +
                "D|  ---   ---   ---   ---   ---   ---   ---   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   ---   ---   ---  |E\n" +
                "F|  ---   ---   ---   ---   ---   ---   ---   ---  |F\n" +
                "G|  ---   ---   ---   01B   ---   ---   ---   01B  |G\n" +
                "H|  ---   ---   ---   ---   01B   ---   ---   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  ","MOVE A2,NW,1");

        boolean t2=test(Board.WHITE,"     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   ---   ---   ---   ---   ---   ---   ---  |A\n" +
                "B|  ---   ---   ---   ---   ---   ---   ---   ---  |B\n" +
                "C|  ---   ---   ---   02W   ---   ---   ---   ---  |C\n" +
                "D|  01W   ---   ---   ---   ---   ---   ---   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   ---   ---   01W  |E\n" +
                "F|  ---   ---   ---   ---   04W   ---   ---   ---  |F\n" +
                "G|  ---   ---   ---   ---   ---   02W   ---   ---  |G\n" +
                "H|  ---   ---   ---   ---   ---   ---   03B   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  \n","MOVE D1,NW,1");



        boolean t3=test(Board.WHITE,"     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   ---   ---   01W   ---   ---   ---   ---  |A\n" +
                "B|  ---   ---   01W   ---   ---   ---   ---   ---  |B\n" +
                "C|  ---   ---   ---   ---   ---   ---   ---   ---  |C\n" +
                "D|  ---   ---   02W   ---   ---   ---   ---   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   ---   ---   03W  |E\n" +
                "F|  ---   ---   ---   ---   ---   ---   ---   ---  |F\n" +
                "G|  ---   ---   ---   ---   ---   ---   ---   ---  |G\n" +
                "H|  ---   ---   ---   ---   ---   ---   02B   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  ","MOVE A4,NW,1");

        boolean t4= test(Board.WHITE,"     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   ---   ---   ---   ---   ---   ---   ---  |A\n" +
                "B|  ---   ---   ---   ---   ---   ---   02W   ---  |B\n" +
                "C|  ---   ---   ---   ---   ---   ---   ---   ---  |C\n" +
                "D|  ---   ---   ---   ---   ---   ---   ---   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   01B   ---   03W  |E\n" +
                "F|  ---   ---   ---   ---   02W   ---   02W   ---  |F\n" +
                "G|  ---   ---   ---   06B   ---   02B   ---   ---  |G\n" +
                "H|  ---   ---   ---   ---   01B   ---   ---   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  ","MOVE B7,NW,2");

        boolean t5=test(Board.WHITE,"     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   01W   ---   ---   ---   ---   ---   ---  |A\n" +
                "B|  ---   ---   ---   ---   ---   ---   ---   ---  |B\n" +
                "C|  ---   ---   ---   ---   ---   ---   ---   ---  |C\n" +
                "D|  ---   ---   ---   ---   ---   ---   02W   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   ---   ---   ---  |E\n" +
                "F|  ---   ---   ---   ---   ---   ---   ---   ---  |F\n" +
                "G|  ---   ---   ---   ---   ---   05B   ---   ---  |G\n" +
                "H|  ---   ---   ---   ---   05W   ---   02W   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  ","MOVE A2,NW,1");
        boolean t6= test(Board.WHITE,"     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   02B   ---   ---   ---   02B   ---   01B  |A\n" +
                "B|  01W   ---   ---   ---   ---   ---   ---   ---  |B\n" +
                "C|  ---   ---   ---   ---   ---   ---   ---   ---  |C\n" +
                "D|  ---   ---   ---   ---   02B   ---   ---   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   ---   ---   ---  |E\n" +
                "F|  ---   ---   ---   ---   02B   ---   01B   ---  |F\n" +
                "G|  ---   ---   ---   ---   ---   ---   ---   ---  |G\n" +
                "H|  ---   ---   ---   ---   ---   ---   ---   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  ","MOVE B1,NE,1");
        boolean t7=test(Board.BLACK,"     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   ---   ---   01W   ---   ---   ---   ---  |A\n" +
                "B|  ---   ---   ---   ---   02W   ---   02W   ---  |B\n" +
                "C|  ---   ---   ---   02B   ---   ---   ---   ---  |C\n" +
                "D|  ---   ---   ---   ---   ---   ---   ---   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   ---   ---   03W  |E\n" +
                "F|  ---   ---   ---   ---   ---   ---   ---   ---  |F\n" +
                "G|  ---   01B   ---   ---   ---   ---   ---   ---  |G\n" +
                "H|  02B   ---   01B   ---   ---   ---   ---   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  \n","MOVE H1,SW,2");
        boolean t8=test(Board.BLACK, "     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   ---   ---   03B   ---   03W   ---   ---  |A\n" +
                "B|  ---   ---   ---   ---   ---   ---   ---   ---  |B\n" +
                "C|  ---   ---   ---   ---   ---   ---   ---   ---  |C\n" +
                "D|  ---   ---   ---   ---   ---   ---   ---   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   ---   ---   ---  |E\n" +
                "F|  ---   ---   ---   ---   ---   ---   ---   ---  |F\n" +
                "G|  ---   ---   ---   ---   ---   ---   ---   ---  |G\n" +
                "H|  ---   ---   02B   ---   04B   ---   ---   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  ","MOVE H5,SW,1");

        boolean t9=test(Board.WHITE,"     1     2     3     4     5     6     7     8  \n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "A|  ---   02W   ---   ---   ---   ---   ---   ---  |A\n" +
                "B|  03W   ---   03W   ---   01W   ---   ---   ---  |B\n" +
                "C|  ---   ---   ---   ---   ---   ---   ---   01W  |C\n" +
                "D|  ---   ---   ---   ---   ---   ---   01W   ---  |D\n" +
                "E|  ---   ---   ---   ---   ---   ---   ---   ---  |E\n" +
                "F|  ---   ---   ---   ---   ---   ---   ---   ---  |F\n" +
                "G|  ---   ---   ---   ---   ---   ---   ---   ---  |G\n" +
                "H|  01B   ---   ---   ---   ---   ---   03B   ---  |H\n" +
                "   ───── ───── ───── ───── ───── ───── ───── ─────\n" +
                "     1     2     3     4     5     6     7     8  ","MOVE A2,NW,1");

    }

    public Board copy() {
        Board b = new Board(this.h);
        for (int i = 0; i < 8; i++)
            b.board[i] = Arrays.copyOf(board[i], board[i].length);

        return b;
    }

    public int checkWinner() {
        int b = 0;
        int w = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isPositionOfPlayer(board, i, j, WHITE)) {
                    w += board[i][j];
                }
                if (isPositionOfPlayer(board, i, j, BLACK)) {
                    b -= board[i][j];
                }
            }
        }
        if (b > 0 && w == 0) return BLACK;
        if (w > 0 && b == 0) return WHITE;
        return 0;
    }

    public Board(Heuristics h) {
        this.h = h;
        setBoard(0, 3, BLACK, 12);
        setBoard(7, 4, WHITE, 12);

    }

    public static boolean isPositionOfPlayer(int[][] board, int i, int j, int player) {

        return Math.signum(board[i][j]) == Math.signum(player);
    }

    public List<Move> getAllOfType(int player, Move.Type type) {
        return getPossibleMoves(player).stream().filter(f -> f.getType().equals(type)).collect(Collectors.toList());
    }

    public synchronized List<Move> getPossibleMoves(int player) {


        List<Move> moves = new LinkedList<>();

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                moves.addAll(allMoves(board, i, j, player));

        if (moves.isEmpty()) {
            int i = 0;
            int j = 0;
            for (int k = 0; k < 8; k++)
                for (int s = 0; s < 8; s++)
                    if (board[k][s] * player > 0) {
                        j = s;
                        i = k;
                        break;
                    }
            moves.add(new Move(i, j, i, j, 0, player, Move.Type.STALL));
        }


        Collections.sort(moves);
        return moves;

    }

    public void setBoard(int i, int j, int player, int b) {
        board[i][j] = b * player;
    }

    private static List<Move> allMoves(int[][] board, int i, int j, int player) {
        List<Move> moves = new LinkedList<>();
        //se la cella appartiene al giocatore considerato
        //considero il giocatore bianco
        if (player == WHITE && isPositionOfPlayer(board, i, j, WHITE)) {
            int riga = i, colonna = j;
            //genero le mosse in verticale

            //k rappresenta il numero di celle di cui mi sposto
            //fin tanto che mi sposto al massimo del numero di celle che ho, e non vado fuori dalla Board
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga - k >= 0; k += 2) {

                if (isPositionOfPlayer(board, riga - k, colonna, WHITE))
                    moves.add(new Move(riga, colonna, riga - k, colonna, k, WHITE, Move.Type.MERGE));
                if (k >= Math.abs(board[riga - k][colonna]) && isPositionOfPlayer(board, riga - k, colonna, BLACK))
                    moves.add(new Move(riga, colonna, riga - k, colonna, k, WHITE, Move.Type.CAPTURE));
                if (board[riga - k][colonna] == 0)
                    moves.add(new Move(riga, colonna, riga - k, colonna, k, WHITE, Move.Type.BASE));
            }
            //genero le mosse capture a destra per il bianco
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && colonna + k < 8; k += 2)
                if (isPositionOfPlayer(board, riga, colonna + k, BLACK) && k >= Math.abs(board[riga][colonna + k]))
                    moves.add(new Move(riga, colonna, riga, colonna + k, k, WHITE, Move.Type.CAPTURE));

            //genero le mosse capture a sinistra per il bianco
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && colonna - k >= 0; k += 2)
                if (isPositionOfPlayer(board, riga, colonna - k, BLACK) && k >= Math.abs(board[riga][colonna - k]))
                    moves.add(new Move(riga, colonna, riga, colonna - k, k, WHITE, Move.Type.CAPTURE));


            //genero le mosse sulla diagonale principale per il bianco
            for (int k = 1; k <= Math.abs(board[riga][colonna]) && riga - k >= 0 && colonna - k >= 0; k++) {
                if (isPositionOfPlayer(board, riga - k, colonna - k, WHITE))
                    moves.add(new Move(riga, colonna, riga - k, colonna - k, k, WHITE, Move.Type.MERGE));
                if (isPositionOfPlayer(board, riga - k, colonna - k, BLACK) && k >= Math.abs(board[riga - k][colonna - k]))
                    moves.add(new Move(riga, colonna, riga - k, colonna - k, k, WHITE, Move.Type.CAPTURE));
                if (board[riga - k][colonna - k] == 0)
                    moves.add(new Move(riga, colonna, riga - k, colonna - k, k, WHITE, Move.Type.BASE));
            }

            //genero le mosse sulla diagonale secondaria per il bianco
            for (int k = 1; k <= Math.abs(board[riga][colonna]) && riga - k >= 0 && colonna + k < 8; k++) {
                if (isPositionOfPlayer(board, riga - k, colonna + k, WHITE))
                    moves.add(new Move(riga, colonna, riga - k, colonna + k, k, WHITE, Move.Type.MERGE));
                if (isPositionOfPlayer(board, riga - k, colonna + k, BLACK) && k >= Math.abs(board[riga - k][colonna + k]))
                    moves.add(new Move(riga, colonna, riga - k, colonna + k, k, WHITE, Move.Type.CAPTURE));
                if (board[riga - k][colonna + k] == 0)
                    moves.add(new Move(riga, colonna, riga - k, colonna + k, k, WHITE, Move.Type.BASE));
            }
            //genero le mosse CAPTURE all'indietro per il bianco
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga + k < 8; k += 2)
                //se la cella generata contiene una pedina avversaria di dimensione minore o uguale alla mia, aggiungo la possibile mossa CAPTURE
                if (isPositionOfPlayer(board, riga + k, colonna, BLACK) && k >= Math.abs(board[riga + k][colonna]))
                    moves.add(new Move(riga, colonna, riga + k, colonna, k, WHITE, Move.Type.CAPTURE));

            //genero le mosse CAPTURE all'indietro sulla diagonale secondaria per il bianco
            for (int k = 1; k <= Math.abs(board[riga][colonna]) && riga + k < 8 && colonna - k >= 0; k++)
                if (isPositionOfPlayer(board, riga + k, colonna - k, BLACK) && k >= Math.abs(board[riga + k][colonna - k]))
                    moves.add(new Move(riga, colonna, riga + k, colonna - k, k, WHITE, Move.Type.CAPTURE));

            //genero le mosse CAPTURE all'indietro sulla diagonale principale per il bianco
            for (int k = 1; k <= Math.abs(board[riga][colonna]) && riga + k < 8 && colonna + k < 8; k++)
                if (isPositionOfPlayer(board, riga + k, colonna + k, BLACK) && k >= Math.abs(board[riga + k][colonna + k]))
                    moves.add(new Move(riga, colonna, riga + k, colonna + k, k, WHITE, Move.Type.CAPTURE));


            if (riga-Math.abs(board[riga][colonna])<0){
                int out=Math.abs(board[riga][colonna]-(riga));
                int n=Math.abs(Math.abs(board[riga][colonna])-out);

                for (int s=n+2 ; s <= out+n; s++,s++){
                    moves.add(new Move(riga, colonna,  riga-s, colonna, s, WHITE, Move.Type.DEL));
            }}
            //Diagonale /
            if (riga-Math.abs(board[riga][colonna])<0 ||colonna+Math.abs(board[riga][colonna])>=7){
                int out=Math.min(Math.abs(riga),Math.abs(colonna-7));
                int n=Math.abs(Math.abs(board[riga][colonna])-out);

                for (int s=out+1 ; s <= out+n; s++) {
                    moves.add(new Move(riga, colonna, riga - s, colonna + s, s, WHITE, Move.Type.DEL));
                }
            }

            if (riga-Math.abs(board[riga][colonna])<0 ||colonna-Math.abs(board[riga][colonna])<0) {
                int out=Math.min(Math.abs(riga),Math.abs(colonna));
                int n=Math.abs(Math.abs(board[riga][colonna])-out);
//                System.out.println("OUT "+out);
//                System.out.println("N "+n);
                for (int s=out+1 ; s <= out+n; s++){
                    moves.add(new Move(riga, colonna,  riga- s,  colonna- s, s, WHITE, Move.Type.DEL));}
            }

        } else if (player == BLACK && isPositionOfPlayer(board, i, j, BLACK)) {
            int riga = i, colonna = j;
            //genero le mosse in verticale
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga + k < 8; k += 2) {
                if (isPositionOfPlayer(board, riga + k, colonna, BLACK))
                    moves.add(new Move(riga, colonna, riga + k, colonna, k, BLACK, Move.Type.MERGE));
                if (isPositionOfPlayer(board, riga + k, colonna, WHITE) && k >= Math.abs(board[riga + k][colonna]))
                    moves.add(new Move(riga, j, riga + k, colonna, k, BLACK, Move.Type.CAPTURE));
                if (board[riga + k][colonna] == 0)
                    moves.add(new Move(riga, j, riga + k, colonna, k, BLACK, Move.Type.BASE));
            }
            //genero le mosse capture a destra per il nero
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && colonna + k < 8; k += 2)
                if (isPositionOfPlayer(board, riga, colonna + k, WHITE) && k >= Math.abs(board[riga][colonna + k]))
                    moves.add(new Move(riga, colonna, riga, colonna + k, k, BLACK, Move.Type.CAPTURE));

            //genero le mosse capture a sinistra per il nero
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && colonna - k >= 0; k += 2)
                if (isPositionOfPlayer(board, riga, colonna - k, WHITE) && k >= Math.abs(board[riga][colonna - k]))
                    moves.add(new Move(riga, colonna, riga, colonna - k, k, BLACK, Move.Type.CAPTURE));


            //genero le mosse sulla diagonale principale per il nero
            for (int k = 1; k <= Math.abs(board[i][j]) && riga + k < 8 && colonna + k < 8; k++) {
                if (isPositionOfPlayer(board, riga + k, colonna + k, BLACK))
                    moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.MERGE));
                if (isPositionOfPlayer(board, riga + k, colonna + k, WHITE) && k >= Math.abs(board[riga + k][colonna + k]))
                    moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.CAPTURE));
                if (board[riga + k][colonna + k] == 0)
                    moves.add(new Move(i, j, riga + k, colonna + k, k, BLACK, Move.Type.BASE));
            }
            //genero le mosse sulla diagonale secondaria per il nero
            for (int k = 1; k <= Math.abs(board[i][j]) && riga + k < 8 && colonna - k >= 0; k++) {

                if (isPositionOfPlayer(board, riga + k, colonna - k, BLACK))
                    moves.add(new Move(i, j, riga + k, colonna - k, k, BLACK, Move.Type.MERGE));
                if (isPositionOfPlayer(board, riga + k, colonna - k, WHITE) && k >= Math.abs(board[riga + k][colonna - k]))
                    moves.add(new Move(i, j, riga + k, colonna - k, k, BLACK, Move.Type.CAPTURE));
                if (board[riga + k][colonna - k] == 0)
                    moves.add(new Move(i, j, riga + k, colonna - k, k, BLACK, Move.Type.BASE));
            }
            //genero le mosse CAPTURE all'indietro per il nero
            for (int k = 2; k <= Math.abs(board[riga][colonna]) && riga - k >= 0; k += 2)
                //se la cella generata contiene una pedina avversaria di dimensione minore o uguale alla mia, aggiungo la possibile mossa CAPTURE
                if (isPositionOfPlayer(board, riga - k, colonna, WHITE) && k >= Math.abs(board[riga - k][colonna]))
                    moves.add(new Move(riga, colonna, riga - k, colonna, k, BLACK, Move.Type.CAPTURE));

            //genero le mosse CAPTURE all'indietro sulla diagonale secondaria per il nero
            for (int k = 1; k <= Math.abs(board[riga][colonna]) && riga - k >= 0 && colonna + k < 8; k++)
                if (isPositionOfPlayer(board, riga - k, colonna + k, WHITE) && k >= Math.abs(board[riga - k][colonna + k]))
                    moves.add(new Move(riga, colonna, riga - k, colonna + k, k, BLACK, Move.Type.CAPTURE));


            //genero le mosse CAPTURE all'indietro sulla diagonale principale per il nero
            for (int k = 1; k <= Math.abs(board[riga][colonna]) && riga - k >= 0 && colonna - k >= 0; k++)
                if (isPositionOfPlayer(board, riga - k, colonna - k, WHITE) && k >= Math.abs(board[riga - k][colonna - k]))
                    moves.add(new Move(riga, colonna, riga - k, colonna - k, k, BLACK, Move.Type.CAPTURE));

            if (riga+Math.abs(board[riga][colonna])>7){
                int out=Math.abs(board[riga][colonna])-(7-riga);
                int n=Math.abs(Math.abs(board[riga][colonna])-out);
                for (int s=n+2 ; s <= out+n; s++,s++)
                    moves.add(new Move(riga, colonna,  riga+s, colonna, s, BLACK, Move.Type.DEL));
            }
//          Diagonale /
            if (riga+Math.abs(board[riga][colonna])>7 ||colonna-Math.abs(board[riga][colonna])<0){
                int out=Math.min(Math.abs(riga-7),Math.abs(colonna));
                int n=Math.abs(Math.abs(board[riga][colonna])-out);

                for (int s=out+1 ; s <= out+n; s+=1)
                    moves.add(new Move(riga, colonna, riga + s, colonna - s, s, BLACK, Move.Type.DEL));
            }
//          Diagonale \
            if (riga+Math.abs(board[riga][colonna])>7 ||colonna+Math.abs(board[riga][colonna])>7) {
                int out=Math.min(Math.abs(riga-7),Math.abs(colonna-7));

                int n=Math.abs(Math.abs(board[riga][colonna])-out);
              ;
                for (int s=out+1 ; s <= out+n; s++)
                    moves.add(new Move(riga, colonna, riga + s, colonna + s, s, BLACK, Move.Type.DEL));
            }

        }
        return moves;
    }

    private Callable<List<Move>> getMoves(int[][] board, int i, int j, int player) {
        return new Callable<List<Move>>() {
            @Override
            public List<Move> call() throws Exception {
                return allMoves(board, i, j, player);

            }
        };
    }

    public int get(int i, int j) {
        return board[i][j];
    }

    public void makeMove(Move m) {

        switch (m.getType()) {
            case BASE: {
                board[m.getFromI()][m.getFromJ()] -= m.getN() * m.getPlayerMover();
                board[m.getToI()][m.getToJ()] += m.getN() * m.getPlayerMover();
                break;
            }
            case CAPTURE: {
                board[m.getFromI()][m.getFromJ()] -= m.getN() * m.getPlayerMover();
                m.setOldN(board[m.getToI()][m.getToJ()] * m.getPlayerMover());
                board[m.getToI()][m.getToJ()] = 0;
                board[m.getToI()][m.getToJ()] += m.getN() * m.getPlayerMover();
                break;
            }
            case MERGE: {
                board[m.getFromI()][m.getFromJ()] -= m.getN() * m.getPlayerMover();
                board[m.getToI()][m.getToJ()] += m.getN() * m.getPlayerMover();
                break;
            }
            case DEL: {
                board[m.getFromI()][m.getFromJ()] -= m.getN() * m.getPlayerMover();
                break;
            }
        }

    }

    public void undoMove(Move m) {
        switch (m.getType()) {
            case BASE: {
                board[m.getFromI()][m.getFromJ()] += m.getN() * m.getPlayerMover();
                board[m.getToI()][m.getToJ()] -= m.getN() * m.getPlayerMover();
                break;
            }
            case CAPTURE: {
                board[m.getFromI()][m.getFromJ()] += m.getN() * m.getPlayerMover();
                board[m.getToI()][m.getToJ()] = m.getOldN() * m.getPlayerMover();
                break;
            }
            case MERGE: {
                board[m.getFromI()][m.getFromJ()] += m.getN() * m.getPlayerMover();
                board[m.getToI()][m.getToJ()] -= m.getN() * m.getPlayerMover();
                break;
            }
            case DEL: {
                board[m.getFromI()][m.getFromJ()] += m.getN() * m.getPlayerMover();
                break;
            }
        }

    }

    public String toString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 7; j++) {
                String s = board[i][j] > 0 ? "W" : board[i][j] < 0 ? "B" : "";

                sb.append(Math.abs(board[i][j]) + s + " |");
            }
            String s = board[i][7] > 0 ? "W" : board[i][7] < 0 ? "B" : "";
            sb.append(Math.abs(board[i][7]) + s + "\n");
            sb.append(" ");

            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("     ");
        for (int i = 0; i < 8; i++)
            sb.append(i + "   ");
        sb.append("\n");
        for (int i = 0; i < 8; i++) {

            sb.append(i + ":  ");
            for (int j = 0; j < 7; j++) {

                sb.append(cell(i, j) + "|");
            }
            sb.append(cell(i, 7) + "\n");
            sb.append("   ");

            for (int j = 0; j < 8; j++) {
                sb.append("----");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String cell(int i, int j) {
        int l = 3;
        String player = board[i][j] > 0 ? "W" : board[i][j] < 0 ? "B" : "";
        int n = Math.abs(board[i][j]);
        if (n > 9) {
            return n + "" + player;
        } else if (n == 0)
            return " 0 ";

        return " " + n + player;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Arrays.equals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] onlyPlayer(int player) {
        int[][] res = new int[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (isPositionOfPlayer(board, i, j, player))
                    res[i][j] = get(i, j) * player;
        return res;
    }

    public double eval(Move move, int player) {
        return h.eval(this, move, player);
    }

    public void setH(Heuristics h) {
        this.h = h;
    }

    public int getWhiteNow() {
        int w = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isPositionOfPlayer(board, i, j, WHITE)) {
                    w += board[i][j];
                }
            }
        }
        return w;
    }

    public int getBlackNow() {
        int b = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isPositionOfPlayer(board, i, j, BLACK)) {
                    b -= board[i][j];
                }
            }
        }
        return b;
    }

    public int getPlayerNow(int player) {
        return player == WHITE ? getWhiteNow() : getBlackNow();
    }
}
