
public class LabeledStatementNode extends StatementNode{

	private String label;
	private StatementNode statement;
	
	public LabeledStatementNode(String label, StatementNode statement)
	{
		this.label = label;
		this.statement = statement;
	}
	
	public String getLabel()
	{
		return label.substring(0, label.length() -1);
	}
	
	public StatementNode getStatement()
	{
		return statement;
	}
	
	
	
	@Override
	public String toString() 
	{
		if(statement == null)
			return label;
		return label +" "+ statement;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(statement);
		
	}

}
