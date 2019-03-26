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
