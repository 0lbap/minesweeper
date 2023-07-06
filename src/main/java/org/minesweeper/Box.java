package org.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Box {
    private int index;
    private JButton button;
    private boolean isBomb;
    private boolean isOpened;
    private boolean isDiscovered;
    private boolean isFlagged;
    private boolean isQuestioned;
    private int bombsNear;
    private String text;
    private MinesweeperFrame minesweeperFrame;

    public Box(int index, MinesweeperFrame minesweeperFrame) {
        this.index = index;
        this.minesweeperFrame = minesweeperFrame;
        isOpened = false;
        isFlagged = false;
        isQuestioned = false;
        button = new JButton();
        button.setText(" ");
        button.setFont(new Font("Monospace", Font.BOLD, 14));
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Only allow interactions if the button is enabled
                if(button.isEnabled()) {

                    // Open the box with left click
                    if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                        open();
                        if(isBomb) {
                            // End the game if the player reveals a bomb
                            minesweeperFrame.gameOver();
                        } else {
                            // Check if every non-bomb box have been opened
                            minesweeperFrame.checkWin();
                        }
                        // Reveal the area if the player reveals a hole
                        if(bombsNear == 0 && !isBomb) {
                            minesweeperFrame.discover(index);
                            minesweeperFrame.checkWin();
                        }
                    }

                    // Set a flag or a question mark with right click
                    if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                        if(!isOpened) {
                            if(isFlagged) {
                                isFlagged = false;
                                isQuestioned = true;
                                button.setText("?");
                            } else if(isQuestioned) {
                                isQuestioned = false;
                                button.setText(" ");
                            } else {
                                isFlagged = true;
                                button.setText("F");
                            }
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public int getIndex() {
        return index;
    }

    public JButton getButton() {
        return button;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setIsBomb(boolean isBomb) {
        this.isBomb = isBomb;
        setText("x");
    }

    public int getBombsNear() {
        return bombsNear;
    }

    public void setBombsNear(int bombsNear) {
        this.bombsNear = bombsNear;
        if(!isBomb) {
            setText(String.valueOf(bombsNear));
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void open() {
        if(isOpened) return;
        button.setText(text);
        if(text.equals("0")) {
            button.setForeground(new Color(192, 192, 192));
        } else if(text.equals("1")) {
            button.setForeground(new Color(21, 0, 255));
        } else if(text.equals("2")) {
            button.setForeground(new Color(0, 128, 1));
        } else if(text.equals("3")) {
            button.setForeground(new Color(255, 2, 0));
        } else if(text.equals("4")) {
            button.setForeground(new Color(5, 0, 127));
        } else if(text.equals("5")) {
            button.setForeground(new Color(128, 0, 0));
        } else if(text.equals("6")) {
            button.setForeground(new Color(0, 128, 128));
        } else if(text.equals("7")) {
            button.setForeground(new Color(0, 0, 0));
        } else if(text.equals("8")) {
            button.setForeground(new Color(128, 128, 128));
        }
        isOpened = true;
    }

    public void close() {
        isOpened = false;
        button.setText(" ");
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }

    public void setDiscovered(boolean isDiscovered) {
        this.isDiscovered = isDiscovered;
    }
}
