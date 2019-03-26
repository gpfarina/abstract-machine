
class Call extends Command {
	public ArrayList<Command> bindReturn(NameStackElement actual, IOClosureStackElement cl) throws Exception{
		ArrayList<Command> body=new ArrayList<Command>();
		if(cl.getValue().getBody()!=null && cl.getValue().getBody().size()>0){
			for(Command c : cl.getValue().getBody()){
				body.add(c.clone());
			}
			if(body.get(body.size()-1) instanceof Return){
				body.remove(body.size()-1);
				body.add(new ReturnIO(cl.getValue().getFormal().clone(), actual.clone()));
			}
			else{
				body.add(new HiddenIO(cl.getValue().getFormal().clone(), actual.clone()));	
			}
		}
		else { throw new Exception("empty program closure"); }
		return body;
	}

