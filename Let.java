class Let extends Command{
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()&&!conf.envs.isEmpty()){
			conf.stks.push(new Stack<StackElement>());
			HashMap<String, StackElement> h=new HashMap<String, StackElement>(); 
			conf.envs.peek().push(h);
		}
		else{
			throw new Exception("Empty stack of stacks or stack of stack of envs! This should not happen -- Let --");
		}
		return conf;
	}
}
