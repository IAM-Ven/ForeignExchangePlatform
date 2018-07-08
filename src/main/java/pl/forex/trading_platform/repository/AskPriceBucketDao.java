package pl.forex.trading_platform.repository;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.forex.trading_platform.domain.AskPriceBucket;
import pl.forex.trading_platform.domain.Instrument;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class AskPriceBucketDao {

    @PersistenceContext
    EntityManager entityManager;

    public void save(AskPriceBucket askPriceBucket) {
        entityManager.persist(askPriceBucket);
    }

    public AskPriceBucket findById(long id) {
        return entityManager.find(AskPriceBucket.class, id);
    }

    public void update(AskPriceBucket entity) {
        entityManager.merge(entity);
    }
    public void delete(AskPriceBucket entity) { ;
        entityManager.remove(entityManager.contains(entity) ?
                entity : entityManager.merge(entity));

    }
}
