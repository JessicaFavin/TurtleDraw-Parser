public class ReadException extends Exception {
	protected Sym expected;
	protected Token found;

    public ReadException(Sym expected, Token found) {
		super("expected "+expected+", found "+found);
		this.expected=expected;
		this.found=found;
    }
	
	public Sym getExpected(){
		return expected;
	}
	
	public Token getFound(){
		return found;
	}

}
