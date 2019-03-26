class HiddenIO extends Return{
	private NameStackElement farg, aarg;
	public HiddenIO(NameStackElement f, NameStackElement a){this.farg=f; this.aarg=a;}
	public Configuration exec(Configuration conf) throws Exception{ 
		conf.program.get(0).remove(0);

		StackElement val=null;
 		if (conf.stks.size()>0){
 			val=((Evaluable) farg).eval(conf);
 			conf.stks.pop();
 			conf.envs.pop();
 			conf.envs.peek().peek().remove(aarg.toString());
 			conf.envs.peek().peek().put(aarg.toString(), val);
 			return conf;
 		}
 		else{

 			conf.envs.peek().pop();
 		}
 	 	return conf;
	}
}
