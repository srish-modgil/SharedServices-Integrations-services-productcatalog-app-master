package com.sephora.services.productcatalog.mapper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import net.minidev.json.JSONObject;

public class SkuMapper {

        public static Object maptoTargetSku(String skuValue, String url) {

                ObjectMapper objectMapper = new ObjectMapper();
                // Create an empty ObjectNode
                ObjectNode skuNode = objectMapper.createObjectNode();

                try {
                        Object jsonDocument = Configuration.defaultConfiguration().jsonProvider()
                                        .parse(skuValue);

                        String enterpriseskuid = JsonPath.read(jsonDocument,
                                        "$.data.attributes.enterpriseskuid.values[0].value") != null
                                                        ? JsonPath.read(jsonDocument,
                                                                        "$.data.attributes.enterpriseskuid.values[0].value")
                                                        : "";
                        boolean isDefaultSku = false;
                        // String productname = "";
                        // if (JsonPathEval(jsonDocument, "$.data.attributes.productname")) {
                        // productname = JsonPath.read(jsonDocument,
                        // "$.data.attributes.productname.values[0].value") != null
                        // ? JsonPath.read(jsonDocument,
                        // "$.data.attributes.productname.values[0].value")
                        // : "";
                        // }
                        String primaryproduct = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.primaryproduct")) {
                                primaryproduct = JsonPath.read(jsonDocument,
                                                "$.data.attributes.primaryproduct.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.primaryproduct.values[0].value")
                                                                : "";
                        }
                        String biType = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.bitype")) {
                                biType = JsonPath.read(jsonDocument,
                                                "$.data.attributes.bitype.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.bitype.values[0].value")
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
                        String efficacyApplication = "";
                        if (JsonPathEval(jsonDocument, "$.properties.application")) {
                                efficacyApplication = JsonPath.read(jsonDocument,
                                                "$.properties.application") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.properties.application")
                                                                : "";
                        }
                        String skutype = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.skutype")) {
                                skutype = JsonPath.read(jsonDocument,
                                                "$.data.attributes.skutype.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.skutype.values[0].value")
                                                                : "";
                        }
                        String upc = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.upc")) {
                                upc = JsonPath.read(jsonDocument, "$.data.attributes.upc.values[0].value") != null
                                                ? JsonPath.read(jsonDocument, "$.data.attributes.upc.values[0].value")
                                                : "";
                        }
                        String upcTrimmed = removeLeadingZeros(upc);
                        String countryofmanufacture = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.mcscountryofmanufacture")) {
                                countryofmanufacture = JsonPath.read(jsonDocument,
                                                "$.data.attributes.mcscountryofmanufacture.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.mcscountryofmanufacture.values[0].value")
                                                                : "";
                        }
                        boolean isactive = JsonPath.read(jsonDocument,
                                        "$.data.attributes.isactive.values[0].value") != null
                                                        ? JsonPath.read(jsonDocument,
                                                                        "$.data.attributes.isactive.values[0].value")
                                                        : false;
                        // boolean isPrimary = JsonPath.read(jsonDocument,
                        // "$.data.attributes.isprimary.values[0].value") != null
                        // ? JsonPath.read(jsonDocument, "$.data.attributes.isprimary.values[0].value")
                        // : false;
                        boolean comingsoon = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.comingsoon")) {
                                comingsoon = JsonPath.read(jsonDocument,
                                                "$.data.attributes.comingsoon.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.comingsoon.values[0].value")
                                                                : false;
                        }
                        String comingsoonenddate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.comingsoonenddate")) {
                                comingsoonenddate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.comingsoonenddate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.comingsoonenddate.values[0].value")
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
                        String newuntildate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.newuntildate")) {
                                newuntildate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.newuntildate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.newuntildate.values[0].value")
                                                                : "";
                        }
                        boolean isthisproductnewtosephora = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.isthisproductnewtosephora")) {
                                isthisproductnewtosephora = JsonPath.read(jsonDocument,
                                                "$.data.attributes.isthisproductnewtosephora.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.isthisproductnewtosephora.values[0].value")
                                                                : false;
                        }
                        int maxpurchaseqtyperorder = 0;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.maxpurchaseqtyperorder")) {
                                maxpurchaseqtyperorder = JsonPath.read(jsonDocument,
                                                "$.data.attributes.maxpurchaseqtyperorder.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.maxpurchaseqtyperorder.values[0].value")
                                                                : 0;
                        }
                        String outofstocktreatment = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.outofstocktreatment")) {
                                outofstocktreatment = JsonPath.read(jsonDocument,
                                                "$.data.attributes.outofstocktreatment.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.outofstocktreatment.values[0].value")
                                                                : "";
                        }
                        boolean excludefromsearch = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.excludefromsearch")) {
                                excludefromsearch = JsonPath.read(jsonDocument,
                                                "$.data.attributes.excludefromsearch.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.excludefromsearch.values[0].value")
                                                                : false;
                        }
                        boolean reservationsenabled = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.reservationsenabled")) {
                                reservationsenabled = JsonPath.read(jsonDocument,
                                                "$.data.attributes.reservationsenabled.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.reservationsenabled.values[0].value")
                                                                : false;
                        }
                        boolean callcenteronly = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.callcenteronly")) {
                                callcenteronly = JsonPath.read(jsonDocument,
                                                "$.data.attributes.callcenteronly.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.callcenteronly.values[0].value")
                                                                : false;
                        }
                        boolean isstoreonly = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.isstoreonly")) {
                                isstoreonly = JsonPath.read(jsonDocument,
                                                "$.data.attributes.isstoreonly.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.isstoreonly.values[0].value")
                                                                : false;
                        }
                        boolean isthisproductonlineonly = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.isthisproductonlineonly")) {
                                isthisproductonlineonly = JsonPath.read(jsonDocument,
                                                "$.data.attributes.isthisproductonlineonly.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.isthisproductonlineonly.values[0].value")
                                                                : false;
                        }
                        boolean isbiexclusive = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.isbiexclusive")) {
                                isbiexclusive = JsonPath.read(jsonDocument,
                                                "$.data.attributes.isbiexclusive.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.isbiexclusive.values[0].value")
                                                                : false;
                        }
                        String biexclusivelevel = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.biexclusivelevel")) {
                                biexclusivelevel = JsonPath.read(jsonDocument,
                                                "$.data.attributes.biexclusivelevel.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.biexclusivelevel.values[0].value")
                                                                : "";
                        }
                        boolean giftwrappable = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.giftwrappable")) {
                                giftwrappable = JsonPath.read(jsonDocument,
                                                "$.data.attributes.giftwrappable.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.giftwrappable.values[0].value")
                                                                : false;
                        }
                        String finish = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.finish")) {
                                finish = JsonPath.read(jsonDocument, "$.data.attributes.finish.values[0].value") != null
                                                ? JsonPath.read(jsonDocument,
                                                                "$.data.attributes.finish.values[0].value")
                                                : "";
                        }
                        JsonNode finishArr = objectMapper.createArrayNode();
                        ((com.fasterxml.jackson.databind.node.ArrayNode) finishArr).add(finish);

                        String coverage = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.coverage")) {
                                coverage = JsonPath.read(jsonDocument,
                                                "$.data.attributes.coverage.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.coverage.values[0].value")
                                                                : "";
                        }
                        JsonNode coverageArr = objectMapper.createArrayNode();
                        ((com.fasterxml.jackson.databind.node.ArrayNode) coverageArr).add(coverage);

                        String fullingredientslistpublished = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.fullingredientslistpublished")) {
                                fullingredientslistpublished = JsonPath.read(jsonDocument,
                                                "$.data.attributes.fullingredientslistpublished.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.fullingredientslistpublished.values[0].value")
                                                                : "";
                        }
                        String mcshazmatvalue = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.mcshazmatvalue")) {
                                mcshazmatvalue = JsonPath.read(jsonDocument,
                                                "$.data.attributes.mcshazmatvalue.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.mcshazmatvalue.values[0].value")
                                                                : "";
                        }

                        String fillsizeoz = "";
                        if (JsonPathEval(jsonDocument,
                                        "$.data.attributes.fillsize.group[0].fillsizeoz.values[0].value")) {
                                fillsizeoz = JsonPath.read(jsonDocument,
                                                "$.data.attributes.fillsize.group[0].fillsizeoz.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.fillsize.group[0].fillsizeoz.values[0].value")
                                                                : "";
                        }
                        String fillsizemlg = "";
                        if (JsonPathEval(jsonDocument,
                                        "$.data.attributes.fillsize.group[0].fillsizemlg.values[0].value")) {
                                fillsizemlg = JsonPath.read(jsonDocument,
                                                "$.data.attributes.fillsize.group[0].fillsizemlg.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.fillsize.group[0].fillsizemlg.values[0].value")
                                                                : "";
                        }
                        // String valuesetdescription = "";
                        // if (JsonPathEval(jsonDocument, "$.data.attributes.valuesetdescription")) {
                        // valuesetdescription = JsonPath.read(jsonDocument,
                        // "$.data.attributes.valuesetdescription.values[0].value") != null
                        // ? JsonPath.read(jsonDocument,
                        // "$.data.attributes.valuesetdescription.values[0].value")
                        // : "";
                        // }

                        // String shadename = "";
                        // if (JsonPathEval(jsonDocument, "$.data.attributes.shadename")) {
                        // shadename = JsonPath.read(jsonDocument,
                        // "$.data.attributes.shadename.values[0].value") != null
                        // ? String.valueOf(JsonPath.read(jsonDocument,
                        // "$.data.attributes.shadename.values[0].value"))
                        // : "";
                        // }
                        boolean firstaccess = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.firstaccess")) {
                                firstaccess = JsonPath.read(jsonDocument,
                                                "$.data.attributes.firstaccess.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.firstaccess.values[0].value")
                                                                : false;
                        }
                        String firstaccessrestrictedcountry = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.firstaccessrestrictedcountry")) {
                                firstaccessrestrictedcountry = JsonPath.read(jsonDocument,
                                                "$.data.attributes.firstaccessrestrictedcountry.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.firstaccessrestrictedcountry.values[0].value")
                                                                : "";
                        }
                        String firstaccessstartdate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.firstaccessstartdate")) {
                                firstaccessstartdate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.firstaccessstartdate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.firstaccessstartdate.values[0].value")
                                                                : "";
                        }
                        String firstaccessenddate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.firstaccessenddate")) {
                                firstaccessenddate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.firstaccessenddate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.firstaccessenddate.values[0].value")
                                                                : "";
                        }
                        boolean limitedtimeoffer = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.limitedtimeoffer")) {
                                limitedtimeoffer = JsonPath.read(jsonDocument,
                                                "$.data.attributes.limitedtimeoffer.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.limitedtimeoffer.values[0].value")
                                                                : false;
                        }
                        String limitedtimeofferrestrictedcountry = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.limitedtimeofferrestrictedcountry")) {
                                limitedtimeofferrestrictedcountry = JsonPath.read(jsonDocument,
                                                "$.data.attributes.limitedtimeofferrestrictedcountry.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.limitedtimeofferrestrictedcountry.values[0].value")
                                                                : "";
                        }
                        String limitedtimeofferstartdate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.limitedtimeofferstartdate")) {
                                limitedtimeofferstartdate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.limitedtimeofferstartdate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.limitedtimeofferstartdate.values[0].value")
                                                                : "";
                        }
                        String limitedtimeofferenddate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.limitedtimeofferenddate")) {
                                limitedtimeofferenddate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.limitedtimeofferenddate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.limitedtimeofferenddate.values[0].value")
                                                                : "";
                        }
                        boolean appexclusive = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.appexclusive")) {
                                appexclusive = JsonPath.read(jsonDocument,
                                                "$.data.attributes.appexclusive.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.appexclusive.values[0].value")
                                                                : false;
                        }
                        String appexclusiverestrictedcountry = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.appexclusiverestrictedcountry")) {
                                appexclusiverestrictedcountry = JsonPath.read(jsonDocument,
                                                "$.data.attributes.appexclusiverestrictedcountry.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.appexclusiverestrictedcountry.values[0].value")
                                                                : "";
                        }
                        String appexclusivestartdate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.appexclusivestartdate")) {
                                appexclusivestartdate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.appexclusivestartdate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.appexclusivestartdate.values[0].value")
                                                                : "";
                        }
                        String appexclusiveenddate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.appexclusiveenddate")) {
                                appexclusiveenddate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.appexclusiveenddate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.appexclusiveenddate.values[0].value")
                                                                : "";
                        }
                        boolean productisexclusivetosephora = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.productisexclusivetosephora")) {
                                productisexclusivetosephora = JsonPath.read(jsonDocument,
                                                "$.data.attributes.productisexclusivetosephora.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.productisexclusivetosephora.values[0].value")
                                                                : false;
                        }
                        String onlyatsephoraenddate = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.onlyatsephoraenddate")) {
                                onlyatsephoraenddate = JsonPath.read(jsonDocument,
                                                "$.data.attributes.onlyatsephoraenddate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.onlyatsephoraenddate.values[0].value")
                                                                : "";
                        }
                        boolean isthisproductlimitededition = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.isthisproductlimitededition")) {
                                isthisproductlimitededition = JsonPath.read(jsonDocument,
                                                "$.data.attributes.isthisproductlimitededition.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.isthisproductlimitededition.values[0].value")
                                                                : false;
                        }
                        String badges = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.associatedbadges")) {
                                badges = JsonPath.read(jsonDocument,
                                                "$.data.attributes.associatedbadges.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.associatedbadges.values[0].value")
                                                                : "";
                        }
                        String primaryconcern = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.primaryconcern")) {
                                primaryconcern = JsonPath.read(jsonDocument,
                                                "$.data.attributes.primaryconcern.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.primaryconcern.values[0].value")
                                                                : "";
                        }

                        String pantoneskintoneidprimary = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.pantoneskintoneidprimary")) {
                                pantoneskintoneidprimary = JsonPath.read(jsonDocument,
                                                "$.data.attributes.pantoneskintoneidprimary.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.pantoneskintoneidprimary.values[0].value")
                                                                : "";
                        }
                        // String hairtype = "";
                        // if (JsonPathEval(jsonDocument, "$.data.attributes.hairtype")) {
                        // hairtype = JsonPath.read(jsonDocument,
                        // "$.data.attributes.hairtype.values[0].value") != null
                        // ? JsonPath.read(jsonDocument,
                        // "$.data.attributes.hairtype.values[0].value")
                        // : "";
                        // }
                        String efficacy = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.efficacy")) {
                                efficacy = JsonPath.read(jsonDocument,
                                                "$.data.attributes.efficacy.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.efficacy.values[0].value")
                                                                : "";
                        }
                        String efficacyingredient = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.efficacyingredient")) {
                                efficacyingredient = JsonPath.read(jsonDocument,
                                                "$.data.attributes.efficacyingredient.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.efficacyingredient.values[0].value")
                                                                : "";
                        }
                        String fullsizesku = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.fullsizesku")) {
                                fullsizesku = JsonPath.read(jsonDocument,
                                                "$.data.attributes.fullsizesku.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.fullsizesku.values[0].value")
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
                        String productline = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.productline")) {
                                productline = JsonPath.read(jsonDocument,
                                                "$.data.attributes.productline.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.productline.values[0].value")
                                                                : "";
                        }

                        String labValue = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.labcode")) {
                                labValue = JsonPath.read(jsonDocument,
                                                "$.data.attributes.labcode.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.labcode.values[0].value")
                                                                : "";
                        }

                        // boolean isinventoryavailable = false;
                        // if (JsonPathEval(jsonDocument, "$.data.attributes.isinventoryavailable")) {
                        // isinventoryavailable = JsonPath.read(jsonDocument,
                        // "$.data.attributes.isinventoryavailable.values[0].value") != null
                        // ? JsonPath.read(jsonDocument,
                        // "$.data.attributes.isinventoryavailable.values[0].value")
                        // : false;
                        // }
                        String departmentnumber = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.departmentnumber")) {
                                departmentnumber = JsonPath.read(jsonDocument,
                                                "$.data.attributes.departmentnumber.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.departmentnumber.values[0].value")
                                                                : "";
                        }
                        String classnumber = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.classnumber")) {
                                classnumber = JsonPath.read(jsonDocument,
                                                "$.data.attributes.classnumber.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.classnumber.values[0].value")
                                                                : "";
                        }
                        String subclassnumber = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.subclassnumber")) {
                                subclassnumber = JsonPath.read(jsonDocument,
                                                "$.data.attributes.subclassnumber.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.subclassnumber.values[0].value")
                                                                : "";
                        }

                        String skuvariationsize = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.skuvariationsize")) {
                                skuvariationsize = JsonPath.read(jsonDocument,
                                                "$.data.attributes.skuvariationsize.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.skuvariationsize.values[0].value")
                                                                : "";
                        }
                        String skuvariationtype = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.skuvariationtype")) {
                                skuvariationtype = JsonPath.read(jsonDocument,
                                                "$.data.attributes.skuvariationtype.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.skuvariationtype.values[0].value")
                                                                : "";
                        }
                        String bidescription = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.bidescription")) {
                                bidescription = JsonPath.read(jsonDocument,
                                                "$.data.attributes.bidescription.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.bidescription.values[0].value")
                                                                : "";
                        }
                        String colornumber = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.colornumber")) {
                                colornumber = JsonPath.read(jsonDocument,
                                                "$.data.attributes.colornumber.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.colornumber.values[0].value")
                                                                : "";
                        }

                        JsonNode nailtypeNode = objectMapper.createArrayNode();
                        if (JsonPathEval(jsonDocument, "$.data.attributes.nailtype")) {
                                String nailtype = JsonPath.read(jsonDocument,
                                                "$.data.attributes.nailtype.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.nailtype.values[0].value")
                                                                : "";
                                ((com.fasterxml.jackson.databind.node.ArrayNode) nailtypeNode)
                                                .add(nailtype);
                        }

                        JsonNode fragranceImpr = objectMapper.createArrayNode();
                        if (JsonPathEval(jsonDocument, "$.data.attributes.fragranceimpressionforvegan")) {
                                String fragranceimpressionforvegan = JsonPath.read(jsonDocument,
                                                "$.data.attributes.fragranceimpressionforvegan.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.fragranceimpressionforvegan.values[0].value")
                                                                : "";
                                ((com.fasterxml.jackson.databind.node.ArrayNode) fragranceImpr)
                                                .add(fragranceimpressionforvegan);
                        }

                        JsonNode falseeyelashvolumeNode = objectMapper.createArrayNode();
                        if (JsonPathEval(jsonDocument, "$.data.attributes.falseeyelashvolume")) {
                                String falseeyelashvolume = JsonPath.read(jsonDocument,
                                                "$.data.attributes.falseeyelashvolume.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.falseeyelashvolume.values[0].value")
                                                                : "";
                                ((com.fasterxml.jackson.databind.node.ArrayNode) falseeyelashvolumeNode)
                                                .add(falseeyelashvolume);
                        }

                        // sunProtections
                        JsonNode sunProtectionsNode = objectMapper.createArrayNode();
                        if (JsonPathEval(jsonDocument, "$.data.attributes.spflevel")) {
                                String sunProtections = JsonPath.read(jsonDocument,
                                                "$.data.attributes.spflevel.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.spflevel.values[0].value")
                                                                : "";
                                ((com.fasterxml.jackson.databind.node.ArrayNode) sunProtectionsNode)
                                                .add(sunProtections);
                        }

                        String taxonomy = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.merchandisingtaxonomy")) {
                                taxonomy = JsonPath.read(jsonDocument,
                                                "$.data.attributes.merchandisingtaxonomy.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.merchandisingtaxonomy.values[0].value")
                                                                : "";
                        }

                        String heroimagealttext = "";
                        if (JsonPathEval(jsonDocument, "$.data.attributes.heroimagealttext")) {
                                heroimagealttext = JsonPath.read(jsonDocument,
                                                "$.data.attributes.heroimagealttext.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.heroimagealttext.values[0].value")
                                                                : "";
                        }

                        boolean nonreturnable = false;
                        if (JsonPathEval(jsonDocument, "$.data.attributes.nonreturnable")) {
                                nonreturnable = JsonPath.read(jsonDocument,
                                                "$.data.attributes.nonreturnable.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.nonreturnable.values[0].value")
                                                                : false;
                        }

                        String[] taxonomyList = taxonomy.split(">>");
                        String refinement = taxonomyList[1].toString();

                        JsonNode formulationRefinement = objectMapper.createArrayNode();
                        if (refinement.equalsIgnoreCase("Fragrance")) {
                                if (JsonPathEval(jsonDocument, "$.data.attributes.formulationfragrance")) {
                                        String formulationfragrance = JsonPath.read(jsonDocument,
                                                        "$.data.attributes.formulationfragrance.values[0].value") != null
                                                                        ? JsonPath.read(jsonDocument,
                                                                                        "$.data.attributes.formulationfragrance.values[0].value")
                                                                        : "";
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) formulationRefinement)
                                                        .add(formulationfragrance);
                                }
                        }

                        if (refinement.equalsIgnoreCase("Makeup")) {
                                if (JsonPathEval(jsonDocument, "$.data.attributes.formulationmakeupcomplexion")) {
                                        String formulationmakeupcomplexion = JsonPath.read(jsonDocument,
                                                        "$.data.attributes.formulationmakeupcomplexion.values[0].value") != null
                                                                        ? JsonPath.read(jsonDocument,
                                                                                        "$.data.attributes.formulationmakeupcomplexion.values[0].value")
                                                                        : "";
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) formulationRefinement)
                                                        .add(formulationmakeupcomplexion);
                                }
                        }

                        if (refinement.equalsIgnoreCase("Skincare")) {
                                if (JsonPathEval(jsonDocument, "$.data.attributes.formulationskincare")) {
                                        String formulationskincare = JsonPath.read(jsonDocument,
                                                        "$.data.attributes.formulationskincare.values[0].value") != null
                                                                        ? JsonPath.read(jsonDocument,
                                                                                        "$.data.attributes.formulationskincare.values[0].value")
                                                                        : "";
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) formulationRefinement)
                                                        .add(formulationskincare);
                                }
                        }

                        if (refinement.equalsIgnoreCase("Hair")) {
                                if (JsonPathEval(jsonDocument, "$.data.attributes.formulationtypehair")) {
                                        String formulationtypehair = JsonPath.read(jsonDocument,
                                                        "$.data.attributes.formulationtypehair.values[0].value") != null
                                                                        ? JsonPath.read(jsonDocument,
                                                                                        "$.data.attributes.formulationtypehair.values[0].value")
                                                                        : "";
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) formulationRefinement)
                                                        .add(formulationtypehair);
                                }
                        }

                        if (refinement.equalsIgnoreCase("Makeup")) {
                                if (JsonPathEval(jsonDocument, "$.data.attributes.formulationtypemakeup")) {
                                        String formulationtypemakeup = JsonPath.read(jsonDocument,
                                                        "$.data.attributes.formulationtypemakeup.values[0].value") != null
                                                                        ? JsonPath.read(jsonDocument,
                                                                                        "$.data.attributes.formulationtypemakeup.values[0].value")
                                                                        : "";
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) formulationRefinement)
                                                        .add(formulationtypemakeup);
                                }
                        }

                        boolean imageUpdated = false;
                        String publishedDateString = "";
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        if (JsonPathEval(jsonDocument, "$.data.attributes.publishedurlupdate")) {
                                publishedDateString = JsonPath.read(jsonDocument,
                                                "$.data.attributes.publishedurlupdate.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.attributes.publishedurlupdate.values[0].value")
                                                                : "";

                                if (!publishedDateString.isEmpty()) {
                                        LocalDateTime publishedDateTime = LocalDateTime.parse(publishedDateString,
                                                        formatter);
                                        LocalDate today = LocalDate.now();
                                        LocalDate yesterday = today.minusDays(1);

                                        LocalDate publishedDate = publishedDateTime.toLocalDate();
                                        if (publishedDate.isEqual(today) || publishedDate.isEqual(yesterday)) {
                                                imageUpdated = true;
                                        }
                                }
                        }

                        // ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(skuValue);

                        String productname = "";
                        boolean nameFlag = false;
                        JsonNode productNameNode = rootNode.path("data").path("attributes")
                                        .path("productname")
                                        .path("values");

                        for (JsonNode nameNode : productNameNode) {
                                String locale = nameNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        productname = nameNode.path("value").asText();
                                        nameFlag = true;
                                }
                                if (nameFlag) {
                                        break;
                                }

                        }

                        JsonNode fragrancetypeNode = objectMapper.createArrayNode();
                        JsonNode fragTypeNode = rootNode.path("data").path("attributes")
                                        .path("fragrancetype")
                                        .path("values");
                        for (JsonNode valueNode : fragTypeNode) {
                                String fragrancetype = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) fragrancetypeNode)
                                                .add(fragrancetype);
                        }

                        JsonNode realtedBadgesNode = objectMapper.createArrayNode();
                        JsonNode badgesNode = rootNode.path("data").path("relationships").path("realtedbadges");
                        String badgeStr = "";
                        for (JsonNode badge : badgesNode) {
                                badgeStr = badge.path("relTo").path("data")
                                                .path("attributes").path("displayname")
                                                .path("values").path(0).path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) realtedBadgesNode)
                                                .add(badgeStr);
                        }

                        String badgeAltText = "";
                        if (JsonPathEval(jsonDocument, "$.data.relationships.realtedbadges")) {
                                badgeAltText = JsonPath.read(jsonDocument,
                                                "$.data.relationships.realtedbadges[0].relTo.data.attributes.imagealttext.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.relationships.realtedbadges[0].relTo.data.attributes.imagealttext.values[0].value")
                                                                : "";
                        }

                        String badgeImgUrl = "";
                        if (JsonPathEval(jsonDocument, "$.data.relationships.realtedbadges")) {
                                badgeImgUrl = JsonPath.read(jsonDocument,
                                                "$.data.relationships.realtedbadges[0].relTo.data.attributes.imageurl.values[0].value") != null
                                                                ? JsonPath.read(jsonDocument,
                                                                                "$.data.relationships.realtedbadges[0].relTo.data.attributes.imageurl.values[0].value")
                                                                : "";
                        }

                        // String realtedbadges = "";
                        // if (JsonPathEval(jsonDocument, "$.data.attributes.realtedbadges")) {
                        // realtedbadges = JsonPath.read(jsonDocument,
                        // "$.data.attributes.realtedbadges[0].relTo.data.attributes.badgeid.values[0].value")
                        // != null
                        // ? JsonPath.read(jsonDocument,
                        // "$.data.attributes.realtedbadges[0].relTo.data.attributes.badgeid.values[0].value")
                        // : "";
                        // }

                        JsonNode concernsRefinement = objectMapper.createArrayNode();
                        if (refinement.equalsIgnoreCase("Hair")) {
                                JsonNode hairConcernsNode = rootNode.path("data").path("attributes")
                                                .path("hairconcerns")
                                                .path("values");
                                for (JsonNode valueNode : hairConcernsNode) {
                                        String hairconcerns = valueNode.path("value").asText();
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) concernsRefinement)
                                                        .add(hairconcerns);
                                }
                        }

                        if (refinement.equalsIgnoreCase("Makeup")) {
                                JsonNode skincarConcernsNode = rootNode.path("data").path("attributes")
                                                .path("skincareconcernsaddressedmakeup")
                                                .path("values");
                                for (JsonNode valueNode : skincarConcernsNode) {
                                        String skincareConcern = valueNode.path("value").asText();
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) concernsRefinement)
                                                        .add(skincareConcern);
                                }
                        }
                        if (refinement.equalsIgnoreCase("Skincare")) {
                                JsonNode skinConcernsNode = rootNode.path("data").path("attributes")
                                                .path("skincareconcernsaddressedskincare")
                                                .path("values");
                                for (JsonNode valueNode : skinConcernsNode) {
                                        String skinConcern = valueNode.path("value").asText();
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) concernsRefinement)
                                                        .add(skinConcern);
                                }
                        }

                        JsonNode valuePriceNode = rootNode.path("data").path("attributes").path("valueprice")
                                        .path("group");
                        JsonNode wholesalePrice = objectMapper.createArrayNode();
                        for (JsonNode valueNode : valuePriceNode) {
                                if (valueNode.path("usvalue") != null && !valueNode.path("usvalue").isEmpty()) {
                                        String wholesaleCurrency = "USD";
                                        String wholesaleCountry = "US";
                                        Integer priceValue = valueNode.path("usvalue").path("values")
                                                        .path(0).path("value").asInt();

                                        // double wholesaleSalePrice = Double.valueOf(String.valueOf(priceValue));

                                        JsonNode priceItem = objectMapper.createObjectNode()
                                                        .put("salePrice", priceValue)
                                                        .put("currency", wholesaleCurrency)
                                                        .put("country", wholesaleCountry);

                                        ((com.fasterxml.jackson.databind.node.ArrayNode) wholesalePrice)
                                                        .add(priceItem);
                                }

                                if (valueNode.path("cavalue") != null && !valueNode.path("cavalue").isEmpty()) {
                                        String wholesaleCurrencyCA = "CAD";
                                        String wholesaleCountryCA = "CA";
                                        Integer priceValueCA = valueNode.path("cavalue").path("values")
                                                        .path(0).path("value").asInt();

                                        // double wholesaleSalePriceCA = Double.valueOf(String.valueOf(priceValueCA));

                                        JsonNode priceItemCA = objectMapper.createObjectNode()
                                                        .put("salePrice", priceValueCA)
                                                        .put("currency", wholesaleCurrencyCA)
                                                        .put("country", wholesaleCountryCA);

                                        ((com.fasterxml.jackson.databind.node.ArrayNode) wholesalePrice)
                                                        .add(priceItemCA);
                                }
                        }

                        // highlights
                        JsonNode valuesNode = rootNode.path("data").path("relationships").path("relatedhighlights");

                        JsonNode outputHighlightsNode = objectMapper.createArrayNode();
                        for (JsonNode valueNode : valuesNode) {
                                String imageUrl = valueNode.path("relTo").path("data").path("attributes")
                                                .path("imageurl")
                                                .path("values").path(0).path("value").asText();
                                String badgeImageURL = url + imageUrl;

                                String imageAltTxt = "";
                                boolean txtFlag = false;
                                JsonNode badgeAltTextNode = valueNode.path("relTo").path("data").path("attributes")
                                                .path("imagealttext")
                                                .path("values");
                                for (JsonNode textNode : badgeAltTextNode) {
                                        String locale = textNode.path("locale").asText();
                                        if (locale.equalsIgnoreCase("en-US")) {
                                                imageAltTxt = textNode.path("value").asText();
                                                txtFlag = true;
                                        }
                                        if (txtFlag) {
                                                break;
                                        }

                                }

                                JsonNode badgeNode = valueNode.path("relTo").path("data").path("attributes")
                                                .path("displayname")
                                                .path("values");
                                String badgeName = "";
                                boolean badgeFlag = false;
                                for (JsonNode node : badgeNode) {
                                        String locale = node.path("locale").asText();
                                        if (locale.equalsIgnoreCase("en-US")) {
                                                badgeName = node.path("value").asText();
                                                badgeFlag = true;
                                        }
                                        if (badgeFlag) {
                                                break;
                                        }

                                }

                                JsonNode outputItem = objectMapper.createObjectNode()
                                                .put("badgeName", badgeName)
                                                .put("badgeAltText", imageAltTxt)
                                                .put("badgeImageURL", badgeImageURL);
                                ((com.fasterxml.jackson.databind.node.ArrayNode) outputHighlightsNode).add(outputItem);
                        }

                        JsonNode skuvariationNode = rootNode.path("data").path("attributes")
                                        .path("skuvariationconcentration")
                                        .path("values");
                        String skuvariationconcentration = "";
                        boolean skuvariationFlag = false;
                        for (JsonNode valueNode : skuvariationNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        skuvariationconcentration = valueNode.path("value").asText();
                                        skuvariationFlag = true;
                                }
                                if (skuvariationFlag) {
                                        break;
                                }

                        }

                        JsonNode skuformulationNode = rootNode.path("data").path("attributes")
                                        .path("skuvariationformulation")
                                        .path("values");
                        String skuvariationformulation = "";
                        boolean skuFormulationFlag = false;
                        for (JsonNode valueNode : skuformulationNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        skuvariationformulation = valueNode.path("value").asText();
                                        skuFormulationFlag = true;
                                }
                                if (skuFormulationFlag) {
                                        break;
                                }

                        }

                        String shadedescription = "";
                        JsonNode shadedescriptionNode = rootNode.path("data").path("attributes")
                                        .path("shadedescription")
                                        .path("values");
                        boolean shadeDescFlag = false;
                        for (JsonNode valueNode : shadedescriptionNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        shadedescription = valueNode.path("value").asText();
                                        shadeDescFlag = true;
                                }
                                if (shadeDescFlag) {
                                        break;
                                }

                        }

                        String skuadditionalinfo = "";
                        JsonNode skuadditionalinfoNode = rootNode.path("data").path("attributes")
                                        .path("skuadditionalinfo")
                                        .path("values");
                        boolean infoFlag = false;
                        for (JsonNode valueNode : skuadditionalinfoNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        skuadditionalinfo = valueNode.path("value").asText();
                                        infoFlag = true;
                                }
                                if (infoFlag) {
                                        break;
                                }

                        }

                        JsonNode skuColorNode = rootNode.path("data").path("attributes")
                                        .path("skuvariationcolor")
                                        .path("values");
                        String skuvariationcolor = "";
                        boolean skuColorFlag = false;
                        for (JsonNode valueNode : skuColorNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        skuvariationcolor = valueNode.path("value").asText();
                                        skuColorFlag = true;
                                }
                                if (skuColorFlag) {
                                        break;
                                }

                        }

                        JsonNode skuvariationscentNode = rootNode.path("data").path("attributes")
                                        .path("skuvariationscent")
                                        .path("values");
                        String skuvariationscent = "";
                        boolean skuScentFlag = false;
                        for (JsonNode valueNode : skuvariationscentNode) {
                                String locale = valueNode.path("locale").asText();
                                if (locale.equalsIgnoreCase("en-US")) {
                                        skuvariationscent = valueNode.path("value").asText();
                                        skuScentFlag = true;
                                }
                                if (skuScentFlag) {
                                        break;
                                }

                        }

                        // images
                        JsonNode hasImagesNode = rootNode.path("data").path("relationships").path("hasimages");
                        String imgUrl = "";
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

                                if (imageNode.path("attributes").path("isprimary") != null) {

                                        boolean isPrimary = imageNode.path("attributes").path("isprimary")
                                                        .path("values")
                                                        .path(0).path("value").asBoolean();
                                        if (isPrimary) {
                                                imgUrl = imageNode.path("relTo").path("data")
                                                                .path("attributes").path("property_originalfilename")
                                                                .path("values").path(0).path("value").asText();
                                        }
                                }
                        }

                        for (Map.Entry<Integer, String> entry : seqMap.entrySet()) {

                                String orgFileName = entry.getValue();

                                for (String imageType : new String[] { "thumb", "hero", "Lhero", "zoom" }) {
                                        String imageURL = url + "/productimages/sku/" + orgFileName + "?imwidth=";
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

                        // ageRange
                        JsonNode agerange = rootNode.path("data").path("attributes").path("agerange")
                                        .path("values");
                        JsonNode agerangeArr = objectMapper.createArrayNode();
                        for (JsonNode ageNode : agerange) {
                                String ageValue = ageNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) agerangeArr)
                                                .add(ageValue);

                        }

                        // restricted country
                        JsonNode availCountry = rootNode.path("data").path("attributes").path("availablecountries")
                                        .path("values");
                        JsonNode restrictedCountry = objectMapper.createArrayNode();
                        boolean containsUS = false;
                        boolean containsCanada = false;
                        for (JsonNode valueNode : availCountry) {
                                String value = valueNode.path("value").asText();
                                if (value.equalsIgnoreCase("US")) {
                                        containsUS = true;
                                }
                                if (value.equalsIgnoreCase("Canada")) {
                                        containsCanada = true;
                                }
                        }
                        if (containsUS && containsCanada) {
                        } else if (containsUS) {
                                ((com.fasterxml.jackson.databind.node.ArrayNode) restrictedCountry).add("CANADA");
                        } else if (containsCanada) {
                                ((com.fasterxml.jackson.databind.node.ArrayNode) restrictedCountry)
                                                .add("UNITED STATES");
                        }

                        // bestforskintypes
                        JsonNode bestforskintypes = rootNode.path("data").path("attributes").path("bestforskintypes")
                                        .path("values");
                        JsonNode bestforskintypesArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : bestforskintypes) {
                                String skintypes = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) bestforskintypesArr).add(skintypes);
                        }

                        // benefits
                        JsonNode benefits = rootNode.path("data").path("attributes").path("benefits")
                                        .path("values");
                        JsonNode benefitsArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : benefits) {
                                String benefitStr = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) benefitsArr).add(benefitStr);
                        }

                        // shoppingpreferences
                        JsonNode shoppingpreferences = rootNode.path("data").path("attributes")
                                        .path("shoppingpreferences")
                                        .path("values");
                        JsonNode shoppingpreferArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : shoppingpreferences) {
                                String preferenceStr = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) shoppingpreferArr).add(preferenceStr);
                        }

                        // fragrancefamily
                        JsonNode fragrancefamily = rootNode.path("data").path("attributes").path("fragrancefamily")
                                        .path("values");
                        JsonNode fragrancefamilyArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : fragrancefamily) {
                                String fragFamily = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) fragrancefamilyArr).add(fragFamily);
                        }

                        // isthisproductaminisize
                        JsonNode sizeRefinement = rootNode.path("data").path("attributes")
                                        .path("isthisproductaminisize")
                                        .path("values");
                        JsonNode sizeRefinementArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : sizeRefinement) {
                                String sizeFamily = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) sizeRefinementArr).add(sizeFamily);
                        }

                        // colorfamily
                        JsonNode colorfamily = rootNode.path("data").path("attributes").path("colorfamily")
                                        .path("values");
                        JsonNode colorfamilyArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : colorfamily) {
                                String colrFamily = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) colorfamilyArr).add(colrFamily);
                        }

                        // pantoneskintoneidsecondary
                        JsonNode pantoneskintoneidsecondary = rootNode.path("data").path("attributes")
                                        .path("pantoneskintoneidsecondary")
                                        .path("values");
                        JsonNode pantoneskintoneidsecondaryArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : pantoneskintoneidsecondary) {
                                String pantoneskintoneid = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) pantoneskintoneidsecondaryArr)
                                                .add(pantoneskintoneid);
                        }

                        // bristlelashtype
                        JsonNode bristlelashtype = rootNode.path("data").path("attributes").path("bristlelashtype")
                                        .path("values");
                        JsonNode bristlelashtypeArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : bristlelashtype) {
                                String bristletype = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) bristlelashtypeArr).add(bristletype);
                        }

                        // formulationmakeupcomplexion
                        JsonNode formulationmakeupcomplexion = rootNode.path("data").path("attributes")
                                        .path("formulationmakeupcomplexion")
                                        .path("values");
                        JsonNode formulaArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : formulationmakeupcomplexion) {
                                String complexion = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) formulaArr).add(complexion);
                        }

                        // formulationmakeupcomplexion
                        JsonNode alternateProductIdsNode = rootNode.path("data").path("attributes")
                                        .path("associatedproducts")
                                        .path("values");
                        JsonNode alternateProductIds = objectMapper.createArrayNode();
                        for (JsonNode valueNode : alternateProductIdsNode) {
                                String prodId = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) alternateProductIds).add(prodId);
                        }

                        // hairtype
                        JsonNode hairTypeNode = rootNode.path("data").path("attributes")
                                        .path("hairtype")
                                        .path("values");
                        JsonNode hairtypeArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : hairTypeNode) {
                                String hairType = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) hairtypeArr).add(hairType);
                        }

                        // hairtexturedhg
                        JsonNode hairTextureNode = rootNode.path("data").path("attributes")
                                        .path("hairtexturedhg")
                                        .path("values");
                        JsonNode hairTextureArr = objectMapper.createArrayNode();
                        for (JsonNode valueNode : hairTextureNode) {
                                String hairTexture = valueNode.path("value").asText();
                                ((com.fasterxml.jackson.databind.node.ArrayNode) hairTextureArr).add(hairTexture);
                        }

                        // vendor pre release
                        /*
                         * JsonNode preReleaseVendors = objectMapper.createArrayNode();
                         * if (JsonPathEval(jsonDocument, "$.data.attributes.vendorprerelase")) {
                         * 
                         * JsonNode vendorPreRelNode = rootNode.path("data").path("attributes")
                         * .path("vendorprerelase").path("group").path(0)
                         * .path("prereleasevendorname").path("values");
                         * 
                         * String preReleaseDate =
                         * rootNode.path("data").path("attributes").path("vendorprerelase")
                         * .path("group").path(0).path("vendorprerelasedate").path("values")
                         * .path(0).path("value").asText();
                         * 
                         * for (JsonNode valueNode : vendorPreRelNode) {
                         * String vendorName = valueNode.path("value").asText();
                         * JsonNode preReleaseVendorData = objectMapper.createObjectNode()
                         * .put("vendorName", vendorName)
                         * .put("preReleaseDate", preReleaseDate);
                         * ((com.fasterxml.jackson.databind.node.ArrayNode) preReleaseVendors)
                         * .add(preReleaseVendorData);
                         * 
                         * }
                         * }
                         */

                        JsonNode preReleaseVendors = objectMapper.createArrayNode();
                        if (JsonPathEval(jsonDocument, "$.data.attributes.vendorfeedavailability")) {

                                JsonNode vendorPreRelNode = rootNode.path("data").path("attributes")
                                                .path("vendorfeedavailability").path("group");

                                for (JsonNode valueNode : vendorPreRelNode) {
                                        String vendorName = valueNode.path("vendorname").path("values")
                                                        .path(0).path("value").asText();
                                        String preReleaseDate = "";
                                        String feedexclusionstartdate = "";
                                        String feedexclusionenddate = "";
                                        if (valueNode.path("vendorprerelasedate") != null) {
                                                preReleaseDate = valueNode.path("vendorprerelasedate").path("values")
                                                                .path(0).path("value").asText();
                                        }

                                        if (valueNode.path("feedexclusionstartdate") != null) {
                                                feedexclusionstartdate = valueNode.path("feedexclusionstartdate")
                                                                .path("values")
                                                                .path(0).path("value").asText();
                                        }

                                        if (valueNode.path("feedexclusionenddate") != null) {
                                                feedexclusionenddate = valueNode.path("feedexclusionenddate")
                                                                .path("values")
                                                                .path(0).path("value").asText();
                                        }

                                        JsonNode preReleaseVendorData = objectMapper.createObjectNode()
                                                        .put("vendorName", vendorName)
                                                        .put("preReleaseDate", preReleaseDate)
                                                        .put("feedExclusionStartSate", feedexclusionstartdate)
                                                        .put("feedExclusionEndDate", feedexclusionenddate);
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) preReleaseVendors)
                                                        .add(preReleaseVendorData);

                                }
                        }

                        // feedexclusion
                        JsonNode feedExcludes = objectMapper.createArrayNode();
                        if (JsonPathEval(jsonDocument, "$.data.attributes.feedexclusion")) {
                                JsonNode feedExNode = rootNode.path("data").path("attributes").path("feedexclusion")
                                                .path("group");

                                for (JsonNode excludeNode : feedExNode) {
                                        String vendorName = excludeNode.path("vendorname").path("values").path(0)
                                                        .path("value").asText();
                                        String feedExclusionStartDate = excludeNode.path("feedexclusionstartdate")
                                                        .path("values").path(0).path("value").asText();
                                        String feedExclusionEndDate = excludeNode.path("feedexclusionenddate")
                                                        .path("values").path(0).path("value").asText();

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

                        // shadename
                        String shadename = "";
                        if (rootNode.path("data").path("attributes").path("shadename")
                                        .path("values").path(0).path("value") != null) {

                                shadename = rootNode.path("data").path("attributes").path("shadename")
                                                .path("values").path(0).path("value").asText();
                        }

                        String size = fillsizeoz + " / " + fillsizemlg;
                        String urlExtn = "";
                        if (!badgeStr.isEmpty()) {
                                urlExtn = "&pb=" + badgeStr;// "&pb=2020-03-sephora-clean-2019";
                        }

                        JsonNode ingredientsPreferences = objectMapper.createArrayNode();
                        if (JsonPathEval(jsonDocument, "$.data.attributes.ingredientpreferences")) {
                                JsonNode ingredientsPreferencesNode = rootNode.path("data").path("attributes")
                                                .path("ingredientpreferences")
                                                .path("values");

                                for (JsonNode ingreNode : ingredientsPreferencesNode) {
                                        String ingredient = ingreNode.path("value").asText();
                                        ((com.fasterxml.jackson.databind.node.ArrayNode) ingredientsPreferences)
                                                        .add(ingredient);

                                }

                        }

                        skuNode.put("skuId", enterpriseskuid);
                        skuNode.put("name", productname);
                        // skuNode.put("isDefaultSku", isDefaultSku);
                        skuNode.put("isProductPrimaryForSku", "");// toDO
                        skuNode.put("primaryProductId", primaryproduct);
                        skuNode.put("biType", biType);
                        skuNode.set("alternateProductIds", alternateProductIds);// toDO-check other skus
                        skuNode.put("skuClass", skutype);
                        skuNode.put("description", skuadditionalinfo);
                        skuNode.put("descriptionNonHTML", skuadditionalinfo);
                        skuNode.put("upc", upcTrimmed);
                        skuNode.put("countryOfManufacture", countryofmanufacture);
                        skuNode.set("feedExcludes", feedExcludes);
                        skuNode.put("active", String.valueOf(isactive));
                        skuNode.put("comingSoon", String.valueOf(comingsoon));
                        skuNode.put("comingSoonEndDate", formatDate(comingsoonenddate));
                        skuNode.put("startDate", formatDate(uslivedate));
                        skuNode.put("endDate", formatDate(usenddate));
                        skuNode.put("expireDateOfNew", formatDate(newuntildate));
                        skuNode.put("newTrait", String.valueOf(isthisproductnewtosephora));
                        skuNode.put("maxQuantityAllowed", String.valueOf(maxpurchaseqtyperorder));
                        // skuNode.put("inStock", String.valueOf(isinventoryavailable));
                        skuNode.put("outOfStockTreatment", outofstocktreatment);
                        skuNode.put("excludeFromSiteSearch", String.valueOf(excludefromsearch));
                        skuNode.put("reservable", String.valueOf(reservationsenabled));
                        skuNode.put("csrOnly", String.valueOf(callcenteronly));
                        skuNode.put("storeOnly", String.valueOf(isstoreonly));
                        skuNode.put("onlineOnly", String.valueOf(isthisproductonlineonly));
                        skuNode.put("biOnly", String.valueOf(isbiexclusive));
                        skuNode.put("biExclusivityLevel", biexclusivelevel);
                        skuNode.put("giftWrappable", String.valueOf(giftwrappable));
                        skuNode.set("finishRefinement", finishArr);
                        skuNode.set("coverageRefinement", coverageArr);
                        // skuNode.set("formulationRefinement", formulaArr);
                        skuNode.put("ingredients", fullingredientslistpublished);
                        skuNode.put("ingredientsNonHTML", fullingredientslistpublished);
                        skuNode.set("ingredientsPreferences", ingredientsPreferences);// todo
                        skuNode.put("hazmat", mcshazmatvalue);
                        skuNode.put("size", size);
                        // skuNode.put("variationType", valuesetdescription);
                        skuNode.put("variationValue", shadename);
                        skuNode.put("isLimitedTimeOffer", String.valueOf(limitedtimeoffer));
                        skuNode.put("isFirstAccessOffer", String.valueOf(firstaccess));
                        skuNode.put("firstAccessOfferRestrCountry", firstaccessrestrictedcountry);
                        skuNode.put("firstAccessOfferStartDate", formatDate(firstaccessstartdate));
                        skuNode.put("firstAccessOfferEndDate", formatDate(firstaccessenddate));
                        skuNode.put("isAppExclusiveOffer", String.valueOf(appexclusive));
                        skuNode.put("appExclusiveOfferRestrCountry", appexclusiverestrictedcountry);
                        skuNode.put("appExclusiveOfferStartDate", formatDate(appexclusivestartdate));
                        skuNode.put("appExclusiveOfferEndDate", formatDate(appexclusiveenddate));
                        skuNode.put("exclusive", String.valueOf(productisexclusivetosephora));
                        skuNode.put("exclusiveEndDate", formatDate(onlyatsephoraenddate));
                        skuNode.put("limitedEdition", String.valueOf(isthisproductlimitededition));
                        // skuNode.put("badgeAltText", badges);
                        skuNode.put("productLine", productline);
                        skuNode.set("highlights", outputHighlightsNode);
                        skuNode.set("alternateImages", alternateImagesNode);
                        skuNode.put("department", departmentnumber);
                        skuNode.put("classNumber", classnumber);
                        skuNode.put("subclass", subclassnumber);
                        skuNode.set("restrictedCountry", restrictedCountry);
                        skuNode.put("imageUpdated", imageUpdated);
                        skuNode.put("imageLastUpdated", formatDate(publishedDateString));
                        skuNode.put("heroImageURL", url + "/productimages/sku/" + imgUrl + "?imwidth=270" + urlExtn);
                        skuNode.put("heroImageNoOverlayURL", url + "/productimages/sku/" + imgUrl + "?imwidth=270");
                        skuNode.put("largeHeroImageURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=450" + urlExtn);
                        skuNode.put("largeHeroImageNoOverlayURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=450");
                        skuNode.put("gridImageURL", url + "/productimages/sku/" + imgUrl + "?imwidth=135" + urlExtn);
                        skuNode.put("gridImageNoOverlayURL", url + "/productimages/sku/" + imgUrl + "?imwidth=135");
                        skuNode.put("smallGridImageURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=135" + urlExtn);
                        skuNode.put("smallGridImageNoOverlayURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=135");
                        skuNode.put("zoomImageURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=2000" + urlExtn);
                        skuNode.put("zoomImageNoOverlayURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=2000");
                        skuNode.put("largeThumbnailImageURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=62" + urlExtn);
                        skuNode.put("largeThumbnailImageNoOverlayURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=62");
                        skuNode.put("thumbnailImageURL",
                                        url + "/productimages/sku/" + imgUrl + "?imwidth=62" + urlExtn);
                        skuNode.put("thumbnailImageNoOverlayURL", url + "/productimages/sku/" + imgUrl + "?imwidth=62");
                        skuNode.set("wholeSalePrice", wholesalePrice);
                        skuNode.put("concentration", skuvariationconcentration);
                        skuNode.put("formulation", skuvariationformulation);
                        skuNode.put("colorName", skuvariationcolor);
                        skuNode.put("scent", skuvariationscent);
                        skuNode.put("size", skuvariationsize);
                        skuNode.put("limitedTimeOfferRestrCountry", limitedtimeofferrestrictedcountry);
                        skuNode.put("limitedTimeOfferStartDate", formatDate(limitedtimeofferstartdate));
                        skuNode.put("limitedTimeOfferEndDate", formatDate(limitedtimeofferenddate));
                        skuNode.set("preReleaseVendors", preReleaseVendors);
                        skuNode.put("primaryConcern", primaryconcern);
                        skuNode.set("shoppingPreference", shoppingpreferArr);
                        skuNode.set("fragranceImpr", fragranceImpr);
                        skuNode.set("fragranceType", fragrancetypeNode);
                        skuNode.set("cosmeticType", nailtypeNode);
                        skuNode.set("cosmeticVolume", falseeyelashvolumeNode);
                        skuNode.put("primarySkinTone", pantoneskintoneidprimary);
                        skuNode.set("hairType", hairtypeArr);
                        skuNode.put("efficacyValue", efficacy);
                        skuNode.put("efficacyIngredient", efficacyingredient);
                        skuNode.put("fullSizeSkuId", fullsizesku);
                        skuNode.put("variationType", variationtype);
                        skuNode.set("benefitsRefinement", benefitsArr);
                        skuNode.set("fragranceFamilyRefinement", fragrancefamilyArr);
                        skuNode.set("colorFamily", colorfamilyArr);
                        skuNode.set("skinType", bestforskintypesArr);
                        skuNode.set("secondarySkinTone", pantoneskintoneidsecondaryArr);
                        skuNode.set("bristleType", bristlelashtypeArr);
                        skuNode.put("sephoraType", skuvariationtype);
                        skuNode.put("biDescription", bidescription);
                        skuNode.put("colorNumber", colornumber);
                        skuNode.put("color", skuvariationcolor);
                        // skuNode.set("badgeName", realtedBadgesNode);
                        skuNode.put("badgeName", badgeStr);
                        skuNode.put("badgeAltText", badgeAltText);
                        skuNode.put("badgeImageURL", badgeImgUrl);
                        skuNode.set("ageRange", agerangeArr);
                        skuNode.put("lastModifiedOn", formatDate(lastModifiedOn));
                        skuNode.set("hairTexture", hairTextureArr);
                        skuNode.set("formulationRefinement", formulationRefinement);
                        skuNode.set("concernsRefinement", concernsRefinement);
                        skuNode.put("efficacyApplication", efficacyApplication);
                        skuNode.put("labValue", labValue);
                        skuNode.set("sunProtections", sunProtectionsNode);
                        skuNode.put("heroImageAltText", heroimagealttext);
                        skuNode.put("nonreturnable", String.valueOf(nonreturnable));
                        skuNode.set("sizeRefinement", sizeRefinementArr);

                } catch (

                Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return skuNode;
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

        public static String removeLeadingZeros(String input) {

                String processedInput = input.replaceFirst("^0+", "");
                int minLength = 12;
                StringBuilder stringBuilder = new StringBuilder();

                // Check if the length is less than the desired length
                if (processedInput.length() < minLength) {
                        // Calculate the number of zeros to add
                        int zerosToAdd = minLength - processedInput.length();

                        // Add the necessary number of leading zeros
                        for (int i = 0; i < zerosToAdd; i++) {
                                stringBuilder.append('0');
                        }

                        // Append the processed input
                        stringBuilder.append(processedInput);

                        // Set the processed input as the result
                        processedInput = stringBuilder.toString();

                }
                stringBuilder.delete(0, stringBuilder.length());
                return processedInput;
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
