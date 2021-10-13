package com.foretell.sportsmeetings.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthenticationResDto {

    @ApiModelProperty(example = "user")
    private String username;

    @ApiModelProperty(example = "eyJhbGciOiJ.IUzUxMiJ9")
    private String token;

    private List<String> roles;

}