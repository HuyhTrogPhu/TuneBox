package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRevenueInfo {
    private UserSell user;

    private Double todayRevenue;

    private Double weekRevenue;

    private Double monthRevenue;

    private Double yearRevenue;
}
