package com.atbmtt.l01.MetaStorage.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class ActivateToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NaturalId
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount account;
    @Column(nullable = false)
    private Date expiredTime;

    public ActivateToken(String token) {
        this.token = token;
        expiredTime = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof ActivateToken)){
            return false;
        }
        return id != null && id.equals(((ActivateToken) o).id);
    }
    @Override
    public int hashCode(){
        return getClass().hashCode();
    }
}
