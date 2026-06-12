#include "nv_lexer.h"
#include "base.h"
#include <ctype.h>
#include <stdio.h>

// Lexing helper functions
static inline char NvLexer_Current(NvLexer_Tokenizer* lexer) {
	if (lexer->pos.pos >= lexer->source.length) return '\0';
	return lexer->source.data[lexer->pos.pos];
}
static inline char NvLexer_Next(NvLexer_Tokenizer* lexer) {
	if (lexer->pos.pos > lexer->source.length) return '\0';
	return lexer->source.data[++lexer->pos.pos];
}
static inline char NvLexer_Prev(NvLexer_Tokenizer* lexer) {
	if (lexer->pos.pos <= 0 || lexer->pos.pos > lexer->source.length+1) return '\0';
	return lexer->source.data[--lexer->pos.pos];
}
static inline char NvLexer_PeekNext(NvLexer_Tokenizer* lexer) {
	if (lexer->pos.pos > lexer->source.length) return '\0';
	return lexer->source.data[lexer->pos.pos + 1];
}
static inline char NvLexer_PeekPrev(NvLexer_Tokenizer* lexer) {
	if (lexer->pos.pos <= 0 || lexer->pos.pos > lexer->source.length+1) return '\0';
	return lexer->source.data[lexer->pos.pos - 1];
}
static inline void NvLexer_Advance(NvLexer_Tokenizer* lexer, size_t n) {
	lexer->pos.pos += n;
}

///////////////////////////////////////////////////////////

// New lexer
NvLexer_Tokenizer* NvLexer_TokenizerNew(Arena* arena, String source) {
	NvLexer_Tokenizer* lexer = ArenaAlloc(arena, sizeof(NvLexer_Tokenizer));
	*lexer = (NvLexer_Tokenizer) { .source = source };
	return lexer;
}
// Free lexer
void NvLexer_TokenizerFree(NvLexer_Tokenizer* lexer) {
	VecFree(lexer->tokens);
}

// Debugging
const char* _NvLexer_FormatTokenType(NvLexer_TokenType type) {
	switch (type) {
		#define X(__type) case NvLexer_TokenType_##__type: return #__type;
		NV_LEXER_TOKEN_X(X)
		#undef X
		default: return NULL;
	}
}
void _NvLexer_PrintTokens(NvLexer_TokenVector tokens) {
	for (size_t i = 0; i < tokens.length; i++) {
		printf("[DEBUG] Token Data: type=%s, data={ data='%s', size=%zu }\n", _NvLexer_FormatTokenType(tokens.data[i].type), tokens.data[i].data.data, tokens.data[i].data.length);
	}
}

// Actual lexing process
void NvLexer_Tokenize(Arena* arena, NvLexer_Tokenizer* lexer) {
	#define QuickPushToken(token_type, token_data, advance) do { VecPush(lexer->tokens, (NvLexer_Token){.type=NvLexer_TokenType_##token_type, .data=token_data}); NvLexer_Advance(lexer, advance); } while(0)

	while (lexer->pos.pos < lexer->source.length) {
		char current = NvLexer_Current(lexer);

		// Whitespace
		if (isspace(current)) {
			NvLexer_Advance(lexer, 1);
			continue;
		}

		// Operators
		#define OperatorCheck(opdata, optype, adv) if ((size_t)StrFindFrom(lexer->source, S(opdata), lexer->pos.pos) == lexer->pos.pos) { QuickPushToken(optype, S(opdata), adv); continue; }
		{
			OperatorCheck("+%", ADDOVERFLOW, 2)
			OperatorCheck("-%", SUBOVERFLOW, 2)
			OperatorCheck("*%", MULOVERFLOW,2)
			OperatorCheck("/%", DIVOVERFLOW, 2)
			OperatorCheck("++", PLUSPLUS, 2)
			OperatorCheck("--", DASHDASH, 2)
			OperatorCheck("+", PLUS, 1)
			OperatorCheck("-", DASH, 1)
			OperatorCheck("*", STAR, 1)
			OperatorCheck("/", SLASH, 1)
			OperatorCheck("%", MOD, 1)
			OperatorCheck("::", COLONCOLON, 2)
			OperatorCheck(":", COLON, 1)
			OperatorCheck(";", SEMICOLON, 1)
			OperatorCheck("@", MACRO, 1)
			OperatorCheck("(", LEFTPAREN, 1)
			OperatorCheck(")", RIGHTPAREN, 1)
			OperatorCheck("[", LEFTBRACKET, 1)
			OperatorCheck("]", RIGHTBRACKET, 1)
			OperatorCheck("{", LEFTCURLY, 1)
			OperatorCheck("}", RIGHTCURLY, 1)
			OperatorCheck("<", LEFTANGLE, 1)
			OperatorCheck(">", RIGHTANGLE, 1)
			OperatorCheck(",", COMMA, 1)
			OperatorCheck("=", EQUALS, 1)
		}
		#undef OperatorCheck

		// Keywords, Booleans & Identifiers
		if (isalpha(current) || current == '_') {
			// Get full alphanumeric string
			const char* start = lexer->source.data + lexer->pos.pos;
			size_t len = 0;

			do { len++; } while (isalnum(NvLexer_Next(lexer)) || NvLexer_Current(lexer) == '_');

			char* iden_cstr = ArenaAllocChars(arena, len + 1);
			memcpy(iden_cstr, start, len);
			iden_cstr[len] = '\0';
			String iden = s(iden_cstr);

			// If keyword or boolean, push that type of token instead
			if (StrEq(iden, S("if")))     { QuickPushToken(IF, iden, 0); continue; }
			if (StrEq(iden, S("for")))    { QuickPushToken(FOR, iden, 0); continue; }
			if (StrEq(iden, S("while")))  { QuickPushToken(WHILE, iden, 0); continue; }
			if (StrEq(iden, S("var")))    { QuickPushToken(VAR, iden, 0); continue; }
			if (StrEq(iden, S("mut")))    { QuickPushToken(MUT, iden, 0); continue; }
			if (StrEq(iden, S("fn")))     { QuickPushToken(FN, iden, 0); continue; }
			if (StrEq(iden, S("struct"))) { QuickPushToken(STRUCT, iden, 0); continue; }
			if (StrEq(iden, S("enum")))   { QuickPushToken(ENUM, iden, 0); continue; }
			if (StrEq(iden, S("return"))) { QuickPushToken(RETURN, iden, 0); continue; }

			if (StrEq(iden, S("true")))   { QuickPushToken(BOOLEAN, iden, 0); continue; }
			if (StrEq(iden, S("false")))  { QuickPushToken(BOOLEAN, iden, 0); continue; }

			// Token is identifier; push that
			QuickPushToken(IDENTIFIER, iden, 0);
			continue;
		}

		// Strings
		if (current == '\'' || current == '\"') {
			// Get full string
			const char* start = lexer->source.data + lexer->pos.pos;
			size_t len = 2; // Accounting for both quotes

			while (NvLexer_Next(lexer) != current) { len++; if (NvLexer_Current(lexer) == '\0') goto NvLexer_Tokenizer_NonterminatingString; }
			NvLexer_Advance(lexer, 1);

			char* str_cstr = ArenaAllocChars(arena, len + 1);
			memcpy(str_cstr, start, len);
			str_cstr[len] = '\0';
			String str = s(str_cstr);

			QuickPushToken(STRING, str, 0);
			continue;

			NvLexer_Tokenizer_NonterminatingString:
			printf("NvLexer_Tokenize: Non-terminating String\n");
			break;
		}

		// Numbers
		if (isdigit(current)) {
			// Get full alphanumeric string
			const char* start = lexer->source.data + lexer->pos.pos;
			size_t len = 0;

			do { len++; } while (isdigit(NvLexer_Next(lexer)) || NvLexer_Current(lexer) == '_');

			char* num_cstr = ArenaAllocChars(arena, len + 1);
			memcpy(num_cstr, start, len);
			num_cstr[len] = '\0';
			String num = s(num_cstr);

			// Token is identifier; push that
			QuickPushToken(NUMBER, num, 0);
			continue;
		}

		// Nothing found, invalid token
		printf("NvLexer_Tokenize: Invalid token\n");
		break;
	}

	#undef QuickPushToken
}