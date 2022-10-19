package com.challenge.tenpo.repository;

import com.challenge.tenpo.model.SessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionData, Long> {

    boolean existsByToken(String token);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    SessionData getByUsername(String username);

}
