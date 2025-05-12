package com.company.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users implements Serializable {
    public static final int USERS_USER_NAME_MAX_LENGTH = 100;
    public static final int USERS_FULL_NAME_MAX_LENGTH = 100;

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    //@SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    //@Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.sql.Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private java.sql.Timestamp updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Tasks> setTask;
}