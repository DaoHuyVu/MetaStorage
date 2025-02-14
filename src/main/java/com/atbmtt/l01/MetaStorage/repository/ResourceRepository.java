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
            SELECT new com.atbmtt.l01.MetaStorage.dto.ResourceDto(r.id,r.name,r.uploadTime,r.lastUpdate,r.capacity,r.uri,r.isFavourite,r.isTempDelete,r.password,r.sharedAt)
            from Resource r
            join UserResource ur
            on r.id = ur.resource.id
            where ur.isOwner = true and ur.account.email = :email
            """)
    Optional<List<ResourceDto>> findByOwnerName(@Param("email") String userName);
    @Query("""
            SELECT r
            from Resource r
            where uri = :uri
            """)
    Optional<Resource> findByUri(@Param("uri") String uri);

    @Query("""
            SELECT new com.atbmtt.l01.MetaStorage.dto.ResourceDto(r.id,r.name,r.uploadTime,r.lastUpdate,r.capacity,r.uri,r.isFavourite,r.isTempDelete,r.password,r.sharedAt)
            from Resource r
            join UserResource ur
            on r.id = ur.resource.id
            join UserAccount uc
            on ur.account.id = uc.id
            where uc.email = :email and ur.isOwner = false
            """)
    Optional<List<ResourceDto>> findSharedResources(@Param("email") String email);
}
