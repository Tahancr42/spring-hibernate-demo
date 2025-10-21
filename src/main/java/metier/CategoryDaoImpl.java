package metier;

import dao.CategoryDao;
import entities.Category;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CategoryDaoImpl implements CategoryDao {

    private final SessionFactory sessionFactory;

    public CategoryDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session s() { return sessionFactory.getCurrentSession(); }


    @Override
    public boolean create(Category o) {
        s().persist(o);      // Hibernate 6
        return true;
    }

    @Override
    public boolean delete(Category o) {
        // si entité détachée, on la rattache avant remove
        Category managed = o;
        if (!s().contains(o)) {
            managed = s().merge(o);
        }
        s().remove(managed);
        return true;
    }

    @Override
    public boolean update(Category o) {
        s().merge(o);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return s().get(Category.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return s().createQuery("from Category", Category.class)
                .getResultList();
    }


    @Override
    @Transactional(readOnly = true)
    public Category findByName(String name) {
        try {
            Query<Category> q = s().createQuery(
                    "from Category c where lower(c.name) = :n", Category.class);
            q.setParameter("n", name.toLowerCase());
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public boolean deleteById(Long id) {
        Category c = findById(id);
        if (c != null) {
            s().remove(c);
            return true;
        }
        return false;
    }
}
