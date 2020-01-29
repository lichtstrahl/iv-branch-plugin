package iv.plugin;

import iv.plugin.goal.Version;

public class Main {
    public static void main(String[] args) {
        Version version = new Version();
        String branch = version.getBranchName();
        System.out.println("\""+branch+"\"");

        version.getMavenProperty("java.version");
    }
}
