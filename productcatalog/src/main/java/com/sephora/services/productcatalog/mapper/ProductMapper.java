package com.sephora.services.productcatalog.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ProductMapper {

        public static Object mapTargetProduct(String productValue, String url) {

                // JSONObject jo = new JSONObject();
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode jo = objectMapper.createObjectNode();

                try {
                        Object jsonDocument = Configuration.defaultConfiguration().jsonProvider()
                                        .parse(productValue);

                        String productId = JsonPath.read(jsonDocument,
                                        "$.data.attributes.productid.values[0].value") != null
                                                        ? JsonPath.read(jsonDocument,
                                                                        "$.data.attributes.productid.values[0].value")
                                                        : "";
                        // String productName = "";
                        // if (JsonPathEval(jsonDocument, "$.data.attributes.productname")) {
                        // productName = JsonPath.read(jsonDocument,
                        // "$.data.attributes.productname.values[0].value") != null
                        // ? JsonPath.read(jsonDocument,
                        // "$.data.attributes.productname.values[0].value")
                        // : "";
                        // }
                        boolean isActive = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.isactive")) {
                                isActive = JsonPath.read(jsonDocument,
                                                "$.data.attributes.isactive.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.isactive.values[0].value")
                                                                : false;

                        }
                        String subclassnumber = "";
                        String prodType = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.producttype")) {
                                prodType = JsonPath.read(jsonDocument,
                                                "$.data.attributes.producttype.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.producttype.values[0].value")
                                                                : "";
                        }
                        String seofriendlyurl = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.seofriendlyurl")) {
                                seofriendlyurl = JsonPath.read(jsonDocument,
                                                "$.data.attributes.seofriendlyurl.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.seofriendlyurl.values[0].value")
                                                                : "";
                        }
                        String pageUrl = url + seofriendlyurl;
                        String defaultsku = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.defaultsku")) {
                                defaultsku = JsonPath.read(jsonDocument,
                                                "$.data.attributes.defaultsku.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.defaultsku.values[0].value")
                                                                : "";
                        }
                        // String primarycategory = "";
                        // if (JsonPathEval(jsonDocument, "$.data.attributes.primarycategory")) {
                        // primarycategory = JsonPath.read(jsonDocument,
                        // "$.data.attributes.primarycategory.values[0].properties.referenceDataIdentifier")
                        // != null
                        // ? JsonPath.read(jsonDocument,
                        // "$.data.attributes.primarycategory.values[0].properties.referenceDataIdentifier")
                        // : "";
                        // }

                        String mcsbrandid = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.mcsbrandid")) {
                                mcsbrandid = JsonPath.read(jsonDocument,
                                                "$.data.attributes.mcsbrandid.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.mcsbrandid.values[0].value")
                                                                : "";
                        }

                        String variationtype = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.variationtype")) {
                                variationtype = JsonPath.read(jsonDocument,
                                                "$.data.attributes.variationtype.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.variationtype.values[0].value")
                                                                : "";
                        }
                        String uslivedate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.uslivedate")) {
                                uslivedate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.uslivedate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.uslivedate.values[0].value")
                                                                : "";
                        }
                        String usenddate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.usenddate")) {
                                usenddate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.usenddate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.usenddate.values[0].value")
                                                                : "";
                        }

                        String swatchtype = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.swatchtype")) {
                                swatchtype = JsonPath.read(jsonDocument,
                                                "$.data.attributes.swatchtype.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.swatchtype.values[0].value")
                                                                : "";
                        }
                        String lastModifiedOn = "";
                        if (JsonPathEval(jsonDocument, "$.properties.modifiedDate")) {
                                lastModifiedOn = JsonPath.read(jsonDocument,
                                                "$.properties.modifiedDate") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.properties.modifiedDate")
                                                                : "";
                        }

                        String keywords = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.keywords")) {
                                keywords = JsonPath.read(jsonDocument,
                                                "$.data.attributes.keywords.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.keywords.values[0].value")
                                                                : "";
                        }
                        ObjectNode keywordsObj = objectMapper.createObjectNode();
                        keywordsObj.put("KEYWORDS", keywords);

                        // ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(productValue);
                        JsonNode valuesNode = rootNode.path("data").path("attributes").path("associatedcategoryids")
                                        .path("values");

                        JsonNode associatedcategories = objectMapper.createArrayNode();
                        // List<String> associatedcategories = new ArrayList<>();
                        for (JsonNode valueNode : valuesNode) {
                                String catId = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) associatedcategories)
                                                .add(catId);
                                // String referenceData =
                                // valueNode.path("properties").path("referenceDataIdentifier")
                                // .asText();
                                // if (!referenceData.isEmpty()) {
                                // ((com.fasterxml.jackson.databind.node.ArrayNode) associatedcategories)
                                // .add(referenceData);
                                // }
                        }
                        // feedexclusion
                        JsonNode feedExcludes = objectMapper.createArrayNode();
                        if (JsonPathEval(jsonDocument, "$.data.attributes.restrictfromvendorfeeds")) {
                                JsonNode feedExNode = rootNode.path("data").path("attributes")
                                                .path("restrictfromvendorfeeds")
                                                .path("values");

                                for (JsonNode excludeNode : feedExNode) {
                                        String vendorName = excludeNode.path("value").asText();

                                        // JsonNode feedExcludeVendor = objectMapper.createObjectNode()
                                        // .put("vendorName", vendorName)
                                        // .put("feedExclusionStartDate", feedExclusionStartDate)
                                        // .put("feedExclusionEndDate", feedExclusionEndDate);
                                        // ((com.fasterxml.jackson.databind.node.ArrayNode) feedExcludes)
                                        // .add(feedExcludeVendor);
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) feedExcludes)
                                                        .add(vendorName);

                                }

                        }

                        // editorialseometadescriptionpublished
                        String editorialPublished = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.editorialseometadescriptionpublished")) {
                                editorialPublished = JsonPath.read(jsonDocument,
                                                "$.data.attributes.editorialseometadescriptionpublished.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.editorialseometadescriptionpublished.values[0].value")
                                                                : "";
                        }

                        // seopagetitle
                        String seopagetitle = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.seopagetitle")) {
                                seopagetitle = JsonPath.read(jsonDocument,
                                                "$.data.attributes.seopagetitle.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.seopagetitle.values[0].value")
                                                                : "";
                        }

                        // childskus
                        JsonNode childskus = rootNode.path("data").path("relationships").path("childskus");
                        JsonNode childSkusArr = objectMapper.createArrayNode();
                        // Map<String, String> childSkuMap = new HashMap<String, String>();
                        ObjectNode childSkuNode = objectMapper.createObjectNode();

                        for (JsonNode valueNode : childskus) {
                                String childSku = valueNode.path("relTo").path("data").path("attributes")
                                                .path("enterpriseskuid").path("values").path(0).path("value").asText();
                                String sequence = valueNode.path("attributes").path("assetsequence").path("values")
                                                .path(0).path("value").asText();
                                // childSkuMap.put(sequence, childSku);
                                childSkuNode.put(childSku, sequence);
                        }

                        ((com.fasterxml.jackson.databind.node.ArrayNode) childSkusArr).add(childSkuNode);

                        // useitwithproducts
                        JsonNode useitwithproducts = rootNode.path("data").path("attributes").path("useitwithproducts")
                                        .path("values");
                        JsonNode useitwithArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : useitwithproducts) {
                                String useItWith = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) useitwithArr).add(useItWith);
                        }

                        // productimages
                        JsonNode hasImagesNode = rootNode.path("data").path("relationships").path("hasimages");
                        JsonNode alternateImagesNode = objectMapper.createArrayNode();
                        int seqCntr = 1;
                        Map<Integer, String> seqMap = new HashMap<>();
                        for (JsonNode imageNode : hasImagesNode) {

                                String originalFilename = imageNode.path("relTo").path("data")
                                                .path("attributes").path("property_originalfilename")
                                                .path("values").path(0).path("value").asText();
                                int assetSeq = 0;
                                if (!originalFilename.contains("main-zoom")) {
                                        if (imageNode.path("attributes").path("assetsequence") != null) {
                                                String seq = imageNode.path("attributes")
                                                                .path("assetsequence").path("values").path("0")
                                                                .path("value").asText();
                                                if (!seq.isEmpty()) {
                                                        assetSeq = Integer.parseInt(seq);
                                                } else {
                                                        assetSeq = seqCntr;
                                                        seqCntr++;
                                                }
                                        } else {
                                                assetSeq = seqCntr;
                                                seqCntr++;
                                        }
                                        seqMap.put(assetSeq, originalFilename);
                                }

                        }

                        for (Map.Entry<Integer, String> entry : seqMap.entrySet()) {
                                String orgUrl = entry.getValue();
                                for (String imageType : new String[] { "thumb", "hero", "Lhero", "zoom" }) {
                                        String imageURL = url + "/productimages/product/" + orgUrl + "?imwidth=";
                                        switch (imageType) {
                                                case "thumb":
                                                        imageURL += "62";
                                                        break;
                                                case "hero":
                                                        imageURL += "270";
                                                        break;
                                                case "Lhero":
                                                        imageURL += "450";
                                                        break;
                                                case "zoom":
                                                        imageURL += "2000";
                                                        break;
                                        }
                                        JsonNode alternateImageItem = objectMapper.createObjectNode()
                                                        .put("imageType", imageType)
                                                        .put("imageURL", imageURL);
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) alternateImagesNode)
                                                        .add(alternateImageItem);

                                }
                        }

                        // productName
                        JsonNode productnameNode = rootNode.path("data").path("attributes")
                                        .path("productname")
                                        .path("values");
                        String productName = "";
                        boolean prodFlag = false;
                        for (JsonNode valueNode : productnameNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        productName = valueNode.path("value").asText();
                                        prodFlag = true;
                                }
                                if (prodFlag) {
                                        break;
                                }

                        }

                        // abouttheproductlongpublished
                        JsonNode publishedNode = rootNode.path("data").path("attributes")
                                        .path("abouttheproductlongpublished")
                                        .path("values");
                        String abouttheproductlongpublished = "";
                        boolean publishedFlag = false;
                        for (JsonNode valueNode : publishedNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        abouttheproductlongpublished = valueNode.path("value").asText();
                                        publishedFlag = true;
                                }
                                if (publishedFlag) {
                                        break;
                                }

                        }

                        // quicklookpublished
                        JsonNode quicklookpublishedNode = rootNode.path("data").path("attributes")
                                        .path("quicklookpublished")
                                        .path("values");
                        String quicklookpublished = "";
                        boolean quickLookFlag = false;
                        for (JsonNode valueNode : quicklookpublishedNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        quicklookpublished = valueNode.path("value").asText();
                                        quickLookFlag = true;
                                }
                                if (quickLookFlag) {
                                        break;
                                }

                        }

                        // abouttheproductshortpublished
                        JsonNode shortPublisheddNode = rootNode.path("data").path("attributes")
                                        .path("abouttheproductshortpublished")
                                        .path("values");
                        String abouttheproductshortpublished = "";
                        boolean shortPubFlag = false;
                        for (JsonNode valueNode : shortPublisheddNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        abouttheproductshortpublished = valueNode.path("value").asText();
                                        shortPubFlag = true;
                                }
                                if (shortPubFlag) {
                                        break;
                                }

                        }

                        // howtousepublished
                        JsonNode howtousepublishedNode = rootNode.path("data").path("attributes")
                                        .path("howtousepublished")
                                        .path("values");
                        String howtousepublished = "";
                        boolean howtousepublishedFlag = false;
                        for (JsonNode valueNode : howtousepublishedNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        howtousepublished = valueNode.path("value").asText();
                                        howtousepublishedFlag = true;
                                }
                                if (howtousepublishedFlag) {
                                        break;
                                }

                        }

                        // abouttheproductshortpublished
                        JsonNode longPublisheddNode = rootNode.path("data").path("attributes")
                                        .path("abouttheproductlongpublished")
                                        .path("values");
                        String longPublished = "";
                        boolean longFlag = false;
                        for (JsonNode valueNode : longPublisheddNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        longPublished = valueNode.path("value").asText();
                                        longFlag = true;
                                }
                                if (longFlag) {
                                        break;
                                }

                        }

                        // primarycategory
                        JsonNode categoriesNode = rootNode.path("data").path("relationships").path("categories");

                        String primarycategory = "";
                        for (JsonNode categoryNode : categoriesNode) {
                                if (categoryNode.path("attributes").path("isprimary") != null) {
                                        boolean isPrimary = categoryNode.path("attributes").path("isprimary")
                                                        .path("values").path(0).path("value").asBoolean();

                                        if (isPrimary) {
                                                primarycategory = categoryNode.path("relTo").path("data")
                                                                .path("attributes").path("categoryid")
                                                                .path("values").path(0).path("value").asText();
                                        }
                                }

                        }

                        jo.put("productId", productId);
                        jo.put("name", productName);
                        jo.put("active", isActive);
                        jo.set("keywords", keywordsObj);
                        jo.put("productSpecies", prodType);
                        jo.put("productDetailPageURL", pageUrl);
                        jo.put("defaultSkuId", defaultsku);
                        jo.set("categoryIds", associatedcategories);
                        jo.put("primaryCategoryId", primarycategory);
                        jo.put("brandId", mcsbrandid);
                        jo.put("description", abouttheproductlongpublished);
                        jo.put("descriptionNonHTML", abouttheproductlongpublished);
                        jo.put("quickLookDescription", quicklookpublished);
                        jo.put("shortDescription", abouttheproductshortpublished);
                        jo.put("shortDescriptionNonHTML", abouttheproductshortpublished);
                        jo.put("suggestedUsage", howtousepublished);
                        jo.put("suggestedUsageNonHTML", howtousepublished);
                        jo.put("variationType", variationtype);
                        jo.set("feedExcludes", feedExcludes);
                        jo.put("startDate", formatDate(uslivedate));
                        jo.put("endDate", formatDate(usenddate));
                        jo.put("seoMetaDescription", editorialPublished);
                        jo.put("sephoraDescription", longPublished);
                        jo.put("pageTitle", seopagetitle);
                        jo.set("childSKUs", childSkusArr);
                        jo.set("ancillarySkuIds", useitwithArr);
                        jo.put("swatchType", swatchtype);
                        jo.set("alternateImages", alternateImagesNode);
                        jo.put("lastModifiedOn", formatDate(lastModifiedOn));

                        JsonNode skuArray = objectMapper.createArrayNode();
                        // skuArray.add(skuObj);

                        jo.set("sku", skuArray);

                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return jo;
        }

        public static Boolean JsonPathEval(Object jsonDocument, String jsonPath) {
                Boolean expresionVal = true;
                try {
                        JsonPath.read(jsonDocument, jsonPath);
                        Configuration conf = Configuration.defaultConfiguration()
                                        .addOptions(Option.SUPPRESS_EXCEPTIONS);
                        Object result = JsonPath.using(conf).parse(jsonDocument).read(jsonPath);

                        // Check if the property exists or not
                        if (result != null) {
                                expresionVal = true;
                        } else {
                                System.out.println("Property is missing.");
                        }
                } catch (Exception e) {
                        // TODO: handle exception
                        expresionVal = false;
                }
                return expresionVal;

        }

        public static String formatDate(String inDate) throws Exception {

                // input date format
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

                // output date format
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = "";

                // Parse the input date string
                if (!inDate.isEmpty()) {

                        Date date = inputDateFormat.parse(inDate);
                        // Format the date as per the desired output format
                        outputDate = outputDateFormat.format(date);
                }

                return outputDate;

        }
}
