package org.proj.slidingpuzzle.game.controller;

import org.proj.slidingpuzzle.game.model.PuzzleModel;
import org.proj.slidingpuzzle.game.view.PuzzleView;

import javax.swing.*;
import java.util.List;
import java.util.stream.IntStream;
import java.awt.*;



public class PuzzleController {
    private final PuzzleModel model;
    private final PuzzleView view;
    private boolean isTimerStarted = false;

    public PuzzleController(PuzzleModel model, PuzzleView view) {
        this.model = model;
        this.view = view;
    }



    public void initController() {
        List<JButton> buttonList = view.getButtons();


        IntStream.range(0, buttonList.size())
                .forEach(i -> buttonList.get(i)
                        .addActionListener(e -> handleButtonClick(i)));

        view.getRearrangeButton().addActionListener(e -> {
            model.rearrangePuzzle();
            model.resetGame();
            view.update();
            view.updateMoves(0);
            view.updateScore(0);
            view.stopTimer();
            view.resetTimerDisplay();
            isTimerStarted = false;
        });


        view.getPauseButton().addActionListener(e -> view.togglePause());

        view.getConfigComboBox().addActionListener(e -> {
            String config = (String) view.getConfigComboBox().getSelectedItem();
            if (config != null) {
                model.loadCustomConfiguration(config);
                model.resetGame();
                view.update();
                view.updateMoves(0);
                view.updateScore(0);
                view.stopTimer();
                view.resetTimerDisplay();
                isTimerStarted = false;
            }

        });



        view.getShuffleButton().addActionListener(e -> {
            model.shuffle();
            model.resetGame();
            view.update();
            view.updateMoves(0);
            view.updateScore(0);
            view.stopTimer();
            view.resetTimerDisplay();
            isTimerStarted = false;
        });




    }



    private void handleButtonClick(int index) {

        if (view.isPaused()) return;

        if (!isTimerStarted) {
            view.startTimer();
            isTimerStarted = true;
        }

        int size = model.getSize();
        int sqrtSize = (int) Math.sqrt(size);
        int emptyTile = size - 1;
        boolean moved = false;
        int emptyPos = -1;

        if (index % sqrtSize != 0 && model.getTile(index - 1) == emptyTile) {
            emptyPos = index - 1;
            moved = true;
        } else if ((index + 1) % sqrtSize != 0 && model.getTile(index + 1) == emptyTile) {
            emptyPos = index + 1;
            moved = true;
        } else if (index >= sqrtSize && model.getTile(index - sqrtSize) == emptyTile) {
            emptyPos = index - sqrtSize;
            moved = true;
        } else if (index < size - sqrtSize && model.getTile(index + sqrtSize) == emptyTile) {
            emptyPos = index + sqrtSize;
            moved = true;
        }

        if (moved) {
            view.animateTileMove(index, emptyPos);
            model.swapTiles(index, emptyPos);

            model.incrementMoves();
            model.calculateScore();
            view.updateMoves(model.getMoves());
            view.updateScore(model.getScore());

            if (model.getScore() <= 0) {
                view.showGameOverDialog();
                return; // Empêche tout mouvement supplémentaire
            }

            if (model.isSolved()) {
                model.calculateScore();
                int score = model.getScore();
                model.updateBestScore(score);
                view.updateBestScore(model.getBestScore());
                view.stopTimer();
                int totalSeconds = view.getSecondsElapsed();
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;
                String formattedTime = String.format("%02d:%02d", minutes, seconds);

                JDialog winnerDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Victoire !", true);
                winnerDialog.setSize(400, 300);
                winnerDialog.setLocationRelativeTo(view);
                winnerDialog.setUndecorated(true);
                winnerDialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0x4A148C), 3));

                JPanel mainPanel = new JPanel(new BorderLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        GradientPaint gradient = new GradientPaint(
                                0, 0, new Color(0x8E24AA),
                                getWidth(), getHeight(), new Color(0xFFA000)
                        );
                        g2d.setPaint(gradient);
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                // Titre
                JLabel titleLabel = new JLabel("YOU WIN !", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
                titleLabel.setForeground(Color.WHITE);
                mainPanel.add(titleLabel, BorderLayout.NORTH);

                JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
                infoPanel.setOpaque(false);

                JLabel scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
                JLabel movesLabel = new JLabel("Moves: " + model.getMoves(), SwingConstants.CENTER);
                JLabel timeLabel = new JLabel("Time: " + formattedTime, SwingConstants.CENTER);

                Font infoFont = new Font("Arial", Font.BOLD, 18);
                scoreLabel.setFont(infoFont);
                movesLabel.setFont(infoFont);
                timeLabel.setFont(infoFont);
                scoreLabel.setForeground(Color.WHITE);
                movesLabel.setForeground(Color.WHITE);
                timeLabel.setForeground(Color.WHITE);

                infoPanel.add(scoreLabel);
                infoPanel.add(movesLabel);
                infoPanel.add(timeLabel);
                mainPanel.add(infoPanel, BorderLayout.CENTER);

                // Bouton OK
                JButton okButton = new JButton("OK");
                okButton.setFont(new Font("Arial", Font.BOLD, 16));
                okButton.setBackground(new Color(0x4CAF50));
                okButton.setForeground(Color.WHITE);
                okButton.setFocusPainted(false);
                okButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
                okButton.addActionListener(e -> winnerDialog.dispose());

                JPanel buttonPanel = new JPanel();
                buttonPanel.setOpaque(false);
                buttonPanel.add(okButton);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);

                winnerDialog.add(mainPanel);
                winnerDialog.setVisible(true);
            }
        }
    }

}

