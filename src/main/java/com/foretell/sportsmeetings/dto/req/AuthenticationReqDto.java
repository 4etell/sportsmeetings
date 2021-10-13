package com.foretell.sportsmeetings.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AuthenticationReqDto {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 1, max = 30, message = "Min size of username: 1. Max size of username: 30")
    @ApiModelProperty(example = "user")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 256, message = "Min size of password: 6. Max size of password: 6")
    @ApiModelProperty(example = "123456")
    private String password;

}

