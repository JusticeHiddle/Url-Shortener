package com.example.url_shortener;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UrlController
 * 短網址服務對外接口，提供生成與轉址功能。
 */
@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * 產生短網址
     * @param url 原始長網址
     * @return 完整的短網址
     */
    @PostMapping("/api/shorten")
    public String shorten(@RequestParam String url) {
        String shortCode = urlService.shortenUrl(url);
        
        // TODO: 生產環境應將 Domain 配置於 application.properties，避免 Hardcode
        return "http://localhost:8080/" + shortCode; 
    }

    /**
     * 短網址轉向
     * @param shortCode Base62 短代碼
     * @param response 用於執行 HTTP 302 Redirect
     */
    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getOriginalUrl(shortCode);
        
        response.sendRedirect(originalUrl);
    }
}