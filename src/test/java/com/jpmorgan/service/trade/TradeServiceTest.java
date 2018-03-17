package com.jpmorgan.service.trade;

import com.google.common.collect.Lists;
import com.jpmorgan.domain.Stock;
import com.jpmorgan.domain.StockType;
import com.jpmorgan.domain.Trade;
import com.jpmorgan.domain.TradeType;
import com.jpmorgan.exception.StockNotFoundException;
import com.jpmorgan.exception.TradingException;
import com.jpmorgan.repository.TradeRepository;
import com.jpmorgan.service.stock.StockService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.jpmorgan.exception.StockNotFoundException.MSG_STOCK_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sougata Bhattacharjee
 * On 17.03.18
 */
public class TradeServiceTest {

    private StockService stockServiceMock;
    private TradeRepository tradeRepositoryMock;
    private TradeService tradeService;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        stockServiceMock = mock(StockService.class);
        tradeRepositoryMock = mock(TradeRepository.class);

        tradeService = new TradeServiceImpl();
        ReflectionTestUtils.setField(tradeService, "stockService", stockServiceMock);
        ReflectionTestUtils.setField(tradeService, "tradeRepository", tradeRepositoryMock);
    }

    @Test
    public void testRecordTrade() {
        final String stockSymbol = "TEA";
        final Stock stock = new Stock(stockSymbol, 10, 10, 100, 12d, StockType.COMMON);

        final Trade trade1 = new Trade(new Date(), 15, TradeType.BUY, stock, 12d);
        final Trade trade2 = new Trade(new Date(), 15, TradeType.BUY, stock, 13d);
        final Trade trade3 = new Trade(new Date(), 10, TradeType.SELL, stock, 14d);
        final List<Trade> expectedTrades = Lists.newArrayList(trade1, trade2, trade3);

        when(stockServiceMock.findStockBySymbol(stockSymbol)).thenReturn(stock);
        when(tradeRepositoryMock.save(trade1)).thenReturn(trade1);
        when(tradeRepositoryMock.save(trade2)).thenReturn(trade2);
        when(tradeRepositoryMock.save(trade3)).thenReturn(trade3);
        when(tradeRepositoryMock.findByStockSymbol(stockSymbol)).thenReturn(expectedTrades);

        tradeService.recordTrade(stockSymbol, trade1.getStockQuantity(), trade1.getTradeType(), trade1.getTradePrice());
        tradeService.recordTrade(stockSymbol, trade2.getStockQuantity(), trade2.getTradeType(), trade2.getTradePrice());
        tradeService.recordTrade(stockSymbol, trade3.getStockQuantity(), trade3.getTradeType(), trade3.getTradePrice());

        final List<Trade> actualTrades = tradeService.findTradesByStockSymbol(stockSymbol);

        assertEquals(expectedTrades.size(), actualTrades.size());

        actualTrades.forEach(trade -> {
            assertEquals(stock, trade.getStock());
        });

        // stock price updated according to the last trade price 14d
        assertThat(stockServiceMock.findStockBySymbol(stockSymbol).getMarketPrice(), equalTo(14d));
    }

    @Test(expected = StockNotFoundException.class)
    public void testTradingForUnknownStock() {

        final Stock stock1 = new Stock("TEA", 10, 10, 100, 12d, StockType.COMMON);
        final Stock stock2 = new Stock("POP", 10, 10, 100, 12d, StockType.COMMON);

        final Trade trade = new Trade(new Date(), 15, TradeType.BUY, stock1, 12d);

        when(stockServiceMock.findStockBySymbol("POP"))
                .thenReturn(stock2);

        when(stockServiceMock.findStockBySymbol("TEA"))
                .thenThrow(new StockNotFoundException(MSG_STOCK_NOT_FOUND));

        // when
        tradeService.recordTrade("TEA", trade.getStockQuantity(), trade.getTradeType(), trade.getTradePrice());

        // then
        thrown.expect(StockNotFoundException.class);
        thrown.expectMessage(MSG_STOCK_NOT_FOUND);
    }

    @Test(expected = TradingException.class)
    public void testTradingWithoutQuantity() {

        final Stock stock = new Stock("TEA", 10, 10, 100, 12d, StockType.COMMON);

        final Trade trade = new Trade(new Date(), 0, TradeType.BUY, stock, 12d);

        when(stockServiceMock.findStockBySymbol("TEA"))
                .thenReturn(stock);

        // when
        tradeService.recordTrade("TEA", trade.getStockQuantity(), trade.getTradeType(), trade.getTradePrice());

        // then
        thrown.expect(TradingException.class);
        thrown.expectMessage("Shares quantity should be greater than zero while trading");
    }

    @Test(expected = TradingException.class)
    public void testTradingWithoutTradingPrice() {

        final Stock stock = new Stock("TEA", 10, 10, 100, 12d, StockType.COMMON);

        final Trade trade = new Trade(new Date(), 0, TradeType.BUY, stock, 0);

        when(stockServiceMock.findStockBySymbol("TEA"))
                .thenReturn(stock);

        // when
        tradeService.recordTrade("TEA", trade.getStockQuantity(), trade.getTradeType(), trade.getTradePrice());

        // then
        thrown.expect(TradingException.class);
        thrown.expectMessage("Shares price should be greater than zero while trading");
    }

    @Test
    public void testFindAllRecordTrades() {
        final Stock stock1 = new Stock("TEA", 10, 10, 100, 12d, StockType.COMMON);
        final Stock stock2 = new Stock("POP", 10, 10, 100, 12d, StockType.COMMON);


        final Trade trade1 = new Trade(new Date(), 15, TradeType.BUY, stock1, 12d);
        final Trade trade2 = new Trade(new Date(), 15, TradeType.BUY, stock1, 13d);
        final Trade trade3 = new Trade(new Date(), 10, TradeType.SELL, stock1, 14d);
        final Trade trade4 = new Trade(new Date(), 10, TradeType.SELL, stock2, 14d);
        final Trade trade5 = new Trade(new Date(), 10, TradeType.SELL, stock2, 14d);
        final List<Trade> tradesWithStock1 = Lists.newArrayList(trade1, trade2, trade3);
        final List<Trade> tradesWithStock2 = Lists.newArrayList(trade4, trade5);

        when(stockServiceMock.findStockBySymbol("TEA")).thenReturn(stock1);
        when(stockServiceMock.findStockBySymbol("POP")).thenReturn(stock2);
        when(tradeRepositoryMock.save(trade1)).thenReturn(trade1);
        when(tradeRepositoryMock.save(trade2)).thenReturn(trade2);
        when(tradeRepositoryMock.save(trade3)).thenReturn(trade3);
        when(tradeRepositoryMock.save(trade4)).thenReturn(trade4);
        when(tradeRepositoryMock.save(trade5)).thenReturn(trade5);
        when(tradeRepositoryMock.findByStockSymbol("TEA")).thenReturn(tradesWithStock1);
        when(tradeRepositoryMock.findByStockSymbol("POP")).thenReturn(tradesWithStock2);
        final Iterable<Trade> iterable = Lists.newArrayList(trade1, trade2, trade3, trade4, trade5);
        when(tradeRepositoryMock.findAll()).thenReturn(iterable);

        tradeService.recordTrade(stock1.getSymbol(), trade1.getStockQuantity(), trade1.getTradeType(), trade1.getTradePrice());
        tradeService.recordTrade(stock1.getSymbol(), trade2.getStockQuantity(), trade2.getTradeType(), trade2.getTradePrice());
        tradeService.recordTrade(stock1.getSymbol(), trade3.getStockQuantity(), trade3.getTradeType(), trade3.getTradePrice());
        tradeService.recordTrade(stock2.getSymbol(), trade4.getStockQuantity(), trade4.getTradeType(), trade4.getTradePrice());
        tradeService.recordTrade(stock2.getSymbol(), trade5.getStockQuantity(), trade5.getTradeType(), trade5.getTradePrice());

        final List<Trade> actualTrades = tradeService.findAllTrades();

        assertEquals(tradesWithStock1.size() + tradesWithStock2.size(), actualTrades.size());

        assertEquals(tradesWithStock1.size(),
                actualTrades.stream().filter(trade -> trade.getStock().equals(stock1)).collect(Collectors.toList()).size());

        assertEquals(tradesWithStock2.size(),
                actualTrades.stream().filter(trade -> trade.getStock().equals(stock2)).collect(Collectors.toList()).size());
    }

}
