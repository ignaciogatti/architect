package architect.engine.mast.parser;

import java.util.ArrayList;
import java.util.List;

import architect.engine.mast.Result;
import architect.engine.mast.TimeResult;
import architect.engine.mast.Transaction;



/**
 * @author ariel
 */
public class MastParser {

	private IndexBuffer   tokenBuffer   = null;
	private IndexBuffer   elementBuffer = null;
	private int           elementIndex  = 0;
	private MastTokenizer mastTokenizer = null;
	
	public MastParser() {
		this(new IndexBuffer(8192,true), new IndexBuffer(8192,true));
	}

	public MastParser(IndexBuffer tokenBuffer, IndexBuffer elementBuffer) {
		this.tokenBuffer   = tokenBuffer;
		this.mastTokenizer = new MastTokenizer(this.tokenBuffer);
		this.elementBuffer = elementBuffer;
	}


	public IndexBuffer parse(DataCharBuffer dataBuffer) {
		this.elementIndex  = 0;

		this.mastTokenizer.reinit(dataBuffer, this.tokenBuffer);

		parseObject(this.mastTokenizer);

		this.elementBuffer.count = this.elementIndex;

		return this.elementBuffer;
	}

	private void parseObject(MastTokenizer tokenizer) {
		assertHasMoreTokens(tokenizer);
		tokenizer.parseToken();
		// Debe comenzar con un String (Transaction)
		assertThisTokenType(tokenizer.tokenType(), TokenTypes.MAST_STRING_TOKEN);
		setElementData(tokenizer, ElementTypes.MAST_OBJECT_START);


		tokenizer.nextToken();
		tokenizer.parseToken();
		byte tokenType = tokenizer.tokenType();

		assertThisTokenType(tokenType, TokenTypes.MAST_PARENTHESIS_LEFT); // No lo agrego como elemento

		tokenizer.nextToken();
		tokenizer.parseToken();
		tokenType = tokenizer.tokenType();

		while( tokenType != TokenTypes.MAST_SEMICOLON) {

			assertThisTokenType(tokenType, TokenTypes.MAST_STRING_TOKEN);
			setElementData(tokenizer, ElementTypes.MAST_PROPERTY_NAME);

			tokenizer.nextToken();
			tokenizer.parseToken();
			tokenType = tokenizer.tokenType();

			assertThisTokenType(tokenType, TokenTypes.MAST_ARROW);

			tokenizer.nextToken();
			tokenizer.parseToken();
			tokenType = tokenizer.tokenType();

			switch(tokenType) {
			case TokenTypes.MAST_STRING_TOKEN       : { setElementData(tokenizer, ElementTypes.MAST_PROPERTY_VALUE_STRING);    } break;
			case TokenTypes.MAST_NUMBER_TOKEN       : { setElementData(tokenizer, ElementTypes.MAST_PROPERTY_VALUE_NUMBER);    } break;
			case TokenTypes.MAST_PARENTHESIS_LEFT 	: { ; } break;
			case TokenTypes.MAST_PARENTHESIS_RIGHT 	: { ; } break;
			}

			tokenizer.nextToken();
			tokenizer.parseToken();
			tokenType = tokenizer.tokenType();

			// Puede encontrar comas y parentesis anidados (listas) los omite a todos.
			while (tokenType == TokenTypes.MAST_COMMA || tokenType == TokenTypes.MAST_PARENTHESIS_LEFT || tokenType == TokenTypes.MAST_PARENTHESIS_RIGHT) {
				tokenizer.nextToken();  //skip , tokens if found here.
				tokenizer.parseToken();
				tokenType = tokenizer.tokenType();
			}

			/*switch(tokenType) {
				case TokenTypes.MAST_STRING_TOKEN       : { setElementData(tokenizer, ElementTypes.MAST_PROPERTY_VALUE_STRING);    } break;
				case TokenTypes.MAST_NUMBER_TOKEN       : { setElementData(tokenizer, ElementTypes.MAST_PROPERTY_VALUE_NUMBER);    } break;
				case TokenTypes.MAST_PARENTHESIS_LEFT 	: {	setElementData(tokenizer, ElementTypes.MAST_LIST_START); } break;
				case TokenTypes.MAST_PARENTHESIS_RIGHT 	: { setElementData(tokenizer, ElementTypes.MAST_LIST_END); } break;
			}

			tokenizer.nextToken();
			tokenizer.parseToken();
			tokenType = tokenizer.tokenType();

			// Puede encontrar comas y parentesis anidados (listas) los omite a todos.
			while (tokenType == TokenTypes.MAST_PARENTHESIS_RIGHT) {
				tokenizer.nextToken();  //skip , tokens if found here.
				tokenizer.parseToken();
				tokenType = tokenizer.tokenType();
				setElementData(tokenizer, ElementTypes.MAST_LIST_END);
			}

			if (tokenType == TokenTypes.MAST_COMMA) {
				tokenizer.nextToken();  //skip , tokens if found here.
				tokenizer.parseToken();
				tokenType = tokenizer.tokenType();
			}*/

		}
		//setElementData(tokenizer, ElementTypes.MAST_OBJECT_END);
	}


	private void parseList(MastTokenizer tokenizer) {
		setElementData(tokenizer, ElementTypes.MAST_LIST_START);
		int parenthesisCount = 1;

		tokenizer.nextToken();
		tokenizer.parseToken();

		while((tokenizer.tokenType() != TokenTypes.MAST_PARENTHESIS_RIGHT) || (parenthesisCount > 0) ) {

			byte tokenType = tokenizer.tokenType(); // extracted only for debug purposes.


			switch(tokenType) {
			case TokenTypes.MAST_PARENTHESIS_LEFT	: { parenthesisCount++; break; } 
			case TokenTypes.MAST_STRING_TOKEN       : { parseObject(tokenizer);} break;
			//case TokenTypes.JSON_STRING_ENC_TOKEN   : { setElementData(tokenizer, ElementTypes.JSON_ARRAY_VALUE_STRING_ENC);} break;
			//case TokenTypes.MAST_NUMBER_TOKEN       : { setElementData(tokenizer, ElementTypes.MAST_PROPERTY_VALUE_NUMBER);    } break;
			//case TokenTypes.JSON_BOOLEAN_TOKEN      : { setElementData(tokenizer, ElementTypes.JSON_ARRAY_VALUE_BOOLEAN);   } break;
			//case TokenTypes.JSON_NULL_TOKEN         : { setElementData(tokenizer, ElementTypes.JSON_ARRAY_VALUE_NULL);      } break;
			//case TokenTypes.JSON_CURLY_BRACKET_LEFT : { parseObject(tokenizer); } break;
			// todo add arrays in arrays support
			}


			tokenizer.nextToken();
			tokenizer.parseToken();
			tokenType = tokenizer.tokenType();
			if(tokenType == TokenTypes.MAST_COMMA) {
				tokenizer.nextToken();
				tokenizer.parseToken();
				tokenType = tokenizer.tokenType();
			}
		}

		setElementData(tokenizer, ElementTypes.MAST_LIST_END);
	}

	private void setElementData(MastTokenizer tokenizer, byte elementType) {
		this.elementBuffer.position[this.elementIndex] = tokenizer.tokenPosition();
		this.elementBuffer.length  [this.elementIndex] = tokenizer.tokenLength();
		this.elementBuffer.type    [this.elementIndex] = elementType;
		this.elementIndex++;
	}

	private final void assertThisTokenType(byte tokenType, byte expectedTokenType) {
		if(tokenType != expectedTokenType) {
			throw new ParserException("Token type mismatch: Expected " + expectedTokenType + " but found " + tokenType);
		}
	}


	private void assertHasMoreTokens(MastTokenizer tokenizer) {
		if(! tokenizer.hasMoreTokens()) {
			throw new ParserException("Expected more tokens available in the tokenizer");
		}
	}

	public Transaction parseTransaction(String t) {
				
		DataCharBuffer buffer = new DataCharBuffer(t.toCharArray());
		IndexBuffer result = this.parse(buffer);

		List<String> elements = new ArrayList<String>();
		// Obtiene los elementos
		for(int i = 0; i < result.count; i++)
			elements.add(t.substring(result.position[i], result.position[i] + result.length[i]));

		// Crea la transacción
		Transaction transaction = new Transaction();
		transaction.setName(elements.get(elements.indexOf(Transaction.NAME) + 1)); //El elemento siguiente al elemento NAME

		Result results = new Result();
		results.setType(elements.get(elements.indexOf(Transaction.TYPE) + 1));
		results.setEvent_name(elements.get(elements.indexOf(Transaction.EVENT_NAME) + 1));
		results.setNum_suspensions(Integer.parseInt(elements.get(elements.indexOf(Transaction.NUM_SUSPENSIONS) + 1)));
		results.setWorst_bt(Double.parseDouble(elements.get(elements.indexOf(Transaction.WORST_BT) + 1)));

		// Setea los Worst_Global_Response_Times
		List<TimeResult> worsts = new ArrayList<TimeResult>();
		int i_worsts = elements.indexOf(Transaction.WORST_GLOBAL_RT) + 1;
		while (!elements.get(i_worsts).equals(Transaction.BEST_GLOBAL_RT)) {
			worsts.add(new TimeResult(elements.get(i_worsts + 1), Double.parseDouble(elements.get(i_worsts + 3))));
			i_worsts += 4; // Salta cuatro lugares, hasta el proximo ReferenceEvent o Best..
		}
		results.setWorst_global_rt(worsts);

		// Setea los Best_Global_Response_Times
		List<TimeResult> bests = new ArrayList<TimeResult>();
		int i_bests = elements.indexOf(Transaction.BEST_GLOBAL_RT) + 1;
		while (!elements.get(i_bests).equals(Transaction.JITTERS)) {
			bests.add(new TimeResult(elements.get(i_bests + 1), Double.parseDouble(elements.get(i_bests + 3))));
			i_bests += 4; // Salta cuatro lugares, hasta el proximo ReferenceEvent o Jitters..
		}
		results.setBest_global_rt(bests);

		// Setea los Worst_Global_Response_Times
		List<TimeResult> jitters = new ArrayList<TimeResult>();
		int i_jitters = elements.indexOf(Transaction.JITTERS) + 1;
		while (i_jitters < elements.size()) {
			jitters.add(new TimeResult(elements.get(i_jitters + 1), Double.parseDouble(elements.get(i_jitters + 3))));
			i_jitters += 4; // Salta cuatro lugares, hasta el proximo ReferenceEvent o fin.
		}
		results.setWorst_global_rt(jitters);
		List<Result> listResults = new ArrayList<Result>();
		listResults.add(results);
		transaction.setResults(listResults);

		return transaction;
	}

	public static void main(String[] args) {
		IndexBuffer tokenBuffer = new IndexBuffer(8192,true);
		IndexBuffer elementBuffer = new IndexBuffer(8192,true);

		MastParser parser = new MastParser(tokenBuffer, elementBuffer);
		//String s = "Transaction (   Name     => transaction_7_1,   Results  =>       ((Type                          => Timing_Result,         Event_Name                    => o13,         Num_Of_Suspensions            => 0,         Worst_Blocking_Time           => 0.000,         Worst_Global_Response_Times   =>            ((Referenced_Event => external_event_1,              Time_Value       => 5.000)),         Best_Global_Response_Times    =>            ((Referenced_Event => external_event_1,              Time_Value       => 0.000)),         Jitters                       =>            ((Referenced_Event => external_event_1,              Time_Value       => 5.000)))));";
		 String s = "Transaction (   Name     => transaction_8_1," +
"   Results  => " +
"       ((Type                          => Timing_Result," +
 "        Event_Name                    => o19," +
  "       Num_Of_Suspensions            => 0," +
   "      Worst_Blocking_Time           => 0.000," +
    "     Worst_Global_Response_Times   => " +
     "       ((Referenced_Event => external_event_1," +
      "        Time_Value       => 2.500))," +
      "   Best_Global_Response_Times    => " +
      "      ((Referenced_Event => external_event_1," +
      "        Time_Value       => 0.000))," +
      "   Jitters                       => " +
      "      ((Referenced_Event => external_event_1," +
      "        Time_Value       => 2.500)))));";
		DataCharBuffer buffer = new DataCharBuffer(s.toCharArray());
		IndexBuffer result = parser.parse(buffer);
		for(int i = 0; i < result.count; i++) {
			System.out.println(s.substring(result.position[i], result.position[i] + result.length[i]));

		}
		
		System.out.println(parser.parseTransaction(s).getResults().get(0).getWorst_global_rt().get(0).getTime_value());

	}
}
