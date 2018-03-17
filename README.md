# Super Simple Stocks #

Simple stocks price calculation.

### Requirements ###

Provide working source code that will:
  
  a. For a given stock:

    i.   Given a market price as input, calculate the dividend yield.
    ii.  Given a market price as input, calculate the P/E Ratio.
    iii. Record a trade, with timestamp, quantity of shares, buy or sell indicator and
         trade price.
    iv.  Calculate Volume Weighted Stock Price based on trades in past 15 minutes.

  b. Calculate the GBCE All Share Index using the geometric mean of prices for all stocks.
  

### Sample data from the Global Beverage Corporation Exchange ###

Stock Symbol | Type      | Last Dividend | Fixed Dividend | Par Value
:------------|:----------|--------------:|---------------:|-----------:
TEA          | Common    |  0            |                | 100           
POP          | Common    |  8            |                | 100
ALE          | Common    | 23            |                |  60
GIN          | Preferred |  8            | 2%             | 100
JOE          | Common    | 13            |                | 250

## Solution ##

### Technical Stack ###
To implement this solution, the following libraries / tools were used:
  * Editor/Tool - Intellij IDEA  
  * Java 8 - Programming language   
  * SpringBoot: 
    * Spring Web - Used to provide rest api 
    * Spring Core - Used for Dependency Injection.
    * Spring Data - Used for storing and reading data.
  * Maven 3.5.2 - Used for dependency management and as a build tool.  
  * jUnit - unit test framework.
  * Mockito - Used to mock dependencies when unit testing.
  * RestAssured - Rest API testing framework 
  * H2 - In-memory data storage. 
  * Git - Version control system.
  
### Rest API(s) ###

### /stock ###

```$xslt
POST  /stock -- save list of stocks
```
```$xslt
GET  /stock -- get all stocks
```  
```$xslt
GET  /stock/{symbol} -- get stock by symbol
```  
```$xslt
DELETE  /stock -- delete all stocks
```  

### /trade ###
```$xslt
POST  /trade?stock_symbol=&stock_quantity=&trade_type=&trade_price= -- buy or sell trade
```
```$xslt
GET  /trade -- get all trades
```  
```$xslt
DELETE  /trade -- delete all trades
```  

### /super-simple-stocks ###
```$xslt
GET  /super-simple-stocks/calculateDividendYield?stock_symbol= -- dividend for a stock
```  
```$xslt
GET  /super-simple-stocks/calculatePERatio?stock_symbol= -- PE Ratio for a stock
```  
```$xslt
GET  /super-simple-stocks/weightedStockPrice?stock_symbol= -- weighted Stock Price for a stock
```  
```$xslt
GET  /super-simple-stocks/calculateGBCEAllShareIndex -- GBCEAll Share Index
```  
  
### Tests ###

The following tests have been implemented:
* Unit Tests - 
    * SimpleStockServiceTest
        * testCalculateDividendYieldWithCommonStock
        * testCalculateDividendYieldWithPreferredStock
        * testCalculateDividendYieldWithoutMarketPrice
        * testCalculateDividendYieldWithNonExistingStock
        * testCalculatePEStock
        * testCalculateGBCEAllShareIndex
        * testGetWeightedStockPriceWithTradesInTheLast15Minutes
        * testGetWeightedStockPriceWithTradesOlderThanLast15Minutes
    * TradeServiceTest
        * testRecordTrade
        * testTradingForUnknownStock
        * testTradingWithoutQuantity
        * testTradingWithoutTradingPrice
        * testFindAllRecordTrades
    * StockServiceTest
        * testSaveStocks
        * testSaveStocksWithoutPrice
        * testGetStocksBySymbol


* API Tests - 
    * Stock API - 
        * testSaveStocks - to save one or more stocks
        * testGetStocks - to retrieve one or more stocks
        * testGetStocksBySymbol - to get a stock by symbol
        * testGetStocksByWrongSymbol - to get a stock if wrong symbol provided
        * testDeleteAllStocks - to delete all the stocks
    * Trade API - 
        * testTrading - either buy or sale a trade (record a trade)
        * testGetTrades - retrieve trades
        * testDeleteAllTrades - delete all trades
    * SimpleStock API - 
        * testCalculateDividend - calculate dividend for a stock
        * testCalculatePERatio - calculate PE ratio for a stock
        * testCalculateGBCEAllShareIndex - calculate GBCE all share index
        * testCalculateWeightedStockPrice - calculate weighted stock price for a stock
          

#### How to run ####
Before running the project, JDK 1.8 and Maven 3.x.x have to be installed in the system. 
```
> mvn clean package 
> mvn spring-boot:run
```  

### Usage ###
After running the project, sample stock data has to be stored
```$xslt
curl -XPOST  http://localhost:8080/stock -H"Content-type: application/json" -d '[
{
"symbol": "TEA",
"lastDividend": 0,
"fixedDividend": 0,
"parValue":100,
"marketPrice": 30,
"type": "COMMON"
},
{
"symbol": "POP",
"lastDividend": 8,
"fixedDividend": 0,
"parValue":100,
"marketPrice": 40,
"type": "COMMON"
},
{
"symbol": "ALE",
"lastDividend": 23,
"fixedDividend": 0,
"parValue":60,
"marketPrice": 20,
"type": "COMMON"
},
{
"symbol": "GIN",
"lastDividend": 8,
"fixedDividend": 2,
"parValue":100,
"marketPrice": 50,
"type": "PREFERRED"
},
{
"symbol": "JOE",
"lastDividend": 13,
"fixedDividend": 0,
"parValue":250,
"marketPrice": 30,
"type": "COMMON"
}
]'
```

* Calculate Dividend for stock 'POP'
```$xslt
http://localhost:8080/super-simple-stocks/calculateDividendYield?stock_symbol=POP
``` 

* Calculate PE ratio for stock 'POP'
```$xslt
http://localhost:8080/super-simple-stocks/calculatePERatio?stock_symbol=POP
``` 

* Buy a trade
```$xslt
http://localhost:8080/trade?stock_symbol=POP&stock_quantity=10&trade_type=BUY&trade_price=30
```

* Sell a trade
```$xslt
http://localhost:8080/trade?stock_symbol=POP&stock_quantity=10&trade_type=SELL&trade_price=30
``` 

* Calculate weighted stock price for stock 'POP'
```$xslt
http://localhost:8080/super-simple-stocks/weightedStockPrice?stock_symbol=POP
``` 

* Calculate GBCEAllShareIndex
```$xslt
http://localhost:8080/super-simple-stocks/calculateGBCEAllShareIndex
``` 

