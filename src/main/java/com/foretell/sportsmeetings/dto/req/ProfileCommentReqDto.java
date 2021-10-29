package com.foretell.sportsmeetings.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProfileCommentReqDto {
    @NotBlank(message = "RecipentId cannot be blank")
    @Size(min = 1, max = 1000, message = "Max size of text is 1000 and min size is 1")
    private String text;
}
