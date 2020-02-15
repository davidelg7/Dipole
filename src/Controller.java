import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;


public class Controller {
    @FXML
    private GridPane grid;
    private AdvancedText[][] b1= new  AdvancedText[8][8];
    private AdvancedText pressed;
    private Board b = new Board(new H_anto3());
    //private Board b = new Board(new H2());
    private TimerAlphaBeta tab= new TimerAlphaBeta();
    @FXML
    private VBox moves;
    @FXML
    void initialize() {

        for(int i=0;i<8;i++)

            for(int j=0;j<8;j++) {

                AdvancedText t = new AdvancedText("      ",i,j);
                t.setFont(Font.font(30));
                t.setTextFill(Color.BLACK);

                t.setAlignment(Pos.CENTER);

                t.setPrefWidth(80);

                t.setPrefHeight(80);

                t.setOnMouseClicked( event -> {

                    try {

                        if (pressed != null) {

                            runAlg(pressed, (AdvancedText) event.getSource());
                            pressed = null;

                        }

                        else {

                            pressed = (AdvancedText) event.getSource();
                            pressed.setText(pressed.getText() + " S");

                        }

                        event.consume();

                    }catch (Exception e){

                        e.printStackTrace();

                        pressed=null;

                        updateBoard();}
                });

                b1[i][j]=t;
                grid.add(t, j, i);

            }
        Move m= tab.IterativeDeepeningAlphaBeta(b,Board.WHITE,4);//AutoDeepeningAlphaBeta(b,Board.WHITE);
//        System.out.println("ANTOO");
        b.makeMove(m);
    updateBoard();

    }

    private void runAlg(AdvancedText from, AdvancedText to) {

            Move oppMove = new Move(from.getI(), from.getJ(), to.getI(), to.getJ(), 0, -1, null);
            List<Move> l = b.getPossibleMoves(-1);
            oppMove = l.get(l.indexOf(oppMove));
            b.makeMove(oppMove);
            updateBoard();

            Move m = TimerAlphaBeta.IterativeDeepeningAlphaBeta(b,Board.WHITE,4);
            b.makeMove(m);


//        System.out.println();
        updateBoard();

    }
    private void runAlg(Move oppMove) {
        b.makeMove(oppMove);
        updateBoard();
        Move m = TimerAlphaBeta.IterativeDeepeningAlphaBeta(b,Board.WHITE,4);
        b.makeMove(m);
        updateBoard();

    }
    void updateBoard(){
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++){
              if (b.get(i,j)>0)b1[i][j].setTextFill(Color.RED);
              else
                  b1[i][j].setTextFill(Color.BLACK);
              b1[i][j].setText(""+(b.get(i,j)!=0?b.get(i,j):""));
            }
         List<Move>m=b.getPossibleMoves(Board.BLACK);

            moves.getChildren().clear();
        for (Move mov: m)   {
            Button b= new Button(mov.getType()+" ("+mov.getFromI()+"-"+mov.getFromJ()+") -> ("+mov.getToI()+"-"+mov.getToJ()+") "+mov.getN());
            b.setOnMouseClicked(e->{runAlg(mov);e.consume();});
            moves.getChildren().add(b);
        }

    }


}
class AdvancedText extends Label {
    private int i,j;

    public AdvancedText(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public AdvancedText(String text, int i, int j) {
        super(text);
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}

