package com.challenge.tenpo.service;

import com.challenge.tenpo.dto.HistoryDTO;
import com.challenge.tenpo.model.History;

import java.util.List;

public interface IHistoryService {
    void save(HistoryDTO historyDTO);

    List<HistoryDTO> findAll(Integer page, Integer size, String sortBy);

}
