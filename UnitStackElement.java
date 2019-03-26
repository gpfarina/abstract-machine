class UnitStackElement extends StackElement<String> implements Evaluable{
	public UnitStackElement(){val=new String(":unit:");}
		public String toString(){
		return val;
	}
	public UnitStackElement eval(Configuration c){return this;}
}
