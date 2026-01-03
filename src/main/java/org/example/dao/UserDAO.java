package org.example.dao;

import org.example.exception.InvalidLoginException;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAO {

    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Save a new user
    public void saveUser(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Login
    public User login(String email, String password) throws InvalidLoginException {
        try (Session session = sessionFactory.openSession()) {
            // Use fully qualified class name in HQL
            User user = session.createQuery(
                            "FROM org.example.model.User WHERE lower(email) = :email AND password = :password", User.class)
                    .setParameter("email", email.toLowerCase())
                    .setParameter("password", password)
                    .uniqueResult();

            if (user == null) throw new InvalidLoginException("Invalid email or password!");
            return user;
        }
    }

    // Get all users
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            // Use fully qualified class name
            return session.createQuery("FROM org.example.model.User", User.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // return empty list on failure
        }
    }

    // Get user by ID
    public User getUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }
}
