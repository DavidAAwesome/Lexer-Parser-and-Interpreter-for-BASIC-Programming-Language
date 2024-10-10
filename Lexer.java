import java.util.HashMap;
import java.util.LinkedList;
public class Lexer {

	private CodeHandler codeHandler;
	private int lineNumber;
	private int charPosition;
	private HashMap<String, Token.Types> knownWords = new HashMap<>();
	private HashMap<Character, Token.Types> oneCharacter = new HashMap<>();
	private HashMap<String, Token.Types> twoCharacter = new HashMap<>();
	
	public Lexer(String filename)
	{
		codeHandler = new CodeHandler(filename);
		lineNumber = 1;
		charPosition = 0;
		CreateKnownWords();
		OneCharacterSymbols();
		TwoCharacterSymbols();
	}
	
	private void CreateKnownWords()
	{
		knownWords.put("PRINT",Token.Types.PRINT);
		knownWords.put("READ",Token.Types.READ);
		knownWords.put("INPUT",Token.Types.INPUT);
		knownWords.put("DATA",Token.Types.DATA);
		knownWords.put("GOSUB",Token.Types.GOSUB);
		knownWords.put("FOR",Token.Types.FOR);
		knownWords.put("TO",Token.Types.TO);
		knownWords.put("STEP",Token.Types.STEP);
		knownWords.put("NEXT",Token.Types.NEXT);
		knownWords.put("RETURN",Token.Types.RETURN);
		knownWords.put("IF",Token.Types.IF);
		knownWords.put("THEN",Token.Types.THEN);
		knownWords.put("FUNCTION",Token.Types.FUNCTION);
		knownWords.put("WHILE",Token.Types.WHILE);
		knownWords.put("END",Token.Types.END);
	}
	
	public void OneCharacterSymbols()
	{
		oneCharacter.put('=', Token.Types.EQUALS);
		oneCharacter.put('(', Token.Types.LPAREN);
		oneCharacter.put(')', Token.Types.RPAREN);
		oneCharacter.put('<', Token.Types.LTHAN);
		oneCharacter.put('>', Token.Types.GTHAN);
		oneCharacter.put('+', Token.Types.PLUS);
		oneCharacter.put('-', Token.Types.MINUS);
		oneCharacter.put('*', Token.Types.ASTERISK);
		oneCharacter.put('/', Token.Types.SLASH);
		oneCharacter.put('$', Token.Types.DOLLAR);
		oneCharacter.put(',', Token.Types.COMMA);
		
	}
	
	public void TwoCharacterSymbols()
	{
		twoCharacter.put("<=", Token.Types.LESSEQUALS);
		twoCharacter.put(">=", Token.Types.GREATEREQUALS);
		twoCharacter.put("<>", Token.Types.NOTEQUALS);
	}
	
	public LinkedList<Token> lex(String filename)
	{
		LinkedList<Token> tokens = new LinkedList<>();
		char current;
		
		while(!codeHandler.isDone())
		{
			current = codeHandler.Peek(0);
			switch(current)
			{
			case (' '):
				codeHandler.Swallow(1);
				charPosition++;
				break;
			case ('\t'):
				codeHandler.Swallow(1);
				charPosition++;
				break;
			case ('\n'):
				tokens.add(new Token(Token.Types.ENDOFLINE, lineNumber, charPosition));
				codeHandler.Swallow(1);
				lineNumber ++;
				charPosition = 0;
				break;
			case ('\r'):
				codeHandler.Swallow(1);
				charPosition++;
				break;
			case ('"'):
				tokens.add(HandleStringLiteral());
				break;
			default:
				if(Character.isLetter(current) || current == '_')
					tokens.add(ProcessWord());
				else if(Character.isDigit(current) || current == '.')
					tokens.add(ProcessDigit());
				else
					tokens.add(ProcessSymbol());
				break;
			}
		}
		tokens.add(new Token(Token.Types.ENDOFLINE, lineNumber, charPosition));
		return tokens;
	}
	
	public Token HandleStringLiteral()
	{
		codeHandler.Swallow(1);
		charPosition++;
		if(codeHandler.isDone())
			throw new StringIndexOutOfBoundsException("\" needs a closing \" on line " + lineNumber + ", index" + (charPosition-1));
		char current = codeHandler.Peek(0);
		String literal = "";
		while(current != '"')
		{
			if(current == '\\')
			{
				codeHandler.Swallow(1);
				charPosition++;
				if(codeHandler.isDone())
					throw new StringIndexOutOfBoundsException("\" needs a closing \" on line " + lineNumber + ", index" + charPosition);
				current = codeHandler.Peek(0);
				switch (current)
				{
				case ('"'):
					literal += current;
					break;
				case ('\\'):
					literal += current;
					break;
				case ('t'):
					literal += '\t';
					break;
				case ('n'):
					literal += '\n';
					break;
				case ('r'):
					literal += '\r';
					break;
				default:
					throw new IllegalArgumentException("Syntax error for \\ on line " + lineNumber + ", index" + (charPosition-1));
				}
				codeHandler.Swallow(1);
				charPosition++;
				if(codeHandler.isDone())
					throw new StringIndexOutOfBoundsException("\" needs a closing \" on line " + lineNumber + ", index" + charPosition);
				current = codeHandler.Peek(0);
			}
			else
			{
				literal += current;
				codeHandler.Swallow(1);
				charPosition++;
				if(codeHandler.isDone())
					throw new StringIndexOutOfBoundsException("\" needs a closing \" on line " + lineNumber + ", index" + charPosition);
				current = codeHandler.Peek(0);
			}
			
		}
		// Moves forward through the string to go back to lex on the next character
		codeHandler.Swallow(1);
		charPosition++;
		return new Token(Token.Types.STRINGLITERAL, lineNumber, charPosition-1, literal);
		
	}
	
	public Token ProcessWord()
	{
		char current = codeHandler.Peek(0);
		String word = "";
		while(Character.isLetter(current) || Character.isDigit(current) || current == '_')
		{
			word += current;
			codeHandler.Swallow(1);
			charPosition++;
			if(codeHandler.isDone())// Checking to see if done before peeking to prevent errors.
				break;
			current = codeHandler.Peek(0);
		}
		if(current == '$' || current == '%')
		{
			word += current;
			codeHandler.Swallow(1);
			charPosition++;
		}
		if(current == ':')
		{
			word += current;
			codeHandler.Swallow(1);
			charPosition++;
			return new Token(Token.Types.LABEL, lineNumber, charPosition-1, word);
		}
		if(knownWords.containsKey(word))
			return new Token(knownWords.get(word), lineNumber, charPosition-1);
		return new Token(Token.Types.WORD, lineNumber, charPosition-1, word);
	}
	
	public Token ProcessDigit()
	{
		char current = codeHandler.Peek(0);
		String word = "";
		int decimalFlag = 0;
		while(Character.isDigit(current) || current == '.')
		{
			if(current == '.')
			{
				if(decimalFlag > 0)
					break;
				else
					decimalFlag++;
			}
			word += current;
			codeHandler.Swallow(1);
			charPosition++;
			if(codeHandler.isDone())// Checking to see if done before peeking to prevent errors.
				break;
			current = codeHandler.Peek(0);
		}
		if(word.equals(".")) // Useful if there is a single . present in the file.
			throw new NumberFormatException("Cannot have a single \".\" in the text file.");
		return new Token(Token.Types.NUMBER, lineNumber, charPosition-1, word);
	}
	
	public Token ProcessSymbol()
	{
		char current = codeHandler.Peek(0);
		String symbol = "";
		symbol += current;
		codeHandler.Swallow(1);
		charPosition++;
		if(codeHandler.isDone())
			if(oneCharacter.containsKey(current))
				return new Token(oneCharacter.get(current),lineNumber, charPosition-1);
			else
				throw new IllegalArgumentException("Syntax error at line " + lineNumber + ", index " + (charPosition-1) + ".");
		char next = codeHandler.Peek(0);
		symbol += next;
		// Checks for two character symbols first in case the first character of the two character is a one character
		if(twoCharacter.containsKey(symbol))
		{
			codeHandler.Swallow(1);
			charPosition++;
			return new Token(twoCharacter.get(symbol), lineNumber, charPosition-1);
		}
		else if(oneCharacter.containsKey(current))
			return new Token(oneCharacter.get(current),lineNumber,charPosition-1);
		else
			throw new IllegalArgumentException("Syntax error at line " + lineNumber + ", index " + (charPosition-1) + ".");
	}
	
}
