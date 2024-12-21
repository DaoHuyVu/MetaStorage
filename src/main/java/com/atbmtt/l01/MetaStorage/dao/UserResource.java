package com.atbmtt.l01.MetaStorage.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserResource {
    @EmbeddedId
    private UserResourceId userResourceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private UserAccount account;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("resourceId")
    private Resource resource;
    @Column(nullable = false)
    private boolean isOwner;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        UserResource that = (UserResource) o;
        return Objects.equals(that.resource,resource)
                && Objects.equals(that.account,account);
    }
    @Override
    public int hashCode(){
        return Objects.hash(account,resource);
    }
    public UserResource(Resource resource,UserAccount account){
        this.userResourceId = new UserResourceId(account.getId(),resource.getId());
        this.account = account;
        this.resource = resource;
    }
}
