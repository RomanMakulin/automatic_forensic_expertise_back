package com.example.gigachat.controller;

import com.example.gigachat.SSLUtils;
import com.example.gigachat.service.GigaChatAuthService;
import com.example.gigachat.service.GigaChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gigachat")
public class GigaChatController {

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) throws Exception {
        SSLUtils.disableSSLVerification();
        String token = GigaChatAuthService.getAuthToken();
        return GigaChatService.sendMessage(message, token);
    }
}
