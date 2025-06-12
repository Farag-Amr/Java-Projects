import javax.swing.*;
import java.awt.*;

public class TicTacToe extends JFrame {
    private JButton[] buttons = new JButton[9];

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 60));
            add(buttons[i]);
        }
        setVisible(true);
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}