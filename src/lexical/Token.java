package lexical;

public class Token {

    private final TokenType tokenType;
    private String lexeme;
    private int line;

    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Token(TokenType tokenType, String lexeme) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return tokenType + ":  " + lexeme + "   Line=" + line;
    }
}
