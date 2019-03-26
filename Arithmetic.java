

class Arithmetic extends Command{
	Operator<StackElement,StackElement> oper;
	public Arithmetic(Operator op){
		this.oper=op;
	}
	public Configuration exec(Configuration c) throws Exception{
		c.program.get(0).remove(0);
		if (!c.stks.isEmpty()){
					if(c.stks.peek().size()>1){
						StackElement stel1=c.stks.peek().pop();
						StackElement stel2=c.stks.peek().pop();
						c.stks.peek().push(stel2);
						c.stks.peek().push(stel1);
						if (stel1 instanceof Evaluable) {
							Evaluable v1=(Evaluable)stel1;
							StackElement val1;
							try {val1=v1.eval(c);}
							catch(AException e){
								e.c.stks.peek().push(new ErrorStackElement());
								return c;								
							}
							if (stel2 instanceof Evaluable){
								Evaluable v2=(Evaluable)stel2;
								StackElement val2;
								try{val2=v2.eval(c);}
								catch(AException e){
									c.stks.peek().push(new ErrorStackElement());
									return c;								
								}
								if(val1 instanceof IntegerStackElement && val2 instanceof IntegerStackElement ||
								   val1 instanceof StringStackElement && val2 instanceof StringStackElement   ||
								   val1 instanceof BooleanStackElement && val2 instanceof BooleanStackElement){
									c.stks.peek().pop();
									c.stks.peek().pop();
									try{c.stks.peek().push(oper.op(val1,val2,
															   c.stks.peek()));}
									catch(AException e){
										c.stks.peek().push(stel2);
										c.stks.peek().push(stel1);
										c.stks.peek().push(new ErrorStackElement());
									}
									return c;
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
	}
}
