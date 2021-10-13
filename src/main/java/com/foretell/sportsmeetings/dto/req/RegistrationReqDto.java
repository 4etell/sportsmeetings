package com.foretell.sportsmeetings.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegistrationReqDto {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 1, max = 30, message = "Min size of username: 1. Max size of username: 30")
    @ApiModelProperty(example = "user")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 256, message = "Min size of password: 6. Max size of password: 6")
    @ApiModelProperty(example = "123456")
    private String password;

    @NotBlank(message = "Firstname cannot be blank")
    @Size(min = 1, max = 200, message = "Min size of firstname: 1. Max size of firstname: 200")
    @ApiModelProperty(example = "ivan")
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank")
    @Size(min = 1, max = 200, message = "Min size of lastname: 1. Max size of lastname: 200")
    @ApiModelProperty(example = "ivanov")
    private String lastName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email cannot be blank")
    @ApiModelProperty(example = "ivanivan@mail.ru")
    private String email;
}
