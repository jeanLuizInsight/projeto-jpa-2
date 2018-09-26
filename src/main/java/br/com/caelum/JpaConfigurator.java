package br.com.caelum;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
public class JpaConfigurator {

	@Bean
	public DataSource getDataSource() throws PropertyVetoException {
		// configurando pool de conexões utilizando padrão c3p0
	    ComboPooledDataSource dataSource = new ComboPooledDataSource();
	    dataSource.setDriverClass("com.mysql.jdbc.Driver");
	    dataSource.setUser("root");
	    dataSource.setPassword("ae3Fresqor10@");
	    dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/projeto_jpa");
	    
	    dataSource.setMinPoolSize(3);
	    dataSource.setMaxPoolSize(5);
	    // a cada um segundo testamos as conexões ociosas
	    dataSource.setIdleConnectionTestPeriod(1); 
	    return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setPackagesToScan("br.com.caelum");
		entityManagerFactory.setDataSource(dataSource);

		entityManagerFactory
				.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Properties props = new Properties();

		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.hbm2ddl.auto", "update");
		// usando cache nivel 2
		props.setProperty("hibernate.use_second_level_cache", "true");
		props.setProperty("hibernate.cache.use_query_cache", "true");
		props.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
		
		entityManagerFactory.setJpaProperties(props);
		return entityManagerFactory;
	}

	@Bean
	public JpaTransactionManager getTransactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

}
