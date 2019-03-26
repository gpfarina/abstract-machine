class ReturnIO extends Return{
	NameStackElement farg, aarg;
	public ReturnIO(){super();}
	public ReturnIO(NameStackElement farg, NameStackElement aarg){this.farg=farg; this.aarg=aarg;}
 	public Configuration exec(Configuration conf) throws Exception{
 		conf.program.get(0).remove(0);
 		StackElement val=null;
 		if (conf.stks.peek().size()>0){
 			val=((Evaluable) farg).eval(conf);
 			Configuration c=super.setStackEnvs(conf);
 			c.envs.peek().peek().remove(aarg.toString());
 			c.envs.peek().peek().put(aarg.toString(), val);
 			return c;
 		}
 		else{
 			conf.envs.peek().pop();
 			conf.envs.peek().peek().put(aarg.toString(), new ErrorStackElement());
 		}
 	 	return conf;
 	}
}
