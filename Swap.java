
class Swap extends Command{
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()){
			if(conf.stks.peek().size()>=2){
				StackElement stel1 = conf.stks.peek().pop();
				StackElement stel2 = conf.stks.peek().pop();
				conf.stks.peek().push(stel1);
				conf.stks.peek().push(stel2);
				return conf;
			}
			else{
				conf.stks.peek().push(new ErrorStackElement());
				return conf;
			}
		}
		else{
			throw new Exception("Empty stack of stacks! This should not happen");
		}
	}
}

