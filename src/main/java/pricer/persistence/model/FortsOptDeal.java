package pricer.persistence.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

//@Entity
//@Table(name = "forts_opt_deal")
public class FortsOptDeal {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("ticker")
    private String ticker;

    @Column("option_type")
    private String optionType;

    @Column("deal_timestamp")
    private String timestamp;

    @Column("price")
    private double price;

    @Column("qty")
    private Integer qty;

    @Column("open_pos")
    private double openPos;

    @Column("direction")
    private String dealSide;
}
