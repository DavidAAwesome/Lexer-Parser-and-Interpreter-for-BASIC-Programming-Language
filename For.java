
public class For extends StatementNode{

	private AssignmentNode assignment;
	private int to;
	private int step;
	
	public For(AssignmentNode assignment, int to)
	{
		this.assignment = assignment;
		this.to = to;
		step = 1;
	}
	
	public For(AssignmentNode assignment, int to, int step)
	{
		this.assignment = assignment;
		this.to = to;
		this.step = step;
	}
	
	public AssignmentNode getAssignment()
	{
		return assignment;
	}
	
	public int getTo()
	{
		return to;
	}
	
	public int getStep()
	{
		return step;
	}
	
	@Override
	public String toString() {
		return "FOR " + assignment + " TO " + to + " STEP " + step;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(assignment);
		
	}

}
