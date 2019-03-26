class Pop extends Command{
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()){
			if(conf.stks.peek().size()>=1){
				conf.stks.peek().pop();
				return conf;
			}
			else{
				conf.stks.peek().push(new ErrorStackElement());
				return conf;
			}
		}
		else{
			throw new Exception("Empty stack of stacks! This should not happen");
		}
	}
}
