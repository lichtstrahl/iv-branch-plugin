package iv.plugin.goal;

import lombok.Data;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Data
@Mojo(name = "hello")
public class Hello extends AbstractMojo {
    @Parameter(property = "msg", defaultValue = "default message")
    private String msg;
    @Parameter(property = "name", defaultValue = "NO_NAME")
    private String name;


    public void execute() throws MojoExecutionException {
        getLog().info("Hello " + name);
    }
}
