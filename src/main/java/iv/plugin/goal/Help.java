package iv.plugin.goal;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;


@Mojo(name = "help")
public class Help extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        this
                .info("Version: " + "2.2")
                .info("Goals:")
                .info("\thello - print \"welcome\" message.")
                .info("\t\tNeed parameter from configuration <name>")
                .info("\t")
                .info("\tversion")
                .info("\t\tAppend into application.yml new property.")
                .info("\t\tNeed parameter from configuration <versionPropertyName>")
                .info("\tcheck")
                .info("\t\tShow current value from <versionPropertyName>")
                .info("");
    }

    private Help info(String msg) {
        getLog().info(msg);
        return this;
    }
}
