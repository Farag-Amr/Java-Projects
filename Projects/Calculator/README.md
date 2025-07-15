# Java Swing Calculator

A robust calculator application built with Java Swing, following the MVC (Model-View-Controller) design pattern.  
This project demonstrates clean separation of concerns, a modular parsing and evaluation pipeline, and a user-friendly interface.

---

## ✨ Features

- Classic calculator operations: addition, subtraction, multiplication, division, exponentiation
- Parentheses support with smart input logic
- Handles negative numbers and decimals
- Error handling for invalid input and division by zero
- Clear, delete, and smart parentheses button
- Responsive and resizable Swing UI

---

## 🏗️ Application Structure

The project is organized as follows:

```text
CalculatorApp.java                      // Application entry point
CalculatorModel.java                    // Model: holds state and business logic
CalculatorView.java                     // View: Swing UI
CalculatorController.java               // Controller: connects UI and logic

Parser/                                 // Parsing and evaluation pipeline
    Lexer.java                          // Tokenizes input strings
    Token.java                          // Represents a single token
    TokenType.java                      // Enum for token types
    ShuntingYardParser.java             // Converts infix to postfix (RPN)
    ReversePolishNotationEvaluator.java // Evaluates RPN expressions
```

---

## 🧠 Algorithms Used

### 1. **Lexer (Tokenizer)**

- Converts the user's input string into a list of tokens (numbers, operators, parentheses).
- Handles unary plus and minus for negative numbers.

### 2. **Shunting Yard Algorithm**

- Converts infix expressions (e.g., `3 + 4 * 2 / (1 - 5) ^ 2 ^ 3`) into postfix (Reverse Polish Notation, RPN).
- Correctly applies operator precedence and associativity (PEMDAS rules).
- Supports parentheses and all calculator operators.

### 3. **Reverse Polish Notation (RPN) Evaluation**

- Evaluates the postfix token list using a stack.
- Handles all supported operators and checks for errors (e.g., division by zero).

---

## 🧩 MVC Pattern

- **Model (`CalculatorModel`)**  
  Manages the calculator's state, processes input, validates expressions, and performs evaluation using the parsing pipeline.

- **View (`CalculatorView`)**  
  Displays the calculator UI using Java Swing. Forwards user actions (button presses) to the controller.

- **Controller (`CalculatorController`)**  
  Handles user input, updates the model, and refreshes the view with the latest state.

- **App (`CalculatorApp`)**  
  Wires together the model, view, and controller, and starts the application.

---

## 🚦 How It Works

1. **User enters input** via the calculator buttons.
2. **Controller** receives the input and passes it to the **Model**.
3. **Model**:
   - Tokenizes the input (`Lexer`)
   - Converts tokens to RPN (`ShuntingYardParser`)
   - Evaluates the result (`ReversePolishNotationEvaluator`)
   - Handles validation and error states
4. **View** displays the updated expression or result.

---

## 📦 How to Run

1. Clone the repository:
   ```sh
   git clone https://github.com/Farag-Amr/Java-Projects.git
   ```
