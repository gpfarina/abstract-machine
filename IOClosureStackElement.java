class IOClosureStackElement extends StackElement<Closure> implements Evaluable{
	public IOClosureStackElement(NameStackElement f, ArrayList<Command> code, Stack<HashMap<String,StackElement>> e){
		setValue(new Closure(f,code,e));
	}
	public IOClosureStackElement eval(Configuration c){return this;}
		public IOClosureStackElement clone(){try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		    ObjectOutputStream out = new ObjectOutputStream(bos);
        		out.writeObject(this);
   	    	 	ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
    	    	ObjectInputStream in = new ObjectInputStream(bis);
        		IOClosureStackElement copied = (IOClosureStackElement) in.readObject();
        		return copied;
        	}catch(Exception e){
        		e.printStackTrace();
        		System.out.println("Error in deep-copying StackElement");
        		System.exit(-1);
        	}
        	return null;}
}
