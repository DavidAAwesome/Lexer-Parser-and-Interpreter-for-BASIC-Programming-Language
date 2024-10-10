import java.util.LinkedList;

public class DataNode extends StatementNode{

	private LinkedList<Node> nodes;
	
	public DataNode(LinkedList<Node> nodes)
	{
		this.nodes = nodes;
	}
	
	public LinkedList<Node> getNodes()
	{
		return nodes;
	}
	
	@Override
	public String toString() {
		String word = "DATA ";
		for(Node node : nodes)
			word += node + " ";
		return word;
	}

	@Override
	public void accept(Visitor visitor) {
		for(var node : nodes)
			visitor.visit(node);
		
	}

}
