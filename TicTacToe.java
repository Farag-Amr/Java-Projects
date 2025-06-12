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
        if (checkWin()) {
            turnLabel.setText((xTurn ? "X" : "O") + " wins!");
            disableButtons();
        } else if (isBoardFull()) {
            turnLabel.setText("Draw!");
        } else {
            xTurn = !xTurn;
            turnLabel.setText((xTurn ? "X" : "O") + "'s Turn");
        }
    }

    private boolean checkWin() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = buttons[i].getText();
        }
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].equals("") &&
                    board[i][0].equals(board[i][1]) &&
                    board[i][1].equals(board[i][2]))
                return true;
            if (!board[0][i].equals("") &&
                    board[0][i].equals(board[1][i]) &&
                    board[1][i].equals(board[2][i]))
                return true;
        }
        // Check diagonals
        if (!board[0][0].equals("") &&
                board[0][0].equals(board[1][1]) &&
                board[1][1].equals(board[2][2]))
            return true;
        if (!board[0][2].equals("") &&
                board[0][2].equals(board[1][1]) &&
                board[1][1].equals(board[2][0]))
            return true;
        return false;
    }

    private boolean isBoardFull() {
        for (JButton button : buttons) {
            if (button.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void disableButtons() {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}