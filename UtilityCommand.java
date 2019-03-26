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
