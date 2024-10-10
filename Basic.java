import java.util.LinkedList;
public class Basic {

	
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.err.println("Error: Provide one filename/path as an argument");
			System.exit(1);
		}
		
		String filename = args[0];
		Lexer lexer = new Lexer(filename);
		LinkedList<Token> tokens = lexer.lex(filename);
		for(var token : tokens)
			System.out.print(token + " ");
		System.out.println();
		
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		System.out.println(statementsNode);
	}
}
