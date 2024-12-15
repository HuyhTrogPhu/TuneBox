package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyPostStatsDto {
    private LocalDate date;
    private long postCount;
    private long likeCount;
    private long commentCount;
    private double engagementRate;
}

