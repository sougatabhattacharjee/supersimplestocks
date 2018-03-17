package apiTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jpmorgan.domain.TradeType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static com.jpmorgan.controller.SimpleStocksController.SUPER_SIMPLE_STOCKS_URL;
import static com.jpmorgan.util.Constants.PARAM_STOCK_NAME;
import static util.TestUtil.createStocks;

/**
 * Created by Sougata Bhattacharjee
 * On 13.03.18
 */
public class SimpleStockControllerAT extends AbstractAT {

    @Test
    public void testCalculateDividend() throws JsonProcessingException {
        saveStocks(createStocks());

        // calculate DividendYield for known stock
        given().
                when().
                param(PARAM_STOCK_NAME, "TEA").
                get("http://localhost:8080" + SUPER_SIMPLE_STOCKS_URL + "/calculateDividendYield").
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK);

        // calculate DividendYield for unknown stock
        given().
                when().
                param(PARAM_STOCK_NAME, "TEST").
                get("http://localhost:8080" + SUPER_SIMPLE_STOCKS_URL + "/calculateDividendYield").
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testCalculatePERatio() throws JsonProcessingException {
        saveStocks(createStocks());

        // calculate PERatio for known stock
        given().
                when().
                param(PARAM_STOCK_NAME, "TEA").
                get("http://localhost:8080" + SUPER_SIMPLE_STOCKS_URL + "/calculatePERatio").
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK);

        // calculate PERatio for unknown stock
        given().
                when().
                param(PARAM_STOCK_NAME, "TEST").
                get("http://localhost:8080" + SUPER_SIMPLE_STOCKS_URL + "/calculatePERatio").
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testCalculateGBCEAllShareIndex() throws JsonProcessingException {
        saveStocks(createStocks());

        // calculate PERatio for known stock
        given().
                when().
                get("http://localhost:8080" + SUPER_SIMPLE_STOCKS_URL + "/calculateGBCEAllShareIndex").
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK);

        // delete everything
        deleteTrades();
        deleteStocks();

        // calculate GBCEAllShareIndex when there is no stock
        given().
                when().
                get("http://localhost:8080" + SUPER_SIMPLE_STOCKS_URL + "/calculateGBCEAllShareIndex").
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testCalculateWeightedStockPrice() throws JsonProcessingException {
        saveStocks(createStocks());
        trading("TEA", TradeType.BUY);
        trading("POP", TradeType.BUY);

        // calculate PERatio for known stock
        given().
                when().
                param(PARAM_STOCK_NAME, "TEA").
                get("http://localhost:8080" + SUPER_SIMPLE_STOCKS_URL + "/weightedStockPrice").
                prettyPeek().
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK);
    }

}
