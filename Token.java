class Token {
    protected Sym symbol;
    public Token(Sym s) {
    	symbol=s;
    }
    public Sym symbol() {
    	return symbol;
    }
    public boolean isEqual(Token t){
    	return (t.symbol !=this.symbol);
    }
    public String toString(){
    	return "Symbol : "+this.symbol;
    }
}

class IntToken extends Token {
	private double value;
    public IntToken(Sym s, double v){
		super(s);
		value=v;
	}
	public double getValue(){
		return this.value;
	}
    public String toString(){
    	return "Symbol : "+this.symbol+ " value : "+this.value;
    }
}

class VarToken extends Token {
	private String value;
    public VarToken(Sym s, String v){
		super(s);
		value=v;
	}
	public String getValue(){
		return this.value;
	}
    public String toString(){
    	return "Symbol : "+this.symbol+ " value : "+this.value;
    }
}
