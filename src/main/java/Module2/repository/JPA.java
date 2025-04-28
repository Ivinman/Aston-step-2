package Module2.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;
import java.util.function.Function;

@Slf4j
public class JPA {
	private final SessionFactory factory;

	public JPA() {
		factory = new Configuration().configure().buildSessionFactory();
	}

	public JPA(Properties properties) {
		factory = new Configuration().addProperties(properties).addAnnotatedClass(User.class).buildSessionFactory();
	}

	private EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

	public <T> T run(Function<EntityManager, T> action) {
		EntityManager manager = getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		try {
			transaction.begin();
			T result = action.apply(manager);
			transaction.commit();
			return result;
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			log.error("Transaction failed: {}", e.getMessage(), e);
			throw new JPAException("Cant perform transaction", e);
		} finally {
			if (manager.isOpen()) {
				manager.close();
			}
		}
	}

	public void exit() {
		System.out.println("clousing");
		factory.close();
	}
}
