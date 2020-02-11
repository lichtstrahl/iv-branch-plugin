package iv.plugin.goal;

import lombok.Data;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.*;
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
    @Parameter(name = "exportFilename", defaultValue = DEFAULT_EXPORT_FILENAME)
    private String exportFilename;
    @Parameter(name = "rewriteFile", defaultValue = "false")
    private boolean rewriteFile;
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
        export();
    }


    public void export() {
        if (exportFilename.equals(DEFAULT_EXPORT_FILENAME))
            getLog().warn("Default export exportFilename: " + DEFAULT_EXPORT_FILENAME);


        File exportFile = new File(exportFilename);
        if (exportFile.exists() && !rewriteFile) {
            getLog().error("File exists and <rewriteFile> is false");
            return;
        }
        exportFile.delete();

        try {
            boolean create = exportFile.createNewFile();

            if (!create) {
                getLog().error("Can't create file " + exportFilename);
                return;
            }

            FileWriter writer = new FileWriter(exportFile);
            // Получаем текущее значение версии из pom.xml ипишем в файл
            writer.write(mavenProject.getProperties().getProperty(versionPropertyName));
            writer.close();
        } catch (IOException e) {
            getLog().error(e.getMessage());
        }
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

    private boolean isValidProperty(String name) {
        return mavenProject
                .getProperties()
                .stringPropertyNames()
                .contains(name);
    }
}