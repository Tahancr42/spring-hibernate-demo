import dao.IDao;
import entities.Product;
import metier.ProductDaoImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import util.HibernateConfig;

public class Presentation2 {
    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(HibernateConfig.class)) {


            ProductDaoImpl productDao = context.getBean(ProductDaoImpl.class);


            Product product = new Product();
            product.setNom("Produit 1");
            product.setPrix(100.0);
            product.setDescription("Demo");
            product.setQuantite(5);

            productDao.create(product);

            System.out.println("Produit sauvegardé : " + product.getNom());
        }
    }
}
