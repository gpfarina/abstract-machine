class IntegerStackElement extends StackElement<Integer>implements Evaluable{
	public IntegerStackElement(int v){
		val=new Integer(v);
	}
	public String toString(){
		return Integer.toString(val);
	}
	public IntegerStackElement eval(Configuration c){return this;}
	public IntegerStackElement clone(){return new IntegerStackElement(val.intValue());}
}

