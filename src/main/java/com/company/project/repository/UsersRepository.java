package com.company.project.repository;

import com.company.project.entity.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UsersRepository extends SimpleJpaRepository<Users, Integer> {
    EntityManager entityManager;
    protected JpaEntityInformation<Users, ?> entityInformation;

    public UsersRepository(EntityManager entityManager) {
        super(Users.class, entityManager);
        this.entityManager = entityManager;
    }

    public Users findOne(int id){
        Query query = entityManager.createQuery("SELECT u " + "FROM Users u WHERE u.id = :id");
        query.setParameter("id", id);
        List<Users> list = query.getResultList();
        if(list.size() > 0){
            return (Users)list.get(0);
        }

        return null;
    }

    @Transactional
    public List<Users> search(
            Integer userId,
            Integer tasksStatus,
            String tasksDetailName) {
        List<Users> results = null;
        try {
            String sQuery =
                    "SELECT u FROM Users u"
                    + " LEFT JOIN FETCH u.setTask t"
                    + " LEFT JOIN FETCH t.tasksDetail td"
                    + " WHERE"
                    + " 1 = 1";
            if(userId != null){
                sQuery += " AND u.ID = :userId";
            }

            if(tasksStatus != null){
                sQuery += " AND t.status = :tasksStatus";
            }

            if(tasksDetailName != null){
                sQuery += " AND td.name LIKE :tasksDetailName";
            }

            Query query = entityManager.createQuery(sQuery);
            if(userId != null) {
                query.setParameter("userId", userId);
            }

            if(tasksStatus != null){
                query.setParameter("tasksStatus", tasksStatus);
            }

            if(tasksDetailName != null){
                query.setParameter("tasksDetailName", "%" + tasksDetailName + "%");
            }

            results = query.getResultList();
            return results;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return new ArrayList<Users>();
    }
}