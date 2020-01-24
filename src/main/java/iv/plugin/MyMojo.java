package iv.plugin;

import lombok.Data;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Data
@Mojo(name = "hello")
public class MyMojo extends AbstractMojo {
    @Parameter(property = "msg", defaultValue = "default message")
    private String msg;

    public void execute() throws MojoExecutionException {
        getLog().info("Hello " + msg);
    }
}
