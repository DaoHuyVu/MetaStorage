package com.atbmtt.l01.MetaStorage.dao;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
public class User{
    @Id
    private Long id;
    @Column(name = "name",nullable = false,columnDefinition = "nvarchar(255)")
    private String userName;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private UserAccount userAccount;
    public User(String name){
        this.userName = name;
    }
}
