package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.item.model.Comment;

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
    private LocalDateTime created;
}
