public class LexerException extends Exception {
	public LexerException(int line, int column) {
		super("Unexpected character at line " + line + " column " + column + ".");
	}
}