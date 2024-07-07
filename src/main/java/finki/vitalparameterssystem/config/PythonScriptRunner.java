package finki.vitalparameterssystem.config;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class PythonScriptRunner {

    @PostConstruct
    public void runPythonScript() {
        try {
            // Path to your Python script relative to the resources folder
            ClassPathResource resource = new ClassPathResource("simulator.py");
            Path tempScript = Files.createTempFile("simulator", ".py");
            Files.copy(resource.getInputStream(), tempScript, StandardCopyOption.REPLACE_EXISTING);

            // Execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder("python3", tempScript.toString());
            Process process = processBuilder.start();

            // Capture the output of the script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

            // Delete the temporary script file
            Files.delete(tempScript);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
