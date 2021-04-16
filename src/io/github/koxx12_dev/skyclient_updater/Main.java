package io.github.koxx12_dev.skyclient_updater;


import java.awt.*;
import java.io.Console;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main{
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        Console console = System.console();
        if (console == null && !GraphicsEnvironment.isHeadless()){
            String fileloc = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + fileloc + "\""});
        } else {
           Code.main(new String[0]);
           System.out.println("Program has ended, please type 'exit' to close the console");
        }
    }

}