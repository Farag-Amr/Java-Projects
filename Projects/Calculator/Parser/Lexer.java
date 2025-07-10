package Projects.Calculator.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts an input string into a list of Tokens using a manual parser.
 * This approach avoids regular expressions and provides more control.
 */
public class Lexer {

    /**
     * The main public method to tokenize an expression.
     * It performs a two-pass process:
     * 1. A raw tokenization of numbers and operators.
     * 2. A processing pass to correctly identify and merge unary operators.
     *
     * @param expression The mathematical expression string.
     * @return A list of tokens ready for the Shunting-Yard parser.
     */
    public static List<Token> tokenize(String expression) {
        // First, perform a raw tokenization of the string
        List<Token> rawTokens = manualTokenize(expression.replace(" ", ""));
        // Second, process the raw tokens to handle unary operators
        return processUnaryOperators(rawTokens);
    }

    /**
     * Iterates through the expression character by character to create a raw list
     * of tokens.
     * It does not distinguish between binary and unary operators.
     */
    private static List<Token> manualTokenize(String expression) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                boolean hasDecimal = false;
                // Loop to consume all characters of a number (digits and one decimal point)
                while (i < expression.length()) {
                    char current = expression.charAt(i);
                    if (Character.isDigit(current)) {
                        number.append(current);
                    } else if (current == '.' && !hasDecimal) {
                        number.append(current);
                        hasDecimal = true;
                    } else {
                        // Found a character that is not part of the number, so break
                        break;
                    }
                    i++;
                }
                tokens.add(new Token(TokenType.NUMBER, number.toString()));
                continue; // Skip the i++ at the end of the outer loop
            } else if (c == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "("));
            } else if (c == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")"));
            } else if ("+-x÷^".indexOf(c) != -1) {
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
            } else {
                throw new IllegalArgumentException("Unknown character in expression: " + c);
            }
            i++;
        }
        return tokens;
    }

    /**
     * Processes a raw token list to correctly handle unary plus and minus.
     * A '-' or '+' is unary if it's at the start of the expression or follows an
     * operator or left parenthesis.
     */
    private static List<Token> processUnaryOperators(List<Token> rawTokens) {
        List<Token> processedTokens = new ArrayList<>();
        for (int i = 0; i < rawTokens.size(); i++) {
            Token currentToken = rawTokens.get(i);

            // We only care about operators that could be unary: '+' and '-'
            if (currentToken.getType() == TokenType.OPERATOR &&
                    (currentToken.getValue().equals("-") || currentToken.getValue().equals("+"))) {

                boolean isUnary = (i == 0); // It's unary if it's the first token
                if (!isUnary) {
                    Token prevToken = rawTokens.get(i - 1);
                    // It's also unary if it follows another operator or a left parenthesis
                    if (prevToken.getType() == TokenType.OPERATOR || prevToken.getType() == TokenType.LEFT_PAREN) {
                        isUnary = true;
                    }
                }

                if (isUnary) {
                    // Ensure there's a number token immediately after the unary operator
                    if (i + 1 < rawTokens.size() && rawTokens.get(i + 1).getType() == TokenType.NUMBER) {
                        Token numberToken = rawTokens.get(i + 1);
                        if (currentToken.getValue().equals("-")) {
                            // Merge the '-' with the number token
                            processedTokens.add(new Token(TokenType.NUMBER, "-" + numberToken.getValue()));
                        } else {
                            // For unary '+', we can just add the number token itself
                            processedTokens.add(numberToken);
                        }
                        i++; // Increment i to skip the number token we just processed
                    } else {
                        // This is a syntax error (e.g., "5 * - / 2"), add the operator and let the
                        // parser handle the error
                        processedTokens.add(currentToken);
                    }
                } else {
                    // It's a binary operator, add it as is
                    processedTokens.add(currentToken);
                }
            } else {
                // Not a potential unary operator, so just add the token to the list
                processedTokens.add(currentToken);
            }
        }
        return processedTokens;
    }
}
