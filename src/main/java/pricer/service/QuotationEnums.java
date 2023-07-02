package pricer.service;

public class QuotationEnums {
    // change over time (business needs)
    public enum INSTRUMENT {
        EUROPEAN_CALL,
        EUROPEAN_PUT,
        AMERICAN_CALL,
        AMERICAN_PUT
    }

    public enum BASE_ASSET {
        USDRUB("Si");

        public final String moexTicker;

        BASE_ASSET(String moexTicker) {
            this.moexTicker = moexTicker;
        }

        public String getQuotationCurrency() {
            return this.name().substring(this.name().length() - 3);
        }
    }
}
