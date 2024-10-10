
public class Next extends StatementNode{

	private VariableNode variable;
	
	public Next(VariableNode variable)
	{
		this.variable = variable;
	}
	
	public VariableNode getVariable()
	{
		return variable;
	}
	
	@Override
	public String toString() {
		return "NEXT "+ variable;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(variable);
		
	}

}
