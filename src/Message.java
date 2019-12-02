import java.io.*;
import java.net.Socket;

public class Message {
    static char[] rows = new char[]{'A','B','C','D','E','F','G','H'};
    public String message;
    String address; //Variabile static dell'agente intelligente
    int port; //Variabile static dell'agente intelligente
//    Socket s; //Variabile static


    public Message(int fromI, int fromJ, int toI, int toJ) {
        StringBuilder sb = new StringBuilder();
        String pos = convertToPos(fromI, fromJ);
        String direction = getDirection(fromI, fromJ, toI, toJ);
        int numMove = numMoves(fromI, fromJ, toI, toJ);
        sb.append("MOVE ").append(pos).append(',').append(direction).append(',').append(numMove);
        this.message = sb.toString();
    }

    private int numMoves(int fromI, int fromJ, int toI, int toJ) {
        //mossa base in verticale (nord o sud)
        if (fromJ == toJ) {
            return Math.abs(fromI - toI);
        }
        //mossa capture in orizzontale
        else if (fromI == toI) {
            return Math.abs(fromJ - toJ);
        }
        //mossa in diagonale
        else {
            return Math.abs(fromI - toI);
        }

    }

    private String convertToPos(int fromI, int fromJ) {
        return ""+rows[fromI]+(fromJ+1);
    }

    private String getDirection(int fromI, int fromJ, int toI, int toJ) {
        //Sud o Nord
        if (fromJ == toJ)
            return (fromI < toI) ? "S" : "N";
        //Est o Ovest
        else if (fromI == toI)
            return (fromJ < toJ) ? "E" : "W";
        //Sud-Est o Sud-Ovest
        else if (fromI < toI)
            return (fromJ < toJ) ? "SE" : "SW";
        //Nord-Est o Nord-Ovest
        else if (fromI > toI)
            return (fromJ > toJ) ? "NW" : "NE";

        return "Error";
    }

    public void send(Socket s) {
        try {
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            pw.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String... args){

        System.out.println(new Message(6,3,7,4).message);
    }
}
