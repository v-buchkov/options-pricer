package pricer.persistence.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pricer.persistence.model.RiskFreeRate;

@Repository
public interface RiskFreeRateRepository extends CrudRepository<RiskFreeRate, Long> {
    @Query("SELECT rate FROM risk_free_rate " +
            "WHERE currency = :currency " +
            "ORDER BY record_timestamp DESC " +
            "LIMIT 1")
    double getCurrentRiskFreeRate(String currency);
}
