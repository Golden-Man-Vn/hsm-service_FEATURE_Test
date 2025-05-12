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
@Table(name = "tasks_detail")
public class TasksDetail implements Serializable {
    public static final int TASKS_DETAIL_NAME_MAX_LENGTH = 255;
    public static final int TASKS_DETAIL_CONTENT_MAX_LENGTH = 4000;

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_detail_id_seq")
    //@SequenceGenerator(name = "tasks_detail_id_seq", sequenceName = "tasks_detail_id_seq", allocationSize = 1)
    //@Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

    @Column(name = "note")
    private String note;

    @Column(name = "bug_severity")
    private Integer bugSeverity;

    @Column(name = "bug_steps_to_reproduce")
    private String bugStepsToReproduce;

    @Column(name = "feature_business_value")
    private Integer featureBusinessValue;

    @Column(name = "feature_deadline")
    private java.sql.Timestamp featureDeadline;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.sql.Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private java.sql.Timestamp updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tasksDetail")
    private Set<Tasks> setTask;
}

