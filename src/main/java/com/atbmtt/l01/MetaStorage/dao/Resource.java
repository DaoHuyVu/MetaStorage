package com.atbmtt.l01.MetaStorage.dao;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;

public class Resource {
    @Id
    @Tsid
    private Long id;
}
