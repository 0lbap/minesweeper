package org.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;

public class MainFrame extends JFrame {
    JLabel welcomeLabel;
    JLabel selectDifficultyLabel;
    JPanel difficultyPanel;
    ButtonGroup difficultyButtonGroup;
    JRadioButton beginnerRadioButton;
    JRadioButton intermediateRadioButton;
    JRadioButton expertRadioButton;
    JPanel customPanel;
    JRadioButton customRadioButton;
    JFormattedTextField customTilesHTextField;
    JLabel customTilesXLabel;
    JFormattedTextField customTilesVTextField;
    JLabel customNBombsLabel;
    JFormattedTextField customNBombsTextField;
    JButton createGameButton;

    public MainFrame() {
        setTitle("Minesweeper");
        setSize(300, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        welcomeLabel = new JLabel("Welcome to Minesweeper!");
        welcomeLabel.setFont(new Font("Sans", Font.PLAIN, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(10, 0, 10, 0);
        add(welcomeLabel, c);

        selectDifficultyLabel = new JLabel("Select your difficulty:");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.insets = new Insets(0, 0, 10, 0);
        add(selectDifficultyLabel, c);

        difficultyButtonGroup = new ButtonGroup();
        difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new BoxLayout(difficultyPanel, BoxLayout.Y_AXIS));

        beginnerRadioButton = new JRadioButton("Beginner");
        beginnerRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        beginnerRadioButton.setSelected(true);
        difficultyButtonGroup.add(beginnerRadioButton);
        difficultyPanel.add(beginnerRadioButton);

        intermediateRadioButton = new JRadioButton("Intermediate");
        intermediateRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        difficultyButtonGroup.add(intermediateRadioButton);
        difficultyPanel.add(intermediateRadioButton);

        expertRadioButton = new JRadioButton("Expert");
        expertRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        difficultyButtonGroup.add(expertRadioButton);
        difficultyPanel.add(expertRadioButton);

        customPanel = new JPanel();
        customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.X_AXIS));
        customPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        customRadioButton = new JRadioButton("Custom:");
        difficultyButtonGroup.add(customRadioButton);
        customPanel.add(customRadioButton);

        customTilesHTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        customTilesHTextField.setColumns(2);
        customTilesHTextField.setValue(9);
        customPanel.add(customTilesHTextField);

        customTilesXLabel = new JLabel("x");
        customPanel.add(customTilesXLabel);

        customTilesVTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        customTilesVTextField.setColumns(2);
        customTilesVTextField.setValue(9);
        customPanel.add(customTilesVTextField);

        customNBombsLabel = new JLabel(", bombs=");
        customPanel.add(customNBombsLabel);

        customNBombsTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        customNBombsTextField.setColumns(3);
        customNBombsTextField.setValue(10);
        customPanel.add(customNBombsTextField);

        difficultyPanel.add(customPanel);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        add(difficultyPanel, c);

        createGameButton = new JButton("Create Game");
        createGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MinesweeperDifficulty difficulty;
                if(beginnerRadioButton.isSelected()) difficulty = MinesweeperDifficulty.BEGINNER;
                else if(intermediateRadioButton.isSelected()) difficulty = MinesweeperDifficulty.INTERMEDIATE;
                else if(expertRadioButton.isSelected()) difficulty = MinesweeperDifficulty.EXPERT;
                else if(customRadioButton.isSelected()) difficulty = MinesweeperDifficulty.CUSTOM;
                else difficulty = MinesweeperDifficulty.BEGINNER;
                setVisible(false);
                createGame(difficulty);
            }
        });
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        add(createGameButton, c);

        setVisible(true);
    }

    public void createGame(MinesweeperDifficulty difficulty) {
        int tilesH, tilesV, nBombs;
        switch (difficulty) {
            case BEGINNER: {
                tilesH = 9;
                tilesV = 9;
                nBombs = 10;
                break;
            }
            case INTERMEDIATE: {
                tilesH = 16;
                tilesV = 16;
                nBombs = 40;
                break;
            }
            case EXPERT: {
                tilesH = 30;
                tilesV = 16;
                nBombs = 99;
                break;
            }
            case CUSTOM: {
                try {
                    customTilesHTextField.commitEdit();
                } catch (ParseException e) {
                    customTilesHTextField.setValue(9);
                }
                try {
                    customTilesVTextField.commitEdit();
                } catch (ParseException e) {
                    customTilesVTextField.setValue(9);
                }
                try {
                    customNBombsTextField.commitEdit();
                } catch (ParseException e) {
                    customNBombsTextField.setValue(10);
                }
                tilesH = ((Long)customTilesHTextField.getValue()).intValue();
                tilesV = ((Long)customTilesVTextField.getValue()).intValue();
                nBombs = ((Long)customNBombsTextField.getValue()).intValue();

                // Clamping the values (the minimum grid is 3x3)
                tilesH = Math.min(99, Math.max(3, tilesH));
                tilesV = Math.min(99, Math.max(3, tilesV));
                nBombs = Math.min(tilesH*tilesV, Math.max(0, nBombs));
                break;
            }
            default: {
                tilesH = 9;
                tilesV = 9;
                nBombs = 10;
                break;
            }
        }
        MinesweeperFrame game = new MinesweeperFrame(tilesH, tilesV, nBombs, this);
    }
}
