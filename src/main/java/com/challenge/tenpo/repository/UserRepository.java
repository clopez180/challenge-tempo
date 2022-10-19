package com.challenge.tenpo.repository;

import com.challenge.tenpo.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {

    UserData findByUsername(String username);

    boolean existsUserByUsername(String username);

}
