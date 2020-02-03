package iv.plugin.goal;

import org.apache.maven.plugin.AbstractMojo;

abstract public class BaseGoal extends AbstractMojo {
    protected static final String DEFAULT_VERSION_PROPERTY = "iv.version";
    protected static final String DEFAULT_EXPORT_FILENAME = "iv-version-export";
}
