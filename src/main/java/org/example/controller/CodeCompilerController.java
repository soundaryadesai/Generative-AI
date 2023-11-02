package org.example.controller;

import org.example.model.CodeRequest;
import org.example.service.CodeCompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/code")
public class CodeCompilerController {
    @Autowired
    CodeCompilerService codeCompilerService;

    @PostMapping("/compiler")
    public String codeCompiler(@RequestBody CodeRequest code) {
        return codeCompilerService.codeCompiler( code.getPrompt());
    }

}