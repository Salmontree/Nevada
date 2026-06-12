#include "nv_parser.h"

NvParser_AST NvParser_NewAST(NvLexer_TokenVector *tokens) {
    return (NvParser_AST){.tokens = tokens};
}

void NvParser_ParseTokens(NvParser_AST* ast) {
    
}