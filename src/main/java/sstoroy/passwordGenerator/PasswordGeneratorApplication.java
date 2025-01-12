package sstoroy.passwordGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sstoroy.passwordGenerator.model.settings.SettingKey;
import sstoroy.passwordGenerator.model.settings.manager.PasswordSettings;
import sstoroy.passwordGenerator.model.settings.manager.PasswordSettingsImpl;

import java.util.Map;

@SpringBootApplication
@Controller
public class PasswordGeneratorApplication {
	public static void main(String[] args) {
		SpringApplication.run(PasswordGeneratorApplication.class, args);
	}

	@GetMapping({"/", "/index"})
	public String getForm(@RequestParam Map<String, String> params, Model model) {
		PasswordSettings defaultSettings = new PasswordSettingsImpl();
		PasswordSettings settings = new PasswordSettingsImpl().setSettings(params);
		model.addAttribute("settings", settings);
		model.addAttribute("defaultSettings", defaultSettings);
		model.addAttribute("allSettingKeys", SettingKey.values());
		return "index";
	}
}
