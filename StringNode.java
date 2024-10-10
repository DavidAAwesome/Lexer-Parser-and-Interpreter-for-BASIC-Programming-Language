
public class StringNode extends Node{
	
	private String string;
	
	public StringNode(String string)
	{
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}

	@Override
	public void accept(Visitor visitor) {
		
	}

}
