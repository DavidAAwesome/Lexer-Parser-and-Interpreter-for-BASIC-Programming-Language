
public class BoolOpNode extends Node{

	private Node left;
	private Operations operation;
	private Node right;
	
	public BoolOpNode(Node left, Operations operation, Node right)
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
		String sign;
		switch(operation)// Switch case created to change from an enum to a character.
		{
		case GTHAN:
			sign = ">";
			break;
		case GREATEREQUALS:
			sign = ">=";
			break;
		case LTHAN:
			sign = "<";
			break;
		case LESSEQUALS:
			sign = "<=";
			break;
		case NOTEQUALS:
			sign = "<>";
			break;
		case EQUALS:
			sign = "=";
			break;
		default:
			throw new IllegalArgumentException("Math operation not found");
			
		}
		return "(" + left + sign + right + ")";
	}
	
	public enum Operations
	{
		GTHAN, GREATEREQUALS, LTHAN, LESSEQUALS, NOTEQUALS, EQUALS
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(left);
		visitor.visit(right);
		
	}

}
