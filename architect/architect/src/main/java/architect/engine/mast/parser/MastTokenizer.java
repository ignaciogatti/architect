package architect.engine.mast.parser;


/**
 */
public class MastTokenizer {

    private DataCharBuffer dataBuffer  = null;
    private IndexBuffer    tokenBuffer = null;

    private int tokenIndex   = 0;
    private int dataPosition = 0;
    private int tokenLength  = 0;

    public MastTokenizer(IndexBuffer tokenBuffer) {
        this.tokenBuffer = tokenBuffer;
    }

    public MastTokenizer(DataCharBuffer dataBuffer, IndexBuffer tokenBuffer) {
        this.dataBuffer  = dataBuffer;
        this.tokenBuffer = tokenBuffer;
    }

    public void reinit(DataCharBuffer dataBuffer, IndexBuffer tokenBuffer) {
        this.dataBuffer  = dataBuffer;
        this.tokenBuffer = tokenBuffer;
        this.tokenIndex  = 0;
        this.dataPosition= 0;
        this.tokenLength = 0;
    }

    public boolean hasMoreTokens() {
        return (this.dataPosition + this.tokenLength) < this.dataBuffer.length ;
    }


    public void parseToken() {
        skipWhiteSpace();
        //this.tokenLength = 0;

        this.tokenBuffer.position[this.tokenIndex] = this.dataPosition;
        char nextChar = this.dataBuffer.data[this.dataPosition];

        switch(nextChar) {
            case '(' :  { this.tokenBuffer.type[this.tokenIndex] = TokenTypes.MAST_PARENTHESIS_LEFT; } break;
            case ')' :  { this.tokenBuffer.type[this.tokenIndex] = TokenTypes.MAST_PARENTHESIS_RIGHT; } break;
            case '=' :  { parseArrowToken(); this.tokenBuffer.type[this.tokenIndex] = TokenTypes.MAST_ARROW; } break;
            case ',' :  { this.tokenBuffer.type[this.tokenIndex] = TokenTypes.MAST_COMMA; } break;
            case ';' :  { this.tokenBuffer.type[this.tokenIndex] = TokenTypes.MAST_SEMICOLON; } break;

            case '0' :  ;
            case '1' :  ;
            case '2' :  ;
            case '3' :  ;
            case '4' :  ;
            case '5' :  ;
            case '6' :  ;
            case '7' :  ;
            case '8' :  ;
            case '9' :  { parseNumberToken(); this.tokenBuffer.type[this.tokenIndex] = TokenTypes.MAST_NUMBER_TOKEN; } break;

            //case 'f' :  { if(parseFalse()) { this.tokenBuffer.type[this.tokenIndex] = TokenTypes.JSON_BOOLEAN_TOKEN;} } break;
            //case 't' :  { if(parseTrue())  { this.tokenBuffer.type[this.tokenIndex] = TokenTypes.JSON_BOOLEAN_TOKEN;} } break;
            //case 'n' :  { if(parseNull())  { this.tokenBuffer.type[this.tokenIndex] = TokenTypes.JSON_NULL_TOKEN;} } break;

            default    :  { parseStringToken(); this.tokenBuffer.type[this.tokenIndex] = TokenTypes.MAST_STRING_TOKEN; }
        }

        this.tokenBuffer.length[this.tokenIndex] = this.tokenLength;
    }

    private boolean parseNull() {
        if(
            this.dataBuffer.data[this.dataPosition + 1] == 'u' &&
            this.dataBuffer.data[this.dataPosition + 2] == 'l' &&
            this.dataBuffer.data[this.dataPosition + 3] == 'l' )
        {
            //this.tokenLength = 4;
            return true;
        }
        return false;

    }

    private boolean parseTrue() {
        if(
            this.dataBuffer.data[this.dataPosition + 1] == 'r' &&
            this.dataBuffer.data[this.dataPosition + 2] == 'u' &&
            this.dataBuffer.data[this.dataPosition + 3] == 'e' )
        {
            this.tokenLength = 4;
            return true;
        }
        return false;
    }

    private boolean parseFalse() {
        if(
            this.dataBuffer.data[this.dataPosition + 1] == 'a' &&
            this.dataBuffer.data[this.dataPosition + 2] == 'l' &&
            this.dataBuffer.data[this.dataPosition + 3] == 's' &&
            this.dataBuffer.data[this.dataPosition + 4] == 'e'
                )  {
            this.tokenLength = 5;
            return true;
        }
        return false;
    }

    private void parseNumberToken() {
        this.tokenLength = 1;

        boolean isEndOfNumberFound = false;
        while(!isEndOfNumberFound) {
            switch(this.dataBuffer.data[this.dataPosition + this.tokenLength]){
                case '0'   :  ;
                case '1'   :  ;
                case '2'   :  ;
                case '3'   :  ;
                case '4'   :  ;
                case '5'   :  ;
                case '6'   :  ;
                case '7'   :  ;
                case '8'   :  ;
                case '9'   :  ;
                case '+'   :  ;
                case 'E'   :  ;
                case '.'   :  { this.tokenLength++; } break;

                default    :  { isEndOfNumberFound = true; }
            }
        }
    }
    
    private void parseArrowToken() {
    	this.tokenLength = 1;

        boolean isEndOfNumberFound = false;
        while(!isEndOfNumberFound) {
            switch(this.dataBuffer.data[this.dataPosition + this.tokenLength]){
                case '>'   :  { this.tokenLength++; } break;
                default    :  { isEndOfNumberFound = true; }
            }
        }
    }

    private void parseStringToken() {
        int tempPos = this.dataPosition;
        boolean containsEncodedChars = false;
        boolean endOfStringFound     = false;
        while(!endOfStringFound) {
            tempPos++;
            switch(this.dataBuffer.data[tempPos]) {
            	case '(' :
            	case ')' :
            	case '=' :
            	case ',' :
            	case ';' :
            	case ' ' : { endOfStringFound = true; break; }
                //case '\\' : { containsEncodedChars = true; break; }
            }
        }
        if(containsEncodedChars) {
            this.tokenBuffer.type[this.tokenIndex] = TokenTypes.JSON_STRING_ENC_TOKEN;
        } else {
            this.tokenBuffer.type[this.tokenIndex] = TokenTypes.MAST_STRING_TOKEN;
        }

        this.tokenBuffer.position[this.tokenIndex] = this.dataPosition;
        this.tokenLength = tempPos - this.dataPosition; //(tempPos - this.dataPosition - 1) ; // +2 to include the enclosing quote chars ("").
    }



    private void skipWhiteSpace() {
        boolean isWhiteSpace = true;
        while(isWhiteSpace) {
            switch(this.dataBuffer.data[this.dataPosition]) {
                case ' '    :  ;  /* falling through - all white space characters are treated the same*/
                case '\r'   :  ;
                case '\n'   :  ;
                case '\t'   :  { this.dataPosition++; } break;

                default     :  { isWhiteSpace = false; }  /* any non white space char will break the while loop */
            }
        }
    }

    public void nextToken() {
        switch(this.tokenBuffer.type[this.tokenIndex]){
            case TokenTypes.MAST_STRING_TOKEN         : { this.dataPosition += this.tokenBuffer.length[this.tokenIndex]; break;}
            //case TokenTypes.JSON_STRING_ENC_TOKEN     : { this.dataPosition += this.tokenBuffer.length[this.tokenIndex]; break;} // +2 because of the quotes
            case TokenTypes.MAST_PARENTHESIS_LEFT	  : {this.dataPosition++; break;}
            case TokenTypes.MAST_PARENTHESIS_RIGHT    : {this.dataPosition++; break;}
            //case TokenTypes.JSON_SQUARE_BRACKET_LEFT  : {this.dataPosition++; break;}
            //case TokenTypes.JSON_SQUARE_BRACKET_RIGHT : {this.dataPosition++; break;}
            case TokenTypes.MAST_ARROW                : {this.dataPosition+=2; break;}
            case TokenTypes.MAST_COMMA                : {this.dataPosition++; break;}
            //case TokenTypes.JSON_NULL_TOKEN           : {this.dataPosition+=4; break;}
            case TokenTypes.MAST_SEMICOLON			  : {this.dataPosition++; break;}
            default                                   : {this.dataPosition += this.tokenLength;}
        }
        //this.dataPosition += this.tokenBuffer.length[this.tokenIndex]; //move data position to end of current token.
        this.tokenIndex++;  //point to next token index array cell.
    }

    public int tokenPosition() {
        return this.tokenBuffer.position[this.tokenIndex];
    }

    public int tokenLength() {
        return this.tokenBuffer.length[this.tokenIndex];
    }

    public byte tokenType() {
        return this.tokenBuffer.type[this.tokenIndex];
    }

}
