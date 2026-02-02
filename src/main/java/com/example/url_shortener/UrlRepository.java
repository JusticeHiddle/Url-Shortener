package com.example.url_shortener;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Url 資料庫存取層
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    // 繼承 JpaRepository 即自動具備標準 CRUD 功能
}