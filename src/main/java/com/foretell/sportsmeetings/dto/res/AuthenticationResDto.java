package com.foretell.sportsmeetings.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthenticationResDto {

    @ApiModelProperty(example = "eyJhbGciOiJ.IUzUxMiJ9")
    private final String token;

}