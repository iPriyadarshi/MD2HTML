package markdown.parser;

import markdown.lexer.Token;
import markdown.lexer.TokenType;
import markdown.parser.nodes.*;

import java.util.List;

public class MarkdownParser implements Parser {

    private List<Token> tokens;
    private int position;

    @Override
    public DocumentNode parse(List<Token> tokens) {

        this.tokens = tokens;
        this.position = 0;

        DocumentNode document = new DocumentNode();

        while (!isAtEnd()) {

            Node block = parseBlock();

            if (block != null) {
                document.addChild(block);
            }
        }

        return document;
    }

    private Node parseBlock() {

        if (check(TokenType.HASH)) {
            return parseHeading();
        }

        if (check(TokenType.TEXT)) {
            return parseParagraph();
        }

        // skip unexpected tokens
        advance();

        return null;
    }

    private HeadingNode parseHeading() {

        int level = 0;

        // count number of # symbols
        while (match(TokenType.HASH)) {
            level++;
        }

        HeadingNode heading = new HeadingNode(level);

        // heading text
        while (!check(TokenType.NEWLINE) && !isAtEnd()) {

            Token token = advance();

            if (token.getType() == TokenType.TEXT) {

                heading.addChild(new TextNode(token.getValue()));
            }
        }

        consumeNewline();

        return heading;
    }

    private ParagraphNode parseParagraph() {

        ParagraphNode paragraph = new ParagraphNode();

        while (!check(TokenType.NEWLINE) && !isAtEnd()) {

            Token token = advance();

            if (token.getType() == TokenType.TEXT) {

                paragraph.addChild(new TextNode(token.getValue()));
            }
        }

        consumeNewline();

        return paragraph;
    }

    // helper methods

    private boolean match(TokenType type) {

        if (check(type)) {
            advance();
            return true;
        }

        return false;
    }

    private boolean check(TokenType type) {

        if (isAtEnd()) return false;

        return peek().getType() == type;
    }

    private Token advance() {

        if (!isAtEnd()) {
            position++;
        }

        return previous();
    }

    private boolean isAtEnd() {

        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {

        return tokens.get(position);
    }

    private Token previous() {

        return tokens.get(position - 1);
    }

    private void consumeNewline() {

        if (check(TokenType.NEWLINE)) {
            advance();
        }
    }
}