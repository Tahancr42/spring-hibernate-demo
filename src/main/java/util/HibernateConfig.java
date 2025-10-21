package util;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"dao", "metier"}) // pas besoin de scanner "entities"
@PropertySource("classpath:application.properties")
public class HibernateConfig {

    // ---- valeurs lues depuis application.properties (Spring Boot style OK) ----
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${spring.jpa.hibernate.ddl-auto:update}") // valeur par défaut "update"
    private String hibernateDdlAuto;

    @Value("${spring.jpa.show-sql:false}")
    private String showSql;

    @Value("${spring.jpa.properties.hibernate.format_sql:false}")
    private String formatSql;

    // --------------------------------------------------------------------------

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(dataSource);
        // IMPORTANT : package des entités
        sf.setPackagesToScan("entities");

        Properties props = new Properties();
        props.put("hibernate.dialect", hibernateDialect);
        props.put("hibernate.hbm2ddl.auto", hibernateDdlAuto); // create / update / validate / none
        props.put("hibernate.show_sql", showSql);
        props.put("hibernate.format_sql", formatSql);

        // Pour que sessionFactory.getCurrentSession() fonctionne avec Spring
        props.put("hibernate.current_session_context_class",
                "org.springframework.orm.hibernate5.SpringSessionContext");

        // (Optionnel) gérer la timezone des timestamps côté JDBC
        // props.put("hibernate.jdbc.time_zone", "UTC");

        sf.setHibernateProperties(props);
        return sf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        // Injection par paramètre = plus propre
        return new HibernateTransactionManager(sessionFactory);
    }
}
