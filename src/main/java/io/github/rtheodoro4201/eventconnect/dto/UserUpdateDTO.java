package io.github.rtheodoro4201.eventconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 255)
    private String password;
}
