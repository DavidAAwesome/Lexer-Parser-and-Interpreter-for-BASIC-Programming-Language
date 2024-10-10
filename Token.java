
public class Token {

	private Types type;
	private String value;
	private int lineNumber;
	private int charPosition;
	
	public Token(Types type, int lineNumber, int charPosition)
	{
		this.type = type;
		this.lineNumber = lineNumber;
		this.charPosition = charPosition;
	}
	
	public Token(Types type, int lineNumber, int charPosition, String value)
	{
		this.type = type;
		this.lineNumber = lineNumber;
		this.charPosition = charPosition;
		this.value = value;
	}
	
	public Types getType()
	{
		return type;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public String toString()
	{
		if(value == null)
			if(type == Types.ENDOFLINE)
				return "EndOfLine";
			else
				return "" + type;
		return type + "(" + value + ")";
	}
	
	public enum Types
	{
		WORD, LABEL, NUMBER, ENDOFLINE, STRINGLITERAL, 
		//Known Words
		PRINT, READ, INPUT, DATA, GOSUB, FOR, TO, STEP, NEXT, RETURN, IF, THEN, FUNCTION, WHILE, END, 
		//Single Character
		EQUALS, LPAREN, RPAREN, LTHAN, GTHAN, PLUS, MINUS, ASTERISK, SLASH, DOLLAR, COMMA, 
		//Two Character
		LESSEQUALS, GREATEREQUALS, NOTEQUALS, 

	}
}
