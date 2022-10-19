package com.challenge.tenpo.service;

import com.challenge.tenpo.dto.HistoriesResponseDTO;
import com.challenge.tenpo.dto.HistoryDTO;
import java.math.BigDecimal;

public interface IChallengeService {

    BigDecimal getAdd(BigDecimal numberOne, BigDecimal numberTwo);

    HistoriesResponseDTO getHistories(Integer offset, Integer pageSize, String sortBy);

    void save(HistoryDTO historyDTO);
}
