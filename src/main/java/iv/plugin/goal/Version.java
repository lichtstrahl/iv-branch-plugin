package iv.plugin.goal;

import javassist.ClassPool;
import lombok.Data;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

// Данная цель запускается при сборке (по умолчанию)
@Data
@Mojo(name = "version", defaultPhase = LifecyclePhase.COMPILE)
public class Version extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Map<String, String> context = getPluginContext();
        ClassPool pool = ClassPool.getDefault();



        String branch = getBranchName();
    }

    public String getBranchName() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("git", "rev-parse", "--abbrev-ref", "HEAD");

        String branchName = "NO_NAME";

        try {
            Process process = builder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                branchName = output.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return branchName;
        }
    }

    public void findApplicationYml() {
        mavenProject
                .getResources()
                .forEach(r -> {
                    System.out.println(r.getDirectory());
                });
    }
}
