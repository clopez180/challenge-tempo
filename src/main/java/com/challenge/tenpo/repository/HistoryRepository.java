package com.challenge.tenpo.repository;

import com.challenge.tenpo.model.History;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends PagingAndSortingRepository<History, Long> {
}
