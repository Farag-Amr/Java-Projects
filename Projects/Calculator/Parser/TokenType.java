// Lists all possible token types for the calculator.
package Projects.Calculator.Parser;

public enum TokenType {
    NUMBER, // e.g., 3.14
    OPERATOR, // e.g., +, -, x, /, ^
    LEFT_PAREN, // (
    RIGHT_PAREN, // )
    FUNCTION; // For future extensions like sin, cos

    // Helper to check if this is an operator
    public boolean isOperator() {
        return this == OPERATOR;
    }
}
