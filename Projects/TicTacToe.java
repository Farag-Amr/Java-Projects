package Projects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[9];
    private JButton playAgainButton;
    private boolean xTurn = true;
    private JLabel turnLabel;

    // Menu components
    private JPanel menuPanel;
    private JButton startButton;
    private JButton colorsButton;
    private JPanel gamePanel;
    private JButton mainMenuButton;
    private JPanel colorsMenuPanel;

    private Color xColor = Color.BLACK;
    private Color oColor = Color.BLACK;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLayout(new BorderLayout());

        // Main menu panel setup
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(255, 200, 60)); // Bright yellow-orange background

        JLabel titleLabel = new JLabel("TicTacToe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = new JButton("Play");
        startButton.setFont(new Font("Arial", Font.BOLD, 48));
        startButton.setBackground(Color.GREEN);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> showGamePanel());

        colorsButton = new JButton("Colors");
        colorsButton.setFont(new Font("Arial", Font.BOLD, 36));
        colorsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        colorsButton.addActionListener(e -> showColorsMenu());

        // Rainbow icon for the Colors button
        colorsButton.setIcon(new Icon() {
            @Override
            public int getIconWidth() {
                return 60;
            }

            @Override
            public int getIconHeight() {
                return 36;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                int w = getIconWidth();
                int h = getIconHeight();
                Color[] rainbow = {
                        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
                };
                float[] fractions = {
                        0f, 1f / 6f, 2f / 6f, 3f / 6f, 4f / 6f, 5f / 6f, 1f
                };
                LinearGradientPaint paint = new LinearGradientPaint(
                        x, y, x + w, y, fractions, rainbow);
                g2.setPaint(paint);
                g2.fillRect(x, y, w, h);
                g2.dispose();
            }
        });

        // Add spacing and buttons to the menu
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        menuPanel.add(startButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        menuPanel.add(colorsButton);
        menuPanel.add(Box.createVerticalGlue());

        add(menuPanel, BorderLayout.CENTER);

        // Game panel setup
        gamePanel = new JPanel(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 60));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(this);
            boardPanel.add(buttons[i]);
        }
        gamePanel.add(boardPanel, BorderLayout.CENTER);

        turnLabel = new JLabel("X's Turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        gamePanel.add(turnLabel, BorderLayout.SOUTH);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 28));
        playAgainButton.setBackground(Color.GREEN);
        playAgainButton.setOpaque(true);
        playAgainButton.setVisible(false);
        playAgainButton.addActionListener(e -> resetBoard());

        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 28));
        mainMenuButton.setBackground(Color.LIGHT_GRAY);
        mainMenuButton.setOpaque(true);
        mainMenuButton.setVisible(false);
        mainMenuButton.addActionListener(e -> showMainMenu());

        // Panel for Play Again and Main Menu buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        playAgainButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        mainMenuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        playAgainButton.setPreferredSize(new Dimension(0, 60));
        mainMenuButton.setPreferredSize(new Dimension(0, 60));

        buttonPanel.add(playAgainButton);
        buttonPanel.add(mainMenuButton);
        buttonPanel.setOpaque(false);

        gamePanel.add(buttonPanel, BorderLayout.NORTH);
        gamePanel.add(boardPanel, BorderLayout.CENTER);
        gamePanel.add(turnLabel, BorderLayout.SOUTH);

        // Colors menu panel setup
        colorsMenuPanel = new JPanel(null);
        colorsMenuPanel.setBackground(new Color(255, 200, 60));

        // Large X in top left with outline
        JLabel xLabel = new JLabel("X") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setFont(getFont());
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                String text = getText();
                FontMetrics fm = g2.getFontMetrics();
                int x = 0;
                int y = fm.getAscent();
                // Draw black outline
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(4));
                g2.drawString(text, x + 1, y + 1);
                g2.drawString(text, x - 1, y - 1);
                g2.drawString(text, x + 1, y - 1);
                g2.drawString(text, x - 1, y + 1);
                // Draw the X in the selected color
                g2.setColor(getForeground());
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        xLabel.setFont(new Font("Arial", Font.BOLD, 160));
        xLabel.setForeground(Color.BLACK);
        xLabel.setBounds(40, 40, 180, 180);
        colorsMenuPanel.add(xLabel);

        // Dropdown menu for X
        String[] xOptions = { "Black", "Red", "Blue", "Green", "Orange", "Purple", "Pink" };
        JComboBox<String> xDropdown = new JComboBox<>(xOptions);
        xDropdown.setFont(new Font("Arial", Font.PLAIN, 28));
        xDropdown.setBounds(40, 230, 180, 50);
        colorsMenuPanel.add(xDropdown);

        // Change X color when a new color is selected
        xDropdown.addActionListener(e -> {
            String selected = (String) xDropdown.getSelectedItem();
            Color color = getColorFromName(selected);
            xLabel.setForeground(color);
            xColor = color;
        });

        // Large O in top right with outline
        JLabel oLabel = new JLabel("O") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setFont(getFont());
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                String text = getText();
                FontMetrics fm = g2.getFontMetrics();
                int x = 0;
                int y = fm.getAscent();
                // Draw black outline
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(4));
                g2.drawString(text, x + 1, y + 1);
                g2.drawString(text, x - 1, y - 1);
                g2.drawString(text, x + 1, y - 1);
                g2.drawString(text, x - 1, y + 1);
                // Draw the O in the selected color
                g2.setColor(getForeground());
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        oLabel.setFont(new Font("Arial", Font.BOLD, 160));
        oLabel.setForeground(Color.BLACK);
        oLabel.setBounds(780, 40, 180, 180);
        colorsMenuPanel.add(oLabel);

        // Dropdown menu for O
        String[] oOptions = { "Black", "Red", "Blue", "Green", "Orange", "Purple", "Pink" };
        JComboBox<String> oDropdown = new JComboBox<>(oOptions);
        oDropdown.setFont(new Font("Arial", Font.PLAIN, 28));
        oDropdown.setBounds(780, 230, 180, 50);
        colorsMenuPanel.add(oDropdown);

        // Change O color when a new color is selected
        oDropdown.addActionListener(e -> {
            String selected = (String) oDropdown.getSelectedItem();
            Color color = getColorFromName(selected);
            oLabel.setForeground(color);
            oColor = color;
        });

        // Back button to return to main menu
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 32));
        backButton.setBounds(400, 800, 200, 60);
        backButton.addActionListener(e -> showMainMenu());
        colorsMenuPanel.add(backButton);

        setVisible(true);
    }

    // Shows the game panel
    private void showGamePanel() {
        remove(menuPanel);
        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Shows the colors menu
    private void showColorsMenu() {
        remove(menuPanel);
        remove(gamePanel);
        add(colorsMenuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Shows the main menu
    private void showMainMenu() {
        remove(gamePanel);
        remove(colorsMenuPanel);
        add(menuPanel, BorderLayout.CENTER);
        mainMenuButton.setVisible(false);
        playAgainButton.setVisible(false);
        resetBoard();
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        // Get the button that was clicked
        JButton btn = (JButton) e.getSource();

        // Ignore if the button already has a value
        if (!btn.getText().equals(""))
            return;

        // Set the button text to X or O depending on whose turn it is
        btn.setText(xTurn ? "X" : "O");

        // Set the color of the button text based on the selected color from the menu
        btn.setForeground(xTurn ? xColor : oColor);

        // Check if someone has won after this move
        int[] winIndices = checkWin();
        if (winIndices != null) {
            // Highlight the winning buttons in orange
            for (int idx : winIndices) {
                buttons[idx].setBackground(Color.ORANGE);
                buttons[idx].setOpaque(true);
            }
            // Show who won in the label
            turnLabel.setText((xTurn ? "X" : "O") + " wins!");
            // Disable all non-winning buttons
            disableButtons(winIndices);
            // Show the play again button and Main Menu button
            playAgainButton.setVisible(true);
            mainMenuButton.setVisible(true);
        } else if (isBoardFull()) {
            // If the board is full and no one won, it's a draw
            turnLabel.setText("Draw!");
            // Disable all buttons
            disableButtons(null);
            // Show the play again button and Main Menu button
            playAgainButton.setVisible(true);
            mainMenuButton.setVisible(true);
        } else {
            // Switch turns and update the label
            xTurn = !xTurn;
            turnLabel.setText((xTurn ? "X" : "O") + "'s Turn");
        }
    }

    private int[] checkWin() {
        // Build a 2D array of the board's current state
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = buttons[i].getText();
        }
        // Check all rows for a win
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].equals("") &&
                    board[i][0].equals(board[i][1]) &&
                    board[i][1].equals(board[i][2])) {
                return new int[] { i * 3, i * 3 + 1, i * 3 + 2 };
            }
        }
        // Check all columns for a win
        for (int i = 0; i < 3; i++) {
            if (!board[0][i].equals("") &&
                    board[0][i].equals(board[1][i]) &&
                    board[1][i].equals(board[2][i])) {
                return new int[] { i, i + 3, i + 6 };
            }
        }
        // Check the main diagonal for a win
        if (!board[0][0].equals("") &&
                board[0][0].equals(board[1][1]) &&
                board[1][1].equals(board[2][2])) {
            return new int[] { 0, 4, 8 };
        }
        // Check the anti-diagonal for a win
        if (!board[0][2].equals("") &&
                board[0][2].equals(board[1][1]) &&
                board[1][1].equals(board[2][0])) {
            return new int[] { 2, 4, 6 };
        }
        // No winner found
        return null;
    }

    private boolean isBoardFull() {
        for (JButton button : buttons) {
            if (button.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void disableButtons(int[] winIndices) {
        if (winIndices == null) {
            // Disable all buttons if it's a draw
            for (JButton button : buttons) {
                button.setEnabled(false);
            }
            return;
        }
        // Disable only non-winning buttons
        for (int i = 0; i < buttons.length; i++) {
            boolean isWinning = false;
            for (int idx : winIndices) {
                if (i == idx) {
                    isWinning = true;
                    break;
                }
            }
            if (!isWinning) {
                buttons[i].setEnabled(false);
            }
        }
    }

    private void resetBoard() {
        // Reset all buttons to empty, enabled, and remove any color
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
            button.setBackground(null);
            button.setOpaque(true);
        }
        // Set turn back to X and update the label
        xTurn = true;
        turnLabel.setText("X's Turn");
        playAgainButton.setVisible(false);
        mainMenuButton.setVisible(false);
    }

    // Returns a Color object based on the color name
    private Color getColorFromName(String name) {
        switch (name) {
            case "Black":
                return Color.BLACK;
            case "Red":
                return Color.RED;
            case "Blue":
                return Color.BLUE;
            case "Green":
                return Color.GREEN;
            case "Orange":
                return Color.ORANGE;
            case "Purple":
                return new Color(128, 0, 128);
            case "Pink":
                return Color.PINK;
            default:
                return Color.BLACK;
        }
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}