package pricer.persistence.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pricer.persistence.model.SpotPrice;

@Repository
public interface SpotPriceRepository extends CrudRepository<SpotPrice, Long> {
    @Query("SELECT (bid + ask) / 2 FROM spot_price " +
            "WHERE asset = :asset and " +
            "bid IS NOT NULL and " +
            "ask IS NOT NULL " +
            "ORDER BY record_timestamp DESC " +
            "LIMIT 1")
    double getCurrentSpot(@Param("asset") String asset);

    @Query("SELECT (bid + ask) / 2 FROM spot_price " +
            "WHERE asset = :asset and " +
            "record_timestamp >= :dateStart and " +
            "record_timestamp <= :dateStart and " +
            "bid IS NOT NULL and " +
            "ask IS NOT NULL " +
            "ORDER BY record_timestamp")
    double[] getSpotPricesForPeriod(@Param("asset") String asset, @Param("dateStart") String dateStart,
                                    @Param("dateEnd") String dateEnd);

}
