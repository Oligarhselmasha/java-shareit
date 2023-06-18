package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "items")
@NoArgsConstructor

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @NotNull
    @Column
    private String name;

    @NotBlank
    @NotNull
    @Column
    private String description;

    @JsonProperty("available")
    @Column
    private Boolean isFree;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @ToString.Exclude
    private Request request;

    @Transient
    private BookingDto lastBooking;

    @Transient
    private BookingDto nextBooking;

    @Transient
    private List<CommentDto> comments = new ArrayList<>();

    @Transient
    private Integer requestId;
}
