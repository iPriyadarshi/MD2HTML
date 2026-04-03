package markdown.lexer;

public enum TokenType {

    // structure
    HASH,          // #
    DASH,          // -
    NEWLINE,

    // text
    TEXT,

    // inline formatting
    STAR,          // *
    BACKTICK,      // `
    OPEN_BRACKET,  // [
    CLOSE_BRACKET, // ]
    OPEN_PAREN,    // (
    CLOSE_PAREN,   // )

    EOF
}