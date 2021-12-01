package com.foretell.sportsmeetings.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MeetingCategoryReqDto {
    @NotBlank(message = "Name cannot be blank")
    @Size(message = "Max length of name is 100")
    private String name;
}
