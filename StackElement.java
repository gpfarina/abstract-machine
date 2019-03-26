class StackElement<T> implements Serializable{
		T val;
		public void writeToFile(BufferedWriter bw){
			try{		
				bw.write(this.toString()+"\n");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		public T getValue(){return val;}
		public void setValue(T t){val=t;}
		public StackElement<T> clone(){
			try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		    ObjectOutputStream out = new ObjectOutputStream(bos);
        		out.writeObject(this);
   	    	 	ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
    	    	ObjectInputStream in = new ObjectInputStream(bis);
        		T copied = (T) in.readObject();
        		StackElement<T> stcopied=new StackElement<T>();
        		stcopied.setValue(copied);
        		return stcopied;
        	}catch(Exception e){
        		e.printStackTrace();
        		System.out.println("Error in deep-copying StackElement");
        		System.exit(-1);
        	}
        	return null;
		}
}
