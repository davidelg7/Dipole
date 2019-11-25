

import java.util.Objects;

public class Move {
    private int fromI;
    private int fromJ;
    private int toI;
    private int toJ;
    private int n;
    static final int BASE=0;
    static final int MERGE=1;
    static final int CAPTURE=2;

    public Move(int fromI, int fromJ, int toI, int toJ, int n,int type) {
        if (type!=0||type!=1||type!=2) throw new IllegalArgumentException("Type must be (0,1,2) value found "+type);
        this.fromI = fromI;
        this.fromJ = fromJ;
        this.toI = toI;
        this.toJ = toJ;
        this.n=n;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return fromI == move.fromI &&
                fromJ == move.fromJ &&
                toI == move.toI &&
                toJ == move.toJ &&
                n == move.n;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromI, fromJ, toI, toJ, n);
    }

    @Override
    public String toString() {
        return "Move{" +
                "fromI=" + fromI +
                ", fromJ=" + fromJ +
                ", toI=" + toI +
                ", toJ=" + toJ +
                ", n=" + n +
                '}';
    }
}
