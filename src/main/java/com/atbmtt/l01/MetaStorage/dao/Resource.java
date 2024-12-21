package com.atbmtt.l01.MetaStorage.dao;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
import java.time.LocalDateTime;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Resource {
    @Id
    @Tsid
    private Long id;
    @Column(nullable = false,columnDefinition = "nvarchar(255)")
    private String name;
    @Column(nullable = false)
    private LocalDateTime uploadTime;
    @Column(nullable = false)
    private LocalDateTime lastUpdate;
    @Column(nullable = false,columnDefinition = "nvarchar(255)")
    private String uri;
    @Column(nullable = false,precision = 2)
    private Long capacity;
    @OneToMany(mappedBy = "resource",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserResource> users = new ArrayList<>();

    public Resource(String name, LocalDateTime uploadTime, LocalDateTime lastUpdate, String uri, Long capacity) {
        this.name = name;
        this.uploadTime = uploadTime;
        this.lastUpdate = lastUpdate;
        this.uri = uri;
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addUser(UserAccount account){
        UserResource resource = new UserResource(this,account);
        users.add(resource);
        account.getResources().add(resource);
    }
    public void removeUser(UserAccount account){
        for(UserResource user : users){
            if(user.getResource().equals(this) && user.getAccount().equals(account)){
                user.setResource(null);
                user.setAccount(null);
                account.getResources().remove(user);
                users.remove(user);
                break;
            }
        }
    }
}
