#define MATE_IMPLEMENTATION
#include "mate.h"
#include <string.h>

int main(int argc, char** argv) {
	CreateConfig((MateOptions){.compiler = CLANG});
	
	StartBuild();
	{
		ExecutableOptions opts_release = (ExecutableOptions) {
			.output = "nevada",
			.std = FLAG_STD_C99,
			.warnings = FLAG_WARNINGS_NONE,
			.optimization = FLAG_OPTIMIZATION_AGGRESSIVE,
		};
		ExecutableOptions opts_debug = (ExecutableOptions) {
			.output = "nevada",
			.std = FLAG_STD_C99,
			.warnings = FLAG_WARNINGS_VERBOSE,
			.optimization = FLAG_OPTIMIZATION_BASIC,
		};
		ExecutableOptions opts = opts_release;
		if (argc != 0) { 
			Assert(argc == 2, "There should be 1 argument: 'debug' or 'release' (defaults to 'release')");
			char valid_arg = 0;
			if (strcmp(argv[1], "debug") == 0)   { opts = opts_debug;  valid_arg = 1; }
			if (strcmp(argv[1], "release") == 0) { opts = opts_release; valid_arg = 1; }
			Assert(valid_arg == 1, "Invalid argument; expected 'debug' or 'release'");
		}

		Executable exe = CreateExecutable(opts);

		AddIncludePaths(exe, "./src");
		AddFile(exe, "./src/*.c");
		InstallExecutable(exe);

		errno_t err = CreateCompileCommands(exe); if (err != SUCCESS) LogError("Creating compile commands failed with error: %d", err);
		// errExe = RunCommand(exe.outputPath); if (errExe != SUCCESS) LogError("Running executable failed with error: %d", errExe);
	}
	EndBuild();
	
	return 0;
}
