
public class Gosub extends StatementNode{

	private String identifier;
	
	public Gosub(String identifier)
	{
		this.identifier = identifier;
	}
	
	public String getIdentifier()
	{
		return identifier;
	}
	
	@Override
	public String toString() {
		return "GOSUB " + identifier;
	}

	@Override
	public void accept(Visitor visitor) {
		
	}

}
