import java.util.LinkedList;

public class DataNodeVisitor implements Visitor{

	private LinkedList<Node> data;
	
	public DataNodeVisitor(LinkedList<Node> data)
	{
		this.data = data;
	}
	
	
	@Override
	public void visit(Node node) {
		node.accept(this);
		if(node instanceof DataNode) {
			DataNode data = (DataNode) node;
			for(var item : data.getNodes())
				this.data.add(item);
		}
	}

	
	
	
}
