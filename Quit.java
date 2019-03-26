class Quit extends Command{
	private String oFile;
	public Quit(String str){this.oFile=str;}
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()){
			//We assume all the let are matched with their end so there should be only one external stack to write
			ArrayList<StackElement> alist = new ArrayList<StackElement>(conf.stks.pop());
			ListIterator<StackElement> itr_st=alist.listIterator(alist.size());
    		try{
    			File file = new File(oFile);
    			FileWriter fw = new FileWriter(file);
	  			BufferedWriter bw = new BufferedWriter(fw);
    			while(itr_st.hasPrevious()) {
    				itr_st.previous().writeToFile(bw);
    			}
    			bw.close();
    		}
    		catch(Exception e){
				e.printStackTrace();
    		}
			return conf;
		}
		else{
			throw new Exception("Empty stack of stacks! This should not happen");
		}
	}
}
