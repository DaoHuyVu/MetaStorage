package com.atbmtt.l01.MetaStorage.repository;

import com.atbmtt.l01.MetaStorage.dao.ActivateToken;
import com.atbmtt.l01.MetaStorage.dto.ActivateTokenDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivateTokenRepository extends JpaRepository<ActivateToken,Long> {
    @Query("""
            SELECT new com.atbmtt.l01.MetaStorage.dto.ActivateTokenDto(t.token,t.expiredTime,t.account.id)
            from ActivateToken t where token = :token
            """)
    ActivateTokenDto findByToken(@Param("token") String token);
}
