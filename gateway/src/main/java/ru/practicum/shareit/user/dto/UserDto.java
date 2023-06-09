package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(groups = {OnCreate.class})
    private String name;

    @NotEmpty(groups = {OnCreate.class})
    @Email(groups = {OnCreate.class, OnUpdate.class})
    private String email;
}
