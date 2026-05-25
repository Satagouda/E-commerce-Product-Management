package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {
    
    @Column(nullable = false, unique = true, length = 50)
    private String name; // ADMIN, USER, SELLER, CUSTOMER_SUPPORT
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
    
    public enum RoleType {
        ADMIN, USER, SELLER, CUSTOMER_SUPPORT
    }
}
