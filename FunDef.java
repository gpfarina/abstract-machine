class FunDef extends Command{
	NameStackElement name;
	NameStackElement farg;
	boolean flag=true;
	public FunDef(NameStackElement name, NameStackElement f){this.name=name; this.farg=f;}
	ArrayList<Command> parseBody(ArrayList<ArrayList<Command>> list) throws Exception{
		int ctr=0;
		Iterator<ArrayList<Command>> itr_ex=list.iterator();
		Iterator<Command> itr_int;
		ArrayList<Command> body=new ArrayList<Command>();
		while(itr_ex.hasNext()) {
			itr_int=itr_ex.next().iterator();
			while(itr_int.hasNext()){
				Command cand=itr_int.next();
      			if(cand instanceof FunEnd && ctr==0){itr_int.remove();return body;}
				else if (cand instanceof FunDef || cand instanceof FunIODef){ctr++;}      			
				else if (cand instanceof FunEnd){ctr--;}
				body.add(cand.clone());
				itr_int.remove();
			}
			itr_ex.remove();
		}
		throw new Exception("Fun definition not ended");
	}
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		ArrayList<Command> body=this.parseBody(conf.program);
		if(conf.envs.isEmpty()){throw new Exception("It should not happen: empty stack of envs");}
		else{
			conf.envs.peek().peek().remove(name.toString());	
			if(flag){			
				ClosureStackElement c=new ClosureStackElement(farg, body, copyEnvs(conf.envs.peek()));
				conf.envs.peek().peek().put(name.toString(), c);
				}
			else{
				IOClosureStackElement c=new IOClosureStackElement(farg, body, copyEnvs(conf.envs.peek()));
				conf.envs.peek().peek().put(name.toString(), c);
			}
			conf.stks.peek().push(new UnitStackElement());
		}
		return conf;
	}
}

