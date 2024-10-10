import java.util.LinkedList;
import java.util.Optional;

public class TokenHandler {
	
	private LinkedList<Token> tokens;
	
	public TokenHandler(LinkedList<Token> tokens)
	{
		this.tokens = tokens;
	}
	
	public Optional<Token> Peek(int j)
	{
		if(j < tokens.size())
			return Optional.of(tokens.get(j));
		else
			throw new ArrayIndexOutOfBoundsException("Cannot peek " + j + "characters ahead.");
	}
	
	public boolean MoreTokens()
	{
		if(!tokens.isEmpty())
			return true;
		return false;
	}
	
	public Optional<Token> MatchAndRemove(Token.Types type)
	{
		if(tokens.isEmpty())
			return Optional.empty();
		if(tokens.getFirst().getType() == type)
		{
			Token match = tokens.remove();
			return Optional.of(match);
		}
		return Optional.empty();
	}
}
