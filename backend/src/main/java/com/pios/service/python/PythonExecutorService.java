package com.pios.service.python;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PythonExecutorService {

    @Value("${python.module.path:python}")
    private String pythonModulePath;

    @Value("${python.executable:python3}")
    private String pythonExecutable;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode executePythonScript(String scriptPath, String... args) {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path pythonPath = projectRoot.resolve(pythonModulePath);
            Path scriptFullPath = pythonPath.resolve(scriptPath);

            String[] command = new String[args.length + 2];
            command[0] = pythonExecutable;
            command[1] = scriptFullPath.toString();
            System.arraycopy(args, 0, command, 2, args.length);

            log.info("Executing Python script: {}", String.join(" ", command));

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(pythonPath.toFile());
            pb.environment().put("PYTHONPATH", pythonPath.toString());
            pb.redirectErrorStream(true);

            Process process = pb.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                log.error("Python script execution timeout");
                return createErrorResponse("Execution timeout");
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                log.error("Python script exited with code {}: {}", exitCode, output);
                return createErrorResponse("Script failed with exit code: " + exitCode);
            }

            return objectMapper.readTree(output.toString());

        } catch (Exception e) {
            log.error("Failed to execute Python script", e);
            return createErrorResponse(e.getMessage());
        }
    }

    public JsonNode fetchMacroData(String dataType) {
        return executePythonScript(
            "data_fetcher/macro_data.py",
            dataType
        );
    }

    public JsonNode executeFetcher(String fetcherName, String method, String paramsJson) {
        return executePythonScript(
            "data_fetcher/executor.py",
            fetcherName,
            method,
            paramsJson
        );
    }

    private JsonNode createErrorResponse(String error) {
        return objectMapper.createObjectNode()
            .put("success", false)
            .put("error", error);
    }
}
