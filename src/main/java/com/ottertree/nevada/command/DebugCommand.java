package com.ottertree.nevada.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value="debug", description="/debug ... *used for development purposes, does NOT do anything beneficial*")
public class DebugCommand {
    @Main
    private void handle(int finals) {
        
    }
}
