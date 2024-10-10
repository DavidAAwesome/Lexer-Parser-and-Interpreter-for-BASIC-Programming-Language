
public class If extends StatementNode{

	private Node expression;
	private String label;
	
	public If(Node expression, String label)
	{
		this.expression = expression;
		this.label = label;
	}
	
	public Node getExpression()
	{
		return expression;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	@Override
	public String toString() {
		return "IF " + expression + " THEN " + label;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(expression);
		
	}

}
