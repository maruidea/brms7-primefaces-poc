package mortgages.mortgages;

import java.util.List;
import java.util.Map;

public class CreditFieldInfo {
    
    private Map<String,List<String>> allowedRoles;

    public Map<String, List<String>> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(Map<String, List<String>> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }
    
}
