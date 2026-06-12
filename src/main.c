#define BASE_IMPLEMENTATION
#include <base.h>
#include "nv_lexer.h"
#include "nv_read.h"
#include "nv_parser.h"

int main(int argc, char** argv) {
	// Init
	Assert(argc == 2, "Argument error");
	Arena* arena = ArenaCreate(2048);

	// Read source
	FileReadResult source = NvRead_ReadSourceFile(arena, s(argv[1]));
	Assert(source.error == SUCCESS, "Unable to read source file; error: %d", source.error);
	// printf("%s\n", source.data.data);

	// Lex source
	NvLexer_Tokenizer* lexer = NvLexer_TokenizerNew(arena, source.data);
	NvLexer_Tokenize(arena, lexer);

	_NvLexer_PrintTokens(lexer->tokens);

	// Shutdown
	NvLexer_TokenizerFree(lexer);
	ArenaFree(arena);
	return 0;
}
