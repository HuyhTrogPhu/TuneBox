package org.example.library.service;

import java.util.Arrays;
import java.util.List;

public class SensitiveContentService {

    // Danh sách từ ngữ nhạy cảm
    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
        "cc", "vai lol", "dit", "du ma"
    );

    // Phương thức kiểm tra nếu nội dung chứa từ ngữ nhạy cảm
    public boolean containsSensitiveContent(String content) {
        for (String word : SENSITIVE_WORDS) {
            if (content.toLowerCase().contains(word.toLowerCase())) {
                return true; // Nếu tìm thấy từ nhạy cảm
            }
        }
        return false; // Không tìm thấy từ nhạy cảm
    }
}
