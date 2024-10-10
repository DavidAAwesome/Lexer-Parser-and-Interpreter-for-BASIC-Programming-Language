import java.util.LinkedList;

public class StatementsNode extends StatementNode{
	
	private LinkedList<StatementNode> statementNodes = new LinkedList<>();
	
	public void addtoStatements(StatementNode statementNode)
	{
		statementNodes.add(statementNode);
	}
	
	public LinkedList<StatementNode> getStatements()
	{
		return statementNodes;
	}

	@Override
	public String toString() {
		String nodes = "";
		for(Node node : statementNodes)
			nodes += node + " ";
		return nodes;
	}

	@Override
	public void accept(Visitor visitor) {
		for(var node : statementNodes)
			visitor.visit(node);
		
	}

}
