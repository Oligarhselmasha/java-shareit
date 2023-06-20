package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @JsonProperty("id")
    private Integer id;
    
    private String text;

    private String authorName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;
}
