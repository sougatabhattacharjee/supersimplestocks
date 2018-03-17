package apiTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jpmorgan.domain.Stock;
import com.jpmorgan.domain.Trade;
import com.jpmorgan.domain.TradeType;
import com.jpmorgan.util.NumberUtil;
import org.apache.http.HttpStatus;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jpmorgan.controller.StockController.STOCK_URL;
import static com.jpmorgan.controller.TradeController.TRADE_URL;
import static com.jpmorgan.util.Constants.PARAM_SHARE_QUANTITY;
import static com.jpmorgan.util.Constants.PARAM_STOCK_NAME;
import static com.jpmorgan.util.Constants.PARAM_TRADE_PRICE;
import static com.jpmorgan.util.Constants.PARAM_TRADE_TYPE;

/**
 * Created by Sougata Bhattacharjee
 * On 15.03.18
 */
public abstract class AbstractAT {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        RestAssured.port = 8080;
        RestAssured.baseURI = "localhost";
    }

    public List<Stock> saveStocks(final List<Stock> stocks) throws JsonProcessingException {
        return Arrays.asList(
                given().accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(stocks)).log().all()
                        .when()
                        .post("http://localhost:8080" + STOCK_URL)
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().as(Stock[].class));
    }

    public List<Stock> deleteStocks() throws JsonProcessingException {
        return Arrays.asList(
                given().accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .when()
                        .delete("http://localhost:8080" + STOCK_URL)
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().as(Stock[].class));
    }

    public Trade trading(final String stockSymbol, final TradeType tradeType) throws JsonProcessingException {
        return
                given().
                        when().
                        param(PARAM_STOCK_NAME, stockSymbol).
                        param(PARAM_SHARE_QUANTITY, NumberUtil.randInt()).
                        param(PARAM_TRADE_TYPE, tradeType.toString()).
                        param(PARAM_TRADE_PRICE, NumberUtil.randDouble()).
                        post("http://localhost:8080" + TRADE_URL).
                        prettyPeek().
                        then().
                        assertThat().
                        statusCode(HttpStatus.SC_OK).extract().as(Trade.class);
    }

    public void deleteTrades() throws JsonProcessingException {
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .delete("http://localhost:8080" + TRADE_URL)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}
