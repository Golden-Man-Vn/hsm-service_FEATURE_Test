package com.company.project.repository;

import com.company.project.entity.TasksDetail;
import com.company.project.entity.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TasksDetailRepository extends SimpleJpaRepository<TasksDetail, Integer> {
    EntityManager entityManager;
    protected JpaEntityInformation<TasksDetail, ?> entityInformation;

    public TasksDetailRepository(EntityManager entityManager) {
        super(TasksDetail.class, entityManager);
        this.entityManager = entityManager;
    }

    public TasksDetail findOne(int id){
        Query query = entityManager.createQuery("SELECT u " + "FROM TasksDetail u WHERE u.id = :id");
        query.setParameter("id", id);
        List<TasksDetail> list = query.getResultList();
        if(list.size() > 0){
            return (TasksDetail)list.get(0);
        }

        return null;
    }
}