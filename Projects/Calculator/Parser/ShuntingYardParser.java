package Projects.Calculator.Parser;

// Converts infix tokens (like 3 + 4) to postfix (RPN) for easier evaluation.
import java.util.*;

/**
 * Implements the Shunting-Yard algorithm to convert an infix expression
 * (List<Token>) into a postfix/RPN expression (List<Token>).
 */
public class ShuntingYardParser {
    private final List<Token> infixTokens;
    // Operator precedence and associativity
    private static final Map<String, Integer> PRECEDENCE = new HashMap<>();
    private static final Map<String, Boolean> RIGHT_ASSOCIATIVE = new HashMap<>();

    static {
        // Precedence levels
        PRECEDENCE.put("+", 2);
        PRECEDENCE.put("-", 2);
        PRECEDENCE.put("x", 3);
        PRECEDENCE.put("÷", 3);
        PRECEDENCE.put("^", 4);

        // Associativity (only ^ is right-associative)
        RIGHT_ASSOCIATIVE.put("^", true);
    }

    public ShuntingYardParser(List<Token> infixTokens) {
        this.infixTokens = infixTokens;
    }

    // Converts infix to postfix (RPN)
    public List<Token> parse() {
        List<Token> outputQueue = new ArrayList<>();
        Stack<Token> operatorStack = new Stack<>();

        for (Token token : infixTokens) {
            switch (token.getType()) {
                case NUMBER:
                    outputQueue.add(token);
                    break;
                case OPERATOR:
                    // Pop operators with higher or equal precedence
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() == TokenType.OPERATOR &&
                            hasLowerPrecedence(token, operatorStack.peek())) {
                        outputQueue.add(operatorStack.pop());
                    }
                    operatorStack.push(token);
                    break;
                case LEFT_PAREN:
                    operatorStack.push(token);
                    break;
                case RIGHT_PAREN:
                    // Pop until left parenthesis
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() != TokenType.LEFT_PAREN) {
                        outputQueue.add(operatorStack.pop());
                    }
                    if (operatorStack.isEmpty()) {
                        throw new IllegalArgumentException("Mismatched parentheses.");
                    }
                    operatorStack.pop(); // Pop the left parenthesis
                    break;
            }
        }

        // Pop any remaining operators
        while (!operatorStack.isEmpty()) {
            Token op = operatorStack.pop();
            if (op.getType() == TokenType.LEFT_PAREN) {
                throw new IllegalArgumentException("Mismatched parentheses.");
            }
            outputQueue.add(op);
        }

        return outputQueue;
    }

    // Checks operator precedence and associativity
    private boolean hasLowerPrecedence(Token op1, Token op2) {
        int p1 = PRECEDENCE.get(op1.getValue());
        int p2 = PRECEDENCE.get(op2.getValue());
        boolean isRightAssoc = RIGHT_ASSOCIATIVE.getOrDefault(op1.getValue(), false);

        if (isRightAssoc) {
            return p1 < p2;
        }
        return p1 <= p2;
    }
}
