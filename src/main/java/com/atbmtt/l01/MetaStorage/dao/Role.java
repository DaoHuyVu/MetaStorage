//package com.atbmtt.l01.MetaStorage.dao;
//
//import com.atbmtt.l01.MetaStorage.ERole;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import org.antlr.v4.runtime.misc.ObjectEqualityComparator;
//import org.apache.catalina.User;
//
//import java.util.Objects;
//import java.util.Set;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "role")
//public class Role {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
////    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
////    private Set<UserAccount> userAccounts;
//    @Enumerated(value = EnumType.STRING)
//    @Column(nullable = false)
//    private ERole role;
//
//    @Override
//    public boolean equals(Object o){
//        if(o == this) return true;
//        if(o == null || getClass() != o.getClass()) return false;
//        return this.id.equals(((Role) o).id);
//    }
//    @Override
//    public int hashCode(){
//        return Objects.hashCode(id);
//    }
//}
