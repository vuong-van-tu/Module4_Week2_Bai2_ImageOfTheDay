package com.vuongtu.service.Impl;

import com.vuongtu.model.Comment;
import com.vuongtu.service.CommentService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class HibernateCommentServiceImpl implements CommentService {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;
    private final static String queryStr = "Update Comment set likes = :newLike where id= :id";
    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.conf.xml")
                    .buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Comment> showAllComment() {
        String queryStr = "SELECT c FROM Comment as c where (c.date = CURRENT_DATE )";
        TypedQuery<Comment> query = entityManager.createQuery(queryStr, Comment.class);
        return query.getResultList();
    }

    @Override
    public Comment addComment(Comment comment) {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        Comment origin = new Comment();
        origin.setFeedback(comment.getFeedback());
        origin.setAuthor(comment.getAuthor());
        origin.setRating("+ " + comment.getRating());
        origin.setDate(comment.getDate());
        session.save(origin);
        transaction.commit();
        return origin;
    }

    @Override
    public void addLike(Comment comment) {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        comment.setLikes(comment.getLikes()+1);
        Query query = session.createQuery(queryStr);
        query.setParameter("newLike",comment.getLikes());
        query.setParameter("id",comment.getId());
        query.executeUpdate();

        transaction.commit();
        session.clear();
        session.close();
    }

    @Override
    public void disLike(Comment comment) {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        comment.setLikes(comment.getLikes()-1);
        Query query = session.createQuery(queryStr);
        query.setParameter("newLike",comment.getLikes());
        query.setParameter("id",comment.getId());
        query.executeUpdate();

        transaction.commit();
        session.clear();
        session.close();
    }

    @Override
    public Comment findOne(long id) {
        String queryStr = "SELECT c FROM Comment AS c WHERE c.id=:id";
        TypedQuery<Comment> query = entityManager.createQuery(queryStr, Comment.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
}
