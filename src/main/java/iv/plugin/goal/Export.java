package iv.plugin.goal;

import lombok.Data;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Data
@Mojo(name = "export")
public class Export extends BaseGoal {
    @Parameter(defaultValue = "${project}")
    private MavenProject mavenProject;
    @Parameter(name = "exportFilename", defaultValue = DEFAULT_EXPORT_FILENAME)
    private String exportFilename;
    @Parameter(name = "rewriteFile", defaultValue = "false")
    private boolean rewriteFile;
    @Parameter(name = "versionPropertyName", defaultValue = DEFAULT_VERSION_PROPERTY)
    private String versionPropertyName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
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

}
