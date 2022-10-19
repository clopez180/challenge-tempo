package com.challenge.tenpo.service.impl;

import com.challenge.tenpo.dto.HistoriesResponseDTO;
import com.challenge.tenpo.dto.HistoryDTO;
import com.challenge.tenpo.mappers.HistoryMapper;
import com.challenge.tenpo.model.History;
import com.challenge.tenpo.repository.HistoryRepository;
import com.challenge.tenpo.service.IChallengeService;
import com.challenge.tenpo.service.extern.ServiceExternoMock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ChallengeServiceImpl implements IChallengeService {

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    private ServiceExternoMock serviceExternoMock;

    @Override
    public BigDecimal getAdd(BigDecimal  numberOne, BigDecimal  numberTwo) {

        BigDecimal sum = numberOne.add(numberTwo);
        int percent = serviceExternoMock.getValuePercentage();
        return sum.add((sum.multiply(BigDecimal.valueOf(percent / 100.0f))));

    }

    @Override
    public HistoriesResponseDTO getHistories(Integer page, Integer size, String sortBy) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<History> pageResult = historyRepository.findAll(paging);
        List<HistoryDTO> lis = pageResult.hasContent()
                ? pageResult.getContent().stream().map(HistoryMapper::mapAdto).toList()
                : List.of();
        return HistoriesResponseDTO.builder().history(lis).build();
    }

    @Override
    @Transactional
    public void save(HistoryDTO historyDTO) {
        historyRepository.save(HistoryMapper.map(historyDTO));
    }
}
