package mortgages.mortgages;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Version {

    String version;
    
    String revision;
    
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getRevision() {
        return revision;
    }
    public void setRevision(String revision) {
        this.revision = revision;
    }
}
