import java.util.LinkedList;

public class ReadNode extends StatementNode{

	private LinkedList<VariableNode> variables;
	
	public ReadNode(LinkedList<VariableNode> variables)
	{
		this.variables = variables;
	}
	
	public LinkedList<VariableNode> getVariables()
	{
		return variables;
	}
	
	@Override
	public String toString() {
		String word = "READ ";
		for(Node variable : variables)
			word += variable + " ";
		return word;
	}

	@Override
	public void accept(Visitor visitor) {
		for(var node : variables)
			visitor.visit(node);
		
	}

}
