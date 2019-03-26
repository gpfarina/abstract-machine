class Return extends Command{
	protected Configuration setStackEnvs(Configuration conf) throws Exception{
		if(conf.stks.size()>1 && conf.envs.size()>1){
			if (!conf.stks.peek().isEmpty() && !conf.envs.peek().isEmpty()){
				StackElement top=conf.stks.peek().pop();
				if (top instanceof NameStackElement){
					top=((NameStackElement) top).eval(conf);
				}
				conf.stks.pop();
				conf.stks.peek().push(top);
				conf.envs.pop();
			}
			else{
				conf.stks.pop();
				conf.envs.pop();
				conf.stks.peek().push(new ErrorStackElement());
			}
		}else{
			throw new Exception("Stack of stacks with less than 2 elements or Stack of stack of envs with less than 2 elements: you can't return!");
		}
		return conf;
	} 
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		return setStackEnvs(conf);
	}
	public Return(){super();}
}
