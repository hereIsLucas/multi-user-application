package com.example.multiuserapplication.dto;

/**
 * @author : lucas
 * @project : MultiUserApplication
 * @created : 12/07/2024, Friday
 **/
import lombok.Getter;

import java.util.Date;

@Getter
public class BookingRequest {

    private Date date;
    private String dayName;
    private Long roomId;
    private Long userId;
    private String status;

    public void setDate(Date date) { this.date = date; }

    public void setDayName(String dayName) { this.dayName = dayName; }

    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public void setStatus(String status) { this.status = status; }
}