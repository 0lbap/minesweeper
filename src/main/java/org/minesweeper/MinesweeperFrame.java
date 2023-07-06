package org.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

public class MinesweeperFrame extends JFrame {
    private int tilesH; // Number of tiles per row
    private int tilesV; // Number of tiles per column
    private int nBombs; // Number of bombs
    private final int tileSize = 45;
    private ArrayList<Box> boxes;
    private MainFrame mainFrame;

    public MinesweeperFrame(int tilesH, int tilesV, int nBombs, MainFrame mainFrame) {
        this.tilesH = tilesH;
        this.tilesV = tilesV;
        this.nBombs = nBombs;
        this.mainFrame = mainFrame;
        setTitle(tilesH + "x" + tilesV + " Minesweeper | " + nBombs + " Bombs");
        setSize(tilesH*tileSize, tilesV*tileSize);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mainFrame.setVisible(true);
            }
        });
        setLocationRelativeTo(null);
        GridLayout layout = new GridLayout(tilesV, tilesH);
        setLayout(layout);

        // Creating all the boxes
        boxes = new ArrayList<>();
        for(int i = 0; i < tilesH*tilesV; i++) {
            Box b = new Box(i, this);
            boxes.add(b);
            add(b.getButton());
        }

        // Randomly choosing the indexes of bomb boxes
        int[] bombsIndexes = new Random().ints(0, tilesH*tilesV).distinct().limit(nBombs).toArray();
        for(int i = 0; i < nBombs; i++) {
            boxes.get(bombsIndexes[i]).setIsBomb(true);
        }

        // Setting the number of bombs near each box
        for(int i = 0; i < tilesH*tilesV; i++) {
            int bombsNear;

            // Top Left Corner
            if(i == 0) {
                bombsNear = (boxes.get(i+1).isBomb() ? 1 : 0) + (boxes.get(i+tilesH).isBomb() ? 1 : 0) + (boxes.get(i+tilesH+1).isBomb() ? 1 : 0);
            }

            // Top Right Corner
            else if(i == tilesH-1) {
                bombsNear = (boxes.get(i-1).isBomb() ? 1 : 0) + (boxes.get(i+tilesH).isBomb() ? 1 : 0) + (boxes.get(i+tilesH-1).isBomb() ? 1 : 0);
            }

            // Bottom Left Corner
            else if(i == tilesH*(tilesV-1)) {
                bombsNear = (boxes.get(i+1).isBomb() ? 1 : 0) + (boxes.get(i-tilesH).isBomb() ? 1 : 0) + (boxes.get(i-tilesH+1).isBomb() ? 1 : 0);
            }

            // Bottom Right Corner
            else if(i == (tilesH*tilesV)-1) {
                bombsNear = (boxes.get(i-1).isBomb() ? 1 : 0) + (boxes.get(i-tilesH).isBomb() ? 1 : 0) + (boxes.get(i-tilesH-1).isBomb() ? 1 : 0);
            }

            // First row
            else if(i >= 1 && i < tilesH-1) {
                bombsNear = (boxes.get(i-1).isBomb() ? 1 : 0) + (boxes.get(i+1).isBomb() ? 1 : 0) + (boxes.get(i+tilesH-1).isBomb() ? 1 : 0) + (boxes.get(i+tilesH).isBomb() ? 1 : 0) + (boxes.get(i+tilesH+1).isBomb() ? 1 : 0);
            }

            // Last row
            else if(i >= tilesH*(tilesV-1)+1 && i < tilesH*tilesV-1) {
                bombsNear = (boxes.get(i-1).isBomb() ? 1 : 0) + (boxes.get(i+1).isBomb() ? 1 : 0) + (boxes.get(i-tilesH-1).isBomb() ? 1 : 0) + (boxes.get(i-tilesH).isBomb() ? 1 : 0) + (boxes.get(i-tilesH+1).isBomb() ? 1 : 0);
            }

            // First column
            else if(i%tilesH == 0 && i != 0 && i != tilesH*(tilesV-1)) {
                bombsNear = (boxes.get(i-tilesH).isBomb() ? 1 : 0) + (boxes.get(i+tilesH).isBomb() ? 1 : 0) + (boxes.get(i+1-tilesH).isBomb() ? 1 : 0) + (boxes.get(i+1).isBomb() ? 1 : 0) + (boxes.get(i+1+tilesH).isBomb() ? 1 : 0);
            }

            // Last column
            else if(i%tilesH == tilesH-1 && i != tilesH-1 && i != (tilesH*tilesV)-1) {
                bombsNear = (boxes.get(i-tilesH).isBomb() ? 1 : 0) + (boxes.get(i+tilesH).isBomb() ? 1 : 0) + (boxes.get(i-1-tilesH).isBomb() ? 1 : 0) + (boxes.get(i-1).isBomb() ? 1 : 0) + (boxes.get(i-1+tilesH).isBomb() ? 1 : 0);
            }

            else {
                bombsNear = (boxes.get(i-tilesH-1).isBomb() ? 1 : 0) + (boxes.get(i-tilesH).isBomb() ? 1 : 0) + (boxes.get(i-tilesH+1).isBomb() ? 1 : 0) + (boxes.get(i-1).isBomb() ? 1 : 0) + (boxes.get(i+1).isBomb() ? 1 : 0) + (boxes.get(i+tilesH-1).isBomb() ? 1 : 0) + (boxes.get(i+tilesH).isBomb() ? 1 : 0) + (boxes.get(i+tilesH+1).isBomb() ? 1 : 0);
            }

            boxes.get(i).setBombsNear(bombsNear);
        }

        setVisible(true);
    }

    public void gameOver() {
        setTitle("Minesweeper | Game Over!");
        for(Box b : boxes) {
            b.getButton().setEnabled(false);
            if(b.isBomb() && !b.isFlagged()) {
                b.open();
            }
        }
        JOptionPane.showMessageDialog(this, "You exploded!", "Game Over!", JOptionPane.ERROR_MESSAGE);
    }

    public void checkWin() {
        if(boxes.stream().filter(box -> !box.isOpened()).count() == nBombs) {
            setTitle("Minesweeper | You won!");
            for(Box b : boxes) {
                b.getButton().setEnabled(false);
            }
            JOptionPane.showMessageDialog(this, "Congratulations, you won!", "You won!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void discover(int index) {
        // Open each neighbor box and recall discover on the ones that have 0 bombs near AND that have not been discovered yet
        boxes.get(index).setDiscovered(true);

        // Top Left Corner
        if(index == 0) {
            boxes.get(index+1).open();
            boxes.get(index+tilesH).open();
            boxes.get(index+tilesH+1).open();
            if(boxes.get(index+1).getBombsNear() == 0 && !boxes.get(index+1).isDiscovered()) discover(index+1);
            if(boxes.get(index+tilesH).getBombsNear() == 0 && !boxes.get(index+tilesH).isDiscovered()) discover(index+tilesH);
            if(boxes.get(index+tilesH+1).getBombsNear() == 0 && !boxes.get(index+tilesH+1).isDiscovered()) discover(index+tilesH+1);
        }

        // Top Right Corner
        else if(index == tilesH-1) {
            boxes.get(index-1).open();
            boxes.get(index+tilesH).open();
            boxes.get(index+tilesH-1).open();
            if(boxes.get(index-1).getBombsNear() == 0 && !boxes.get(index-1).isDiscovered()) discover(index-1);
            if(boxes.get(index+tilesH).getBombsNear() == 0 && !boxes.get(index+tilesH).isDiscovered()) discover(index+tilesH);
            if(boxes.get(index+tilesH-1).getBombsNear() == 0 && !boxes.get(index+tilesH-1).isDiscovered()) discover(index+tilesH-1);
        }

        // Bottom Left Corner
        else if(index == tilesH*(tilesV-1)) {
            boxes.get(index+1).open();
            boxes.get(index-tilesH).open();
            boxes.get(index-tilesH+1).open();
            if(boxes.get(index+1).getBombsNear() == 0 && !boxes.get(index+1).isDiscovered()) discover(index+1);
            if(boxes.get(index-tilesH).getBombsNear() == 0 && !boxes.get(index-tilesH).isDiscovered()) discover(index-tilesH);
            if(boxes.get(index-tilesH+1).getBombsNear() == 0 && !boxes.get(index-tilesH+1).isDiscovered()) discover(index-tilesH+1);
        }

        // Bottom Right Corner
        else if(index == (tilesH*tilesV)-1) {
            boxes.get(index-1).open();
            boxes.get(index-tilesH).open();
            boxes.get(index-tilesH-1).open();
            if(boxes.get(index-1).getBombsNear() == 0 && !boxes.get(index-1).isDiscovered()) discover(index-1);
            if(boxes.get(index-tilesH).getBombsNear() == 0 && !boxes.get(index-tilesH).isDiscovered()) discover(index-tilesH);
            if(boxes.get(index-tilesH-1).getBombsNear() == 0 && !boxes.get(index-tilesH-1).isDiscovered()) discover(index-tilesH-1);
        }

        // First row
        else if(index >= 1 && index < tilesH-1) {
            boxes.get(index-1).open();
            boxes.get(index+1).open();
            boxes.get(index+tilesH-1).open();
            boxes.get(index+tilesH).open();
            boxes.get(index+tilesH+1).open();
            if(boxes.get(index-1).getBombsNear() == 0 && !boxes.get(index-1).isDiscovered()) discover(index-1);
            if(boxes.get(index+1).getBombsNear() == 0 && !boxes.get(index+1).isDiscovered()) discover(index+1);
            if(boxes.get(index+tilesH-1).getBombsNear() == 0 && !boxes.get(index+tilesH-1).isDiscovered()) discover(index+tilesH-1);
            if(boxes.get(index+tilesH).getBombsNear() == 0 && !boxes.get(index+tilesH).isDiscovered()) discover(index+tilesH);
            if(boxes.get(index+tilesH+1).getBombsNear() == 0 && !boxes.get(index+tilesH+1).isDiscovered()) discover(index+tilesH+1);
        }

        // Last row
        else if(index >= tilesH*(tilesV-1)+1 && index < tilesH*tilesV-1) {
            boxes.get(index-1).open();
            boxes.get(index+1).open();
            boxes.get(index-tilesH-1).open();
            boxes.get(index-tilesH).open();
            boxes.get(index-tilesH+1).open();
            if(boxes.get(index-1).getBombsNear() == 0 && !boxes.get(index-1).isDiscovered()) discover(index-1);
            if(boxes.get(index+1).getBombsNear() == 0 && !boxes.get(index+1).isDiscovered()) discover(index+1);
            if(boxes.get(index-tilesH-1).getBombsNear() == 0 && !boxes.get(index-tilesH-1).isDiscovered()) discover(index-tilesH-1);
            if(boxes.get(index-tilesH).getBombsNear() == 0 && !boxes.get(index-tilesH).isDiscovered()) discover(index-tilesH);
            if(boxes.get(index-tilesH+1).getBombsNear() == 0 && !boxes.get(index-tilesH+1).isDiscovered()) discover(index-tilesH+1);
        }

        // First column
        else if(index%tilesH == 0 && index != 0 && index != tilesH*(tilesV-1)) {
            boxes.get(index-tilesH).open();
            boxes.get(index+tilesH).open();
            boxes.get(index+1-tilesH).open();
            boxes.get(index+1).open();
            boxes.get(index+1+tilesH).open();
            if(boxes.get(index-tilesH).getBombsNear() == 0 && !boxes.get(index-tilesH).isDiscovered()) discover(index-tilesH);
            if(boxes.get(index+tilesH).getBombsNear() == 0 && !boxes.get(index+tilesH).isDiscovered()) discover(index+tilesH);
            if(boxes.get(index+1-tilesH).getBombsNear() == 0 && !boxes.get(index+1-tilesH).isDiscovered()) discover(index+1-tilesH);
            if(boxes.get(index+1).getBombsNear() == 0 && !boxes.get(index+1).isDiscovered()) discover(index+1);
            if(boxes.get(index+1+tilesH).getBombsNear() == 0 && !boxes.get(index+1+tilesH).isDiscovered()) discover(index+1+tilesH);
        }

        // Last column
        else if(index%tilesH == tilesH-1 && index != tilesH-1 && index != (tilesH*tilesV)-1) {
            boxes.get(index-tilesH).open();
            boxes.get(index+tilesH).open();
            boxes.get(index-1-tilesH).open();
            boxes.get(index-1).open();
            boxes.get(index-1+tilesH).open();
            if(boxes.get(index-tilesH).getBombsNear() == 0 && !boxes.get(index-tilesH).isDiscovered()) discover(index-tilesH);
            if(boxes.get(index+tilesH).getBombsNear() == 0 && !boxes.get(index+tilesH).isDiscovered()) discover(index+tilesH);
            if(boxes.get(index-1-tilesH).getBombsNear() == 0 && !boxes.get(index-1-tilesH).isDiscovered()) discover(index-1-tilesH);
            if(boxes.get(index-1).getBombsNear() == 0 && !boxes.get(index-1).isDiscovered()) discover(index-1);
            if(boxes.get(index-1+tilesH).getBombsNear() == 0 && !boxes.get(index-1+tilesH).isDiscovered()) discover(index-1+tilesH);
        }

        else {
            boxes.get(index-tilesH-1).open();
            boxes.get(index-tilesH).open();
            boxes.get(index-tilesH+1).open();
            boxes.get(index-1).open();
            boxes.get(index+1).open();
            boxes.get(index+tilesH-1).open();
            boxes.get(index+tilesH).open();
            boxes.get(index+tilesH+1).open();
            if(boxes.get(index-tilesH-1).getBombsNear() == 0 && !boxes.get(index-tilesH-1).isDiscovered()) discover(index-tilesH-1);
            if(boxes.get(index-tilesH).getBombsNear() == 0 && !boxes.get(index-tilesH).isDiscovered()) discover(index-tilesH);
            if(boxes.get(index-tilesH+1).getBombsNear() == 0 && !boxes.get(index-tilesH+1).isDiscovered()) discover(index-tilesH+1);
            if(boxes.get(index-1).getBombsNear() == 0 && !boxes.get(index-1).isDiscovered()) discover(index-1);
            if(boxes.get(index+1).getBombsNear() == 0 && !boxes.get(index+1).isDiscovered()) discover(index+1);
            if(boxes.get(index+tilesH-1).getBombsNear() == 0 && !boxes.get(index+tilesH-1).isDiscovered()) discover(index+tilesH-1);
            if(boxes.get(index+tilesH).getBombsNear() == 0 && !boxes.get(index+tilesH).isDiscovered()) discover(index+tilesH);
            if(boxes.get(index+tilesH+1).getBombsNear() == 0 && !boxes.get(index+tilesH+1).isDiscovered()) discover(index+tilesH+1);
        }
    }
}
