package Projects.Calculator.Parser;

import java.util.List;
import java.util.Stack;

/**
 * Evaluates a postfix/RPN expression (List<Token>).
 */
public class ReversePolishNotationEvaluator {
    public static double evaluate(List<Token> postfixTokens) {
        Stack<Double> stack = new Stack<>();

        for (Token token : postfixTokens) {
            if (token.getType() == TokenType.NUMBER) {
                // Push numbers onto the stack
                stack.push(Double.parseDouble(token.getValue()));
            } else if (token.getType() == TokenType.OPERATOR) {
                // Pop two numbers and apply the operator
                if (stack.size() < 2) {
                    throw new IllegalArgumentException(
                            "Invalid expression: not enough operands for operator " + token.getValue());
                }
                double right = stack.pop();
                double left = stack.pop();
                double result = 0;

                switch (token.getValue()) {
                    case "+":
                        result = left + right;
                        break;
                    case "-":
                        result = left - right;
                        break;
                    case "x":
                        result = left * right;
                        break;
                    case "÷":
                        if (right == 0)
                            throw new ArithmeticException("Division by zero");
                        result = left / right;
                        break;
                    case "^":
                        result = Math.pow(left, right);
                        break;
                }
                stack.push(result);
            }
        }

        // There should be exactly one result left
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: too many operands.");
        }
        return stack.pop();
    }
}
