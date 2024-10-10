import java.util.LinkedList;

public class PrintNode extends StatementNode{
	
	private LinkedList<Node> nodes;
	
	public PrintNode(LinkedList<Node> nodes) 
	{
		this.nodes = nodes;
	}

	public LinkedList<Node> getNodes()
	{
		return nodes;
	}

	@Override
	public String toString() {
		String print = "PRINT ";
		for(var i : nodes)
			print += i + " ";
		return print;
	}

	@Override
	public void accept(Visitor visitor) {
		for(var node: nodes)
			visitor.visit(node);
		
	}

}
