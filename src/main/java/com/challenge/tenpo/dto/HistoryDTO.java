package com.challenge.tenpo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {

    private Long id;

    private String endpoint;

    private String status;

    private LocalDateTime dateConsumer;

}
