package com.jpmorgan.service.simpleStock;

import com.google.common.collect.Lists;
import com.jpmorgan.domain.Stock;
import com.jpmorgan.domain.StockType;
import com.jpmorgan.domain.Trade;
import com.jpmorgan.domain.TradeType;
import com.jpmorgan.exception.PriceNotFoundException;
import com.jpmorgan.exception.StockNotFoundException;
import com.jpmorgan.exception.TradeNotFoundException;
import com.jpmorgan.service.stock.StockService;
import com.jpmorgan.service.stock.StockServiceImpl;
import com.jpmorgan.service.trade.TradeService;
import com.jpmorgan.service.trade.TradeServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.jpmorgan.exception.StockNotFoundException.MSG_STOCK_NOT_FOUND;
import static com.jpmorgan.util.NumberUtil.roundOffTo2DecPlaces;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sougata Bhattacharjee
 * On 14.03.18
 */
public class SimpleStockServiceTest {

    private StockService stockServiceMock;
    private TradeService tradeServiceMock;
    private SimpleStockService simpleStockService;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        stockServiceMock = mock(StockServiceImpl.class);
        tradeServiceMock = mock(TradeServiceImpl.class);

        simpleStockService = new SimpleStockServiceImpl();
        ReflectionTestUtils.setField(simpleStockService, "stockService", stockServiceMock);
        ReflectionTestUtils.setField(simpleStockService, "tradeService", tradeServiceMock);
    }

    @Test
    public void testCalculateDividendYieldWithCommonStock() {
        final double lastDividend = 10, marketPrice = 45;
        final Stock tea = new Stock("TEA", lastDividend, 0, 0, marketPrice, StockType.COMMON);

        when(stockServiceMock.findStockBySymbol("TEA")).thenReturn(tea);

        assertThat(simpleStockService.calculateDividendYield("TEA"),
                equalTo(roundOffTo2DecPlaces(lastDividend / marketPrice)));
    }

    @Test
    public void testCalculateDividendYieldWithPreferredStock() {
        final double fixedDividend = 10, parValue = 100, marketPrice = 50;
        final Stock gin = new Stock("GIN", 0, fixedDividend, parValue, marketPrice, StockType.PREFERRED);

        when(stockServiceMock.findStockBySymbol("GIN")).thenReturn(gin);

        assertThat(simpleStockService.calculateDividendYield("GIN"),
                equalTo(roundOffTo2DecPlaces((fixedDividend * parValue) / marketPrice)));
    }

    @Test(expected = PriceNotFoundException.class)
    public void testCalculateDividendYieldWithoutMarketPrice() {
        final double lastDividend = 10, marketPrice = 0;
        final Stock tea = new Stock("TEA", lastDividend, 0, 0, marketPrice, StockType.COMMON);

        when(stockServiceMock.findStockBySymbol("TEA")).thenReturn(tea);

        // when
        simpleStockService.calculateDividendYield("TEA");

        // then
        thrown.expect(PriceNotFoundException.class);
        thrown.expectMessage("Price cannot be less than or equal to zero");
    }

    @Test(expected = StockNotFoundException.class)
    public void testCalculateDividendYieldWithNonExistingStock() {

        when(stockServiceMock.findStockBySymbol("TEST"))
                .thenThrow(new StockNotFoundException(MSG_STOCK_NOT_FOUND));

        // when
        simpleStockService.calculateDividendYield("TEST");

        // then
        thrown.expect(StockNotFoundException.class);
        thrown.expectMessage(MSG_STOCK_NOT_FOUND);
    }

    @Test
    public void testCalculatePEStock() {
        final double lastDividend = 10, fixedDividend = 10, parValue = 100, marketPrice = 50;

        final List<Stock> stocks =
                Lists.newArrayList(new Stock("TEA", lastDividend, 0, 0, marketPrice, StockType.COMMON),
                        new Stock("GIN", 0, fixedDividend, parValue, marketPrice, StockType.PREFERRED));

        stocks.forEach(
                stock -> {
                    when(stockServiceMock.findStockBySymbol(stock.getSymbol())).thenReturn(stock);
                    double dividendYield = simpleStockService.calculateDividendYield(stock.getSymbol());
                    assertTrue(dividendYield > 0);
                    assertThat(simpleStockService.calculatePERatio(stock.getSymbol()),
                            equalTo(roundOffTo2DecPlaces(marketPrice / dividendYield)));
                }
        );
    }

    @Test
    public void testCalculateGBCEAllShareIndex() {

        final Stock stock1 = new Stock("TEA", 10, 10, 100, 10, StockType.COMMON);
        final Stock stock2 = new Stock("POP", 10, 10, 100, 20, StockType.COMMON);
        final Stock stock3 = new Stock("GIN", 10, 10, 100, 5, StockType.PREFERRED);

        when(stockServiceMock.findAllStocks()).thenReturn(Lists.newArrayList(stock1, stock2, stock3));

        // stock price of TEA = 10, stock price of POP = 20, stock price of GIN = 5
        // GBCE = cubeRootOf(10 * 20 * 5) = cubeRootOf(1000) = 10.0
        assertThat(10.0, equalTo(simpleStockService.calculateGBCEAllShareIndex()));
    }

    @Test
    public void testGetWeightedStockPriceWithTradesInTheLast15Minutes() throws Exception {
        final Stock stock = new Stock("TEA", 10, 10, 100, 10, StockType.COMMON);

        final Trade trade1 = new Trade(new Date(), 15, TradeType.BUY, stock, 12d);
        final Trade trade2 = new Trade(new Date(), 15, TradeType.BUY, stock, 13d);
        final Trade trade3 = new Trade(new Date(), 10, TradeType.SELL, stock, 14d);

        when(stockServiceMock.findStockBySymbol("TEA")).thenReturn(stock);
        when(tradeServiceMock.findTradesByStockSymbol("TEA")).thenReturn(Lists.newArrayList(trade1, trade2, trade3));

        final double expectedWeightedStockPrice = roundOffTo2DecPlaces((double) (12 * 15 + 13 * 15 + 14 * 10) / 40);

        assertThat(expectedWeightedStockPrice,
                equalTo(simpleStockService.getWeightedStockPrice("TEA")));
    }

    @Test(expected = TradeNotFoundException.class)
    public void testGetWeightedStockPriceWithTradesOlderThanLast15Minutes() throws Exception {
        final Stock stock = new Stock("TEA", 10, 10, 100, 10, StockType.COMMON);

        final Date timeOlderThan15Minutes = new Date(Calendar.getInstance().getTimeInMillis() - 1000000);
        final Trade trade1 = new Trade(timeOlderThan15Minutes, 15, TradeType.BUY, stock, 12d);
        final Trade trade2 = new Trade(timeOlderThan15Minutes, 15, TradeType.BUY, stock, 13d);

        when(stockServiceMock.findStockBySymbol("TEA")).thenReturn(stock);
        when(tradeServiceMock.findTradesByStockSymbol("TEA")).thenReturn(Lists.newArrayList(trade1, trade2));

        // when
        simpleStockService.getWeightedStockPrice("TEA");

        // then
        thrown.expect(StockNotFoundException.class);
        thrown.expectMessage(String.format("Trade record not found with the stock %s", stock.getSymbol()));
    }

}
