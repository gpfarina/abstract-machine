class Configuration{
	Stack<Stack<StackElement>> stks;
	Stack<Stack<HashMap<String,StackElement>>> envs;
	ArrayList<ArrayList<Command>> program;
	public Configuration(Stack<Stack<StackElement>> stks, Stack<Stack<HashMap<String,StackElement>>> envs, ArrayList<ArrayList<Command>> program){
		this.stks=stks;
		this.envs=envs;
		this.program=program;
	}

public Configuration exec(Configuration conf) throws Exception{
		StackElement actual=null, namef=null;
		conf.program.get(0).remove(0);
		if(conf.stks.size()>=1 && conf.envs.size()>=1 && conf.envs.peek().size()>=1){
			if(conf.stks.peek().size()>=2){
				actual = conf.stks.peek().pop();
				namef  = conf.stks.peek().pop();
			try{
				if(namef instanceof Evaluable){
					if(((Evaluable)namef).eval(conf) instanceof ClosureStackElement && actual instanceof Evaluable){
						ClosureStackElement cl=(ClosureStackElement)((Evaluable)namef).eval(conf);
						StackElement  actval=((Evaluable)actual).eval(conf);
						Stack<HashMap<String, StackElement>> env=Command.copyEnvs(cl.getValue().getEnv());
						env.remove(cl.getValue().getFormal().toString());
						env.peek().put(cl.getValue().getFormal().toString(), actval);
						for (int i=cl.getValue().getBody().size()-1;i>=0; --i){
							conf.program.get(0).add(0, cl.getValue().getBody().get(i).clone());
						}
						conf.envs.push(env);					
						conf.stks.push(new Stack<StackElement>());
					}
					else if (((Evaluable)namef).eval(conf) instanceof IOClosureStackElement && actual instanceof NameStackElement){
						IOClosureStackElement cl=(IOClosureStackElement)((Evaluable)namef).eval(conf);
						StackElement  actval=((Evaluable)actual).eval(conf);
						Stack<HashMap<String, StackElement>> env=Command.copyEnvs(cl.getValue().getEnv());
						env.remove(cl.getValue().getFormal().toString());
						env.peek().put(cl.getValue().getFormal().toString(), actval);
						ArrayList<Command> body=new ArrayList<Command>();
						for(Command c : bindReturn((NameStackElement)actual, cl)){
							body.add(c);
						}
						for (int i=body.size()-1;i>=0; --i){
							conf.program.get(0).add(0, body.get(i));
						}
						conf.envs.push(env);					
						conf.stks.push(new Stack<StackElement>());
					}
					else{
						conf.stks.peek().push(namef);
						conf.stks.peek().push(actual);
						conf.stks.peek().push(new ErrorStackElement());						
					}
				}
				else{
					conf.stks.peek().push(namef);
					conf.stks.peek().push(actual);
					conf.stks.peek().push(new ErrorStackElement());
				}
			}
			catch(AException e){
				e.c.stks.peek().push(namef);
				e.c.stks.peek().push(actual);
				e.c.stks.peek().push(new ErrorStackElement());
				return e.c;
			}
			}
			else{
				conf.stks.peek().push(new ErrorStackElement());
			}
		}	
		else{
			throw new Exception("Call");	
		}
		return conf;
	}
}

}
