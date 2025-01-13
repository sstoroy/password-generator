package sstoroy.passwordGenerator.controller;

import org.springframework.web.bind.annotation.*;
import sstoroy.passwordGenerator.model.generator.PasswordGeneratorImpl;
import sstoroy.passwordGenerator.model.settings.manager.PasswordSettingsImpl;
import sstoroy.passwordGenerator.view.PasswordGeneratorResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PasswordGeneratorAPI {

    @PostMapping({"/", "/index"})
    public PasswordGeneratorResponse getAll(@RequestBody Map<String, String> jsonParams) {
        return getResponse(jsonParams);
    }

    @PostMapping("/passwords")
    public List<String> getPasswords(@RequestBody Map<String, String> urlParams) {
        return getResponse(urlParams).getPasswords();
    }

    @PostMapping("/settings")
    public Map<String, String> getSettings(@RequestBody Map<String, String> urlParams) {
        return getResponse(urlParams).getSettings();
    }

    @PostMapping("/params")
    public String getParams(@RequestBody Map<String, String> urlParams) {
        return getResponse(urlParams).getParameterURL();
    }

    private PasswordGeneratorResponse getResponse(Map<String, String> params) {
        return new PasswordGeneratorImpl(
                new PasswordSettingsImpl().setSettings(params)
        );
    }
}
