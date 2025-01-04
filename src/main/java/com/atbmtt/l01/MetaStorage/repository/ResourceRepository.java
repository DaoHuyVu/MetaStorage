package com.atbmtt.l01.MetaStorage.repository;

import com.atbmtt.l01.MetaStorage.dao.Resource;
import com.atbmtt.l01.MetaStorage.dto.ResourceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource,Long> {
    @Query("""
            SELECT new com.atbmtt.l01.MetaStorage.dto.ResourceDto(r.id,r.name,r.uploadTime,r.lastUpdate,r.capacity,r.uri,r.isFavourite,r.isTempDelete)
            from Resource r
            join UserResource ur
            on r.id = ur.resource.id
            where ur.isOwner = true and ur.account.email = :email
            """)
    Optional<List<ResourceDto>> findByOwnerName(@Param("email") String userName);
}
