class StringStackElement extends StackElement<String>implements Evaluable{
	public StringStackElement(String v){
		val=v;
	}
	public String toString(){
		String tmp=new String(val);
		return tmp.replace("\"", "");
	}
	public StringStackElement eval(Configuration c){return this;}
	public StringStackElement clone(){return new StringStackElement(new String(val));}
}
