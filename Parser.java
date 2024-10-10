import java.util.LinkedList;
import java.util.Optional;

public class Parser {

	private TokenHandler tokenHandler;
	
	public Parser(LinkedList<Token> tokens)
	{
		tokenHandler = new TokenHandler(tokens);
	}
	
	public StatementsNode Parse()
	{
		StatementsNode statementsNode = new StatementsNode();
		while(tokenHandler.MoreTokens()) 
		{
			Statements(statementsNode);
		}
		return statementsNode;
	}
	
	private void Statements(StatementsNode statementsNode)
	{
		Optional<StatementNode> statement = Statement();
		while(statement.isPresent())
		{
			statementsNode.addtoStatements(statement.get());// linked list is added to so that multiple expressions can be returned
			if(!AcceptSeperators())
				throw new IllegalArgumentException("Syntax Error");
			statement = Statement();
		}
		
	}
	
	private Optional<StatementNode> Statement()
	{
		var label = LabelStatement();
		if(label.isPresent())
			return Optional.of(label.get());
		
		var assignment = Assignment();
		if(assignment.isPresent())
			return Optional.of(assignment.get());
		
		var print = PrintStatement();
		if (print.isPresent()) 
			return Optional.of(print.get());
		
		var data = DataStatement();
		if (data.isPresent()) 
			return Optional.of(data.get());
		
		var read = ReadStatement();
		if (read.isPresent()) 
			return Optional.of(read.get());
		
		var input = InputStatement();
		if (input.isPresent()) 
			return Optional.of(input.get());
		
		var gosub = GosubStatement();
		if(gosub.isPresent())
			return Optional.of(gosub.get());
		
		var forStatement = ForStatement();
		if(forStatement.isPresent())
			return Optional.of(forStatement.get());
		
		var next = NextStatement();
		if(next.isPresent())
			return Optional.of(next.get());
		
		var ifStatement = IfStatement();
		if(ifStatement.isPresent())
			return Optional.of(ifStatement.get());
		
		var whileStatement = WhileStatement();
		if(whileStatement.isPresent())
			return Optional.of(whileStatement.get());
		
		var returnStatement = ReturnStatement();
		if(returnStatement.isPresent())
			return Optional.of(returnStatement.get());
		
		var end = EndStatement();
		if(end.isPresent())
			return Optional.of(end.get());
		
		return Optional.empty();
	}
	
	private Optional<PrintNode> PrintStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.PRINT).isEmpty())
			return Optional.empty();
		
		LinkedList<Node> toPrint = new LinkedList<>();
		PrintList(toPrint);
		return Optional.of(new PrintNode(toPrint));
	}
	
	private void PrintList(LinkedList<Node> toPrint)
	{
		do{
			var node = Expression();
			if(node.isPresent())
				toPrint.add(node.get());
			else
				throw new IllegalArgumentException("Syntax Error");
		}while(tokenHandler.MatchAndRemove(Token.Types.COMMA).isPresent());
	}
	
	private Optional<DataNode> DataStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.DATA).isEmpty())
			return Optional.empty();
		
		LinkedList<Node> data = new LinkedList<>();
		DataList(data);
		return Optional.of(new DataNode(data));
	}
	
	private void DataList(LinkedList<Node> data)
	{
		do{//This takes only numbers and string literals
			Optional<Token> token = tokenHandler.MatchAndRemove(Token.Types.NUMBER);
			if(token.isPresent())
			{
				if(token.get().getValue().contains("."))
					data.add(new FloatNode(Float.parseFloat(token.get().getValue())));
				else
					data.add(new IntegerNode(Integer.parseInt(token.get().getValue())));
				continue;
			}
			
			token = tokenHandler.MatchAndRemove(Token.Types.STRINGLITERAL);
			if(token.isPresent())
				data.add(new StringNode(token.get().getValue()));
			else
				throw new IllegalArgumentException("Syntax Error");
		}while(tokenHandler.MatchAndRemove(Token.Types.COMMA).isPresent());
	}
	
	private Optional<ReadNode> ReadStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.READ).isEmpty())
			return Optional.empty();
		
		LinkedList<VariableNode> toRead = new LinkedList<>();
		ReadList(toRead);
		return Optional.of(new ReadNode(toRead));
	}
	
	private void ReadList(LinkedList<VariableNode> toRead)
	{
		do{//This takes only variables
			Optional<Token> variable = tokenHandler.MatchAndRemove(Token.Types.WORD);
			if(variable.isPresent())
				toRead.add(new VariableNode(variable.get().getValue()));
			else
				throw new IllegalArgumentException("Syntax Error");
		}while(tokenHandler.MatchAndRemove(Token.Types.COMMA).isPresent());
	}
	
	private Optional<InputNode> InputStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.INPUT).isEmpty())
			return Optional.empty();
		
		Optional<Token> string = tokenHandler.MatchAndRemove(Token.Types.STRINGLITERAL);
		if(string.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		if(tokenHandler.MatchAndRemove(Token.Types.COMMA).isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		LinkedList<VariableNode> variables = new LinkedList<>();
		InputList(variables);
		return Optional.of(new InputNode(new StringNode(string.get().getValue()), variables));
	}
	
	private void InputList(LinkedList<VariableNode> input)
	{
		do{//This takes only variables
			Optional<Token> variable = tokenHandler.MatchAndRemove(Token.Types.WORD);
			if(variable.isPresent())
				input.add(new VariableNode(variable.get().getValue()));
			else
				throw new IllegalArgumentException("Syntax Error");
		}while(tokenHandler.MatchAndRemove(Token.Types.COMMA).isPresent());
	}
	
	private Optional<LabeledStatementNode> LabelStatement()
	{
		var label = tokenHandler.MatchAndRemove(Token.Types.LABEL);
		if(label.isEmpty())
			return Optional.empty();
		
		Optional<StatementNode> statement = Statement();
		if(statement.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		return Optional.of(new LabeledStatementNode(label.get().getValue(),statement.get()));
	}
	
	private Optional<Gosub> GosubStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.GOSUB).isEmpty())
			return Optional.empty();
		
		var identifier = tokenHandler.MatchAndRemove(Token.Types.WORD);
		if(identifier.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		return Optional.of(new Gosub(identifier.get().getValue()));
	}
	
	private Optional<Return> ReturnStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.RETURN).isEmpty())
			return Optional.empty();
		
		if(tokenHandler.Peek(0).get().getType() != Token.Types.ENDOFLINE)
			throw new IllegalArgumentException("Syntax Error");
		
		return Optional.of(new Return());
	}
	
	private Optional<For> ForStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.FOR).isEmpty())
			return Optional.empty();
		
		var assignment = Assignment();
		if(assignment.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		if(tokenHandler.MatchAndRemove(Token.Types.TO).isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		var to = tokenHandler.MatchAndRemove(Token.Types.NUMBER);
		if(to.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		if(to.get().getValue().contains("."))
			throw new IllegalArgumentException("Syntax Error");
		
		
		if(tokenHandler.MatchAndRemove(Token.Types.STEP).isEmpty())
			return Optional.of(new For(assignment.get(),Integer.parseInt(to.get().getValue())));
		
		var step = tokenHandler.MatchAndRemove(Token.Types.NUMBER);
		if(step.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		if(to.get().getValue().contains("."))
			throw new IllegalArgumentException("Syntax Error");
		
		return Optional.of(new For(assignment.get(),Integer.parseInt(to.get().getValue()),Integer.parseInt(step.get().getValue())));
	}
	
	private Optional<Next> NextStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.NEXT).isEmpty())
			return Optional.empty();
		
		var variable = tokenHandler.MatchAndRemove(Token.Types.WORD);
		if(variable.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		var variableNode =  new VariableNode(variable.get().getValue());
		
		return Optional.of(new Next(variableNode));
	}
	
	private Optional<End> EndStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.END).isEmpty())
			return Optional.empty();
		return Optional.of(new End());
	}
	
	private Optional<If> IfStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.IF).isEmpty())
			return Optional.empty();
		
		var booleanExpression = BooleanExpression();
		if(booleanExpression.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		if(tokenHandler.MatchAndRemove(Token.Types.THEN).isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		var label = tokenHandler.MatchAndRemove(Token.Types.WORD);
		if(label.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		return Optional.of(new If(booleanExpression.get(),label.get().getValue()));
		
	}
	
	private Optional<While> WhileStatement()
	{
		if(tokenHandler.MatchAndRemove(Token.Types.WHILE).isEmpty())
			return Optional.empty();
		
		var booleanExpression = BooleanExpression();
		if(booleanExpression.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		
		var label = tokenHandler.MatchAndRemove(Token.Types.WORD);
		if(label.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		return Optional.of(new While(booleanExpression.get(),label.get().getValue()));
	}
	
	private Optional<FunctionNode> FunctionInvocation(Optional<Token> name)
	{
		if(tokenHandler.MatchAndRemove(Token.Types.LPAREN).isEmpty())
			return Optional.empty();
		if(name.isEmpty())
			throw new IllegalArgumentException("Syntax Error");
		FunctionNode.Types type;
		switch(name.get().getValue())
		{
		case "RANDOM":
			type = FunctionNode.Types.RANDOM;
			break;
		case "LEFT$":
			type = FunctionNode.Types.LEFT$;
			break;
		case "RIGHT$":
			type = FunctionNode.Types.RIGHT$;
			break;
		case "MID$":
			type = FunctionNode.Types.MID$;
			break;
		case "NUM$":
			type = FunctionNode.Types.NUM$;
			break;
		case "VAL":
			type = FunctionNode.Types.VAL;
			break;
		case "VAL%":
			type = FunctionNode.Types.VAL2;
			break;
		default:
			throw new IllegalArgumentException("Syntax Error");
		}
		LinkedList<Node> parameters = new LinkedList<Node>();
		FunctionList(parameters);
		return Optional.of(new FunctionNode(type,parameters));
	}
	
	private void FunctionList(LinkedList<Node> list)
	{
		int count = 0;
		do{
			var expression = Expression();
			if(expression.isPresent())
			{
				list.add(expression.get());
				count++;
			}
			else if(count == 0)
				return;
			else
				throw new IllegalArgumentException("Syntax Error");
		}while(tokenHandler.MatchAndRemove(Token.Types.COMMA).isPresent());
	}
	
	private Optional<AssignmentNode> Assignment()
	{
		var variableName = tokenHandler.MatchAndRemove(Token.Types.WORD);
		if(variableName.isEmpty())
			return Optional.empty();
		VariableNode variable = new VariableNode(variableName.get().getValue());
		
		Optional<Token> current = tokenHandler.MatchAndRemove(Token.Types.EQUALS);
		if(current.isPresent())
		{
			var expression = Expression();
			if(expression.isEmpty())
				throw new IllegalArgumentException("Syntax Error");
			return Optional.of(new AssignmentNode(variable, expression.get()));
		}
		else
			throw new IllegalArgumentException("Need an equals after word for the current iteration of the program");
	}
	
	private boolean AcceptSeperators()
	{
		int seperators = 0;
		while(tokenHandler.MoreTokens())
		{
			if(tokenHandler.MatchAndRemove(Token.Types.ENDOFLINE).isPresent())
				seperators++;
			else
				break;
		}
		if(seperators > 0)
			return true;
		return false;
	}
	
	private Optional<Node> BooleanExpression()
	{
		var firstExpression = Expression();
		if(firstExpression.isEmpty())
			return Optional.empty();
		Node booleanExpression = firstExpression.get();
		
		if(tokenHandler.MatchAndRemove(Token.Types.GTHAN).isPresent())
		{
			var expression = Expression();
			if(expression.isEmpty())
				throw new IllegalArgumentException("Syntax Error");
			return Optional.of(new BoolOpNode(booleanExpression, BoolOpNode.Operations.GTHAN, expression.get()));
		}
		if(tokenHandler.MatchAndRemove(Token.Types.GREATEREQUALS).isPresent())
		{
			var expression = Expression();
			if(expression.isEmpty())
				throw new IllegalArgumentException("Syntax Error");
			return Optional.of(new BoolOpNode(booleanExpression, BoolOpNode.Operations.GREATEREQUALS, expression.get()));
		}
		if(tokenHandler.MatchAndRemove(Token.Types.LTHAN).isPresent())
		{
			var expression = Expression();
			if(expression.isEmpty())
				throw new IllegalArgumentException("Syntax Error");
			return Optional.of(new BoolOpNode(booleanExpression, BoolOpNode.Operations.LTHAN, expression.get()));
		}
		if(tokenHandler.MatchAndRemove(Token.Types.LESSEQUALS).isPresent())
		{
			var expression = Expression();
			if(expression.isEmpty())
				throw new IllegalArgumentException("Syntax Error");
			return Optional.of(new BoolOpNode(booleanExpression, BoolOpNode.Operations.LESSEQUALS, expression.get()));
		}
		if(tokenHandler.MatchAndRemove(Token.Types.NOTEQUALS).isPresent())
		{
			var expression = Expression();
			if(expression.isEmpty())
				throw new IllegalArgumentException("Syntax Error");
			return Optional.of(new BoolOpNode(booleanExpression, BoolOpNode.Operations.NOTEQUALS, expression.get()));
		}
		if(tokenHandler.MatchAndRemove(Token.Types.EQUALS).isPresent())
		{
			var expression = Expression();
			if(expression.isEmpty())
				throw new IllegalArgumentException("Syntax Error");
			return Optional.of(new BoolOpNode(booleanExpression, BoolOpNode.Operations.EQUALS, expression.get()));
		}
		throw new IllegalArgumentException("Syntax Error");
		
	}
	
	private Optional<Node> Expression()
	{
		var firstTerm = Term();
		if(firstTerm.isEmpty())
			return Optional.empty();
		Node expression = firstTerm.get();
		
		while(tokenHandler.MoreTokens())
		{
			if(tokenHandler.MatchAndRemove(Token.Types.PLUS).isPresent())
			{
				var term = Term();
				if(term.isEmpty())
					throw new IllegalArgumentException("Syntax Error");
				expression = new MathOpNode(expression, MathOpNode.Operations.ADD, term.get());
			}
			else if(tokenHandler.MatchAndRemove(Token.Types.MINUS).isPresent())
			{
				var term = Term();
				if(term.isEmpty())
					throw new IllegalArgumentException("Syntax Error");
				expression = new MathOpNode(expression, MathOpNode.Operations.SUBTRACT, term.get());
			}
			else
				break;
		}
		return Optional.of(expression);
	}
	
	private Optional<Node> Term()
	{
		var firstFactor = Factor();
		if(firstFactor.isEmpty())
			return Optional.empty();
		Node term = firstFactor.get();
		while(tokenHandler.MoreTokens())
		{
			if(tokenHandler.MatchAndRemove(Token.Types.ASTERISK).isPresent())
			{
				var factor = Factor();
				if(factor.isEmpty())
					throw new IllegalArgumentException("Syntax Error");
				term = new MathOpNode(term, MathOpNode.Operations.MULTIPLY, factor.get());
			}
			else if(tokenHandler.MatchAndRemove(Token.Types.SLASH).isPresent())
			{
				var factor = Factor();
				if(factor.isEmpty())
					throw new IllegalArgumentException("Syntax Error");
				term = new MathOpNode(term, MathOpNode.Operations.DIVIDE, factor.get());
			}
			else
				break;
		}
		return Optional.of(term);
	}
	
	private Optional<Node> Factor()
	{
		Optional<Token> currentToken = tokenHandler.MatchAndRemove(Token.Types.NUMBER);
		if(currentToken.isPresent())
		{
			String tokenValue = currentToken.get().getValue();
			if(tokenValue.contains("."))
				return Optional.of(new FloatNode(Float.parseFloat(tokenValue)));
			return Optional.of(new IntegerNode(Integer.parseInt(tokenValue)));
		}
		
		currentToken = tokenHandler.MatchAndRemove(Token.Types.WORD);
		if(currentToken.isPresent())
		{
			var function = FunctionInvocation(currentToken);
			if(function.isPresent())
			{
				if(tokenHandler.MatchAndRemove(Token.Types.RPAREN).isEmpty())
					throw new IllegalArgumentException("Syntax Error");
				return Optional.of(function.get());
			}
			else
				return Optional.of(new VariableNode(currentToken.get().getValue()));
		}
		
		currentToken = tokenHandler.MatchAndRemove(Token.Types.STRINGLITERAL);
		if(currentToken.isPresent())
			return Optional.of(new StringNode(currentToken.get().getValue()));
		
		if(tokenHandler.MatchAndRemove(Token.Types.LPAREN).isPresent())
		{
			var expression = Expression();
			if(expression.isEmpty())
				throw new IllegalArgumentException("Syntax Error");
			Node factor = expression.get();
			if(tokenHandler.MatchAndRemove(Token.Types.RPAREN).isEmpty())
				throw new IllegalArgumentException("No right parenthesis found for left parenthesis.");
			return Optional.of(factor);
		}
		return Optional.empty();
	}
	
	
}
