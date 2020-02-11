package iv.plugin.goal;

import lombok.Data;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Данная цель запускается при сборке (по умолчанию)
@Data
@Mojo(name = "version")
public class Version extends BaseGoal {
    public static final String JAVA_VERSION = "java.version";
    private static final String SEPARATOR = ",";

    @Parameter(defaultValue = "${project}")
    private MavenProject mavenProject;
    @Parameter(name = "versionPropertyName", defaultValue = DEFAULT_VERSION_PROPERTY)
    private String versionPropertyName;


    @Parameter(name = "ignoreBranch", defaultValue = "")
    private String ignoreBranch;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (versionPropertyName.equals(DEFAULT_VERSION_PROPERTY)) {
            getLog().warn("versionPropertyName not set in configuration. Default: iv.version");
        }

        List<String> ignoreBranchNames = Arrays.stream(ignoreBranch.split(SEPARATOR))
                .map(String::trim)
                .collect(Collectors.toList());

        String branch = getBranchName();
        System.out.println("Branch name: " + branch);
        boolean ignore = ignoreBranchNames.contains(branch);
        if (ignore)
            getLog().warn("This branch ignored (from configuration)");

        String projectVersion = getMavenProperty(versionPropertyName);
        System.out.println("Project: " + projectVersion);
        String version = ignore
            ? projectVersion
            : projectVersion.concat("-").concat(branch);
        String newProjectVersion = setMavenProperty(versionPropertyName, version);
        System.out.println("Project: " + newProjectVersion);
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

    public String getMavenProperty(String propertyName) {
        return mavenProject
                .getProperties()
                .getProperty(propertyName);
    }

    public String setMavenProperty(String propertyName, String value) {
        mavenProject
                .getProperties()
                .setProperty(propertyName, value);
        System.setProperty(propertyName, value);
        return mavenProject
                .getProperties()
                .getProperty(propertyName);
    }
}
