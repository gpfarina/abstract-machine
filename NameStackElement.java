class NameStackElement extends StackElement<String>implements Evaluable{
	public NameStackElement(String str){
		val=str;
	}
	public String toString(){
		return val;
	}	
	public NameStackElement clone(){return new NameStackElement(new String(val));}
	public StackElement eval(Configuration c) throws Exception{
		StackElement rtr=null;
		boolean found=false;
		if(c.envs!=null){
			Stack<Stack<HashMap<String, StackElement>>> envsXTmp=new Stack<Stack<HashMap<String,StackElement>>>();
			while(!c.envs.isEmpty()&&!found){
				Stack<HashMap<String, StackElement>> envs=c.envs.pop();
				envsXTmp.push(envs);
				Stack<HashMap<String, StackElement>> envsTmp=new Stack<HashMap<String,StackElement>>();
				if(envs!=null){
					while(!envs.isEmpty() && !found){
						HashMap<String, StackElement> env=envs.pop();
						envsTmp.push(env);
						if(env.containsKey(this.toString())){
							found=true;
							rtr=env.get(this.toString());
						}
					}
					while(!envsTmp.empty()){envs.push(envsTmp.pop());}
				}
			}
			while(!envsXTmp.isEmpty()){c.envs.push(envsXTmp.pop());}	
		}
		else{
			throw new Exception("Null stack of stack of envs");
		}
		if (rtr==null){ throw new AException(c);}
		return rtr;
		}	
}
