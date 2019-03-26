class If extends Command{
		public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()){
			if(conf.stks.peek().size()>=3){
						StackElement stelt=conf.stks.peek().pop();
						StackElement stelf=conf.stks.peek().pop();
						StackElement stelg=conf.stks.peek().pop();
						conf.stks.peek().push(stelg);
						conf.stks.peek().push(stelf);
						conf.stks.peek().push(stelt);
						if(stelg instanceof Evaluable){
							Evaluable vg=(Evaluable) stelg;
							StackElement valg;
							try {valg =vg.eval(conf);}
							catch(AException e){
								conf.stks.peek().push(new ErrorStackElement());	
								return conf;
							}
							if(valg instanceof BooleanStackElement){
								BooleanStackElement bg=(BooleanStackElement) valg;
								conf.stks.peek().pop();
								conf.stks.peek().pop();
								conf.stks.peek().pop();
								if(bg.getValue().booleanValue()){
									conf.stks.peek().push(stelt);
									return conf;
								}
								else{
									conf.stks.peek().push(stelf);
									return conf;
								}
							}
							else{
								conf.stks.peek().push(new ErrorStackElement());
								return conf;								
							}
						}
						else{
							conf.stks.peek().push(new ErrorStackElement());
							return conf;
						}
			}
			else{
				conf.stks.peek().push(new ErrorStackElement());
				return conf;
			}
		}
		else{
			throw new Exception("Stack of stacks with size <=1. This should not happen");
		}
	}	
}
