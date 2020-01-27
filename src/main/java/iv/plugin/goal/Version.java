package iv.plugin.goal;

import lombok.Data;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

// Данная цель запускается при сборке (по умолчанию)
@Data
@Mojo(name = "version", defaultPhase = LifecyclePhase.COMPILE)
public class Version extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ProcessBuilder process = new ProcessBuilder();
        process.command("cmd.exe", "/c", "dir");
    }
}
