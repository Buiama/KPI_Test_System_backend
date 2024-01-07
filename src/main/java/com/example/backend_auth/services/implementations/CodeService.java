package com.example.backend_auth.services.implementations;

import com.example.backend_auth.DTOs.responses.CodeResponse;
import com.example.backend_auth.DTOs.responses.ExecutedCodeResponse;
import com.example.backend_auth.models.Code;
import com.example.backend_auth.repositories.ICodeRepository;
import com.example.backend_auth.services.interfaces.ICodeService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CodeService implements ICodeService {
    private final ICodeRepository codeRepository;
    public void saveCode(Code code) {
        codeRepository.save(code);
    }
    public List<CodeResponse> getAllCodes() {
        return codeRepository.findAll().stream()
                .map(code -> new CodeResponse(code.getCodeId(), code.getLanguage(),
                        code.getCode(), code.getStudent().getEmail()))
                .collect(Collectors.toList());
    }

    public ExecutedCodeResponse executeCode(String language, String code) {
        String extension = getExtension(language);
        File file= new File("Main"+extension);
        try(FileWriter writer = new FileWriter(file, false)) {
            writer.write(code);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Pair<Integer, StringBuilder> execution = switch (language) {
            case "cpp" -> executeCpp(new File(file.getAbsolutePath()));
            case "csharp" -> executeCsharp(new File(file.getAbsolutePath()));
            case "go" -> executeGo(new File(file.getAbsolutePath()));
            case "java" -> executeJava(new File(file.getAbsolutePath()));
//            case "javascript" -> executeJS(file);
            case "julia" -> executeJulia(new File(file.getAbsolutePath()));
            case "kotlin" -> executeKotlin(new File(file.getAbsolutePath()));
            case "python" -> executePython(new File(file.getAbsolutePath()));
            case "typescript" -> executeTS(new File(file.getAbsolutePath()));
            default -> executeJS(new File(file.getAbsolutePath()));
        };

        return ExecutedCodeResponse.builder()
                .exitCode(execution.getFirst()).executed(execution.getSecond().toString()).build();
    }

    private Pair<Integer, StringBuilder> executeTS(File file) {
        int exitCode = 0;
        StringBuilder executed = new StringBuilder();
        return Pair.of(exitCode, executed);
    }
    private Pair<Integer, StringBuilder> executePython(File file) {
        String[] command = new String[]{"python", file.getAbsolutePath()};
        try {
            return executeCommand(command);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private Pair<Integer, StringBuilder> executeKotlin(File file) {
        String fileNameWithoutExt = getNameWithoutExt(file);
        String[] compileCmd = new String[]{"kotlinc",
                file.getAbsolutePath(), "-include-runtime", "-d", fileNameWithoutExt + ".jar"};
        String[] runCmd = new String[]{"java", "-jar", fileNameWithoutExt + ".jar"};
        try {
            executeCommand(compileCmd);
            return executeCommand(runCmd);
        }
        catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private Pair<Integer, StringBuilder> executeJulia(File file) {
        String[] command = new String[]{"julia", file.getAbsolutePath()};
        try {
            return executeCommand(command);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private Pair<Integer, StringBuilder> executeJS(File file) {
        String[] command = new String[]{"node", file.getAbsolutePath()};
        try {
            return executeCommand(command);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private Pair<Integer, StringBuilder> executeJava(File file) {
        String[] compileCmd = new String[]{"javac", file.getAbsolutePath()};
        String[] runCmd = new String[]{"java", "-cp", file.getParent(), getNameWithoutExt(file)};
        try {
            System.out.println(Arrays.toString(compileCmd));
            executeCommand(compileCmd);
            System.out.println(Arrays.toString(runCmd));
            return executeCommand(runCmd);
        }
        catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private static Pair<Integer, StringBuilder> executeCommand(String[] command) throws IOException, InterruptedException {
        int exitCode = 0;
        StringBuilder executed = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                executed.append(line);
            }

            exitCode = process.waitFor();
        }
        catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return Pair.of(exitCode, executed);
    }
    private Pair<Integer, StringBuilder> executeGo(File file) {
        int exitCode = 0;
        StringBuilder executed = new StringBuilder();
        return Pair.of(exitCode, executed);
    }
    private Pair<Integer, StringBuilder> executeCsharp(File file) {
        String[] buildCmd = new String[]{"dotnet", "build", file.getAbsolutePath()};
        String[] runCmd = new String[]{"dotnet", "run", "--project", file.getAbsolutePath()};
        try {
            executeCommand(buildCmd);
            return executeCommand(runCmd);
        }
        catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private Pair<Integer, StringBuilder> executeCpp(File file) {
        String executablePath = file.getParent() + File.separator + getNameWithoutExt(file);
        String[] compileCmd = new String[]{"g++", file.getAbsolutePath(), "-o", executablePath};
        String[] runCmd = new String[]{executablePath};
        try {
            executeCommand(compileCmd);
            return executeCommand(runCmd);
        }
        catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private String getExtension(String language) {
        return switch (language) {
            case "csharp" -> ".cs";
            case "javascript" -> ".js";
            case "julia" -> ".jl";
            case "kotlin" -> ".kt";
            case "python" -> ".py";
            case "rust" -> ".rs";
            case "typescript" -> ".ts";
            default -> "." + language;
        };
    }
    private String getNameWithoutExt(File file) {
        return file.getName().replaceFirst("[.][^.]+$", "");
    }
}
