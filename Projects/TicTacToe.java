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

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLayout(new BorderLayout());

        // --- MENU PANEL ---
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(255, 200, 60)); // Orangeish yellow

        JLabel titleLabel = new JLabel("TicTacToe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = new JButton("Play");
        startButton.setFont(new Font("Arial", Font.BOLD, 48));
        startButton.setBackground(Color.GREEN);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> showGamePanel());

        // Adds a simple icon with a rainbow image for the Colors button
        colorsButton = new JButton("Colors");
        colorsButton.setFont(new Font("Arial", Font.BOLD, 36));
        colorsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sets a rainbow gradient icon to the left of the text
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

        // Add vertical spacing and buttons to menu
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        menuPanel.add(startButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        menuPanel.add(colorsButton);
        menuPanel.add(Box.createVerticalGlue());

        add(menuPanel, BorderLayout.CENTER);

        // --- GAME PANEL ---
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

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Expands buttons to fill the width of the panel
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

        setVisible(true);
    }

    // Show the game panel and hide the menu
    private void showGamePanel() {
        remove(menuPanel);
        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Show the main menu
    private void showMainMenu() {
        remove(gamePanel);
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

        // If the button already has a value, ignore the click
        if (!btn.getText().equals(""))
            return;

        // Set the button text to X or O depending on whose turn it is
        btn.setText(xTurn ? "X" : "O");

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
            // Disable all buttons (for a draw)
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
        // Reset all buttons to starting position. Empty, enabled and remove any color
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
            button.setBackground(null);
            button.setOpaque(true);
        }
        // Set turn back to X and update the label
        xTurn = true;
        turnLabel.setText("X's Turn");
        playAgainButton.setVisible(false);// Hide the play again button until the next game ends
        mainMenuButton.setVisible(false); // Hide mainMenuButton as well
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}