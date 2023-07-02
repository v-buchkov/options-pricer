package pricer.persistence.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pricer.persistence.model.FortsOptDeal;

// TODO: optimize SQL select ("LIKE" does not use indexing - maybe use explicit types in the repository)
@Repository
public interface FortsOptDealRepository extends CrudRepository<FortsOptDeal, Long> {
    @Query("SELECT AVG(qty) " +
            "FROM forts_opt_deal " +
            "deal_timestamp >= :dateStart and " +
            "deal_timestamp <= :dateEnd and " +
            "WHERE ticker LIKE ':tickerPattern%'")
    double getAverageQtyForPeriod(@Param("tickerPattern") String tickerPattern,
                                  @Param("dateStart") String dateStart,
                                  @Param("dateEnd") String dateEnd);

}
