#ifndef __NV_PARSER_H__
#define __NV_PARSER_H__

#include <base.h>
#include "nv_lexer.h"

typedef struct NvParser_Node NvParser_Node;

#define NV_PARSER_NODE_X(X) \
    X(Span,      { struct { size_t lin; size_t col; size_t idx; } start; struct { size_t lin; size_t col; size_t idx; } end; }) \

#define X(type, data) typedef struct NvParser_Node_##type data NvParser_Node_##type;
NV_PARSER_NODE_X(X)
#undef X

struct NvParser_Node {
    enum {
        #define X(type, data) NvParser_NodeType_##type,
        NV_PARSER_NODE_X(X)
        #undef X
    } type;
    union {
        #define X(type, data) NvParser_Node_##type type;
        NV_PARSER_NODE_X(X)
        #undef X
    } data;
};

typedef struct NvParser_AST {
    NvParser_Node* root;
    NvLexer_TokenVector* tokens;
} NvParser_AST;

NvParser_AST NvParser_NewAST(NvLexer_TokenVector* tokens);

void NvParser_ParseTokens(NvParser_AST* ast);

#endif // #ifndef __NV_PARSER_H__