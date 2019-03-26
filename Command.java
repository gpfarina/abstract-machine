abstract class Command implements Serializable{
	public Configuration exec(Configuration conf) throws Exception{return conf;};
	public Command clone(){
			try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		    ObjectOutputStream out = new ObjectOutputStream(bos);
        		out.writeObject(this);
   	    	 	ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
    	    	ObjectInputStream in = new ObjectInputStream(bis);
        		Command copied = (Command) in.readObject();
        		return copied;
        	}catch(Exception e){
        		e.printStackTrace();
        		System.out.println("Error in deep-copying Command");
        		System.exit(-1);
        	}
        	return null;
		}
	static Stack<HashMap<String, StackElement>> copyEnvs(Stack<HashMap<String, StackElement>> env0){
		Stack<HashMap<String, StackElement>> env1=new Stack<HashMap<String, StackElement>>();
		for(HashMap<String, StackElement> t: env0){
        	HashMap<String, StackElement> ct=new HashMap<String, StackElement>();
        	for(String s: t.keySet()){
        		ct.put(new String(s), t.get(s).clone());
        	}
        	env1.push(ct);
        }
        return env1;
    }
}
