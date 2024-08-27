package com.sephora.services.productcatalog.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.RecordTooLargeException;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.TopologyException;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sephora.services.productcatalog.mapper.ProductMapper;
import com.sephora.services.productcatalog.mapper.SkuMapper;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Service
public class ProductcatalogService {

   @Autowired
   private ObjectMapper objectMapper;

   private final Properties props;
   private final Logger log = LoggerFactory.getLogger(this.getClass());
   private final StreamsBuilder builder = new StreamsBuilder();
   private KafkaStreams streams;
   private String dataType = "";

   KTable<String, String> productTable;
   // KTable<String, String> productRatingTable;
   KTable<String, String> aggregateTable;
   KStream<String, String> skuStream;
   // KTable<String, String> filteredCatalogTable;

   public ProductcatalogService(@Value("${kafka.bootstrap}") String bootstrapServer,
         @Value("${kafka.saslJaasConfig}") String kafkaAuth,
         @Value("${kafka.groupId}") String groupId, @Value("${kafka.topicCatalog}") String topicCatalog,
         @Value("${kafka.topicPrice}") String topicPrice,
         @Value("${kafka.topicBV}") String topicBV, @Value("${kafka.url}") String url,
         @Value("${kafka.targetTopic}") String targetTopic, @Value("${kafka.maxReqSize}") String maxReqSize) {

      // Configure the Kafka Streams application properties
      props = new Properties();
      props.put("application.id", groupId);
      props.put("bootstrap.servers", bootstrapServer);
      props.put("default.key.serde", "org.apache.kafka.common.serialization.Serdes$StringSerde");
      props.put("default.value.serde", "org.apache.kafka.common.serialization.Serdes$StringSerde");
      props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
      props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
      props.put("sasl.jaas.config", kafkaAuth);
      props.put("security.protocol", "SASL_SSL");
      props.put("sasl.mechanism", "PLAIN");
      // Set max.request.size for the producer
      props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxReqSize);
      // props.put("compression.type", "lz4");
      props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");

      try {

         objectMapper = new ObjectMapper();
         System.out.println("step 1");
         processMessage(topicCatalog, topicPrice, topicBV, url, targetTopic, groupId);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void processMessage(String topicCatalog, String topicPrice, String topicBV, String url, String targetTopic,
         String groupId) {

      try {

         KStream<String, String> inStream = builder.stream(topicCatalog);
         // KStream<String, String> bvStream = builder.stream(topicBV);
         // KStream<String, String> priceStream = builder.stream(topicPrice);

         // Split the stream based on the type: product or sku
         KStream<String, String>[] inStreamBranches = inStream.branch((key, value) -> isProduct(value),
               (key, value) -> isSku(value));

         // Process the "product" stream
         KStream<String, String> productStream = inStreamBranches[0];
         KStream<String, String> skuStream = inStreamBranches[1];

         // KStream<String, String>[] branches = productStream.branch(
         // (key, value) -> isEnglishProduct(value), // Filter for French products
         // (key, value) -> isFrenchProduct(value) // Default branch for non-French
         // products
         // );

         // KStream<String, String> englishProductStream = branches[0];

         KTable<String, String> productTable = productStream
               .selectKey((key, value) -> extractProductIdFromProduct(value))
               .groupByKey()
               .reduce((oldValue, newValue) -> newValue)
               .mapValues((key, value) -> convertProductToTargetFormat(value, url), Materialized.as("product-store"));
         // .filter((key, value) -> !value.isEmpty());

         productStream.peek((key, value) -> System.out.println("Product Stream - Key: " + key));
         // KStream<String, String> skuStream = inStream.filter(skuFilter)
         // .selectKey((key, value) -> extractSkuIdFromSku(value))
         // .mapValues((key, value) -> convertSkuToTargetFormat(value));
         // skuStream.toTable(Materialized.as("skuTable"));

         // filteredProductTable = productTable.filter((key, value) -> value != null &&
         // !value.isEmpty());

         // WORKING
         KTable<String, String> skuTable = skuStream
               .selectKey((key, value) -> extractSkuIdFromSku(value))
               .groupByKey()
               .reduce((oldValue, newValue) -> newValue)
               .mapValues((key, value) -> convertSkuToTargetFormat(value, url),
                     Materialized.as("sku-store"));

         productTable.toStream().peek((key, value) -> System.out.println("Product Table Key: " + key));
         skuTable.toStream().foreach((key, value) -> System.out.println("SkuTable Key: " + key));

         // Step 2: Aggregate the reduced SKU values into arrays for each product
         KTable<String, String> aggregatedSkuTable = skuTable
               .groupBy((key, value) -> {
                  String productId = extractProductIdFromSku(value);
                  return KeyValue.pair(productId, value);
               }) // Group by product ID
               .aggregate(
                     () -> "[]", // Initial value is an empty array
                     (productId, reducedSkuValue, aggregate) -> addToJsonArrayUpdated(reducedSkuValue, aggregate),
                     (productId, oldAggregate, aggregate) -> aggregate,
                     Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("aggregated-sku-store")
                           .withKeySerde(Serdes.String())
                           .withValueSerde(Serdes.String()));

         // Print the aggregatedSkuTable to an output topic
         aggregatedSkuTable.toStream().peek((key, value) -> System.out.println("AggregatedSkuTable Table Key: " + key));

         // Join the aggregated SKU table with the product table based on productid
         KTable<String, String> catalogTable = productTable.leftJoin(aggregatedSkuTable,
               (productData, skuData) -> joinProdSkuValues(productData, skuData, url),
               Materialized.<String, String, org.apache.kafka.streams.state.KeyValueStore<org.apache.kafka.common.utils.Bytes, byte[]>>as(
                     "catalog-store") // Use a named store
                     .withKeySerde(Serdes.String())
                     .withValueSerde(Serdes.String()));

         catalogTable.toStream().peek((key, value) -> System.out.println("CatalogTable Key: " + key));
         // Filter the catalogTable to remove empty values

         catalogTable
               .toStream()
               .filter((key, value) -> value != null && !value.isEmpty())
               .to(targetTopic, Produced.with(Serdes.String(), Serdes.String()));
         // catalogTable.toStream().to(targetTopic);

         System.out.println("Joined stream written to table");

         // BV Data processing
         /*
          * bvStream
          * .selectKey((key, value) -> extractKeyFromMessage(value)) // Extract key
          * .leftJoin(catalogTable, (bvValue, catTableValue) -> {
          * if (catTableValue != null) {
          * String updatedCatalog = processBVMessage(bvValue, catTableValue);
          * return updatedCatalog;
          * } else {
          * System.out.println("catTableValue-" + catTableValue);
          * return catTableValue;
          * }
          * },
          * Joined.with(Serdes.String(), null, null))
          * .filter((key, value) -> value != null && !value.isEmpty())
          * .to(targetTopic, Produced.with(Serdes.String(), Serdes.String()));
          * 
          * 
          * // Price processing
          * 
          * KStream<String, String> priceSkuStream = priceStream
          * .selectKey((key, value) -> extractKeyFromPriceMessage(value)) // Extract key
          * .leftJoin(skuTable, (priceValue, skuTableValue) -> {
          * if (skuTableValue != null) {
          * String updatedSku = processPriceMessage(priceValue, skuTableValue);
          * return updatedSku;
          * } else {
          * return priceValue.toString(); // Use the original priceValue if no matching
          * SKU found
          * }
          * });
          * 
          * skuTable = priceSkuStream.toTable();
          * skuTable.toStream().peek((key, value) -> System.out.println("skuTable Key: "
          * + key));
          * 
          * KTable<String, String> aggregatedPriceSkuTable = skuTable
          * .groupBy((key, value) -> {
          * String productId = extractProductIdFromSku(value);
          * return KeyValue.pair(productId, value);
          * }) // Group by product ID
          * .aggregate(
          * () -> "[]", // Initial value is an empty array
          * (productId, reducedSkuValue, aggregate) -> addToJsonArray(reducedSkuValue,
          * aggregate),
          * (productId, oldAggregate, aggregate) -> aggregate,
          * Materialized.<String, String, KeyValueStore<Bytes,
          * byte[]>>as("aggregated-price-sku-store")
          * .withKeySerde(Serdes.String())
          * .withValueSerde(Serdes.String()));
          * 
          * // Join the aggregated SKU table with the product table based on productid
          * KTable<String, String> catalogPriceTable =
          * productTable.join(aggregatedPriceSkuTable,
          * (productData, skuData) -> joinProdSkuValues(productData, skuData, url),
          * Materialized.<String, String,
          * org.apache.kafka.streams.state.KeyValueStore<org.apache.kafka.common.utils.
          * Bytes, byte[]>>as(
          * "catalog-price-store") // Use a named store
          * .withKeySerde(Serdes.String())
          * .withValueSerde(Serdes.String()));
          * 
          * catalogPriceTable.toStream().peek((key, value) ->
          * System.out.println("catalogPriceTable Key: " + key));
          * catalogPriceTable
          * .toStream()
          * .filter((key, value) -> value != null && !value.isEmpty())
          * .to(targetTopic, Produced.with(Serdes.String(), Serdes.String()));
          * // catalogPriceTable.toStream().to(targetTopic);
          * 
          */
         Topology topology = builder.build();
         // Topology avroTopology = avroBuilder.build();
         System.out.println("topology created");

         streams = new KafkaStreams(topology, props);
         streams.start();

         Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
         // Runtime.getRuntime().addShutdownHook(new Thread(avroStreams::close));

      } catch (TopologyException te) {
         System.out.println(te.getMessage());
         te.printStackTrace();
      } catch (RecordTooLargeException recEx) {
         System.out.println(recEx.getMessage());
         recEx.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public String extractKeyFromMessage(String bvValue) {

      try {
         JsonNode rootNode = new ObjectMapper().readTree(bvValue);

         String productId = rootNode.get("BazaarvoicePayload").get("RatingReviewsSummaryPayload")
               .get("productId").asText();

         System.out.println("BV -productId-" + productId);
         return productId;
      } catch (Exception e) {
         // Handle parsing exception
         return e.toString();
      }

   }

   public String extractKeyFromPriceMessage(String priceValue) {
      try {
         JsonNode rootNode = new ObjectMapper().readTree(priceValue);
         String skuId = rootNode.get("sku").asText();

         System.out.println("price skuId-" + skuId);
         return skuId;
      } catch (Exception e) {
         // Handle parsing exception
         return e.toString();
      }

   }

   public String processBVMessage(String value, String catalogData) {

      try {
         String updatedProductData = "";

         ObjectMapper mapper = new ObjectMapper();
         org.json.JSONObject prodNode = new org.json.JSONObject(catalogData);
         JsonNode bvNode = new ObjectMapper().readTree(value);

         // Add new attributes to the JSON object
         String averageRating = bvNode.get("BazaarvoicePayload").get("RatingReviewsSummaryPayload")
               .get("averageRating").asText();
         String totalReviews = bvNode.get("BazaarvoicePayload").get("RatingReviewsSummaryPayload")
               .get("totalReviews").asText();
         prodNode.put("averageRating", averageRating);
         prodNode.put("totalReviews", totalReviews);

         // Convert the modified JSON object back to a string
         // updatedProductData = mapper.writeValueAsString(prodNode);
         return prodNode.toString();

      } catch (Exception e) {
         e.printStackTrace();
         return catalogData;
      }

   }

   public String processPriceMessage(String value, String data) {

      try {

         String updatedProductData = "";

         /*
          * ObjectMapper mapper = new ObjectMapper();
          * JsonNode prodNode = objectMapper.readTree(catalogData);
          * JsonNode priceNode = new ObjectMapper().readTree(value);
          * 
          * String priceSku = priceNode.path("sku").asText();
          * JsonNode skuArr = prodNode.path("sku");
          * System.out.println("priceSku-"+value);
          * System.out.println("catalogData-"+catalogData);
          * 
          * for (JsonNode skuNode : skuArr) {
          * String prodSku = skuNode.path("skuId").asText();
          * JsonNode listItem = objectMapper.createArrayNode();
          * System.out.println("inside for");
          * if (priceSku.equalsIgnoreCase(prodSku)) {
          * 
          * JsonNode priceDetailsArr = priceNode.path("priceDetail");
          * System.out.println("same sku");
          * for (JsonNode effectivePrice : priceDetailsArr) {
          * String effectivedate = effectivePrice.get("effectiveDate").asText();
          * LocalDate inputDate = LocalDate.parse(effectivedate);
          * 
          * LocalDate currentDate = LocalDate.now();
          * LocalDate nextDay = currentDate.plusDays(1);
          * String territory = priceNode.get("territory").asText();
          * String currency = "";
          * if (territory.equalsIgnoreCase("US")) {
          * currency = "USD";
          * } else if (territory.equalsIgnoreCase("CA")) {
          * currency = "CAD";
          * }
          * 
          * System.out.println("currency-"+currency);
          * if (inputDate.isEqual(currentDate) || inputDate.isEqual(nextDay)) {
          * JsonNode priceItem = objectMapper.createObjectNode()
          * .put("listPrice", effectivePrice.get("originalRetail").asText())
          * .put("currency", currency)
          * .put("country", territory);
          * 
          * ((com.fasterxml.jackson.databind.node.ArrayNode) listItem)
          * .add(priceItem);
          * }
          * }
          * }
          * ((ObjectNode) skuNode).put("priceInfo", listItem);
          * }
          * ((ObjectNode) prodNode).set("sku", skuArr);
          * System.out.println("skuArr-"+skuArr);
          * return prodNode.toString();
          */
         String updatedSkuData = "";
         // org.json.JSONObject priceObj = new org.json.JSONObject();
         // org.json.JSONObject listItem = new org.json.JSONObject();
         // org.json.JSONArray outputArray = new org.json.JSONArray();

         JsonNode priceObj = objectMapper.createObjectNode();
         // JsonNode listItem = objectMapper.createObjectNode();
         JsonNode outputArray = objectMapper.createArrayNode();

         ObjectMapper mapper = new ObjectMapper();
         // org.json.JSONObject skuNode = new org.json.JSONObject(data);
         JsonNode skuNode = mapper.readTree(data);
         JsonNode priceNode = new ObjectMapper().readTree(value);
         JsonNode priceDetailsNode = priceNode.path("priceDetail");
         String territory = priceNode.get("territory").asText();
         String currency = "";
         String suffix = "";
         if (territory.equalsIgnoreCase("US")) {
            currency = "USD";
            suffix = "US";
         } else if (territory.equalsIgnoreCase("CA")) {
            currency = "CAD";
            suffix = "CA";
         }
         String originalRetail = priceDetailsNode.get("originalRetail").asText();
         JsonNode priceItem = objectMapper.createObjectNode()
               .put("listPrice", originalRetail)
               .put("currency", currency)
               .put("country", territory);
         ((com.fasterxml.jackson.databind.node.ArrayNode) outputArray)
               .add(priceItem);

         /*
          * JsonNode priceDetailsArr = priceNode.path("priceDetail");
          * for (JsonNode effectivePrice : priceDetailsArr) {
          * // String effectivedate = effectivePrice.get("effectiveDate").asText();
          * // LocalDate inputDate = LocalDate.parse(effectivedate);
          * 
          * // LocalDate currentDate = LocalDate.now();
          * // LocalDate nextDay = currentDate.plusDays(1);
          * String territory = priceNode.get("territory").asText();
          * String currency = "";
          * if (territory.equalsIgnoreCase("US")) {
          * currency = "USD";
          * } else if (territory.equalsIgnoreCase("CA")) {
          * currency = "CAD";
          * }
          * 
          * // if (inputDate.isEqual(currentDate) || inputDate.isEqual(nextDay)) {
          * JsonNode priceItem = objectMapper.createObjectNode()
          * .put("listPrice", effectivePrice.get("originalRetail").asText())
          * .put("currency", currency)
          * .put("country", territory);
          * 
          * ((com.fasterxml.jackson.databind.node.ArrayNode) outputArray)
          * .add(priceItem);
          * 
          * // }
          * }
          */
         String propertyName = "priceInfo" + suffix;
         ((com.fasterxml.jackson.databind.node.ObjectNode) skuNode).set(propertyName, outputArray);
         return skuNode.toString();

      } catch (Exception e) {
         e.printStackTrace();
         return data;
      }

   }

   private boolean isProduct(String value) {
      // Implement your logic to determine if the value represents a product
      // For example, check the "type" field in the JSON message

      ObjectMapper om = new ObjectMapper();
      try {
         JsonNode jsonNode = om.readTree(value);
         String type = jsonNode.get("type").asText();
         System.out.println("type-" + type);
         dataType = type;

         return type.equalsIgnoreCase("product");
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   private boolean isSku(String value) {
      // Implement your logic to determine if the value represents a product
      // For example, check the "type" field in the JSON message

      ObjectMapper om = new ObjectMapper();
      try {
         JsonNode jsonNode = om.readTree(value);
         String type = jsonNode.get("type").asText();
         System.out.println("type-" + type);
         dataType = type;

         return type.equalsIgnoreCase("sku");
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   private boolean isEnglishProduct(String value) {
      try {

         return true;

      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }

   }

   private boolean isFrenchProduct(String value) {
      try {
         return false;

      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }

   }

   private static String addToJsonArrayUpdated(String skuData, String aggregatedData) {
      Map<String, JsonNode> skuMap = new HashMap<>();
      try {
         JsonNode aggNode = null;

         ObjectMapper objectMapper = new ObjectMapper();
         JsonNode arrayNode = objectMapper.createArrayNode();

         if (aggregatedData != null) {
            aggNode = objectMapper.readTree(aggregatedData);

            if (aggNode.isArray()) {

               for (JsonNode sku : aggNode) {
                  if (sku.get("skuId") != null) {
                     String skuId = sku.get("skuId").asText();
                     skuMap.put(skuId, sku);
                  }
               }

               JsonNode skuJsonNode = objectMapper.readTree(skuData); // Parse the skuData JSON string
               if (skuJsonNode.get("skuId") != null) {
                  String sId = skuJsonNode.get("skuId").asText();
                  skuMap.put(sId, skuJsonNode);
               }

               // Iterate over the HashMap and add values to the array
               for (Map.Entry<String, JsonNode> entry : skuMap.entrySet()) {
                  JsonNode valueNode = entry.getValue();
                  ((com.fasterxml.jackson.databind.node.ArrayNode) arrayNode).add(valueNode);
               }
               skuMap.clear();
            }
         } else {
            // Create a new JSON array node containing only skuData
            JsonNode skuJsonNode = objectMapper.readTree(skuData); // Parse the skuData JSON string
            ((ArrayNode) arrayNode).add(skuJsonNode);
         }
         return arrayNode.toString();
      } catch (IOException e) {
         e.printStackTrace();
         return aggregatedData;
      } catch (RecordTooLargeException ex) {
         ex.printStackTrace();
         return aggregatedData;
      } finally {
         skuMap.clear();
      }
   }

   private static String addToJsonArray(String skuData, String aggregatedData) {
      try {
         JsonNode jsonNode = null;

         ObjectMapper mapper = new ObjectMapper();
         if (aggregatedData != null) {
            jsonNode = mapper.readTree(aggregatedData);

            if (jsonNode.isArray()) {
               ArrayNode arrayNode = (ArrayNode) jsonNode;
               JsonNode skuJsonNode = mapper.readTree(skuData); // Parse the skuData JSON string
               arrayNode.add(skuJsonNode);

               /*
                * String skuid = skuJsonNode.path("skuId").asText();
                * 
                * if (skuJsonNode.path("active").asBoolean()) {
                * System.out.println("Sku " + skuid + " is active-" +
                * skuJsonNode.path("active").asBoolean());
                * if (!skuJsonNode.path("primaryProductId").asText().isEmpty()) {
                * System.out.println(
                * "Sku " + skuid + " has primaryProductId-" +
                * skuJsonNode.path("primaryProductId").asText());
                * arrayNode.add(skuJsonNode);
                * System.out.println("Sku node " + skuid + " is added to array");
                * }
                * }
                */
            }
         } else {
            // Create a new JSON array node containing only skuData
            jsonNode = mapper.createArrayNode();
            JsonNode skuJsonNode = mapper.readTree(skuData); // Parse the skuData JSON string
            ((ArrayNode) jsonNode).add(skuJsonNode);
         }
         return jsonNode.toString();
      } catch (IOException e) {
         e.printStackTrace();
         return aggregatedData;
      } catch (RecordTooLargeException ex) {
         ex.printStackTrace();
         return aggregatedData;
      }

   }

   public String extractProductIdFromSku(String skuValue) {

      try {
         JsonNode rootNode = new ObjectMapper().readTree(skuValue);

         String primaryProductId = rootNode.get("primaryProductId").asText();

         System.out.println("sku-primaryProductId-" + primaryProductId);
         return primaryProductId;
      } catch (Exception e) {
         // Handle parsing exception
         return e.toString();
      }

   }

   public String extractSkuIdFromSku(String skuValue) {

      try {
         JsonNode rootNode = new ObjectMapper().readTree(skuValue);

         JsonNode primaryProductNode = rootNode.path("data").path("attributes").path("enterpriseskuid");
         // String primaryProductValue = rootNode.get("primaryProductId").asText();
         String skuId = // rootNode.get("primaryProductId").asText();
               primaryProductNode.path("values").get(0).path("value").asText();

         System.out.println("sku-product id-" + skuId);
         return skuId;
      } catch (Exception e) {
         // Handle parsing exception
         return e.toString();
      }

   }

   public String extSkuIdFromSku(String skuValue) {

      try {
         JsonNode rootNode = new ObjectMapper().readTree(skuValue);

         String primaryProductValue = rootNode.get("skuId").asText();

         System.out.println("sku-product id-" + primaryProductValue);
         return primaryProductValue;
      } catch (Exception e) {
         // Handle parsing exception
         return e.toString();
      }

   }

   public String extractProductIdFromProduct(String prodValue) {

      try {
         JsonNode rootNode = new ObjectMapper().readTree(prodValue);

         JsonNode primaryProductNode = rootNode.path("data").path("attributes").path("productid");
         // String primaryProductValue = rootNode.get("productId").asText();
         String primaryProductValue = primaryProductNode.path("values").get(0).path("value").asText();

         System.out.println("product id-" + primaryProductValue);
         return primaryProductValue;
      } catch (Exception e) {
         // Handle parsing exception
         return e.toString();
      }

   }

   public String extractProductId(String productValue) {

      System.out.println("parsing for productId");
      try {
         JsonNode rootNode = new ObjectMapper().readTree(productValue);
         String productId = rootNode.get("name").asText();
         System.out.println("productId-" + productId);
         return productId;
      } catch (Exception e) {
         // Handle parsing exception
         return e.toString();
      }

   }

   public String extractProductIdForPrice(String priceValue) {

      try {
         JsonNode rootNode = new ObjectMapper().readTree(priceValue);
         String price = rootNode.path("priceDetail").get("originalRetail").asText();
         String country = rootNode.get("territory").asText();
         String currency = "";
         if (country.equals("US")) {
            currency = "USD";
         } else
            currency = "CAD";

         JSONObject jo = new JSONObject();
         jo.put("listPrice", price);
         jo.put("country", country);
         jo.put("currency", currency);

         JSONArray ja = new JSONArray();
         ja.add(jo);

         String finRes = ja.toJSONString();
         return finRes;
      } catch (Exception e) {
         // Handle parsing exception
         return e.toString();
      }

   }

   private static String joinProdSkuValues(String productData, String skuData, String url) {
      try {
         ObjectMapper mapper = new ObjectMapper();
         JsonNode productJson = mapper.readTree(productData);
         JsonNode skuJsonArray;

         if (skuData == null) {
            skuJsonArray = mapper.createArrayNode();
            return productJson.toString();
         } else {
            skuJsonArray = mapper.readTree(skuData);

            if (productJson.get("defaultSkuId") != null) {
               String defSku = productJson.get("defaultSkuId").asText();
               // JsonNode skuJsonArray = mapper.readTree(skuData);
               for (JsonNode sku : skuJsonArray) {
                  String skuid = sku.get("skuId").asText();
                  if (defSku.equalsIgnoreCase(skuid)) {
                     ((ObjectNode) sku).put("isDefaultSku", "true");
                  } else {
                     ((ObjectNode) sku).put("isDefaultSku", "false");
                  }
               }
               ((ObjectNode) productJson).set("sku", skuJsonArray);
               System.out.println("added sku to product");
            }

            if (productJson.get("swatchType") != null) {
               String swatchType = productJson.get("swatchType").asText();
               // JsonNode skuJsonArr = mapper.readTree(skuData);
               for (JsonNode sku2 : skuJsonArray) {
                  String skuId = sku2.get("skuId").asText();
                  String swatchUrl = "";
                  switch (swatchType) {
                     case ("Image - Rectangle"):
                        swatchUrl = url + "/productimages/sku/s" + skuId + "+sw.jpg";
                        break;

                     case ("Image - 36"):
                        swatchUrl = url + "/productimages/sku/s" + skuId + "+sw.jpg";
                        break;

                     case ("Image - 62"):
                        swatchUrl = url + "/productimages/sku/s" + skuId + "+sw-62.jpg";
                        break;

                     case ("Image - 42"):
                        swatchUrl = url + "/productimages/sku/s" + skuId + "+sw-42.jpg";
                        break;

                     default:
                        swatchUrl = url + "/productimages/sku/s" + skuId + "+sw.jpg";
                        break;

                  }
                  ((ObjectNode) sku2).put("swatch", swatchUrl);

               }
               ((ObjectNode) productJson).set("sku", skuJsonArray);
            }

            if (productJson.get("childSKUs") != null) {
               JsonNode childSkuArray = mapper.readTree(productJson.get("childSKUs").toString());
               String seq = "";
               for (JsonNode jsonNode : childSkuArray) {
                  for (JsonNode sku3 : skuJsonArray) {
                     seq = jsonNode.path(sku3.get("skuId").asText()).asText();
                     ((ObjectNode) sku3).put("sequence", seq);
                  }
               }
               ((ObjectNode) productJson).set("sku", skuJsonArray);
            }

            return productJson.toString();
         }
      } catch (IOException e) {
         e.printStackTrace();
         return productData; // Handle parsing error
      } catch (RecordTooLargeException ex) {
         ex.printStackTrace();
         return productData; // Handle parsing error
      }
   }

   private String convertProductToTargetFormat(String prodValue, String url) {
      String finalResp = "";
      try {
         JsonNode finObj = (JsonNode) ProductMapper.mapTargetProduct(prodValue, url);
         System.out.println("converted to target product");
         finalResp = finObj.toString();
         /*
          * String productId = finObj.path("productId").asText();
          * 
          * if (finObj.path("active").asBoolean()) {
          * if (!(finObj.path("primaryCategoryId").asText().isEmpty())) {
          * finalResp = finObj.toString();
          * System.out.println("Product " + productId +
          * " isa ctive and has primary category id");
          * } else {
          * System.out.println("Product " + productId + " has no primary category id");
          * }
          * } else if (!finObj.path("startDate").isEmpty()) {
          * finalResp = finObj.toString();
          * System.out.println("Product " + productId + " has future date");
          * } else {
          * System.out.println("Product " + productId + " is not active and excluded");
          * }
          */

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return finalResp;

   }

   private String convertSkuToTargetFormat(String skuValue, String url) {
      String finalResp = "";
      try {
         JsonNode skuNode = (JsonNode) SkuMapper.maptoTargetSku(skuValue, url);
         System.out.println("converted to target sku");
         finalResp = skuNode.toString();

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return finalResp;

   }

}
