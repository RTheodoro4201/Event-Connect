package io.github.rtheodoro4201.eventconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    @Size(max = 255)
    @Email
    @NotNull
    private String email;

    @Size(max = 255)
    @NotNull
    private String password;
}
