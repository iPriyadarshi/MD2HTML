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

    /*
        BLOCK PARSING
     */

    private Node parseBlock() {

        if (check(TokenType.TRIPLE_BACKTICK)) {
            return parseCodeBlock();
        }

        if (check(TokenType.HORIZONTAL_RULE)) {
            return parseHorizontalRule();
        }

        if (check(TokenType.HASH)) {
            return parseHeading();
        }

        if (check(TokenType.NUMBER) && checkNext(TokenType.DOT)) {
            return parseOrderedList();
        }

        if (check(TokenType.DASH) || check(TokenType.STAR)) {
            return parseList();
        }

        if (check(TokenType.TEXT) || check(TokenType.OPEN_BRACKET) || check(TokenType.STAR) || check(TokenType.BACKTICK)) {
            return parseParagraph();
        }

        advance();

        return null;
    }

    /*
        HORIZONTAL RULE
     */

    private HorizontalRuleNode parseHorizontalRule() {

        advance(); // consume HORIZONTAL_RULE

        consumeNewline();

        return new HorizontalRuleNode();
    }

    /*
        UNORDERED LIST
     */

    private ListNode parseList() {

        ListNode list = new ListNode();

        while (check(TokenType.DASH) || check(TokenType.STAR)) {
            advance(); // consume '-' or '*'

            ListItemNode item = new ListItemNode();

            boolean firstText = true;

            while (!check(TokenType.NEWLINE) && !isAtEnd()) {

                Node inline = parseInline();

                if (inline instanceof TextNode textNode && firstText) {

                    String trimmed = textNode.getText().stripLeading();

                    item.addChild(new TextNode(trimmed));

                    firstText = false;
                } else if (inline != null) {

                    item.addChild(inline);
                }
            }

            consumeNewline();

            list.addChild(item);
        }

        return list;
    }

    /*
        ORDERED LIST
     */

    private OrderedListNode parseOrderedList() {

        OrderedListNode list = new OrderedListNode();

        while (check(TokenType.NUMBER) && checkNext(TokenType.DOT)) {

            advance(); // number
            advance(); // dot

            ListItemNode item = new ListItemNode();

            boolean firstText = true;

            while (!check(TokenType.NEWLINE) && !isAtEnd()) {

                Node inline = parseInline();

                if (inline instanceof TextNode textNode && firstText) {

                    item.addChild(new TextNode(textNode.getText().stripLeading()));

                    firstText = false;
                } else if (inline != null) {

                    item.addChild(inline);
                }
            }

            consumeNewline();

            list.addChild(item);
        }

        return list;
    }

    /*
        CODE BLOCK
     */

    private CodeBlockNode parseCodeBlock() {

        advance(); // consume ```

        String language = "";

        if (!check(TokenType.NEWLINE) && !isAtEnd()) {

            language = advance().getValue().strip();
        }

        consumeNewline();

        CodeBlockNode codeBlock = new CodeBlockNode(language);

        while (!check(TokenType.TRIPLE_BACKTICK) && !isAtEnd()) {

            Token token = advance();

            if (token.getType() == TokenType.NEWLINE) {

                codeBlock.addChild(new TextNode("\n"));
            } else {

                codeBlock.addChild(new TextNode(token.getValue()));
            }
        }

        advance(); // closing ```

        consumeNewline();

        return codeBlock;
    }

    /*
        HEADING
     */

    private HeadingNode parseHeading() {

        int level = 0;

        while (match(TokenType.HASH)) {
            level++;
        }

        HeadingNode heading = new HeadingNode(level);

        boolean firstText = true;

        while (!check(TokenType.NEWLINE) && !isAtEnd()) {

            Token token = advance();

            if (token.getType() == TokenType.TEXT) {

                String value = token.getValue();

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

    /*
        PARAGRAPH
     */

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

    /*
        INLINE PARSING
     */

    private Node parseInline() {

        if (check(TokenType.OPEN_BRACKET)) {
            return parseLink();
        }

        if (check(TokenType.STAR)) {

            // bold
            if (checkNext(TokenType.STAR)) {
                return parseBold();
            }

            // italic
            return parseItalic();
        }

        if (check(TokenType.BACKTICK)) {
            return parseCode();
        }

        return parseText();
    }

    /*
        LINK
     */

    private LinkNode parseLink() {

        advance(); // [

        StringBuilder text = new StringBuilder();

        while (!check(TokenType.CLOSE_BRACKET) && !isAtEnd()) {

            text.append(advance().getValue());
        }

        advance(); // ]

        advance(); // (

        StringBuilder url = new StringBuilder();

        while (!check(TokenType.CLOSE_PAREN) && !isAtEnd()) {

            url.append(advance().getValue());
        }

        advance(); // )

        LinkNode link = new LinkNode(url.toString());

        link.addChild(new TextNode(text.toString()));

        return link;
    }

    /*
        BOLD (supports nested inline)
     */

    private BoldNode parseBold() {

        advance(); // *
        advance(); // *

        BoldNode bold = new BoldNode();

        while (!(check(TokenType.STAR) && checkNext(TokenType.STAR)) && !isAtEnd()) {

            Node inline = parseInline();

            if (inline != null) {
                bold.addChild(inline);
            }
        }

        advance(); // *
        advance(); // *

        return bold;
    }

    /*
        ITALIC (supports nested inline)
     */

    private ItalicNode parseItalic() {

        advance(); // *

        ItalicNode italic = new ItalicNode();

        while (!check(TokenType.STAR) && !isAtEnd()) {

            Node inline = parseInline();

            if (inline != null) {
                italic.addChild(inline);
            }
        }

        advance(); // *

        return italic;
    }

    /*
        INLINE CODE
     */

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

    /*
        HELPERS
     */

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