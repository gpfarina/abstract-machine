class Bind extends Command {
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()&&!conf.envs.isEmpty()&&!conf.envs.peek().isEmpty()){
			if(conf.stks.peek().size()>=2){
				StackElement stel1 = conf.stks.peek().pop();
				StackElement stel2 = conf.stks.peek().pop();
				if (stel2 instanceof NameStackElement && stel1 instanceof Evaluable){
					String name=stel2.toString();
						try{
							StackElement val=((Evaluable)stel1).eval(conf);
							conf.envs.peek().peek().remove(name);			
							conf.envs.peek().peek().put(name, val);
							conf.stks.peek().push(new UnitStackElement());		
						}					
						catch(AException c){
							conf.stks.peek().push(stel2);
							conf.stks.peek().push(stel1);
							conf.stks.peek().push(new ErrorStackElement());														
						}
						finally{return conf;}
				}
				else{
					conf.stks.peek().push(stel2);
					conf.stks.peek().push(stel1);
					conf.stks.peek().push(new ErrorStackElement());
				}
			}
			else{
				conf.stks.peek().push(new ErrorStackElement());
			}
		}
		else{
			throw new Exception("Empty stack of stacks or Stack of envs! This should not happen-- Bind");
		}
		return conf;
	}
}
