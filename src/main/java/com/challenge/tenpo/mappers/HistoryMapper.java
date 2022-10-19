package com.challenge.tenpo.mappers;

import com.challenge.tenpo.dto.HistoryDTO;
import com.challenge.tenpo.model.History;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HistoryMapper {
    public static History map(HistoryDTO historyDTO) {
        return History.builder()
                .endpoint(historyDTO.getEndpoint())
                .status(historyDTO.getStatus())
                .build();
    }
    public static HistoryDTO mapAdto(History history) {
        return HistoryDTO.builder()
                .endpoint(history.getEndpoint())
                .status(history.getStatus())
                .dateConsumer(history.getDateConsumer())
                .build();
    }

}
