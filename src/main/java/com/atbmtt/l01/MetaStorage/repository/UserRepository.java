package com.atbmtt.l01.MetaStorage.repository;

import com.atbmtt.l01.MetaStorage.dao.User;
import com.atbmtt.l01.MetaStorage.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("""
            SELECT new com.atbmtt.l01.MetaStorage.dto.UserDto(u.userName)
            from User u
            WHERE u.id = :id
            """)
    UserDto findUserById(@Param("id") Long id);
}
