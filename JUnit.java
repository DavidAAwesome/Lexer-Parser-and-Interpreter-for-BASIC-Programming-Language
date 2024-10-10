import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import org.junit.Assert;

class JUnit {

	@Test
	public void GeneralTest() throws Exception{
		Lexer lexer = new Lexer("src/text.txt");
		LinkedList<Token> tokens = lexer.lex("bin/text.txt");
		String result = "";
		for(var token : tokens)
			result += token + " ";
		Assert.assertEquals("EndOfLine NUMBER(5) WORD(hello) EndOfLine NUMBER(5.23) NUMBER(8.5) NUMBER(3) EndOfLine NUMBER(8) NUMBER(4) NUMBER(99999) EndOfLine NUMBER(7) NUMBER(4) NUMBER(3) NUMBER(1) EndOfLine NUMBER(2) WORD(number) NUMBER(3) EndOfLine ", result);
		
	}
	
	@Test
	public void DecimalTextTest() throws Exception{
		Lexer lexer = new Lexer("src/decimalText.txt");
		LinkedList<Token> tokens = lexer.lex("bin/decimalText.txt");
		String result = "";
		for(var token : tokens)
			result += token + " ";
		Assert.assertEquals("NUMBER(5.) EndOfLine NUMBER(.01) NUMBER(.6) EndOfLine NUMBER(5055) WORD(daniel4) NUMBER(3.) NUMBER(.4) EndOfLine ", result);
		
	}
	
	@Test
	public void StringTextTest() throws Exception{
		Lexer lexer = new Lexer("src/stringText.txt");
		LinkedList<Token> tokens = lexer.lex("bin/stringText.txt");
		String result = "";
		for(var token : tokens)
			result += token + " ";
		Assert.assertEquals("WORD(one45_) WORD(man5) WORD(hola_) WORD(mok5th) WORD(st_tg) WORD(man$) WORD(and%) EndOfLine ", result);
		
	}
	
	@Test
	public void CodeHandlerPeekStringTest() throws Exception{
		CodeHandler codeHandler = new CodeHandler("src/codeHandlerTester.txt");
		Assert.assertEquals("happy love", codeHandler.PeekString(10));
		codeHandler.Swallow(1);
		Assert.assertEquals("appy lov", codeHandler.PeekString(8));
	}
	
	@Test
	public void CodeHandlerRemainderTest() throws Exception{
		CodeHandler codeHandler = new CodeHandler("src/codeHandlerTester.txt");
		Assert.assertEquals("happy love", codeHandler.Remainder());
		codeHandler.Swallow(1);
		Assert.assertEquals("appy love", codeHandler.Remainder());
	}
	
	@Test
	public void knownWordsTextTest() throws Exception{
		Lexer lexer = new Lexer("src/knowWordsText.txt");
		LinkedList<Token> tokens = lexer.lex("src/knowWordsText.txt");
		String result = "";
		for(var token : tokens)
			result += token + " ";
		Assert.assertEquals("IF WHILE WORD(manyFOR) PRINT RETURN EndOfLine ", result);
		
	}
	
	@Test
	public void stringLiteralTest() throws Exception{
		Lexer lexer = new Lexer("src/stringLiteralTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/stringLiteralTest.txt");
		String result = "";
		for(var token : tokens)
			result += token + " ";
		Assert.assertEquals("STRINGLITERAL(happy) EndOfLine STRINGLITERAL(\"lol\") STRINGLITERAL() EndOfLine STRINGLITERAL(\"\"\") EndOfLine STRINGLITERAL(\\\r\n"
				+ "	\") EndOfLine ", result);
		
	}
	
	@Test
	public void symbolTest() throws Exception{
		Lexer lexer = new Lexer("src/symbolTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/symbolTest.txt");
		String result = "";
		for(var token : tokens)
			result += token + " ";
		Assert.assertEquals("ASTERISK MINUS PLUS EQUALS LESSEQUALS LTHAN EndOfLine GTHAN GTHAN NUMBER(4) PLUS PLUS NUMBER(6) MINUS WORD(k) ASTERISK WORD(l) EndOfLine ", result);
		
	}
	
	@Test
	public void labelTest() throws Exception{
		Lexer lexer = new Lexer("src/labelTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/labelTest.txt");
		String result = "";
		for(var token : tokens)
			result += token + " ";
		Assert.assertEquals("LABEL(wayat:) LABEL(lmaooo:) LABEL(G420___69lmao:) EndOfLine ", result);
		
	}
	
	@Test
	public void mathOpNodeTest() throws Exception{
		Lexer lexer = new Lexer("src/mathOpNodeTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/mathOpNodeTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Assert.assertEquals("c = ((2*5)+(6.2/(4-3))) d = (4+(3*6)) e = (5/10) f = (25-(2/6.9)) ", statementsNode.toString());
		
	}
	
	@Test
	public void printAssignmentTest() throws Exception{
		Lexer lexer = new Lexer("src/printAssignmentTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/printAssignmentTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Assert.assertEquals("man = (3+(5*can)) PRINT man  PRINT lol can (4+3) ((9*25)+6)  ", statementsNode.toString());
		
	}
	
	@Test
	public void dataReadTest() throws Exception{
		Lexer lexer = new Lexer("src/dataReadTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/dataReadTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Assert.assertEquals("DATA peace 10 15  READ lol can man  ", statementsNode.toString());
		
	}
	
	
	@Test 
	public void inputTest() throws Exception{
		Lexer lexer = new Lexer("src/inputTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/inputTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Assert.assertEquals("INPUT brothel super sand  INPUT country test  ", statementsNode.toString());
		
	}
	
	@Test
	public void finalParserTest() throws Exception{
		Lexer lexer = new Lexer("src/finalParserTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/finalParserTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Assert.assertEquals("label: var = RANDOM([]) GOSUB label FOR var = 0 TO 20 STEP 5 NEXT var IF (20<VAL([6])) THEN label WHILE (5<6) endWhileLabel endWhileLabel: var = nice RETURN END ", statementsNode.toString());
	}
	
	@Test
	public void OptimizationTest() throws Exception{
		Lexer lexer = new Lexer("src/OptimizationTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/OptimizationTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Interpreter interpreter = new Interpreter(statementsNode);
		interpreter.optimize();
		Assert.assertEquals("[peace, 10, 15, create, hierarchy]", interpreter.data.toString());
		//Assert.assertEquals("{bomb:=bomb: save = 2, label:=label: man = 5, phantom:=phantom: DATA create hierarchy }", interpreter.labels.toString());
	}
	
	@Test
	public void InterpreterFunctionTest() throws Exception{
		StatementsNode statementsNode = new StatementsNode();
		Interpreter interpreter = new Interpreter(statementsNode);
		
		Assert.assertNotEquals(interpreter.RANDOM(),interpreter.RANDOM());
		
		Assert.assertEquals("fi", interpreter.LEFT$("five", 2));
		Assert.assertEquals("five", interpreter.LEFT$("five", 4));
		Assert.assertEquals("", interpreter.LEFT$("five", 0));
		
		Assert.assertEquals("ve", interpreter.RIGHT$("five", 2));
		Assert.assertEquals("five", interpreter.RIGHT$("five", 4));
		Assert.assertEquals("", interpreter.RIGHT$("five", 0));
		
		Assert.assertEquals("i", interpreter.MID$("five", 1,1));
		Assert.assertEquals("iv", interpreter.MID$("five", 1,2));
		
		Assert.assertEquals("20", interpreter.NUM$(20));
		Assert.assertEquals("20.0", interpreter.NUM$((float)20.0));
		
		Assert.assertEquals(20, interpreter.VAL("20"));
		Assert.assertEquals(20.0, interpreter.VAL2("20.0"), 0);
	}
	
	@Test
	public void InterpreterInputPrintTest() throws Exception{
		Lexer lexer = new Lexer("src/interpreterInputPrintTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/interpreterInputPrintTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Interpreter interpreter = new Interpreter(statementsNode, true);
		interpreter.inputTestList = new LinkedList<String>();
		interpreter.inputTestList.add("can");
		interpreter.inputTestList.add("5");
		interpreter.inputTestList.add("5.6");
		interpreter.printTestList = new LinkedList<String>();
		interpreter.interpret(statementsNode);
		Assert.assertEquals("[can, 5, 5.6, Give a string, integer and float]", interpreter.inputTestList.toString());
		Assert.assertEquals("[can, 5, 5.6]", interpreter.printTestList.toString());
	}
	
	@Test
	public void InterpreterDataReadTest() throws Exception{
		Lexer lexer = new Lexer("src/interpreterDataReadTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/interpreterDataReadTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Interpreter interpreter = new Interpreter(statementsNode);
		interpreter.interpret(statementsNode);
		Assert.assertEquals("{question=756, NUM=2, FOUR=4}", interpreter.integers.toString());
		Assert.assertEquals("{val%=24.9, THREEPOINTFIVE%=3.5}", interpreter.floats.toString());
		Assert.assertEquals("{VAR$=crying}", interpreter.strings.toString());
	}
	
	@Test
	public void InterpreterAssignmentTest() throws Exception{
		Lexer lexer = new Lexer("src/interpreterAssignmentTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/interpreterAssignmentTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Interpreter interpreter = new Interpreter(statementsNode);
		interpreter.interpret(statementsNode);
		Assert.assertEquals("{RAND=5, addition2=17, VAR=5, addition=7}", interpreter.integers.toString());
		Assert.assertEquals("{addition%=5.0, TRIANGLE%=200.5}", interpreter.floats.toString());
		Assert.assertEquals("{BRING$=NICETIES, SWING$=3.5, creation$=NICET, TWIG$=3}", interpreter.strings.toString());
	}
	
//	@Test
//	public void InterpreterIfGosubReturnTest() throws Exception{
//		Lexer lexer = new Lexer("src/IfGosubReturnTest.txt");
//		LinkedList<Token> tokens = lexer.lex("src/IfGosubReturnTest.txt");
//		Parser parser = new Parser(tokens);
//		StatementsNode statementsNode = parser.Parse();
//		Interpreter interpreter = new Interpreter(statementsNode, true);
//		interpreter.printTestList = new LinkedList<String>();
//		interpreter.interpret(statementsNode);
//		Assert.assertEquals("[END, finish, RETURNED]", interpreter.printTestList.toString());
//	}
	
	@Test
	public void InterpreterForNextTest() throws Exception{
		Lexer lexer = new Lexer("src/InterpreterForNextTest.txt");
		LinkedList<Token> tokens = lexer.lex("src/InterpreterForNextTest.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Interpreter interpreter = new Interpreter(statementsNode, true);
		interpreter.printTestList = new LinkedList<String>();
		interpreter.interpret(statementsNode);
		Assert.assertEquals("[2, 4, 6, 8, 10, 1, -, -, 2, -, -, 3, -, -, 4, -, -, 5, -, -]", interpreter.printTestList.toString());
	}
	
	@Test
	public void ClassAverageFromAssignment0Test() throws Exception{
		Lexer lexer = new Lexer("src/ClassAverageFromAssignment0Test.txt");
		LinkedList<Token> tokens = lexer.lex("src/ClassAverageFromAssignment0Test.txt");
		Parser parser = new Parser(tokens);
		StatementsNode statementsNode = parser.Parse();
		Interpreter interpreter = new Interpreter(statementsNode, true);
		interpreter.printTestList = new LinkedList<String>();
		interpreter.interpret(statementsNode);
		Assert.assertEquals("[Student1, : , 86, Student2, : , 83, Student3, : , 87, Student4, : , 81, Student5, : , 85]", interpreter.printTestList.toString());
		
	}

}
