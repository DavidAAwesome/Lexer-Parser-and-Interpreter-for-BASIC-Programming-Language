import java.util.HashMap;

public class LabelNodeVisitor implements Visitor{

	public HashMap<String, LabeledStatementNode> labels;
	
	public LabelNodeVisitor(HashMap<String, LabeledStatementNode> labels)
	{
		this.labels = labels;
	}
	
	@Override
	public void visit(Node node) {
		node.accept(this);
		if(node instanceof LabeledStatementNode)
		{
			LabeledStatementNode label = (LabeledStatementNode) node;
			labels.put(label.getLabel(), label);
		}
	}

	
	
}
