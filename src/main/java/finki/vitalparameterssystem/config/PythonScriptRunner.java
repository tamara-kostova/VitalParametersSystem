package finki.vitalparameterssystem.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class PythonScriptRunner {

    private Process simulatorProcess;
    private Process analyzerProcess;

    @PostConstruct
    public void runPythonScripts() {
        // Run simulator script
        runScript("simulator.py");

        // Run analyzer script
        runScript("analyzer.py");
    }

    private void runScript(String scriptName) {
        Thread scriptThread = new Thread(() -> {
            try {
                // Path to the Python script relative to the resources folder
                ClassPathResource resource = new ClassPathResource(scriptName);
                Path tempScript = Files.createTempFile(scriptName, ".py");
                Files.copy(resource.getInputStream(), tempScript, StandardCopyOption.REPLACE_EXISTING);

                // Execute the Python script
                ProcessBuilder processBuilder = new ProcessBuilder("python", tempScript.toString());
                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // Wait for the process to complete
                int exitCode = process.waitFor();
                System.out.println(scriptName + " exited with code: " + exitCode);

                // Delete the temporary script file
                Files.delete(tempScript);

                // Assign process to the appropriate field
                if ("simulator.py".equals(scriptName)) {
                    simulatorProcess = process;
                } else if ("analyzer.py".equals(scriptName)) {
                    analyzerProcess = process;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        scriptThread.start();
    }

    @PreDestroy
    public void cleanUp() {
        if (simulatorProcess != null) {
            simulatorProcess.destroy();
            try {
                simulatorProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (analyzerProcess != null) {
            analyzerProcess.destroy();
            try {
                analyzerProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
