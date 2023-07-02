package pricer.controller;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pricer.service.QuotationEnums;
import pricer.service.QuotationService;

import java.util.UUID;
import java.sql.Timestamp;

import static pricer.service.StaticParams.timestampFormat;

@RestController
public class PricerApiController {
    private final QuotationService quotationService;

    public PricerApiController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    // TODO: SpringQueryMap
    @GetMapping(value = "/api/pricer/get-quote",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public QuoteResponse getQuote(
            @RequestParam() QuotationEnums.INSTRUMENT instrument,
            @RequestParam() Integer qty,
            @Nullable @RequestParam(required = false, defaultValue = "MARKET") QuotationService.SIDE side,
            @RequestParam() Integer minutesAvailable,
            @RequestParam() double term,
            @Nullable @RequestParam(required = false, defaultValue = "1.0") double strike
    ) {
        // TODO: how to pass through (check ApplicationContext -> pass via Header)
        var quotationRequestId = UUID.randomUUID();

        // TODO: change bid/ask quotes to ParamTuple
        var quoteResponse = quotationService.getQuote(quotationRequestId, instrument, qty, side,
                minutesAvailable, term, strike);
        var bidQuote = quoteResponse[0];
        var askQuote = quoteResponse[1];
        var currentSpot = quotationService.currentSpot;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return new QuoteResponse(quotationRequestId, timestampFormat.format(timestamp), bidQuote, askQuote,
                currentSpot);
    }

    @GetMapping(value = "/api/pricer/get-available-instruments",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public QuotationEnums.INSTRUMENT[] getAvailableInstruments() {
        return QuotationEnums.INSTRUMENT.values();
    }
}
