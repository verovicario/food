package food;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author vvicario
 */
public class Food {

    private static final Logger logger = Logger.getLogger(Food.class.getName());

    /**
     * Get ids for the specified food categories, retrieve food details
     * and create CSV with all this information
     */
    public static void main(String[] args) throws Exception {
        HashMap<String, Set<String>> foodIds = getGeneralInfo();
        getFoodDetails(foodIds);
    }

    /**
     * Iterate per each food id and get details to create the csv file
     */
    private static void getFoodDetails(HashMap<String, Set<String>> foodMap) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(FoodUtils.FILE_PATH));
        Set<String> existentIds = new HashSet<>();
        writer.writeNext(FoodUtils.HEADERS);
        for (String category : foodMap.keySet()) {
            for (String foodId : foodMap.get(category)) {
                if (!existentIds.contains(foodId)) {
                    existentIds.add(foodId);
                    try {
                        URL urlForGetRequest = new URL(FoodUtils.FATSECRET_FOODS_GET_V2_URL + foodId);
                        String readLine;
                        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
                        conection.setRequestMethod("GET");
                        conection.setRequestProperty(FoodUtils.AUTHORIZATION, "Bearer " + FoodUtils.ACCESS_TOKEN);
                        int responseCode = conection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(conection.getInputStream()));
                            StringBuffer response = new StringBuffer();
                            while ((readLine = in.readLine()) != null) {
                                response.append(readLine);
                            }
                            in.close();
                            JsonObject foodsInfo = new Gson().fromJson(response.toString(), JsonObject.class);
                            JsonObject food = (JsonObject) foodsInfo.get("food");
                            JsonArray servings = (JsonArray) food.get("servings").getAsJsonObject().get("serving");
                            for (JsonElement servingDetails : servings) {
                                try {
                                    String[] foodInfo = FoodUtils.createFoodLine(servingDetails, food, category);
                                    if (foodInfo != null) writer.writeNext(foodInfo);
                                } catch (Exception e) {
                                    logger.info("Iteration has failed for food id: " + food.get("food_id") + " Error: " + e.getMessage());
                                }
                            }
                        }

                    } catch (Exception e) {
                        logger.info("Iteration has failed for " + category + " Error: " + e.getMessage());
                    }
                }
            }
        }
        writer.flush();
        writer.close();
    }


    /**
     * Iterate per each food category and get all the food ids
     */
    private static HashMap<String, Set<String>> getGeneralInfo() throws Exception {
        HashMap<String, Set<String>> foodByCategory = new HashMap<>();
        for (String category : FoodUtils.CATEGORIES) {
            try {
                Set<String> foodIds = new HashSet<>();
                JsonObject foodElements = getJsonFoodInfo(0, category);
                if (foodElements != null) {
                    // calculate total pages, API return only 50 registers per page
                    Integer totalPages = foodElements.get("total_results").getAsInt() / 50;
                    Set<String> idsPag0 = getIds(foodElements);
                    if (!idsPag0.isEmpty()) foodIds.addAll(idsPag0);
                    for (int i = 1; i <= totalPages; i++) {
                        foodElements = getJsonFoodInfo(i, category);
                        Set<String> ids = getIds(foodElements);
                        if (!ids.isEmpty()) foodIds.addAll(ids);
                    }
                    foodByCategory.put(category, foodIds);
                }
            } catch (Exception e) {
                logger.info("Error with category: " + category + " Error: " + e.getMessage());
            }

        }
        return foodByCategory;
    }

    /**
     * Retrieve information from JsonObject and generate a list
     * with the food ids
     */
    private static Set<String> getIds(JsonObject foodInf) {
        Set<String> ids = new HashSet<>();
        try {
            JsonArray foodsArray = (JsonArray) foodInf.get("food");
            for (JsonElement food : foodsArray) {
                if (food.getAsJsonObject().get("food_type").toString().contains("Generic")) {
                    ids.add(food.getAsJsonObject().get("food_id").toString().replace("\"", ""));
                }
            }
        } catch (Exception e) {
            logger.info("Error getting ids: " + e.getMessage());
        }
        return ids;
    }

    private static JsonObject getJsonFoodInfo(Integer page, String category) throws Exception {
        URL urlForGetRequest = new URL(FoodUtils.FATSECRET_FOODS_SEARCH_URL + "&page_number=" + page + "&search_expression=" + category);
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty(FoodUtils.AUTHORIZATION, "Bearer " + FoodUtils.ACCESS_TOKEN);
        int responseCode = conection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            JsonObject foodsInfo = new Gson().fromJson(response.toString(), JsonObject.class);
            return (JsonObject) foodsInfo.get("foods");
        }
        return null;
    }

}

