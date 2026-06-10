#include "nv_read.h"

String NvRead_RemoveCommentsFromSource(Arena* arena, String source) {
	// Long comment pass (from // to newline)
	for (ssize_t start_i = StrFind(source, S("//")); start_i != -1; start_i = StrFind(source, S("//"))) {
		ssize_t end_i = StrFindFrom(source, S("\n"), start_i);
		source = StrConcat(arena, StrSlice(arena, source, 0, start_i), StrSlice(arena, source, end_i, -1));
	}

	// C-style comment pass (from /* to */)
	for (ssize_t start_i = StrFind(source, S("/*")); start_i != -1; start_i = StrFind(source, S("/*"))) {
		ssize_t end_i = StrFindFrom(source, S("*/"), start_i) + 2; // +2 because we want to get rid of the end, unlike with newlines in //-style
		source = StrConcat(arena, StrSlice(arena, source, 0, start_i), StrSlice(arena, source, end_i, -1));
	}

	return source;
}

FileReadResult NvRead_ReadSourceFile(Arena* arena, String path) {
	FileReadResult file = FileRead(arena, path, FileStats(path).data.size);
	if (file.error != SUCCESS) return file;

	file.data = NvRead_RemoveCommentsFromSource(arena, file.data);

	return file;
}
