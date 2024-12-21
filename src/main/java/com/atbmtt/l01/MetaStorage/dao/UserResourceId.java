package com.atbmtt.l01.MetaStorage.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserResourceId implements Serializable {
    @Column
    private Long userId;
    @Column
    private Long resourceId;
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        UserResourceId that = (UserResourceId) o;
        return Objects.equals(that.resourceId,resourceId)
                && Objects.equals(that.userId,userId);
    }
    @Override
    public int hashCode(){
        return Objects.hash(userId,resourceId);
    }
}
