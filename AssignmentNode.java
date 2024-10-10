
public class AssignmentNode extends StatementNode{
	
	private VariableNode variableNode;
	private Node assignment;
	
	public AssignmentNode(VariableNode variableNode, Node assignment)
	{
		this.variableNode = variableNode;
		this.assignment = assignment;
	}
	
	public VariableNode getVariable()
	{
		return variableNode;
	}
	
	public Node getAssignment()
	{
		return assignment;
	}
	
	@Override
	public String toString() {
		return variableNode + " = " + assignment;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(variableNode);
		visitor.visit(assignment);
		
	}

}
