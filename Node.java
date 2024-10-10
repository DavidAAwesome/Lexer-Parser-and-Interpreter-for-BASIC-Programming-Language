
public abstract class Node {

	@Override
	public abstract String toString();
	
	public abstract void accept(Visitor visitor);
}
