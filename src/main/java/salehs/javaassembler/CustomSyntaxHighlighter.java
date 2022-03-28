/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salehs.javaassembler;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

/**
 *
 * @author Saleh
 */
public class CustomSyntaxHighlighter extends AbstractTokenMaker {

    private AssemblerConfig config;
    private int currentTokenStart, currentTokenType, startTokenType;

    public CustomSyntaxHighlighter() {
        // System.out.println("Constructor");

    }

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tokenMap = new TokenMap();
        // this.config = new AssemblerConfig("config.json");
        // System.out.println(this.config);
        if (this.config != null) {
            // System.out.println("config defined");
            for (String inst_name : this.getConfig().getInstrucionsName()) {
                tokenMap.put(inst_name, Token.RESERVED_WORD);
            }
        } else {
            // System.out.println("config undefined");
            tokenMap.put("add", Token.RESERVED_WORD);
        }
        return tokenMap;
    }

    @Override
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
        resetTokenList();
        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;
        // Token starting offsets are always of the form:
        // 'startOffset + (currentTokenStart-offset)', but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart'.
        // System.out.println(text);
        int newStartOffset = startOffset - offset;
        currentTokenStart = offset;
        currentTokenType = startTokenType;
        for (int i = offset; i < end; i++) {
            char c = array[i];
            switch (currentTokenType) {
                case Token.NULL:
                    currentTokenStart = i; // Starting a new token here.
                    switch (c) {
                        case ' ':
                        case '\t':
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        case '#':
                            currentTokenType = Token.COMMENT_EOL;
                            break;
                        default:
                            if (RSyntaxUtilities.isDigit(c)) {
                                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            } else if (RSyntaxUtilities.isLetter(c)) {
                                if (this.config != null) {
                                    // System.out.println("config");
                                    for (String inst_name : this.getConfig().getInstrucionsName()) {
                                        if (inst_name.indexOf(c) == 0) {
                                            currentTokenType = Token.RESERVED_WORD;
                                            break;
                                        }
                                    }
                                }
                            } else if (RSyntaxUtilities.isLetter(c) || c == '/' || c == '_') {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }
                            // Anything not currently handled - mark as an identifier
                            currentTokenType = Token.IDENTIFIER;
                            break;
                    } // End of switch (c).
                    break;
                case Token.WHITESPACE:
                    switch (c) {
                        case ' ':
                        case '\t':
                            break; // Still whitespace.
                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE,
                                    newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        case '#':
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE,
                                    newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;
                        default: // Add the whitespace token and start anew.
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE,
                                    newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            if (RSyntaxUtilities.isDigit(c)) {
                                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            } else if (RSyntaxUtilities.isLetter(c) || c == '/' || c == '_') {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }
                            // Anything not currently handled - mark as identifier
                            currentTokenType = Token.IDENTIFIER;
                    } // End of switch (c).
                    break;
                case Token.RESERVED_WORD:
                    addToken(text, currentTokenStart, i - 1, Token.RESERVED_WORD, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.RESERVED_WORD;
                    break;
                default: // Should never happen
                case Token.IDENTIFIER:
                    switch (c) {
                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER,
                                    newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER,
                                    newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        default:
                            if (RSyntaxUtilities.isLetterOrDigit(c) || c == '/' || c == '_') {
                                break; // Still an identifier of some type.
                            }
                            // Otherwise, we're still an identifier (?).

                    } // End of switch (c).
                    break;
                case Token.LITERAL_NUMBER_DECIMAL_INT:
                    switch (c) {
                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT,
                                    newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT,
                                    newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        default:
                            if (RSyntaxUtilities.isDigit(c)) {
                                break; // Still a literal number.
                            }
                            // Otherwise, remember this was a number and start over.
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT,
                                    newStartOffset + currentTokenStart);
                            i--;
                            currentTokenType = Token.NULL;
                    } // End of switch (c).
                    break;
                case Token.COMMENT_EOL:
                    i = end - 1;
                    addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more
                    // token.
                    currentTokenType = Token.NULL;
                    break;
                case Token.LITERAL_STRING_DOUBLE_QUOTE:
                    if (c == '"') {
                        addToken(text, currentTokenStart, i, Token.LITERAL_STRING_DOUBLE_QUOTE,
                                newStartOffset + currentTokenStart);
                        currentTokenType = Token.NULL;
                    }
                    break;
            } // End of switch (currentTokenType).
        } // End of for (int i=offset; i<end; i++).
        switch (currentTokenType) {
            // Remember what token type to begin the next line with.
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
                addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
                break;
            // Do nothing if everything was okay.
            case Token.NULL:
                addNullToken();
                break;
            // All other token types don't continue to the next line...
            default:
                addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
                addNullToken();
        }
        // Return the first token in our linked list.
        return firstToken;
    }

    /**
     * @return the config
     */
    public AssemblerConfig getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(AssemblerConfig config) {
        this.config = config;
        // System.out.println("Inside setConfig" + this.config);
    }

}
