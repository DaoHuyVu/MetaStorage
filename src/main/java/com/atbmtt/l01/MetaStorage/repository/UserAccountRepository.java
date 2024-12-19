package com.atbmtt.l01.MetaStorage.repository;

import com.atbmtt.l01.MetaStorage.dao.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {
    UserAccount findByEmail(@Param("email") String email);
}
