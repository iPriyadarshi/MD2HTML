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
                    addToken(TokenType.BACKTICK, "`");
                    advance();
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
                    addToken(TokenType.NEWLINE, "\\n");
                    advance();
                    break;

                case ' ':
                case '\t':
                    advance();
                    break;

                default:
                    tokenizeText();
            }
        }

        tokens.add(new Token(TokenType.EOF, "", position));

        return tokens;
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