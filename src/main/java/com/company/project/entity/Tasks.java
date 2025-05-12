package com.company.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Tasks implements Serializable {
    public static final int TASKS_CONTENT_MAX_LENGTH = 4000;

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_id_seq")
    //@SequenceGenerator(name = "tasks_id_seq", sequenceName = "tasks_id_seq", allocationSize = 1)
    //@Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //@Column(name = "users_id")
    //private Integer usersId;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    //@Column(name = "task_detail_id")
    //private Integer taskDetailId;

    @ManyToOne
    @JoinColumn(name = "task_detail_id")
    private TasksDetail tasksDetail;

    @Column(name = "status")
    private Integer status;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.sql.Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private java.sql.Timestamp updatedAt;
}