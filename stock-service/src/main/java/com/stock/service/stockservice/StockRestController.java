package com.stock.service.stockservice;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
@RestController
@RequestMapping("/rest")
//@RequestMapping("/rest")

public class StockRestController {
	
	@Autowired
	RestTemplate restTemplate;

	/*
	 * StockResourceRepo repo;
	 * 
	 * public StockRestController(StockResourceRepo repo) { this.repo = repo;
	 * 
	 * }
	 */
		
    @GetMapping("/{username}")
    public List<Quote> getStock(@PathVariable("username") final String username) {
		
		ResponseEntity<List<String>> quoteResponse = restTemplate.exchange("http://localhost:8082/db-service/rest/api/" + username,
				HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
				});
           System.out.println("URLLLLLLLLLL " + quoteResponse);
		List<String> quotes = quoteResponse.getBody();
		return quotes.stream()
				.map( quote -> {
			Stock stock = getStockPrice(quote);
			return new Quote(quote ,stock.getQuote().getPrice());
		}).collect(Collectors.toList());
	}

	
	private Stock  getStockPrice(String quote) {
        try {
            return YahooFinance.get(quote);
        } catch (IOException e) {
            e.printStackTrace();
            return new Stock(quote);
        }
    }
	
	@GetMapping("/name")
	public String getHello() {
		return "Hello....!";
	}
	
}
