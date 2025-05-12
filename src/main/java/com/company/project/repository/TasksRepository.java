package com.company.project.repository;

import com.company.project.entity.Tasks;
import com.company.project.entity.TasksDetail;
import com.company.project.entity.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TasksRepository extends SimpleJpaRepository<Tasks, Integer> {
    EntityManager entityManager;
    protected JpaEntityInformation<Tasks, ?> entityInformation;

    public TasksRepository(EntityManager entityManager) {
        super(Tasks.class, entityManager);
        this.entityManager = entityManager;
    }

    public Tasks findOne(int id){
        Query query = entityManager.createQuery("SELECT u " + "FROM Tasks u WHERE u.id = :id");
        query.setParameter("id", id);
        List<Tasks> list = query.getResultList();
        if(list.size() > 0){
            return (Tasks)list.get(0);
        }

        return null;
    }

    //@Transactional
    public Tasks search(
            Integer userId,
            Integer taskDetailId) {
        try {
            String sQuery ="SELECT t FROM Tasks t"
                    + " WHERE"
                    + " 1 = 1"
                    + " AND t.users.id = :userId"
                    + " AND t.tasksDetail.id = :taskDetailId";
            Query query = entityManager.createQuery(sQuery);
            query.setParameter("userId", userId);
            query.setParameter("taskDetailId", taskDetailId);

            var results = query.getResultList();
            if(results.size() > 0){
                return (Tasks)results.get(0);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            //throw ex;
        }

        return null;
    }
}