

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Move implements Comparable<Move> {
    private int fromI;
    private int fromJ;
    private int toI;
    private int toJ;
    private int n;
    private int oldN;
    private int playerMover;

    public static Move randomMove(Board b, int white) {
        List<Move> m = b.getPossibleMoves(white);
        Collections.shuffle(m);
        return m.get(0);
    }

    public int getOldN() {
        return oldN;
    }

    public void setOldN(int oldN) {
        this.oldN = oldN;
    }

    public int compareTo(Move o) {

        return type.compareTo(o.type);
    }

    public static enum Type {CAPTURE, MERGE, BASE, DEL, STALL}

    private Type type;

    public Move(int fromI, int fromJ, int toI, int toJ, int n, int playerMover, Type type) {
        this.fromI = fromI;
        this.fromJ = fromJ;
        this.toI = toI;
        this.toJ = toJ;
        this.n = n;
        this.type = type;
        this.playerMover = playerMover;
    }

    public int getPlayerMover() {
        return playerMover;
    }

    public void setPlayerMover(int playerMover) {
        this.playerMover = playerMover;
    }

    public int getFromI() {
        return fromI;
    }

    public void setFromI(int fromI) {
        this.fromI = fromI;
    }

    public int getFromJ() {
        return fromJ;
    }

    public void setFromJ(int fromJ) {
        this.fromJ = fromJ;
    }

    public int getToI() {
        return toI;
    }

    public void setToI(int toI) {
        this.toI = toI;
    }

    public int getToJ() {
        return toJ;
    }

    public void setToJ(int toJ) {
        this.toJ = toJ;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return fromI == move.fromI &&
                fromJ == move.fromJ &&
                toI == move.toI &&
                toJ == move.toJ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromI, fromJ, toI, toJ, n, type);
    }

    @Override
    public String toString() {
        return "Move{" +
                "fromI=" + fromI +
                ", fromJ=" + fromJ +
                ", toI=" + toI +
                ", toJ=" + toJ +
                ", n=" + n +
                ", oldN=" + oldN +
                ", playerMover=" + playerMover +
                ", type=" + type +
                '}';
    }
}
