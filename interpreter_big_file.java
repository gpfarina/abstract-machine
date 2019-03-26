import java.util.Stack;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Exception;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.Map; 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
class StackElement<T> implements Serializable{
		T val;
		public void writeToFile(BufferedWriter bw){
			try{		
				bw.write(this.toString()+"\n");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		public T getValue(){return val;}
		public void setValue(T t){val=t;}
		public StackElement<T> clone(){
			try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		    ObjectOutputStream out = new ObjectOutputStream(bos);
        		out.writeObject(this);
   	    	 	ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
    	    	ObjectInputStream in = new ObjectInputStream(bis);
        		T copied = (T) in.readObject();
        		StackElement<T> stcopied=new StackElement<T>();
        		stcopied.setValue(copied);
        		return stcopied;
        	}catch(Exception e){
        		e.printStackTrace();
        		System.out.println("Error in deep-copying StackElement");
        		System.exit(-1);
        	}
        	return null;
		}
}
interface Evaluable{
	public StackElement eval(Configuration c)throws Exception;
}
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
class ClosureStackElement extends StackElement<Closure> implements Evaluable{
	public ClosureStackElement(NameStackElement f, ArrayList<Command> code, Stack<HashMap<String,StackElement>> e){
		setValue(new Closure(f,code,e));
	}
	public ClosureStackElement clone(){try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		    ObjectOutputStream out = new ObjectOutputStream(bos);
        		out.writeObject(this);
   	    	 	ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
    	    	ObjectInputStream in = new ObjectInputStream(bis);
        		ClosureStackElement copied = (ClosureStackElement) in.readObject();
        		return copied;
        	}catch(Exception e){
        		e.printStackTrace();
        		System.out.println("Error in deep-copying StackElement");
        		System.exit(-1);
        	}
        	return null;}
	public ClosureStackElement eval(Configuration c){return this;}
}
class IOClosureStackElement extends StackElement<Closure> implements Evaluable{
	public IOClosureStackElement(NameStackElement f, ArrayList<Command> code, Stack<HashMap<String,StackElement>> e){
		setValue(new Closure(f,code,e));
	}
	public IOClosureStackElement eval(Configuration c){return this;}
		public IOClosureStackElement clone(){try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		    ObjectOutputStream out = new ObjectOutputStream(bos);
        		out.writeObject(this);
   	    	 	ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
    	    	ObjectInputStream in = new ObjectInputStream(bis);
        		IOClosureStackElement copied = (IOClosureStackElement) in.readObject();
        		return copied;
        	}catch(Exception e){
        		e.printStackTrace();
        		System.out.println("Error in deep-copying StackElement");
        		System.exit(-1);
        	}
        	return null;}
}
class UnitStackElement extends StackElement<String> implements Evaluable{
	public UnitStackElement(){val=new String(":unit:");}
		public String toString(){
		return val;
	}
	public UnitStackElement eval(Configuration c){return this;}
}
class IntegerStackElement extends StackElement<Integer>implements Evaluable{
	public IntegerStackElement(int v){
		val=new Integer(v);
	}
	public String toString(){
		return Integer.toString(val);
	}
	public IntegerStackElement eval(Configuration c){return this;}
	public IntegerStackElement clone(){return new IntegerStackElement(val.intValue());}
}
class BooleanStackElement extends StackElement<Boolean>implements Evaluable{
	public BooleanStackElement(boolean v){
		val=new Boolean(v);
	}
	public String toString(){
		return (new String(":"+val.toString()+":"));
	}
	public BooleanStackElement eval(Configuration c){return this;}
	public BooleanStackElement clone(){return new BooleanStackElement(val.booleanValue());}
}
class StringStackElement extends StackElement<String>implements Evaluable{
	public StringStackElement(String v){
		val=v;
	}
	public String toString(){
		String tmp=new String(val);
		return tmp.replace("\"", "");
	}
	public StringStackElement eval(Configuration c){return this;}
	public StringStackElement clone(){return new StringStackElement(new String(val));}
}
class NameStackElement extends StackElement<String>implements Evaluable{
	public NameStackElement(String str){
		val=str;
	}
	public String toString(){
		return val;
	}	
	public NameStackElement clone(){return new NameStackElement(new String(val));}
	public StackElement eval(Configuration c) throws Exception{
		StackElement rtr=null;
		boolean found=false;
		if(c.envs!=null){
			Stack<Stack<HashMap<String, StackElement>>> envsXTmp=new Stack<Stack<HashMap<String,StackElement>>>();
			while(!c.envs.isEmpty()&&!found){
				Stack<HashMap<String, StackElement>> envs=c.envs.pop();
				envsXTmp.push(envs);
				Stack<HashMap<String, StackElement>> envsTmp=new Stack<HashMap<String,StackElement>>();
				if(envs!=null){
					while(!envs.isEmpty() && !found){
						HashMap<String, StackElement> env=envs.pop();
						envsTmp.push(env);
						if(env.containsKey(this.toString())){
							found=true;
							rtr=env.get(this.toString());
						}
					}
					while(!envsTmp.empty()){envs.push(envsTmp.pop());}
				}
			}
			while(!envsXTmp.isEmpty()){c.envs.push(envsXTmp.pop());}	
		}
		else{
			throw new Exception("Null stack of stack of envs");
		}
		if (rtr==null){ throw new AException(c);}
		return rtr;
		}	
}
class ErrorStackElement extends StackElement<String>{
	public String toString(){
		return (new String(":"+"error"+":"));
	}
}
abstract class Operator<T0, T1> extends Command{
	abstract T1 op(T0 a, T0 b, Stack<StackElement> st) throws AException;
}
class Sum extends Operator<IntegerStackElement, IntegerStackElement> {
	public IntegerStackElement op(IntegerStackElement a, IntegerStackElement b, Stack<StackElement> st){return new IntegerStackElement(a.getValue()+b.getValue());}
}
class Sub extends Operator<IntegerStackElement,IntegerStackElement> {
	public IntegerStackElement op(IntegerStackElement a, IntegerStackElement b, Stack<StackElement> st){return new IntegerStackElement(b.getValue()-a.getValue());}
}
class Mul extends Operator<IntegerStackElement, IntegerStackElement> {
	public IntegerStackElement op(IntegerStackElement a, IntegerStackElement b, Stack<StackElement> st){return new IntegerStackElement(a.getValue()*b.getValue());}
}
class Div extends Operator<IntegerStackElement, StackElement>{
	public StackElement op(IntegerStackElement a, IntegerStackElement b, Stack<StackElement> st) throws AException{
	if (a.getValue()==0){
		throw new AException(null);//the value is not needed here
	}
	else {
		return new IntegerStackElement(b.getValue()/a.getValue());
		}
	}
}
class Rem extends Operator<IntegerStackElement, StackElement>{
	public StackElement op(IntegerStackElement a, IntegerStackElement b, Stack<StackElement> st)throws AException{
	if (a.getValue()==0){
			throw new AException(null);//the value is not needed here
	}
	else {
		return new IntegerStackElement(b.getValue()%a.getValue());
		}
	}
}
class Equal extends Operator<IntegerStackElement, BooleanStackElement>{
		public BooleanStackElement op(IntegerStackElement a, IntegerStackElement b, Stack<StackElement> st){return new BooleanStackElement(a.getValue().intValue()==b.getValue().intValue());}
}
class LessThan extends Operator<IntegerStackElement, BooleanStackElement>{
		public BooleanStackElement op(IntegerStackElement a, IntegerStackElement b, Stack<StackElement> st){return new BooleanStackElement(a.getValue().intValue()>b.getValue().intValue());}
}
class Cat extends Operator<StringStackElement, StringStackElement>{
		public StringStackElement op(StringStackElement a, StringStackElement b, Stack<StackElement> st){return new StringStackElement(b.toString()+a.toString());}
}
class And extends Operator<BooleanStackElement, BooleanStackElement>{
		public BooleanStackElement op(BooleanStackElement a, BooleanStackElement b, Stack<StackElement> st){return new BooleanStackElement(a.getValue().booleanValue()&&b.getValue().booleanValue());}
}
class Or extends Operator<BooleanStackElement, BooleanStackElement>{
		public BooleanStackElement op(BooleanStackElement a, BooleanStackElement b, Stack<StackElement> st){return new BooleanStackElement(a.getValue().booleanValue()||b.getValue().booleanValue());}
}
class AException extends Exception{
	Configuration c;
	public AException(Configuration c){this.c=c;}
}
abstract class Command implements Serializable{
	public Configuration exec(Configuration conf) throws Exception{return conf;};
	public Command clone(){
			try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		    ObjectOutputStream out = new ObjectOutputStream(bos);
        		out.writeObject(this);
   	    	 	ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
    	    	ObjectInputStream in = new ObjectInputStream(bis);
        		Command copied = (Command) in.readObject();
        		return copied;
        	}catch(Exception e){
        		e.printStackTrace();
        		System.out.println("Error in deep-copying Command");
        		System.exit(-1);
        	}
        	return null;
		}
	static Stack<HashMap<String, StackElement>> copyEnvs(Stack<HashMap<String, StackElement>> env0){
		Stack<HashMap<String, StackElement>> env1=new Stack<HashMap<String, StackElement>>();
		for(HashMap<String, StackElement> t: env0){
        	HashMap<String, StackElement> ct=new HashMap<String, StackElement>();
        	for(String s: t.keySet()){
        		ct.put(new String(s), t.get(s).clone());
        	}
        	env1.push(ct);
        }
        return env1;
    }
}
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

class Neg extends Command{
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
						if(stval instanceof IntegerStackElement){
							c.stks.peek().pop();
							IntegerStackElement stint=(IntegerStackElement)stval;
							c.stks.peek().push(new IntegerStackElement((-1)*stint.getValue().intValue()));
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

class Quit extends Command{
	private String oFile;
	public Quit(String str){this.oFile=str;}
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()){
			//We assume all the let are matched with their end so there should be only one external stack to write
			ArrayList<StackElement> alist = new ArrayList<StackElement>(conf.stks.pop());
			ListIterator<StackElement> itr_st=alist.listIterator(alist.size());
    		try{
    			File file = new File(oFile);
    			FileWriter fw = new FileWriter(file);
	  			BufferedWriter bw = new BufferedWriter(fw);
    			while(itr_st.hasPrevious()) {
    				itr_st.previous().writeToFile(bw);
    			}
    			bw.close();
    		}
    		catch(Exception e){
				e.printStackTrace();
    		}
			return conf;
		}
		else{
			throw new Exception("Empty stack of stacks! This should not happen");
		}
	}
}
class Pop extends Command{
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()){
			if(conf.stks.peek().size()>=1){
				conf.stks.peek().pop();
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
class Push extends Command{
	private StackElement stel;
	public Push(StackElement stel){this.stel=stel;}
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()){
			conf.stks.peek().push(stel);
		}
		else{
			throw new Exception("Empty stack of stacks! This should not happen");
		}
		return conf;
	}

}
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
class Let extends Command{
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		if(!conf.stks.isEmpty()&&!conf.envs.isEmpty()){
			conf.stks.push(new Stack<StackElement>());
			HashMap<String, StackElement> h=new HashMap<String, StackElement>(); 
			conf.envs.peek().push(h);
		}
		else{
			throw new Exception("Empty stack of stacks or stack of stack of envs! This should not happen -- Let --");
		}
		return conf;
	}
}
class End extends Command{
	public Configuration exec(Configuration conf)throws Exception{
		conf.program.get(0).remove(0);
		if(conf.stks.size()>=1 && conf.envs.size()>=1 && conf.envs.peek().size()>=1) {
			StackElement stel;
			if (!conf.stks.peek().isEmpty()){stel=conf.stks.peek().pop();} else{stel=new ErrorStackElement();}
			conf.stks.pop();
			conf.envs.peek().pop();
			conf.stks.peek().push(stel);
		}
		else{
			throw new Exception(" -- End --");
		}
		return conf;
	}	
}
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
class FunIODef extends FunDef{
		public FunIODef(NameStackElement name, NameStackElement f){super(name,f);this.flag=false;}
}
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
class FunEnd extends Command{
	public Configuration exec(Configuration conf) throws Exception{return null;}//this function should never be called
}

class Return extends Command{
	protected Configuration setStackEnvs(Configuration conf) throws Exception{
		if(conf.stks.size()>1 && conf.envs.size()>1){
			if (!conf.stks.peek().isEmpty() && !conf.envs.peek().isEmpty()){
				StackElement top=conf.stks.peek().pop();
				if (top instanceof NameStackElement){
					top=((NameStackElement) top).eval(conf);
				}
				conf.stks.pop();
				conf.stks.peek().push(top);
				conf.envs.pop();
			}
			else{
				conf.stks.pop();
				conf.envs.pop();
				conf.stks.peek().push(new ErrorStackElement());
			}
		}else{
			throw new Exception("Stack of stacks with less than 2 elements or Stack of stack of envs with less than 2 elements: you can't return!");
		}
		return conf;
	} 
	public Configuration exec(Configuration conf) throws Exception{
		conf.program.get(0).remove(0);
		return setStackEnvs(conf);
	}
	public Return(){super();}
}
class ReturnIO extends Return{
	NameStackElement farg, aarg;
	public ReturnIO(){super();}
	public ReturnIO(NameStackElement farg, NameStackElement aarg){this.farg=farg; this.aarg=aarg;}
 	public Configuration exec(Configuration conf) throws Exception{
 		conf.program.get(0).remove(0);
 		StackElement val=null;
 		if (conf.stks.peek().size()>0){
 			val=((Evaluable) farg).eval(conf);
 			Configuration c=super.setStackEnvs(conf);
 			c.envs.peek().peek().remove(aarg.toString());
 			c.envs.peek().peek().put(aarg.toString(), val);
 			return c;
 		}
 		else{
 			conf.envs.peek().pop();
 			conf.envs.peek().peek().put(aarg.toString(), new ErrorStackElement());
 		}
 	 	return conf;
 	}
}
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
class Configuration{
	Stack<Stack<StackElement>> stks;
	Stack<Stack<HashMap<String,StackElement>>> envs;
	ArrayList<ArrayList<Command>> program;
	public Configuration(Stack<Stack<StackElement>> stks, Stack<Stack<HashMap<String,StackElement>>> envs, ArrayList<ArrayList<Command>> program){
		this.stks=stks;
		this.envs=envs;
		this.program=program;
	}
}

class UtilityCommand{
		private String oFile, iFile;
		public UtilityCommand(String in, String out){this.iFile=in;this.oFile=out;}
		public StackElement stringToStackElement(String str){
			if (str.matches("\".+\"")){
				return new StringStackElement(str);
			}
			else if (str.matches("(-)?\\d+")) {
				return new IntegerStackElement(Integer.parseInt(str));
			}
			else if (str.matches("(_)?[a-z|A-Z][a-z|A-Z|0-9|_]*")){
				return new NameStackElement(str);	
			}
			else if (str.matches(":true:") ){
				return new BooleanStackElement(true);
			}
			else if (str.matches(":false:") ){
				return new BooleanStackElement(false);
			}
			else if (str.matches(":error:") ){
				return new ErrorStackElement();
			}
			else 
				return new ErrorStackElement(); 
		}
		public ArrayList<Command> stringToCommand(String str)throws Exception{
			StackElement potname, potfarg;
			String spl[] = str.split(" ", 2);
			ArrayList<Command> ret=new ArrayList<Command>();
			switch(spl[0]){
				case "end":
					ret.add(new End());
					break;
				case "if":
					ret.add(new If());
					break;
				case "add":
					ret.add(new Arithmetic(new Sum()));
					break;
				case "sub":
					ret.add(new Arithmetic(new Sub()));
					break;
				case "mul":
					ret.add(new Arithmetic(new Mul()));
					break;
				case "div":
					ret.add(new Arithmetic(new Div()));
					break;
				case "rem":
					ret.add(new Arithmetic( new Rem()));
					break;
				case "neg":
					ret.add(new Neg());
					break;
				case "not":
					ret.add(new Not());
					break;
				case "swap":
					ret.add(new Swap());
					break;
				case "pop":
					ret.add(new Pop());
					break;
				case "push":
					ret.add(new Push(stringToStackElement(spl[1])));
					break;
				case "quit":
					ret.add(new Quit(this.oFile));
					break;
				case "and":
					ret.add(new Arithmetic(new And()));
					break;
				case "or":
					ret.add(new Arithmetic(new Or()));
					break;
				case "cat":
					ret.add(new Arithmetic(new Cat()));
					break;
				case "let":
					ret.add(new Let());
					break;
				case "bind":
					ret.add(new Bind());
					break;
				case "equal":
					ret.add(new Arithmetic(new Equal()));
					break;
				case "lessThan":
					ret.add(new Arithmetic(new LessThan()));	
					break;
				case "return":
					ret.add(new Return());
					break;
				case "fun":
					String splz[] = str.split(" ", 3);
					potname=stringToStackElement(splz[1]); 
					potfarg=stringToStackElement(splz[2]);
					if(potname instanceof NameStackElement && potfarg instanceof NameStackElement)
						ret.add(new FunDef((NameStackElement)potname,(NameStackElement)potfarg));
					else{						
						throw new Exception("Can't parse function definition");
					}
					break;
				case "inOutFun":
					String spls[] = str.split(" ", 3);
					potname=stringToStackElement(spls[1]); 
					potfarg=stringToStackElement(spls[2]);
					if(potname instanceof NameStackElement && potfarg instanceof NameStackElement)
						ret.add(new FunIODef((NameStackElement)potname,(NameStackElement)potfarg));
					else{
						throw new Exception("Can't parse in out function definition");
					}
					break;
				case "funEnd":
					ret.add(new FunEnd());
					break;
				case "call":
					ret.add(new Call());
					break;
				default: 
					throw new Exception("Can't parse command: "+spl[0]);
			}
			return ret;
		}
	public ArrayList<ArrayList<Command>> getProgram(){
			ArrayList<ArrayList<Command>> program=new ArrayList<ArrayList<Command>>();
			try (BufferedReader br = new BufferedReader(new FileReader(this.iFile))) {
    			String line;
    			while ((line = br.readLine()) != null) {
       				program.add(this.stringToCommand(line.trim()));
    			}
    			br.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return program;
		}
}
public class interpreter{
	public static void interpreter(String iFile, String oFile) throws Exception{
		UtilityCommand ut=new UtilityCommand(iFile, oFile);
		Stack<Stack<StackElement>> initStack=new Stack<Stack<StackElement>>();
		Stack<Stack<HashMap<String, StackElement>>> initEnv=new Stack<Stack<HashMap<String, StackElement>>>();
		initStack.push(new Stack<StackElement>());
		initEnv.push(new Stack<HashMap<String, StackElement>>());
		initEnv.peek().push(new HashMap<String, StackElement>());
		Configuration conf=new Configuration(initStack, initEnv, ut.getProgram());
		ArrayList<Command> cmdl;
		while(conf.program.size()>0) {
			cmdl=conf.program.get(0);
			while(cmdl.size()>0) {
  				conf=cmdl.get(0).exec(conf);
				cmdl=conf.program.get(0);
			}
			conf.program.remove(0);
		}
	}
	public static void main(String[] args) throws Exception{
		interpreter(args[0], args[1]);
	}
}