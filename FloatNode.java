
public class FloatNode extends Node{

	private float number;
	
	public FloatNode(float number)
	{
		this.number = number;
	}
	
	public float getNumber()
	{
		return number;
	}
	
	@Override
	public String toString() {
		return number +"";
	}

	@Override
	public void accept(Visitor visitor) {
		
	}

}
