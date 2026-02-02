package com.example.url_shortener;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UrlService
 * 處理短網址核心業務邏輯。
 * 實作策略：利用資料庫 Auto-increment ID 進行 Base62 編碼，確保高併發下的唯一性。
 */
@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    /**
     * 產生短網址
     * 流程：DB Persist -> Get ID -> Base62 Encode
     * @param originalUrl 原始長網址
     * @return Base62 短代碼
     */
    @Transactional
    public String shortenUrl(String originalUrl) {
        // 先寫入 DB 以取得唯一的 Auto-increment ID
        Url url = new Url();
        url.setLongUrl(originalUrl);
        Url savedUrl = urlRepository.save(url);

        return encode(savedUrl.getId());
    }

    /**
     * 解析短網址
     * @param shortCode Base62 短代碼
     * @return 原始長網址
     * @throws RuntimeException 若找不到對應記錄
     */
    public String getOriginalUrl(String shortCode) {
        long id = decode(shortCode);

        return urlRepository.findById(id)
                .map(Url::getLongUrl)
                .orElseThrow(() -> new RuntimeException("URL not found for code: " + shortCode));
    }

    // --- Base62 Helper Methods ---

    /**
     * Base62 Encoding (10進位 -> 62進位)
     */
    private String encode(long id) {
        if (id == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            int remainder = (int) (id % 62);
            sb.append(BASE62_CHARS.charAt(remainder));
            id /= 62;
        }
        return sb.reverse().toString();
    }

    /**
     * Base62 Decoding (62進位 -> 10進位)
     */
    private long decode(String shortCode) {
        long id = 0;
        for (char c : shortCode.toCharArray()) {
            int val = BASE62_CHARS.indexOf(c);
            if (val == -1) {
                throw new IllegalArgumentException("Invalid character in short URL: " + c);
            }
            id = id * 62 + val;
        }
        return id;
    }
}