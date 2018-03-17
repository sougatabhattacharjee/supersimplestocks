package util;

import com.google.common.collect.Lists;
import com.jpmorgan.domain.Stock;
import com.jpmorgan.domain.StockType;
import com.jpmorgan.util.NumberUtil;

import java.util.List;

/**
 * Created by Sougata Bhattacharjee
 * On 15.03.18
 */
public class TestUtil {

    /**
     * Create random stocks
     * @return list of stocks
     */
    public static List<Stock> createStocks() {
        final List<Stock> stocks = Lists.newArrayList();

        stocks.add(new Stock("TEA", 10, 10, 100, NumberUtil.randDouble(), StockType.COMMON));
        stocks.add(new Stock("POP", 8, 20, 100, NumberUtil.randDouble(), StockType.COMMON));
        stocks.add(new Stock("ALE", 23, 10, 60, NumberUtil.randDouble(), StockType.COMMON));
        stocks.add(new Stock("GIN", 8, 2, 100, NumberUtil.randDouble(), StockType.PREFERRED));
        stocks.add(new Stock("JOE", 13, 30, 250, NumberUtil.randDouble(), StockType.COMMON));

        return stocks;
    }
}
