class BooleanStackElement extends StackElement<Boolean>implements Evaluable{
	public BooleanStackElement(boolean v){
		val=new Boolean(v);
	}
	public String toString(){
		return (new String(":"+val.toString()+":"));
	}
	public BooleanStackElement eval(Configuration c){return this;}
	public BooleanStackElement clone(){return new BooleanStackElement(val.booleanValue());}
}
