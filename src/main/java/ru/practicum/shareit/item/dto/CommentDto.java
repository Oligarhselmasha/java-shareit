package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @JsonProperty("id")
    private Integer id;

    @Column
    private String text;

    @Column
    private String authorName;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;
}
