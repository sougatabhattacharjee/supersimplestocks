package com.jpmorgan.service.stock;

import com.jpmorgan.domain.Stock;
import com.jpmorgan.domain.StockType;
import com.jpmorgan.repository.StockRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;
import util.TestUtil;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sougata Bhattacharjee
 * On 17.03.18
 */
public class StockServiceTest {

    private StockRepository stockRepositoryMock;
    private StockService stockService;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        stockRepositoryMock = mock(StockRepository.class);

        stockService = new StockServiceImpl();
        ReflectionTestUtils.setField(stockService, "stockRepository", stockRepositoryMock);
    }

    @Test
    public void testSaveStocks() {
        final List<Stock> expectedStocks = TestUtil.createStocks();

        expectedStocks.forEach(stock -> {
            when(stockRepositoryMock.save(stock)).thenReturn(stock);
        });

        // when
        final List<Stock> actualStocks = stockService.saveStocks(expectedStocks);

        assertEquals(expectedStocks.size(), actualStocks.size());
    }

    @Test
    public void testSaveStocksWithoutPrice() {
        final Stock tea = new Stock("TEA", 10, 10, 100, 0, StockType.COMMON);

        when(stockRepositoryMock.save(tea)).thenReturn(tea);

        // when
        final Stock actualStocks = stockService.saveStock(tea);

        // before saving, each stock will be validated and set the market price to a random value if the price is zero
        assertThat(actualStocks.getMarketPrice(), greaterThan(0d));
    }

    @Test
    public void testGetStocksBySymbol() {
        final Stock tea = new Stock("TEA", 10, 10, 100, 10, StockType.COMMON);
        final Stock pop = new Stock("POP", 10, 10, 100, 10, StockType.COMMON);

        when(stockRepositoryMock.save(tea)).thenReturn(tea);
        when(stockRepositoryMock.save(pop)).thenReturn(pop);
        when(stockRepositoryMock.findBySymbol(pop.getSymbol())).thenReturn(Optional.of(pop));
        when(stockRepositoryMock.findBySymbol(tea.getSymbol())).thenReturn(Optional.of(tea));

        // when
        final Stock actualStocks = stockService.findStockBySymbol(tea.getSymbol());

        // then
        assertThat(actualStocks, equalTo(tea));
    }

}
