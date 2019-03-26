class Closure implements Serializable{
	NameStackElement farg;
	ArrayList<Command> program;
	Stack<HashMap<String,StackElement>> envs;
	public Closure(NameStackElement f, ArrayList<Command> code, Stack<HashMap<String,StackElement>> e){
		this.farg=f;
		this.program=code;
		this.envs=e;
	}
	public NameStackElement getFormal(){return farg;}
	public ArrayList<Command> getBody(){return program;}
	public Stack<HashMap<String,StackElement>> getEnv(){return envs;}
	public void setEnv(Stack<HashMap<String,StackElement>> env){this.envs=env;}
}
