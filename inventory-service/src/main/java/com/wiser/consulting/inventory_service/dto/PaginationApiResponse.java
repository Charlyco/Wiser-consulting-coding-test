package com.wiser.consulting.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PaginationApiResponse {
    private Object data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
