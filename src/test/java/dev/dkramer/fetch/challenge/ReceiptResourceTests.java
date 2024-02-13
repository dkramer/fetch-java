package dev.dkramer.fetch.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.dkramer.fetch.challenge.api.ReceiptResource;
import dev.dkramer.fetch.challenge.api.models.GetReceiptPointsResponse;
import dev.dkramer.fetch.challenge.api.models.ProcessReceiptRequest;
import dev.dkramer.fetch.challenge.api.models.ProcessReceiptResponse;
import dev.dkramer.fetch.challenge.util.LocalDateAdapter;
import dev.dkramer.fetch.challenge.util.LocalTimeAdapter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;


@SpringBootTest
class ReceiptResourceTests {

	@Autowired
	private ReceiptResource receiptResource;


	Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
			.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
			.create();




	@SneakyThrows
	@Test
	void firstExample() {
		String json = "{\n" +
				"  \"retailer\": \"Target\",\n" +
				"  \"purchaseDate\": \"2022-01-01\",\n" +
				"  \"purchaseTime\": \"13:01\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"shortDescription\": \"Mountain Dew 12PK\",\n" +
				"      \"price\": \"6.49\"\n" +
				"    },{\n" +
				"      \"shortDescription\": \"Emils Cheese Pizza\",\n" +
				"      \"price\": \"12.25\"\n" +
				"    },{\n" +
				"      \"shortDescription\": \"Knorr Creamy Chicken\",\n" +
				"      \"price\": \"1.26\"\n" +
				"    },{\n" +
				"      \"shortDescription\": \"Doritos Nacho Cheese\",\n" +
				"      \"price\": \"3.35\"\n" +
				"    },{\n" +
				"      \"shortDescription\": \"   Klarbrunn 12-PK 12 FL OZ  \",\n" +
				"      \"price\": \"12.00\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"total\": \"35.35\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 28;
	}

	@SneakyThrows
	@Test
	void secondExample() {
		String json = "{\n" +
				"  \"retailer\": \"M&M Corner Market\",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"14:33\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"shortDescription\": \"Gatorade\",\n" +
				"      \"price\": \"2.25\"\n" +
				"    },{\n" +
				"      \"shortDescription\": \"Gatorade\",\n" +
				"      \"price\": \"2.25\"\n" +
				"    },{\n" +
				"      \"shortDescription\": \"Gatorade\",\n" +
				"      \"price\": \"2.25\"\n" +
				"    },{\n" +
				"      \"shortDescription\": \"Gatorade\",\n" +
				"      \"price\": \"2.25\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"total\": \"9.00\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 109;
	}

	@SneakyThrows
	@Test
	void testRetailer() {
		String json = "{\n" +
				"  \"retailer\": \"M,3M  M.  \",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"12:01\",\n" +
				"  \"items\": [\n" +
				"  ],\n" +
				"  \"total\": \".1\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 4;
	}

	@SneakyThrows
	@Test
	void testDate() {
		String json = "{\n" +
				"  \"retailer\": \",\",\n" +
				"  \"purchaseDate\": \"2022-03-21\",\n" +
				"  \"purchaseTime\": \"12:01\",\n" +
				"  \"items\": [\n" +
				"  ],\n" +
				"  \"total\": \".1\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 6;
	}

	@SneakyThrows
	@Test
	void testTime() {
		String json = "{\n" +
				"  \"retailer\": \",\",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"14:01\",\n" +
				"  \"items\": [\n" +
				"  ],\n" +
				"  \"total\": \".1\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 10;
	}

	@SneakyThrows
	@Test
	void testTotalDivisibleBy25() {
		String json = "{\n" +
				"  \"retailer\": \",\",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"12:01\",\n" +
				"  \"items\": [\n" +
				"  ],\n" +
				"  \"total\": \".25\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 25;
	}

	@SneakyThrows
	@Test
	void testTotalIsInteger() {
		String json = "{\n" +
				"  \"retailer\": \",\",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"12:01\",\n" +
				"  \"items\": [\n" +
				"  ],\n" +
				"  \"total\": \"1\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 75;
	}

	@SneakyThrows
	@Test
	void testEvenItems() {
		String json = "{\n" +
				"  \"retailer\": \",\",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"12:01\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"shortDescription\": \"pK\",\n" +
				"      \"price\": \"6.49\"\n" +
				"    },{\n" +
				"      \"shortDescription\": \"pi\",\n" +
				"      \"price\": \"12.25\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"total\": \".1\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 5;
	}

	@SneakyThrows
	@Test
	void testItemDescriptionAndPrice() {
		String json = "{\n" +
				"  \"retailer\": \",\",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"12:01\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"shortDescription\": \"123   \",\n" +
				"      \"price\": \"20\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"total\": \".1\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 4;
	}

	@SneakyThrows
	@Test
	void testItemDescriptionAndPriceMultiple() {
		String json = "{\n" +
				"  \"retailer\": \",\",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"12:01\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"shortDescription\": \"123   \",\n" +
				"      \"price\": \"20\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"shortDescription\": \"123   \",\n" +
				"      \"price\": \"30\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"total\": \".1\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 15;
	}

	@SneakyThrows
	@Test
	void testItemDescriptionAndPriceRoundingUp() {
		String json = "{\n" +
				"  \"retailer\": \",\",\n" +
				"  \"purchaseDate\": \"2022-03-20\",\n" +
				"  \"purchaseTime\": \"12:01\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"shortDescription\": \"123   \",\n" +
				"      \"price\": \"11\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"total\": \".1\"\n" +
				"}";
		ProcessReceiptRequest processReceiptRequest = gson.fromJson(json, ProcessReceiptRequest.class);

		ProcessReceiptResponse processReceiptResponse = receiptResource.processReceipt(processReceiptRequest);
		GetReceiptPointsResponse getReceiptPointsResponse = receiptResource.getReceiptPoints(processReceiptResponse.getId());


		assert getReceiptPointsResponse.getPoints() == 3;
	}

}
