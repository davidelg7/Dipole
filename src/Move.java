

import java.util.Objects;

public class Move {
    private int fromI;
    private int fromJ;
    private int toI;
    private int toJ;
    private int n;
    public static enum Type{BASE,MERGE,CAPTURE}
    private  Type type;

    public Move(int fromI, int fromJ, int toI, int toJ, int n,Type type) {
        this.fromI = fromI;
        this.fromJ = fromJ;
        this.toI = toI;
        this.toJ = toJ;
        this.n=n;
        this.type=type;
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
                toJ == move.toJ &&
                n == move.n &&
                type == move.type;
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
                ", type=" + type +
                '}';
    }
}
