#ifndef __NV_LEXER_H__
#define __NV_LEXER_H__

#include <base.h>
#include <stddef.h>

/// All token definitions
#define NV_LEXER_TOKEN_X(X) \
	/* Literals */\
	X(NUMBER) \
	X(IDENTIFIER) \
	X(STRING) \
	X(BOOLEAN) \
	\
	/* Keywords */\
	X(IF) \
	X(FOR) \
	X(WHILE) \
	X(VAR) \
	X(MUT) \
	X(PUB) \
	X(PRV) \
	X(FN) \
	X(MACRO) \
	X(TYPE) \
	X(CLASS) \
	X(STRUCT) \
	X(ENUM) \
	X(RETURN) \
	\
	/* Operators */\
	X(PLUS) \
	X(DASH) \
	X(STAR) \
	X(SLASH) \
	X(MOD) \
	X(ADDOVERFLOW) \
	X(SUBOVERFLOW) \
	X(MULOVERFLOW) \
	X(DIVOVERFLOW) \
	X(PLUSPLUS) \
	X(DASHDASH) \
	X(COLON) \
	X(COLONCOLON) \
	X(SEMICOLON) \
	X(DOT) \
	X(AT) \
	X(LEFTPAREN) \
	X(RIGHTPAREN) \
	X(LEFTCURLY) \
	X(RIGHTCURLY) \
	X(LEFTBRACKET) \
	X(RIGHTBRACKET) \
	X(LEFTANGLE) \
	X(RIGHTANGLE) \
	X(COMMA) \
	X(EQUALS)

/// Token types
typedef enum NvLexer_TokenType {
	#define X(type) NvLexer_TokenType_##type,
	NV_LEXER_TOKEN_X(X)
	#undef X
	NvLexer_TOTAL_TOKENS
} NvLexer_TokenType;

/// Raw token data
typedef struct NvLexer_Token {
	NvLexer_TokenType type;
	String data;

	struct {
		struct {
			size_t lin;
			size_t col;
			size_t pos;
		} start;
		struct {
			size_t lin;
			size_t col;
			size_t pos;
		} end;
	} span;
} NvLexer_Token;

/// Token vector definition
VEC_TYPE(NvLexer_TokenVector, NvLexer_Token);

/// Lexer struct
typedef struct NvLexer_Tokenizer {
	NvLexer_TokenVector tokens;
	String source;

	struct {
		size_t lin;
		size_t col;
		size_t pos;
	} pos;
} NvLexer_Tokenizer;

/// Public interface
NvLexer_Tokenizer* NvLexer_TokenizerNew(Arena* arena, String source);
void NvLexer_TokenizerFree(NvLexer_Tokenizer* lexer);
void NvLexer_Tokenize(Arena* arena, NvLexer_Tokenizer* lexer);
void _NvLexer_PrintTokens(NvLexer_TokenVector tokens);

#endif // #ifndef __NV_LEXER_H__
