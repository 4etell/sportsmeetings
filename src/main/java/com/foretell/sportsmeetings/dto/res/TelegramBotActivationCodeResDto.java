package com.foretell.sportsmeetings.dto.res;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TelegramBotActivationCodeResDto {
    private final String code;
}
