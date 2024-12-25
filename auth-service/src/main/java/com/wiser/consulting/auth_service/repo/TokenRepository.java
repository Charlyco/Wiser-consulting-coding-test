package com.wiser.consulting.auth_service.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wiser.consulting.auth_service.entity.AuthToken;

@Repository
public interface TokenRepository extends JpaRepository<AuthToken, Long>{

    Optional<AuthToken> findAuthTokenByToken(String authToken);

    Optional<List<AuthToken>> getAllByUserId(Long userId);

}
