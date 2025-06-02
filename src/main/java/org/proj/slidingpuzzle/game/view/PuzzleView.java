package org.proj.slidingpuzzle.game.view;

import org.proj.slidingpuzzle.game.model.PuzzleModel;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class PuzzleView extends JFrame {
    private final List<JButton> buttons = new ArrayList<>();
    private final PuzzleModel model;
    private final JLabel scoreLabel;
    private final JLabel moveLabel = new JLabel("Moves : 0");
    private final JButton rearrangeButton = new JButton("REARRANGE");
    private final JButton shuffleButton = new JButton("SHUFFLE");
    private final JLabel bestScoreLabel = new JLabel("Best score : 0");
    private Timer timer;
    private int secondsElapsed = 0;
    private final JLabel timerLabel = new JLabel("00:00:00");
    private final JButton pauseButton = new JButton("II");
    private boolean isPaused = false;

    public PuzzleView(PuzzleModel model) {
        this.model = model;
        int gridSize = (int) Math.sqrt(model.getSize());

        setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize, 6, 6));

        initButtons(gridPanel);
        scoreLabel = new JLabel("Score : 0");

        customizeComboBoxRenderer();

        JPanel gridContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gridPanel.setPreferredSize(new Dimension(340, 340));
        gridPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 240, 201)),
                BorderFactory.createEmptyBorder(10, 10, 13, 10)
        ));
        gridContainer.setBorder(BorderFactory.createEmptyBorder(3, 20, 10, 20));
        gridContainer.add(gridPanel);

        gridContainer.setBackground(Color.WHITE);
        add(gridContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(13, 10, 13, 10));
        bottomPanel.add(shuffleButton);
        bottomPanel.add(rearrangeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        Font buttonFont = new Font("Forte", Font.BOLD, 18);
        Color buttonBackground = new Color(3, 100, 168);
        Color buttonTextColor = Color.WHITE;

        initPauseButton();

        shuffleButton.setFont(buttonFont);
        shuffleButton.setBackground(buttonBackground);
        shuffleButton.setForeground(buttonTextColor);
        shuffleButton.setFocusPainted(false);
        shuffleButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        shuffleButton.setFont(new Font("ALGERIAN", Font.BOLD, 18));
        shuffleButton.setPreferredSize(new Dimension(200, 40));

        rearrangeButton.setFont(buttonFont);
        rearrangeButton.setBackground(new Color(0, 126, 53));
        rearrangeButton.setForeground(buttonTextColor);
        rearrangeButton.setFocusPainted(false);
        rearrangeButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 20));
        rearrangeButton.setFont(new Font("ALGERIAN", Font.BOLD, 18));
        rearrangeButton.setPreferredSize(new Dimension(200, 40));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(3, 20, 3, 16));

        bottomPanel.add(pauseButton);

        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<div style='font-size: 50px; font-weight: bold; color: black; font-family: Pristina;'>Play Game</div>" +
                "<div style='font-size: 18px; color: #333; font-family: MV Boli;'>Make best moves in minimum time to make best score</div>" +
                "</div></html>", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(1));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.add(timerLabel);


        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(moveLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(scoreLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(bestScoreLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        JLabel configLabel = new JLabel("Configuration :");
        configLabel.setFont(new Font("FORTE", Font.BOLD, 14));
        infoPanel.add(configLabel);

        infoPanel.add(configComboBox);

        moveLabel.setFont(new Font("Forte", Font.BOLD, 18));
        bestScoreLabel.setFont(new Font("Forte", Font.BOLD, 18));
        scoreLabel.setFont(new Font("Forte", Font.BOLD, 18));
        configLabel.setFont(new Font("Forte", Font.BOLD, 18));
        configLabel.setForeground(Color.DARK_GRAY);

        configComboBox.setFont(new Font("Forte", Font.BOLD, 16));
        configComboBox.setBackground(new Color(255, 168, 0));
        configComboBox.setForeground(Color.DARK_GRAY);
        configComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 100, 0), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        configComboBox.setPreferredSize(new Dimension(180, 35));
        configComboBox.setFocusable(false);
        configComboBox.addActionListener(e -> {
            String selectedConfig = (String) configComboBox.getSelectedItem();
            if (selectedConfig != null) {
                model.loadCustomConfiguration(selectedConfig);
                update();
            }
        });

        topPanel.add(timerPanel);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, getHeight() / 2 - 2, getWidth(), 2);
            }
        };
        separator.setPreferredSize(new Dimension(400, 6));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        topPanel.add(separator);
        separator.setForeground(Color.LIGHT_GRAY);
        topPanel.add(infoPanel);

        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 16));
        add(topPanel, BorderLayout.NORTH);

        setSize(820, 695);
        setLocationRelativeTo(null);

        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/puzzle.png"))).getImage());
        setSize(820, 695);
        setVisible(true);
        getContentPane().setBackground(Color.WHITE);

        pauseButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        pauseButton.setFocusPainted(false);

        timerLabel.setFont(new Font("MV Boli", Font.BOLD, 18));
        configComboBox.setFont(new Font("Forte", Font.ITALIC, 12));
        configComboBox.setBackground(new Color(255, 168, 0));

        moveLabel.setForeground(new Color(33, 33, 33));
        scoreLabel.setForeground(new Color(33, 33, 33));
        bestScoreLabel.setForeground(new Color(33, 33, 33));
        timerLabel.setForeground(new Color(0, 102, 204));
    }




    public void updateBestScore(int bestScore) {
        bestScoreLabel.setText("Best score : " + bestScore);
    }

    private void initButtons(JPanel gridPanel) {
        int size = model.getSize();
        IntStream.range(0, size).forEach(i -> {
            JButton button = createButton();
            buttons.add(button);
            gridPanel.add(button);
            gridPanel.setBackground(new Color(255, 240, 201));
        });
        update();
    }

    public static class StyledComboBox extends JComboBox<String> {
        public StyledComboBox(String[] items) {
            super(items);
            setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton button = new JButton("▼");
                    button.setFont(new Font("Arial", Font.BOLD, 14));
                    button.setForeground(Color.WHITE);
                    button.setContentAreaFilled(false);
                    button.setBorder(BorderFactory.createEmptyBorder());
                    button.setFocusPainted(false);
                    return button;
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
        }
    }

    private void initPauseButton() {
        pauseButton.setFont(new Font("Segue UI Symbol", Font.BOLD, 22));
        pauseButton.setBackground(new Color(255, 165, 0));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 6, 15));
        pauseButton.setFocusPainted(false);
        pauseButton.putClientProperty("FlatLaf.flat", true);
    }


    private JButton createButton() {
        JButton button = new RoundedButton("");
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFont(new Font("Script MT Bold", Font.ITALIC, 40));
        button.setBackground(Color.decode("#f0f0f0"));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        return button;
    }

    public static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setBackground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(255, 240, 201));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            g2.dispose();
        }
    }

    public void startTimer() {
        if (timer != null && timer.isRunning()) return;

        timer = new Timer(1000, e -> {
            secondsElapsed++;
            updateTimerDisplay();
        });
        timer.start();
        updateTimerDisplay();
    }

    private void updateTimerDisplay() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText(String.format("00:%02d:%02d", minutes, seconds));
    }

    public void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            pauseButton.setText("▶");
            stopTimer();
        } else {
            pauseButton.setText("II");
            startTimer();
        }
        pauseButton.repaint();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void customizeComboBoxRenderer() {
        configComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("MV Boli", Font.BOLD, 14));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBackground(isSelected ? new Color(255, 140, 0) : new Color(255, 168, 0));
                label.setForeground(Color.WHITE);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public void update() {
        int size = model.getSize();
        Color correctColor = new Color(26, 200, 134);
        Color incorrectColor = new Color(52, 138, 152);

        for (int i = 0; i < model.getSize(); i++) {
            int value = model.getTile(i);
            JButton button = buttons.get(i);

            if (value == model.getSize() - 1) {
                button.setText("");
                button.setBackground(Color.darkGray);
            } else {
                button.setText(String.valueOf(value + 1));
                button.setBackground(model.isTileCorrect(i) ?
                        new Color(26, 200, 134) : new Color(52, 138, 152));
            }
        }
        revalidate();
        repaint();
        IntStream.range(0, size).forEach(i -> {
            JButton button = buttons.get(i);
            int tile = model.getTile(i);
            if (tile != size - 1) {
                button.setText(String.valueOf(tile + 1));
                button.setBackground(model.isTileCorrect(i) ? correctColor : incorrectColor);
            } else {
                button.setText("");
                button.setBackground(Color.darkGray);
            }
        });
    }

    public List<JButton> getButtons() {
        return buttons;
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    private final JComboBox<String> configComboBox = new StyledComboBox(new String[] {
            "073214568", "124857063", "204153876", "624801753", "670132584", "781635240", "280163547"
    });

    public JComboBox<String> getConfigComboBox() {
        return configComboBox;
    }

    public JButton getRearrangeButton() {
        return rearrangeButton;
    }

    public JButton getShuffleButton() {
        return shuffleButton;
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void updateMoves(int moves) {
        moveLabel.setText("Moves : " + moves);
    }

    public void resetTimerDisplay() {
        secondsElapsed = 0;
        timerLabel.setText("00:00:00");
    }



    public void showGameOverDialog() {
        stopTimer();

        JDialog gameOverDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Game Over", true);
        gameOverDialog.setSize(400, 300);
        gameOverDialog.setLocationRelativeTo(this);
        gameOverDialog.setUndecorated(true);
        gameOverDialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0x8E24AA), 3));

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0xD32F2F),
                        getWidth(), getHeight(), new Color(0x7B1FA2)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titleLabel = new JLabel("GAME OVER!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Message
        JLabel messageLabel = new JLabel("Score atteint 0!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setForeground(Color.WHITE);
        mainPanel.add(messageLabel, BorderLayout.CENTER);

        // Boutons
        JButton tryAgainButton = new JButton("Try Again");
        tryAgainButton.setFont(new Font("Arial", Font.BOLD, 16));
        tryAgainButton.setBackground(new Color(0x4CAF50));
        tryAgainButton.setForeground(Color.WHITE);
        tryAgainButton.addActionListener(e -> {
            model.shuffle();
            resetGame();
            gameOverDialog.dispose();
        });

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 16));
        quitButton.setBackground(new Color(0xF44336));
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(tryAgainButton);
        buttonPanel.add(quitButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        gameOverDialog.add(mainPanel);
        gameOverDialog.setVisible(true);
    }

    private void resetGame() {
        model.resetGame();
        updateMoves(0);
        updateScore(0);
        stopTimer();
        resetTimerDisplay();
        update();
    }

    public void animateTileMove(int fromIndex, int toIndex) {
        JButton movingButton = buttons.get(fromIndex);
        Point start = movingButton.getLocation();
        Point end = buttons.get(toIndex).getLocation();

        Timer animationTimer = new Timer(10, new ActionListener() {
            final int steps = 20;
            int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStep >= steps) {
                    ((Timer)e.getSource()).stop();
                    movingButton.setLocation(end);
                    update();
                    return;
                }

                float ratio = (float)currentStep / steps;
                int x = (int)(start.x + (end.x - start.x) * ratio);
                int y = (int)(start.y + (end.y - start.y) * ratio);

                movingButton.setLocation(x, y);
                currentStep++;
            }
        });
        animationTimer.start();
    }
}

