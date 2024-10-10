import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;


public class Interpreter {
	
	private StatementsNode AST;
	private Scanner scan = new Scanner(System.in);
	private StatementNode current;
	private StatementNode next;
	private Stack<StatementNode> statementStack = new Stack<>();
	
	private Stack<StatementNode> forStack = new Stack<>();
	public boolean testMode = false;
	public List<String> inputTestList;
	public List<String> printTestList;
	
	//Optimization
	public LinkedList<Node> data = new LinkedList<>();
	public HashMap<String, LabeledStatementNode> labels = new HashMap<>();
	
	//Variables
	public HashMap<String, Integer> integers = new HashMap<>();
	public HashMap<String, Float> floats = new HashMap<>();
	public HashMap<String, String> strings = new HashMap<>();
	
	public Interpreter(StatementsNode AST)
	{
		this.AST = AST;
	}
	
	public Interpreter(StatementsNode AST, boolean testMode)
	{
		this.AST = AST;
		this.testMode = testMode;
	}
	
	public void optimize()
	{
		DataNodeVisitor dataVisit = new DataNodeVisitor(data);
		LabelNodeVisitor labelVisit = new LabelNodeVisitor(labels);
		dataVisit.visit(AST);
		labelVisit.visit(AST);
	}
	
	private void buildLinkedList()
	{
		var statements = AST.getStatements();
		current = statements.getFirst();
		StatementNode temp;
		while(statements.size() > 1)
		{
			temp = statements.remove();
			temp.next = statements.getFirst();
		}
	}
	
	public void interpret(StatementsNode statements)
	{
		optimize();
		buildLinkedList();
		while( !(current instanceof End) )//No boolean variable needed
		{
			next = current.next;
			doCurrent();
			current = next;
		}
	}
	
	private void doCurrent()
	{
		if(current instanceof ReadNode)
		{
			read((ReadNode) current);
		}
		else if(current instanceof AssignmentNode)
		{
			assignment((AssignmentNode) current);
		}
		else if(current instanceof InputNode)
		{
			input((InputNode) current);
		}
		else if(current instanceof PrintNode)
		{
			print((PrintNode) current);
		}
		else if(current instanceof If)
		{
			If((If) current);
		}
		else if(current instanceof Gosub)
		{
			gosub((Gosub) current);
		}
		else if(current instanceof Return)
		{
			Return();
		}
		else if(current instanceof For)
		{
			For((For) current);
		}
		else if(current instanceof Next)
		{
			Next();
		}
		else if(current instanceof LabeledStatementNode)
		{
			var statement = (LabeledStatementNode) current;
			current = statement.getStatement();
			doCurrent();
		}
	}
	
	private void Next()
	{
		next = statementStack.pop();
	}
	
	private void For(For forNode)
	{
		String variableName = forNode.getAssignment().getVariable().getName();
		if(!forStack.contains(current))//Makes sure to check if the for loop is not running for the first
		{
			assignment(forNode.getAssignment());
			forStack.push(current);
		}
		
		if(integers.get(variableName) < forNode.getTo())
		{
			statementStack.push(current);
			integers.replace(variableName, integers.get(variableName) + forNode.getStep());
		}
		else
		{
			while( !(next instanceof End) )
			{
				if(next instanceof Next)
					if(((Next)next).getVariable().getName().equals(variableName))
					{
						next = next.next;
						forStack.pop();//Tells the program the for loop will run for the first time again if called again
						break;
					}
				next = next.next;
			}
		}
	}
	
	private void Return()
	{
		next = statementStack.pop();
	}
	
	private void gosub(Gosub gosubNode)
	{
		statementStack.push(gosubNode.next);
		next = labels.get(gosubNode.getIdentifier());
	}
	
	private void If(If ifNode)
	{
		if (evaluateBoolean((BoolOpNode) ifNode.getExpression()))
		{
			next = labels.get(ifNode.getLabel());
		}
	}
	
	private boolean evaluateBoolean(BoolOpNode boolNode)
	{
		switch(evaluate(boolNode.getLeft()))
		{
		case STRING:
			switch(boolNode.getOperation())
			{
			case EQUALS:
				return evaluateString(boolNode.getLeft()).equals(evaluateString(boolNode.getRight()));
			default:
				throw new IllegalArgumentException();
			}
		case FLOAT:
			switch(boolNode.getOperation())
			{
			case GTHAN:
				return evaluateFloat(boolNode.getLeft()) > evaluateFloat(boolNode.getRight());
			case GREATEREQUALS:
				return evaluateFloat(boolNode.getLeft()) >= evaluateFloat(boolNode.getRight());
			case LTHAN:
				return evaluateFloat(boolNode.getLeft()) < evaluateFloat(boolNode.getRight());
			case LESSEQUALS:
				return evaluateFloat(boolNode.getLeft()) <= evaluateFloat(boolNode.getRight());
			case NOTEQUALS:
				return evaluateFloat(boolNode.getLeft()) != evaluateFloat(boolNode.getRight());
			case EQUALS:
				return evaluateFloat(boolNode.getLeft()) == evaluateFloat(boolNode.getRight());
			default:
				throw new IllegalArgumentException();
			}
		case INT:
			switch(boolNode.getOperation())
			{
			case GTHAN:
				return evaluateInt(boolNode.getLeft()) > evaluateInt(boolNode.getRight());
			case GREATEREQUALS:
				return evaluateInt(boolNode.getLeft()) >= evaluateInt(boolNode.getRight());
			case LTHAN:
				return evaluateInt(boolNode.getLeft()) < evaluateInt(boolNode.getRight());
			case LESSEQUALS:
				return evaluateInt(boolNode.getLeft()) <= evaluateInt(boolNode.getRight());
			case NOTEQUALS:
				return evaluateInt(boolNode.getLeft()) != evaluateInt(boolNode.getRight());
			case EQUALS:
				return evaluateInt(boolNode.getLeft()) == evaluateInt(boolNode.getRight());
			default:
				throw new IllegalArgumentException();
			}
		default:
			throw new IllegalArgumentException();
		}
	}
	
	private void read(ReadNode readNode)
	{
		for(var variable : readNode.getVariables())
		{
			if(variable.getName().contains("$"))
			{
				Node node = data.remove();
				if( !(node instanceof StringNode) )
					throw new IllegalArgumentException();
				StringNode string = (StringNode) node;
				strings.put(variable.getName(), string.toString());
			}
			else if(variable.getName().contains("%"))
			{
				Node node = data.remove();
				if( !(node instanceof FloatNode) )
					throw new IllegalArgumentException();
				FloatNode floatNumber = (FloatNode) node;
				floats.put(variable.getName(), floatNumber.getNumber());
			}
			else
			{
				Node node = data.remove();
				if( !(node instanceof IntegerNode) )
					throw new IllegalArgumentException();
				IntegerNode integer = (IntegerNode) node;
				integers.put(variable.getName(), integer.getNumber());
			}
		}
	}
	
	private void assignment(AssignmentNode assignment)
	{
		String assignmentName = assignment.getVariable().getName();
		if(assignmentName.contains("$"))
		{
			strings.put(assignmentName, evaluateString(assignment.getAssignment()));
		}
		else if(assignmentName.contains("%"))
		{
			floats.put(assignmentName, evaluateFloat(assignment.getAssignment()));
		}
		else
		{
			integers.put(assignmentName, evaluateInt(assignment.getAssignment()));
		}
	}
	
	private void input(InputNode input)
	{
		if(!testMode)
		{
			System.out.println(input.getString());
			for(var variable : input.getVariables())
			{
				String variableName = variable.getName();
				if(variableName.contains("$"))
				{
					strings.put(variableName, scan.nextLine());
				}
				else if(variableName.contains("%"))
				{
					floats.put(variableName, scan.nextFloat());
				}
				else
				{
					integers.put(variableName, scan.nextInt());
				}
			}
		}
		else
		{
			inputTestList.add(input.getString().toString());
			int index = 0;
			for(var variable : input.getVariables())
			{
				
				String variableName = variable.getName();
				if(variableName.contains("$"))
				{
					strings.put(variableName, inputTestList.get(index));
				}
				else if(variableName.contains("%"))
				{
					floats.put(variableName, Float.parseFloat(inputTestList.get(index)));
				}
				else
				{
					integers.put(variableName, Integer.parseInt(inputTestList.get(index)));
				}
				index++;
			}
		}
		
	}
	
	private void print(PrintNode print)
	{
		if(!testMode)
		{
			for(var node : print.getNodes())
			{
				switch(evaluate(node))
				{
				case STRING:
					System.out.print(evaluateString(node));
					break;
				case FLOAT:
					System.out.print(evaluateFloat(node));
					break;
				case INT:
					System.out.print(evaluateInt(node));
					break;
				default:
					throw new IllegalArgumentException();
				}
				
			}
		}
		else
		{
			for(var node : print.getNodes())
			{
				switch(evaluate(node))
				{
				case STRING:
					printTestList.add(evaluateString(node));
					break;
				case FLOAT:
					printTestList.add(evaluateFloat(node) + "");
					break;
				case INT:
					printTestList.add(evaluateInt(node) + "");
					break;
				default:
					throw new IllegalArgumentException();
				}
				
			}
		}
		
	}
	
	
	
	public enum type
	{
		STRING, INT, FLOAT
	}
	public type evaluate(Node node)
	{
		if(node instanceof MathOpNode)
		{
			return evaluate( ((MathOpNode) node).getLeft());
		}
		if(node instanceof VariableNode)
		{
			var variable = (VariableNode) node;
			if(variable.getName().contains("$"))
			{
				return type.STRING;
			}
			if(variable.getName().contains("%"))
			{
				return type.FLOAT;
			}
			else
			{
				return type.INT;
			}
		}
		if(node instanceof FunctionNode)
		{
			var function = (FunctionNode) node;
			switch(function.getType())
			{
			case RANDOM:
				return type.INT;
			case LEFT$:
				return type.STRING;
			case RIGHT$:
				return type.STRING;
			case MID$:
				return type.STRING;
			case NUM$:
				return type.STRING;
			case VAL:
				return type.INT;
			case VAL2:
				return type.FLOAT;
			}
		}
		if(node instanceof StringNode)
		{
			return type.STRING;
		}
		if(node instanceof IntegerNode)
		{
			return type.INT;
		}
		if(node instanceof FloatNode)
		{
			return type.FLOAT;
		}
		throw new IllegalArgumentException();
	}
	
	public String evaluateString(Node node)
	{
		if(node instanceof VariableNode)
		{
			VariableNode variable = (VariableNode) node;
			if(strings.containsKey(variable.getName()))
				return strings.get(variable.getName());
			else
				throw new IllegalArgumentException();
		}
		if(node instanceof StringNode)
		{
			return node.toString();
		}
		if(node instanceof FunctionNode)
		{
			FunctionNode function = (FunctionNode) node;
			LinkedList<Node> parameters = function.getParameters();
			switch(function.getType())
			{
			case LEFT$:
				return LEFT$(evaluateString(parameters.get(0)),evaluateInt(parameters.get(1)));
			case RIGHT$:
				return RIGHT$(evaluateString(parameters.get(0)),evaluateInt(parameters.get(1)));
			case MID$:
				return MID$(evaluateString(parameters.get(0)),evaluateInt(parameters.get(1)),evaluateInt(parameters.get(1)));
			case NUM$:
				try
				{
					return NUM$(evaluateInt(parameters.get(0)));
				}
				catch(Exception e)
				{
					return NUM$(evaluateFloat(parameters.get(0)));
				}
			default:
				throw new IllegalArgumentException();
			}
		}
		throw new IllegalArgumentException();
	}
	
	public int evaluateInt(Node node)
	{
		if(node instanceof MathOpNode)
		{
			MathOpNode math = (MathOpNode) node;
			switch(math.getOperation())
			{
			case ADD:
				return evaluateInt(math.getLeft()) + evaluateInt(math.getRight());
			case SUBTRACT:
				return evaluateInt(math.getLeft()) - evaluateInt(math.getRight());
			case MULTIPLY:
				return evaluateInt(math.getLeft()) * evaluateInt(math.getRight());
			case DIVIDE:
				return evaluateInt(math.getLeft()) / evaluateInt(math.getRight());
			default:
				throw new IllegalArgumentException();
			}
		}
		if(node instanceof VariableNode)
		{
			VariableNode variable = (VariableNode) node;
			if(integers.containsKey(variable.getName()))
				return integers.get(variable.getName());
			else
				throw new IllegalArgumentException();
		}
		if(node instanceof IntegerNode) 
		{
			IntegerNode integer = (IntegerNode) node;
			return integer.getNumber();
		}
		if(node instanceof FunctionNode)
		{
			FunctionNode function = (FunctionNode) node;
			LinkedList<Node> parameters = function.getParameters();
			switch(function.getType())
			{
			case RANDOM:
				return RANDOM();
			case VAL:
				return VAL(evaluateString(parameters.get(0)));
			default:
				throw new IllegalArgumentException();
			}
		}
		throw new IllegalArgumentException();
	}
	
	public float evaluateFloat(Node node)
	{
		if(node instanceof MathOpNode)
		{
			MathOpNode math = (MathOpNode) node;
			switch(math.getOperation())
			{
			case ADD:
				return evaluateFloat(math.getLeft()) + evaluateFloat(math.getRight());
			case SUBTRACT:
				return evaluateFloat(math.getLeft()) - evaluateFloat(math.getRight());
			case MULTIPLY:
				return evaluateFloat(math.getLeft()) * evaluateFloat(math.getRight());
			case DIVIDE:
				return evaluateFloat(math.getLeft()) / evaluateFloat(math.getRight());
			default:
				throw new IllegalArgumentException();
			}
		}
		if(node instanceof VariableNode)
		{
			VariableNode variable = (VariableNode) node;
			if(floats.containsKey(variable.getName()))
				return floats.get(variable.getName());
			else
				throw new IllegalArgumentException();
		}
		if(node instanceof FloatNode) 
		{
			FloatNode floatNumber = (FloatNode) node;
			return floatNumber.getNumber();
		}
		if(node instanceof FunctionNode)
		{
			FunctionNode function = (FunctionNode) node;
			LinkedList<Node> parameters = function.getParameters();
			if(function.getType() == FunctionNode.Types.VAL2)
			{
				return VAL2(evaluateString(parameters.get(0)));
			}
			throw new IllegalArgumentException();
		}
		throw new IllegalArgumentException();
	}
	
	public int RANDOM()
	{
		Random random = new Random();
		return random.nextInt();
	}
	
	public String LEFT$(String string, int amount)
	{
		String left = "";
		for(int i = 0; i < amount; i ++)
			left += string.charAt(i);
		return left;
	}
	
	public String RIGHT$(String string, int amount)
	{
		String right = "";
		for(int i = string.length() - amount; i < string.length(); i ++)
			right += string.charAt(i);
		return right;
	}
	
	public String MID$(String string, int left, int right)
	{
		String mid = "";
		for(int i = left; i < left + right; i ++)
			mid += string.charAt(i);
		return mid;
	}
	
	public String NUM$(int number)
	{
		return number + "";
	}
	
	public String NUM$(float number)
	{
		return number + "";
	}
	
	public int VAL(String string)
	{
		if(string.contains("."))
			throw new IllegalArgumentException();
		return Integer.parseInt(string);
	}
	
	public float VAL2(String string)//VAL%
	{
		if(!string.contains("."))
			throw new IllegalArgumentException();
		return Float.parseFloat(string);
	}
	
	
	
	
}
