package apiTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jpmorgan.domain.Trade;
import com.jpmorgan.domain.TradeType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.given;
import static com.jpmorgan.controller.TradeController.TRADE_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static util.TestUtil.createStocks;

/**
 * Created by Sougata Bhattacharjee
 * On 13.03.18
 */
public class TradeControllerAT extends AbstractAT {

    @Test
    public void testTrading() throws JsonProcessingException {
        saveStocks(createStocks());

        assertEquals(trading("ALE", TradeType.SELL).
                getStock().getSymbol(), "ALE");
    }

    @Test
    public void testGetTrades() throws JsonProcessingException {
        saveStocks(createStocks());

        trading("TEA", TradeType.BUY);
        trading("POP", TradeType.BUY);

        final List<Trade> actualTrades = Arrays.asList(given().
                when().
                get("http://localhost:8080" + TRADE_URL).
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                extract().as(Trade[].class));

        assertFalse(actualTrades.isEmpty());
        assertTrue(actualTrades.size() == 2);

        final List<String> actualStocks =
                actualTrades.stream()
                        .map(trade -> trade.getStock().getSymbol())
                        .collect(Collectors.toList());

        assertThat("Both List contains similar items",
                actualStocks, containsInAnyOrder("TEA", "POP"));

    }

    @Test
    public void testDeleteAllTrades() throws JsonProcessingException {
        saveStocks(createStocks());

        final List<Trade> actualTrades = Arrays.asList(
                given().
                        when().
                        delete("http://localhost:8080" + TRADE_URL).
                        prettyPeek().
                        then().
                        assertThat().
                        statusCode(HttpStatus.SC_OK).
                        extract().as(Trade[].class));

        assertTrue(actualTrades.isEmpty());
    }

}
