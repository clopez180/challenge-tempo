package com.challenge.tenpo.service.impl;

import com.challenge.tenpo.dto.HistoryDTO;
import com.challenge.tenpo.mappers.HistoryMapper;
import com.challenge.tenpo.model.History;
import com.challenge.tenpo.repository.HistoryRepository;
import com.challenge.tenpo.service.IHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryService implements IHistoryService {

    @Autowired
    private final HistoryRepository historyRepository;

    @Override
    @Transactional
    public void save(HistoryDTO historyDTO) {
        historyRepository.save(HistoryMapper.map(historyDTO));
    }

    @Override
    public List<HistoryDTO> findAll(Integer page, Integer size, String sortBy) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<History> pageResult = historyRepository.findAll(paging);
        return pageResult.hasContent()
                ? pageResult.getContent().stream().map(HistoryMapper::mapAdto).toList()
                : List.of();
    }
}
