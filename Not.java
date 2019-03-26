class Not extends Command{
	public Configuration exec(Configuration c) throws Exception{
		c.program.get(0).remove(0);
		if (!c.stks.isEmpty()){
			if(c.stks.peek().size()>=1){
				StackElement st=c.stks.peek().pop();
				c.stks.peek().push(st);
				if(st instanceof Evaluable){
					Evaluable val=(Evaluable)st;
					try{
						StackElement stval=val.eval(c);
						if(stval instanceof BooleanStackElement){
							c.stks.peek().pop();
							BooleanStackElement stbool=(BooleanStackElement)stval;
							c.stks.peek().push(new BooleanStackElement(!stbool.getValue().booleanValue()));
						}
						else{
							c.stks.peek().push(new ErrorStackElement());
							return c;
						}
					}
					catch(AException e){
						e.c.stks.peek().push(new ErrorStackElement());
						return e.c;
					}
				}
				else{
					c.stks.peek().push(new ErrorStackElement());
					return c;					
				}
			}
			else{
				c.stks.peek().push(new ErrorStackElement());
				return c;
			}
		}
		else{
			throw new Exception("Empty stack of stacks! It should not happen");
		}
		return c;
	}
}

