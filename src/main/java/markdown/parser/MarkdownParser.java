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

        // count number of #
        while (match(TokenType.HASH)) {
            level++;
        }

        HeadingNode heading = new HeadingNode(level);

        boolean firstText = true;

        while (!check(TokenType.NEWLINE) && !isAtEnd()) {

            Token token = advance();

            if (token.getType() == TokenType.TEXT) {

                String value = token.getValue();

                // remove space after ###
                if (firstText) {
                    value = value.stripLeading();
                    firstText = false;
                }

                heading.addChild(new TextNode(value));
            }
        }

        consumeNewline();

        return heading;
    }

    private ParagraphNode parseParagraph() {

        ParagraphNode paragraph = new ParagraphNode();

        while (!check(TokenType.NEWLINE) && !isAtEnd()) {

            Node inline = parseInline();

            if (inline != null) {
                paragraph.addChild(inline);
            }
        }

        consumeNewline();

        return paragraph;
    }

    private Node parseInline() {

        if (check(TokenType.STAR)) {

            if (checkNext(TokenType.STAR)) {
                return parseBold();
            }

            return parseItalic();
        }

        if (check(TokenType.BACKTICK)) {
            return parseCode();
        }

        return parseText();
    }

    private BoldNode parseBold() {

        // consume **
        advance();
        advance();

        BoldNode bold = new BoldNode();

        while (!check(TokenType.STAR) && !isAtEnd()) {

            bold.addChild(parseText());
        }

        // consume **
        advance();
        advance();

        return bold;
    }

    private ItalicNode parseItalic() {

        advance(); // consume *

        ItalicNode italic = new ItalicNode();

        while (!check(TokenType.STAR) && !isAtEnd()) {

            italic.addChild(parseText());
        }

        advance(); // consume *

        return italic;
    }

    private CodeNode parseCode() {

        advance(); // consume `

        CodeNode code = new CodeNode();

        while (!check(TokenType.BACKTICK) && !isAtEnd()) {

            code.addChild(parseText());
        }

        advance(); // consume `

        return code;
    }

    private TextNode parseText() {

        Token token = advance();

        return new TextNode(token.getValue());
    }

    // helper methods

    private boolean checkNext(TokenType type) {

        if (position + 1 >= tokens.size()) return false;

        return tokens.get(position + 1).getType() == type;
    }

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