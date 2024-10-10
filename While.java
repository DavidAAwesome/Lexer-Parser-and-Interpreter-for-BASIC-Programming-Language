
public class While extends StatementNode{

	private Node expression;
	private String label;
	
	public While(Node expression, String label)
	{
		this.expression = expression;
		this.label = label;
	}
	
	@Override
	public String toString() {
		return "WHILE " + expression + " " + label;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(expression);
		
	}

}
