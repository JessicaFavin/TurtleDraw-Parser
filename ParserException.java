public class ParserException extends Exception {


    public ParserException(String s) {
		super(s);
    }

	@Override
	public String getLocalizedMessage(){
		return ""+super.getLocalizedMessage();
	}
}
