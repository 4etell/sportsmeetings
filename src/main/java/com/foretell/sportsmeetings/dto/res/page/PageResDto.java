package com.foretell.sportsmeetings.dto.res.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PageResDto {
    private final int currentPage;
    private final int totalPage;
}
