package com.atbmtt.l01.MetaStorage.repository;

import com.atbmtt.l01.MetaStorage.dao.UserResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResourceRepository extends JpaRepository<UserResource,Long> {

    @Query("""
            SELECT CASE WHEN COUNT(ur) > 0 then true else false END
            FROM UserResource ur
            WHERE ur.account.id = :accountId and ur.resource.id = :resourceId
            """)
    Boolean checkExist(@Param("accountId") Long accountId,@Param("resourceId") Long resourceId);
}
