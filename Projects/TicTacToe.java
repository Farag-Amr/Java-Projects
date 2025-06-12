package Projects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[9];
    private boolean xTurn = true;
    private JLabel turnLabel;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 60));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(this);
            boardPanel.add(buttons[i]);
        }
        add(boardPanel, BorderLayout.CENTER);

        turnLabel = new JLabel("X's Turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        add(turnLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();

        if (!btn.getText().equals(""))
            return;

        btn.setText(xTurn ? "X" : "O");

        int[] winIndices = checkWin();
        if (winIndices != null) {
            for (int idx : winIndices) {
                buttons[idx].setBackground(Color.ORANGE);
                buttons[idx].setOpaque(true);
            }
            turnLabel.setText((xTurn ? "X" : "O") + " wins!");
            disableButtons(winIndices); // Only disable non-winning buttons
        } else if (isBoardFull()) {
            turnLabel.setText("Draw!");
            disableButtons(null); // Disable all buttons on draw
        } else {
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

    public static void main(String[] args) {
        new TicTacToe();
    }
}