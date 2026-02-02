package com.example.url_shortener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Url Entity
 * 儲存長網址與 ID 的對映關係。
 */
@Entity
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String longUrl;

    /**
     * 建立時間
     * 設定 updatable = false 以確保資料建立後不可被修改，可用於 Audit 或 TTL 清理。
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLongUrl() { return longUrl; }
    public void setLongUrl(String longUrl) { this.longUrl = longUrl; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}