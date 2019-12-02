import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Communication implements Runnable {
    String address; //Variabile static dell'agente intelligente
    int port; //Variabile static dell'agente intelligente
    public static Socket s; //Variabile static

    public Communication(String address, int port) {
        try {
            this.s = new Socket(address, port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(int fromI, int fromJ, int toI, int toJ) {
        new Message(fromI, fromJ, toI, toJ).send(s);
    }

    public void sendMessage(Message m) {
        m.send(s);
    }

    /**
     *
     * @param fromCoord Coordinate della scacchiera di tipo alfanumerico
     * @param direction coordinate geografiche
     * @param numMoves  numero di mosse della pedina
     * @return  lista contenente gli indici i,j della posizione di partenza e della posizione di arrivo della pedina
     */
    public int[] traduciMossa(String fromCoord, String direction, int numMoves) {
        int fromI = Arrays.asList(Message.rows).indexOf(fromCoord.charAt(0));
        int fromJ = Integer.parseInt("" + fromCoord.charAt(1)) - 1;
        int toI = 0, toJ = 0;
        //Sud o Nord
        if (direction == "S" || direction == "N") {
            toI = (direction == "S") ? fromI + numMoves : fromI - numMoves;
            toJ = fromJ;
        }
        //Est o Ovest
        if (direction == "E" || direction == "W") {
            toJ = (direction == "E") ? fromJ + numMoves : fromJ - numMoves;
            toI = fromI;
        }
        //Sud-Est o Sud-Ovest
        if (direction == "SE" || direction == "SW") {
            toI = fromI + numMoves;
            toJ = (direction == "SE") ? fromJ + numMoves : fromJ - numMoves;
        }
        //Nord-Est o Nord-Ovest
        if (direction == "NE" || direction == "NW") {
            toI = fromI - numMoves;
            toJ = (direction == "NE") ? fromJ + numMoves : fromJ - numMoves;
        }
        return new int[]{fromI, fromJ, toI, toJ,numMoves};
    }

    @Override
    public void run() {
        while (true) {
            try {

                int player=0;
                Board b= new Board();

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                boolean more = true;
                while (more) {
                    line = br.readLine();
                    if (line.contains("WELCOME")) {
                        //mi dice se sto giocando come bianco o nero
                        StringTokenizer st = new StringTokenizer(line, " ");
                        st.nextToken();
                        int valPlayer=Integer.parseInt(st.nextToken());
                        player=valPlayer==1?Board.BLACK:Board.WHITE;
                        /**GIOCATORE = g**/
                    }
                    if (line.contains("OPPONENT_MOVE")/*LINE CONTAINS OPPONENT MOVE*/) {
                        StringTokenizer st = new StringTokenizer(line, " ");
                        st.nextToken();
                        String move = st.nextToken();
                        StringTokenizer st2 = new StringTokenizer(move, ",");
                        //TODO metodo che traduce le mosse dell'avversario e le memorizza in un campo dell'agente intelligente (AgenteIntelligente.opponentMove = [fromI,fromJ,toI,toJ])
                        /**AgenteIntelligente.opponentMove = **/
                        int [] arr=traduciMossa(st2.nextToken(), st2.nextToken(), Integer.parseInt(st2.nextToken()));
                        Move m= new Move(arr[0],arr[1],arr[2],arr[3],arr[4],b.otherPlayer(player),null);
                        List<Move> moves= b.getPossibleMoves(b.otherPlayer(player));
                        m=moves.get(moves.indexOf(m));
                        b.makeMove(m);

                    }
                    if (line.contains("YOUR_TURN")) {
                        Move m= Algorithms.AlphaBetaAlg(b,player,new H2(),3);
                        b.makeMove(m);
                        this.sendMessage(/*AGENTE_INTELLIGENTE.message*/new Message(m.getFromI(), m.getFromJ(), m.getToI(), m.getToJ()));//invia la mia mossa
                        more = false;
                    }
                    if (line == null)
                        more = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String... args){
        Thread t = new Thread(new Communication("localhost",8901));

        t.setDaemon(true);
        t.start();

    }
}
