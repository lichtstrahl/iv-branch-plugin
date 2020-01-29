package iv.plugin.goal;

import lombok.Data;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Data
@Mojo(name = "check")
public class CheckVersion extends AbstractMojo {
    public static final String DEFAULT_VERSION_PROPERTY = "iv.version";

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;
    @Parameter(name = "versionPropertyName", defaultValue = DEFAULT_VERSION_PROPERTY, required = false)
    private String versionPropertyName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("Check: " + mavenProject.getProperties().getProperty(versionPropertyName));
        System.out.println("Check system: " + System.getProperty(versionPropertyName));
    }
}
