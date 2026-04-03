package markdown.lexer;

import java.util.ArrayList;
import java.util.List;

public class MarkdownLexer implements Lexer {

    private String input;
    private int position;
    private List<Token> tokens;

    @Override
    public List<Token> tokenize(String input) {

        this.input = input;
        this.position = 0;
        this.tokens = new ArrayList<>();

        while (!isAtEnd()) {
            // Detect horizontal rule at line start
            if (isStartOfLine() && isHorizontalRuleHere()) {

                tokens.add(new Token(TokenType.HORIZONTAL_RULE, "", position));

                // move until newline
                while (!isAtEnd() && peek() != '\n') {
                    advance();
                }

                continue;
            }

            char current = peek();

            switch (current) {

                case '#':
                    addToken(TokenType.HASH, "#");
                    advance();
                    break;

                case '-':
                    addToken(TokenType.DASH, "-");
                    advance();
                    break;

                case '*':
                    addToken(TokenType.STAR, "*");
                    advance();
                    break;

                case '`':
                    if (matchTripleBacktick()) {
                        addToken(TokenType.TRIPLE_BACKTICK, "```");
                        position += 3;
                    } else {
                        addToken(TokenType.BACKTICK, "`");
                        advance();
                    }
                    break;

                case '[':
                    addToken(TokenType.OPEN_BRACKET, "[");
                    advance();
                    break;

                case ']':
                    addToken(TokenType.CLOSE_BRACKET, "]");
                    advance();
                    break;

                case '(':
                    addToken(TokenType.OPEN_PAREN, "(");
                    advance();
                    break;

                case ')':
                    addToken(TokenType.CLOSE_PAREN, ")");
                    advance();
                    break;

                case '\n':
                    addToken(TokenType.NEWLINE, "\n");
                    advance();
                    break;

                case '.':
                    addToken(TokenType.DOT, ".");
                    advance();
                    break;

                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                    tokenizeNumber();
                    break;

                default:
                    tokenizeText();
            }
        }

        tokens.add(new Token(TokenType.EOF, "", position));

        return tokens;
    }

    private boolean isHorizontalRuleHere() {
        int temp = position;
        char symbol = 0;
        int count = 0;

        while (temp < input.length()) {

            char c = input.charAt(temp);

            if (c == '\n') break;

            if (c == ' ') {
                temp++;
                continue;
            }

            if (c == '-' || c == '_' || c == '*') {
                if (symbol == 0) {
                    symbol = c;
                }

                if (c != symbol) {
                    return false;
                }

                count++;
                temp++;
                continue;
            }
            return false;
        }
        return count >= 3;
    }

    private boolean isStartOfLine() {

        return position == 0 || input.charAt(position - 1) == '\n';
    }

    private boolean matchTripleBacktick() {

        return position + 2 < input.length() && input.charAt(position) == '`' && input.charAt(position + 1) == '`' && input.charAt(position + 2) == '`';
    }

    private void tokenizeNumber() {

        int start = position;

        while (!isAtEnd() && Character.isDigit(peek())) {
            advance();
        }

        String number = input.substring(start, position);

        tokens.add(new Token(TokenType.NUMBER, number, start));
    }

    private void tokenizeText() {

        int start = position;

        while (!isAtEnd() && isTextChar(peek())) {
            advance();
        }

        String text = input.substring(start, position);

        tokens.add(new Token(TokenType.TEXT, text, start));
    }

    private boolean isTextChar(char c) {

        return c != '#' && c != '-' && c != '*' && c != '`' && c != '[' && c != ']' && c != '(' && c != ')' && c != '\n';
    }

    private boolean isAtEnd() {
        return position >= input.length();
    }

    private char peek() {
        return input.charAt(position);
    }

    private void advance() {
        position++;
    }

    private void addToken(TokenType type, String value) {
        tokens.add(new Token(type, value, position));
    }
}