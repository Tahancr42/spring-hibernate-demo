package metier;

import dao.IDao;
import entities.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ProductDaoImpl implements IDao<Product> {

    private final SessionFactory sessionFactory;

    public ProductDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session s() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public boolean create(Product product) {
        // Hibernate 6 : préférer persist()
        s().persist(product);
        return true;
    }

    @Override
    public boolean delete(Product product) {
        // Gérer le cas où l'entité est détachée
        Product managed = product;
        if (!s().contains(product)) {
            managed = s().merge(product);
        }
        s().remove(managed);
        return true;
    }

    @Override
    public boolean update(Product product) {
        // merge() fonctionne pour les entités détachées ou managées
        s().merge(product);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return s().get(Product.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return s().createQuery("from Product", Product.class)
                .getResultList(); // Hibernate 6
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(Long categoryId) {
        return s().createQuery(
                        "from Product p where p.category.id = :cid", Product.class)
                .setParameter("cid", categoryId)
                .getResultList();
    }
}
