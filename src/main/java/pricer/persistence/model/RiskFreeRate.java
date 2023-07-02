package pricer.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

//@Entity
//@Table(name = "risk_free_rate")
public class RiskFreeRate {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("record_timestamp")
    private String timestamp;

    @Column("rate")
    private double rate;

    @Column("currency")
    private String currency;
}
