package org.proj.slidingpuzzle.game.view;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import org.proj.slidingpuzzle.game.model.PuzzleModel;
import org.proj.slidingpuzzle.game.controller.PuzzleController;

public class StartMenu extends JFrame {
    public StartMenu() {
        setTitle("Sliding Puzzle - Menu Principal");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0x4A148C), 3));
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/puzzle.png"))).getImage());

        setSize(800, 600);
        setVisible(true);

        JPanel mainPanel = new JPanel() {
            final Image bgImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/jee.jpg"))).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g;


                g2d.setColor(new Color(0xBD702AF8, true)); // Violet plus clair
                g2d.fillRect(0, 0, getWidth(), getHeight());


                if (bgImage != null) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                    g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }


                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.CENTER;


        JLabel title = new JLabel("<html><center>SLIDING PUZZLE<br><span style='font-size:30px'>WELCOME</span></center></html>", SwingConstants.CENTER);
        title.setFont(new Font("Algerian", Font.BOLD, 53));
        title.setBackground(new Color(0xAB00BF));
        title.setForeground(new Color(0xFFFFFF)); // Blanc pur


        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        gbc.gridy = 0;
        mainPanel.add(titlePanel, gbc);

        JButton startButton = createMenuButton("START", new Color(0, 150, 136), new Color(0, 200, 150));
        JButton exitButton = createMenuButton("QUIT", new Color(229, 57, 53), new Color(244, 81, 30));


        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);

        JPanel buttonContainer = new JPanel(new GridBagLayout());
        buttonContainer.setOpaque(false);
        buttonContainer.add(buttonPanel);

        gbc.gridy = 1;
        mainPanel.add(buttonContainer, gbc);


        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> System.exit(0));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(closeButton);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Actions
        startButton.addActionListener(e -> {
            dispose();
            startGame();
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton createMenuButton(String text, Color topColor, Color bottomColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                GradientPaint gradient = new GradientPaint(
                        0, 0, topColor,
                        0, getHeight(), bottomColor
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);


                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);

                
                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(300, 90));
        button.setFont(new Font("Verdana", Font.BOLD, 28));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void startGame() {
        SwingUtilities.invokeLater(() -> {
            PuzzleModel model = new PuzzleModel(3, 3);
            PuzzleView view = new PuzzleView(model);
            PuzzleController controller = new PuzzleController(model, view);
            controller.initController();
        });
        dispose();
    }

}