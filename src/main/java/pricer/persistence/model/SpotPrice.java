package pricer.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

//@Entity
//@Table(name = "spot_price")
public class SpotPrice {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("asset")
    private String asset;

    @Column("record_timestamp")
    private String timestamp;

    @Column("bid")
    private double bid;

    @Column("ask")
    private double ask;
}
