import java.util.LinkedList;

public class InputNode extends StatementNode{
	
	private StringNode string;
	private LinkedList<VariableNode> variables;
	
	public InputNode(StringNode string, LinkedList<VariableNode> variables)
	{
		this.string = string;
		this.variables = variables;
	}
	
	public StringNode getString()
	{
		return string;
	}
	
	public LinkedList<VariableNode> getVariables()
	{
		return variables;
	}
	
	@Override
	public String toString() {
		String word = "INPUT " + string + " ";
		for(Node variable : variables)
			word += variable + " ";
		return word;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(string);
		for(var node : variables)
			visitor.visit(node);
		
	}

}
