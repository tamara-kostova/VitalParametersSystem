//package finki.vitalparameterssystem.config;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//public class ScriptExecutor {
//
//    public void runPythonScript() {
//        String scriptPath = "C:\\Users\\tamar\\IdeaProjects\\VitalParametersSystem\\simulator.py";
//        String pythonPath = "python";
//
//        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath);
//        processBuilder.redirectErrorStream(true);
//
//        try {
//            Process process = processBuilder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//            process.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
