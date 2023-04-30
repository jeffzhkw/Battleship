package battleship;

import javax.swing.*;
import java.awt.*;

public class BoardCell extends JPanel {
    // -1: blue, miss
    // 0: white, unexplored
    // 1: green, ur ship.
    // 2: red, hit
    // 3: dark grey: sunk
    private int color;
    private JPanel filling = new JPanel();
    private JLabel text;

    BoardCell(){
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(50,50));
    }
    BoardCell(int c){
        this();
        this.color = c;
        this.add(this.filling);
    }
    BoardCell(String t){
        this();
        this.text = new JLabel(t);
        this.add(this.text);
    }
    public void setColor(int color){
        this.color = color;
        this.repaint();
    }

    public void paintComponent(Graphics g){
        switch(this.color){
            case -1:
                filling.setBackground(Color.BLUE);
                break;
            case 0:
                filling.setBackground(Color.WHITE);
                break;
            case 1:
                filling.setBackground(Color.GREEN);
                break;
            case 2:
                filling.setBackground(Color.RED);
                break;
            case 3:
                filling.setBackground(Color.DARK_GRAY);
                break;
        }
        g.setColor(Color.BLACK);
        g.drawRect(0,0,50,50);
    }
}
