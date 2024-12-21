package com.atbmtt.l01.MetaStorage.repository;

import com.atbmtt.l01.MetaStorage.dao.UserResource;
import com.atbmtt.l01.MetaStorage.dao.UserResourceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResourceRepository extends JpaRepository<UserResource, UserResourceId> {
}
