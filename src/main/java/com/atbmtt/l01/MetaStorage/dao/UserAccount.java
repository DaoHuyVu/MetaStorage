package com.atbmtt.l01.MetaStorage.dao;

import com.atbmtt.l01.MetaStorage.ERole;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor

public class UserAccount {
    @Id
    @Tsid
    private Long id;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name="is_activated",nullable = false)
    private Boolean isActivated;
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "account",orphanRemoval = true)
    private List<ActivateToken> tokens = new ArrayList<>();
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserResource> resources = new ArrayList<>();
    @Column(name = "role",nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole role;
//    @Column(name = "public_key",nullable = true)
//    public String publicKey;

    public UserAccount(String email, String password) {
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.isActivated = false;
        this.role = ERole.ROLE_USER;
    }
    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return Objects.equals(this.id, ((UserAccount) o).id);
    }
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    public void addToken(ActivateToken token){
        tokens.add(token);
        token.setAccount(this);
    }
    public void removeToken(ActivateToken token){
        tokens.remove(token);
        token.setAccount(null);
    }
}
