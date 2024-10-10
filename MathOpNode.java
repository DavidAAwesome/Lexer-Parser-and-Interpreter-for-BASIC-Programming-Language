
public class MathOpNode extends Node{

	private Node left;
	private Operations operation;
	private Node right;
	
	public MathOpNode(Node left, Operations operation, Node right)
	{
		this.left = left;
		this.operation = operation;
		this.right = right;
	}
	
	public Node getLeft()
	{
		return left;
	}
	
	public Operations getOperation()
	{
		return operation;
	}
	
	public Node getRight()
	{
		return right;
	}
	
	@Override
	public String toString() 
	{
		char sign;
		switch(operation)// Switch case created to change from an enum to a character.
		{
		case ADD:
			sign = '+';
			break;
		case SUBTRACT:
			sign = '-';
			break;
		case MULTIPLY:
			sign = '*';
			break;
		case DIVIDE:
			sign = '/';
			break;
		default:
			throw new IllegalArgumentException("Math operation not found");
			
		}
		return "(" + left + sign + right + ")";
	}
	
	public enum Operations
	{
		ADD, SUBTRACT, MULTIPLY, DIVIDE, 
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(left);
		visitor.visit(right);
		
	}

}
