package apiTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jpmorgan.domain.Stock;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jpmorgan.controller.StockController.STOCK_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static util.TestUtil.createStocks;

/**
 * Created by Sougata Bhattacharjee
 * On 13.03.18
 */
public class StockControllerAT extends AbstractAT {

    @Test
    public void testSaveStocks() throws JsonProcessingException {
        final List<Stock> expectedStocks = createStocks();
        final List<Stock> actualStocks = saveStocks(expectedStocks);

        assertNotNull(actualStocks);
        assertThat(actualStocks.size(), equalTo(expectedStocks.size()));
        assertThat("Both List contains similar items",
                actualStocks, containsInAnyOrder(expectedStocks.toArray()));
    }

    @Test
    public void testGetStocks() throws JsonProcessingException {
        final List<Stock> expectedStocks = createStocks();
        saveStocks(expectedStocks);

        final List<Stock> actualStocks = Arrays.asList(given().
                when().
                get("http://localhost:8080" + STOCK_URL).
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                extract().as(Stock[].class));


        assertNotNull(actualStocks);
        assertThat(actualStocks.size(), equalTo(expectedStocks.size()));
        assertThat("Both List contains similar items",
                actualStocks, containsInAnyOrder(expectedStocks.toArray()));
    }

    @Test
    public void testGetStocksBySymbol() throws JsonProcessingException {
        final List<Stock> expectedStocks = createStocks();
        saveStocks(expectedStocks);

        final String stockSymbol = "GIN";

        final Stock actualStock = given().
                when().
                get("http://localhost:8080" + STOCK_URL + "/" + stockSymbol).
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                extract().as(Stock.class);

        assertNotNull(actualStock);
        assertThat(actualStock.getSymbol(), equalTo(stockSymbol));
    }

    @Test
    public void testGetStocksByWrongSymbol() throws JsonProcessingException {
        final List<Stock> expectedStocks = createStocks();
        saveStocks(expectedStocks);

        final String stockSymbol = "TEST";

        given().
                when().
                get("http://localhost:8080" + STOCK_URL + "/" + stockSymbol).
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testDeleteAllStocks() throws JsonProcessingException {
        final List<Stock> expectedStocks = createStocks();
        saveStocks(expectedStocks);

        final List<Stock> actualStocks = Arrays.asList(
                given().
                        when().
                        delete("http://localhost:8080" + STOCK_URL).
                        prettyPeek().
                        then().
                        assertThat().
                        statusCode(HttpStatus.SC_OK).
                        extract().as(Stock[].class));

        assertTrue(actualStocks.isEmpty());
    }

}
