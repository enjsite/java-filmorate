package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private int eventId;

    private Long timestamp;

    private EventType eventType;

    private Operation operation;

    private Integer userId;

    private Integer entityId;

    public Event(Long timestamp, EventType eventType, Operation operation, Integer userId, Integer entityId) {
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.operation = operation;
        this.userId = userId;
        this.entityId = entityId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("timestamp", timestamp);
        values.put("event_type", eventType);
        values.put("operation", operation);
        values.put("user_id", userId);
        values.put("entity_id", entityId);
        return values;
    }
}
