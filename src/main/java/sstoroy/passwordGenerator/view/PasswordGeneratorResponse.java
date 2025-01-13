package sstoroy.passwordGenerator.view;

import java.util.List;
import java.util.Map;

public interface PasswordGeneratorResponse {
    List<String> getPasswords();
    Map<String, String> getSettings();
    String getParameterURL();
}
