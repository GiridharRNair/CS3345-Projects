package gxn210004;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * Class to represent an expression and its evaluation using an expression tree.
 * It supports conversion between infix and postfix expressions and evaluates them.
 * Internal nodes of the tree represent operators, and leaf nodes represent operands.
 * Supported operators: +, *, -, /, %, ^.
 * Parentheses () are used for grouping operations.
 * Operands are represented by long integers.
 *
 * @author Giridhar Nair
 */
public class Expression {

    /**
     * Enum to represent the types of tokens in the expression.
     * Operators include PLUS, TIMES, MINUS, DIV, MOD, POWER,
     * parentheses are represented by OPEN and CLOSE,
     * NUMBER represents operands, and NIL is a special token.
     */
    public enum TokenType {
        PLUS,
        TIMES,
        MINUS,
        DIV,
        MOD,
        POWER,
        OPEN,
        CLOSE,
        NIL,
        NUMBER,
    }

    /**
     * Class to represent a token in an expression. A token can be an operator
     * or an operand (NUMBER).
     */
    public static class Token {

        TokenType token;
        int priority; // for precedence of operator
        Long number; // used to store number of token = NUMBER
        String string;

        /**
         * Constructor for an operator token.
         *
         * @param op The type of the operator token.
         * @param pri The priority of the operator.
         * @param tok The string representation of the operator.
         */
        Token(TokenType op, int pri, String tok) {
            token = op;
            priority = pri;
            number = null;
            string = tok;
        }

        /**
         * Constructor for a number token.
         *
         * @param tok The string representation of the number.
         */
        Token(String tok) {
            token = TokenType.NUMBER;
            number = Long.parseLong(tok);
            string = tok;
        }

        /**
         * Checks if the token is an operand (number).
         *
         * @return true if the token is a number, false otherwise.
         */
        boolean isOperand() {
            return token == TokenType.NUMBER;
        }

        /**
         * Returns the value of the token if it is an operand.
         *
         * @return The value of the number or 0 if not a number.
         */
        public long getValue() {
            return isOperand() ? number : 0;
        }

        /**
         * Returns the string representation of the token.
         *
         * @return The string representation of the token.
         */
        public String toString() {
            return string;
        }
    }

    Token element;
    Expression left, right;

    /**
     * Converts a string representation of a token to a Token object.
     * Supports operators: +, *, -, /, %, ^, and parentheses.
     *
     * @param tok The string representation of the token.
     * @return The corresponding Token object.
     */
    static Token getToken(String tok) {
        Token result;
        switch (tok) {
            case "+":
                result = new Token(TokenType.PLUS, 1, tok);
                break;
            case "*":
                result = new Token(TokenType.TIMES, 2, tok);
                break;
            case "-":
                result = new Token(TokenType.MINUS, 1, tok);
                break;
            case "/":
                result = new Token(TokenType.DIV, 2, tok);
                break;
            case "%":
                result = new Token(TokenType.MOD, 2, tok);
                break;
            case "^":
                result = new Token(TokenType.POWER, 3, tok);
                break;
            case "(":
                result = new Token(TokenType.OPEN, 0, tok);
                break;
            case ")":
                result = new Token(TokenType.CLOSE, 0, tok);
                break;
            default:
                result = new Token(tok);
                break;
        }
        return result;
    }

    /**
     * Private constructor to create an empty expression node.
     */
    private Expression() {
        element = null;
    }

    /**
     * Private constructor to create an expression node with an operator and its children.
     *
     * @param oper The operator token.
     * @param left The left subtree.
     * @param right The right subtree.
     */
    private Expression(Token oper, Expression left, Expression right) {
        this.element = oper;
        this.left = left;
        this.right = right;
    }

    /**
     * Private constructor to create a leaf expression node with a number token.
     *
     * @param num The number token.
     */
    private Expression(Token num) {
        this.element = num;
        this.left = null;
        this.right = null;
    }

    /**
     * Converts an infix expression represented as a list of tokens to an expression tree.
     *
     * @param exp The list of tokens representing the infix expression.
     * @return The expression tree corresponding to the infix expression.
     */
    public static Expression infixToExpression(List<Token> exp) {
        Deque<Expression> stack = new ArrayDeque<>();
        Deque<Token> operator = new ArrayDeque<>();

        for (Token token : exp) {
            if (token.isOperand()) {
                stack.push(new Expression(token));
            } else if (token.token == TokenType.OPEN) {
                operator.push(token);
            } else if (token.token == TokenType.CLOSE) {
                while (
                    !operator.isEmpty() &&
                    operator.peek().token != TokenType.OPEN
                ) {
                    Expression right = stack.pop();
                    Expression left = stack.pop();
                    stack.push(new Expression(operator.pop(), left, right));
                }
                operator.pop();
            } else {
                while (
                    !operator.isEmpty() &&
                    operator.peek().priority >= token.priority
                ) {
                    Expression right = stack.pop();
                    Expression left = stack.pop();
                    stack.push(new Expression(operator.pop(), left, right));
                }
                operator.push(token);
            }
        }

        while (!operator.isEmpty()) {
            Expression right = stack.pop();
            Expression left = stack.pop();
            stack.push(new Expression(operator.pop(), left, right));
        }

        return stack.pop();
    }

    /**
     * Converts an infix expression represented as a list of tokens to its equivalent postfix expression.
     *
     * @param exp The list of tokens representing the infix expression.
     * @return A list of tokens representing the postfix expression.
     */
    public static List<Token> infixToPostfix(List<Token> exp) {
        List<Token> result = new LinkedList<>();
        Queue<Token> output = new LinkedList<>();
        Deque<Token> stack = new ArrayDeque<>();

        for (Token token : exp) {
            if (token.isOperand()) {
                output.add(token);
            } else if (token.token == TokenType.OPEN) {
                stack.push(token);
            } else if (token.token == TokenType.CLOSE) {
                while (
                    !stack.isEmpty() && stack.peek().token != TokenType.OPEN
                ) {
                    output.add(stack.pop());
                }
                stack.pop();
            } else {
                while (
                    !stack.isEmpty() && stack.peek().priority >= token.priority
                ) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        while (!output.isEmpty()) {
            result.add(output.remove());
        }

        return result;
    }

    /**
     * Evaluates a postfix expression represented as a list of tokens.
     *
     * @param exp The list of tokens representing the postfix expression.
     * @return The evaluated result of the postfix expression.
     */
    public static long evaluatePostfix(List<Token> exp) {
        Deque<Long> stack = new ArrayDeque<>();
        for (Token token : exp) {
            if (token.isOperand()) {
                stack.push(token.getValue());
            } else {
                long right = stack.pop();
                long left = stack.pop();
                switch (token.token) {
                    case PLUS:
                        stack.push(left + right);
                        break;
                    case TIMES:
                        stack.push(left * right);
                        break;
                    case MINUS:
                        stack.push(left - right);
                        break;
                    case DIV:
                        stack.push(left / right);
                        break;
                    case MOD:
                        stack.push(left % right);
                        break;
                    case POWER:
                        stack.push((long) Math.pow(left, right));
                        break;
                }
            }
        }
        return stack.pop();
    }

    /**
     * Evaluates an expression tree and returns its value.
     *
     * @param tree The expression tree to evaluate.
     * @return The evaluated result of the expression tree.
     */
    public static long evaluateExpression(Expression tree) {
        if (tree.left == null && tree.right == null) {
            return tree.element.getValue();
        }

        long left = evaluateExpression(tree.left);
        long right = evaluateExpression(tree.right);
        return switch (tree.element.token) {
            case PLUS -> left + right;
            case TIMES -> left * right;
            case MINUS -> left - right;
            case DIV -> left / right;
            case MOD -> left % right;
            case POWER -> (long) Math.pow(left, right);
            default -> 0;
        };
    }

    /**
     * Main method to test the functionality of the Expression class.
     * Reads an infix expression from input, converts it to an expression tree,
     * prints the postfix expression, and evaluates both postfix and expression tree.
     *
     * @param args Command-line arguments, can be used to provide an input file.
     * @throws FileNotFoundException If the input file is not found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;

        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }

        int count = 0;
        while (in.hasNext()) {
            String s = in.nextLine();
            List<Token> infix = new LinkedList<>();
            Scanner sscan = new Scanner(s);
            int len = 0;
            while (sscan.hasNext()) {
                infix.add(getToken(sscan.next()));
                len++;
            }
            if (len > 0) {
                count++;
                System.out.println("Expression number: " + count);
                System.out.println("Infix expression: " + infix);
                Expression exp = infixToExpression(infix);
                List<Token> post = infixToPostfix(infix);
                System.out.println("Postfix expression: " + post);
                long pval = evaluatePostfix(post);
                long eval = evaluateExpression(exp);
                System.out.println(
                    "Postfix eval: " + pval + " Exp eval: " + eval + "\n"
                );
            }
        }
    }
}
