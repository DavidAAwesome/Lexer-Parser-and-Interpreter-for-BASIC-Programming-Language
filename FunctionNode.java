import java.util.LinkedList;

public class FunctionNode extends Node{

	private Types type;
	private LinkedList<Node> parameters;
	
	public FunctionNode(Types type, LinkedList<Node> parameters)
	{
		this.type = type;
		this.parameters = parameters;
	}
	
	public Types getType()
	{
		return type;
	}
	
	public LinkedList<Node> getParameters()
	{
		return parameters;
	}
	
	
	@Override
	public String toString() {
		return type + "(" + parameters + ")";
	}
	
	public enum Types
	{
		RANDOM, LEFT$, RIGHT$, MID$, NUM$, VAL, VAL2
	}

	@Override
	public void accept(Visitor visitor) {
		for(var node : parameters)
			visitor.visit(node);
		
	}

}
