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
