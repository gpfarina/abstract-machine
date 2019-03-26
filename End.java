class End extends Command{
	public Configuration exec(Configuration conf)throws Exception{
		conf.program.get(0).remove(0);
		if(conf.stks.size()>=1 && conf.envs.size()>=1 && conf.envs.peek().size()>=1) {
			StackElement stel;
			if (!conf.stks.peek().isEmpty()){stel=conf.stks.peek().pop();} else{stel=new ErrorStackElement();}
			conf.stks.pop();
			conf.envs.peek().pop();
			conf.stks.peek().push(stel);
		}
		else{
			throw new Exception(" -- End --");
		}
		return conf;
	}	
}
