package com.comp9900.proj_15.dto;

import com.comp9900.proj_15.entity.Event;
import lombok.Data;
import java.util.List;

@Data
public class EventDetailDTO {
    private Event event;
    private List<ParticipantInfo> participants;

    @Data
    public static class ParticipantInfo {
        private Integer userId;
        private String userName;
    }
} 