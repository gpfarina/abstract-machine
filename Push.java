class Push extends Command{
	private StackElement stel;
	public Push(StackElement stel){this.stel=stel;}
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()){
			conf.stks.peek().push(stel);
		}
		else{
			throw new Exception("Empty stack of stacks! This should not happen");
		}
		return conf;
	}

}
