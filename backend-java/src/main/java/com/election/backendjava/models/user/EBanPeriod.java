package com.election.backendjava.models.user;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum EBanPeriod {
    THREE_DAYS(3),
    SEVEN_DAYS(7),
    FOURTEEN_DAYS(14),
    ONE_MONTH(30),
    PERMANENT(Integer.MAX_VALUE); // Use Integer.MAX_VALUE to represent a permanent ban

    private final int days;

    EBanPeriod(int days) {
        this.days = days;
    }


    public static EBanPeriod getNextPeriod(int currentBanCount) {
        EBanPeriod[] periods = values();
        if (currentBanCount >= periods.length - 1) {
            return PERMANENT;
        }
        return periods[currentBanCount];
    }
}

