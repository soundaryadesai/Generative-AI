package org.example.service;

import org.springframework.stereotype.Service;

import javax.tools.*;
import java.util.Arrays;
import java.util.List;

@Service
public class CodeCompilerService {
    public String codeCompiler(String javaCode) {

        StringBuilder strB= new StringBuilder();
        // Create a new JavaCompiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // Diagnostic collector to store compilation diagnostics (e.g., errors)
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        // FileManager to manage source and class files
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        // Create a source file from the Java code
        List<JavaFileObject> compilationUnits = List.of(new JavaSourceFromString("Code", javaCode));

        // Compilation options (e.g., classpath)
        Iterable<String> options = Arrays.asList("-classpath", System.getProperty("java.class.path"));

        // Perform the compilation
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean success = task.call();

        if (success) {
            System.out.println("Compilation successful!");
            strB.append("Compilation successful !!");
        } else {
            System.out.println("Compilation failed:");

            // Print compilation errors
            for (var diagnostic : diagnostics.getDiagnostics()) {
                System.out.println(diagnostic);
                strB.append(diagnostic);
            }
        }

        return strB.toString();
    }
}
