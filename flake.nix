{
	description = "soietdf";

	inputs = {
		nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
		flake-utils.url = "github:numtide/flake-utils";
	};

	outputs = { self, nixpkgs, flake-utils }:
		flake-utils.lib.eachDefaultSystem (system:
		let
			pkgs = import nixpkgs { inherit system; };
		in
		{
			devShells.default = pkgs.mkShell {
				packages = [ pkgs.jdk8 pkgs.jdk17 ];
				JAVA_HOME = "${pkgs.jdk17.home}";
				JAVA_HOME_8 = "${pkgs.jdk8.home}";
				JAVA_HOME_17 = "${pkgs.jdk17.home}";

				GRADLE_OPTS = "-Dorg.gradle.java.installations.paths=${pkgs.jdk8.home},${pkgs.jdk17.home} -Dorg.gradle.java.installations.auto-detect=true";
			};
		});
}
