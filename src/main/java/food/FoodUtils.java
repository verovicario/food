package food;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vvicario
 */
public class FoodUtils {

    public static final String FILE_PATH = "/home/vvicario/Documents/projects/ids/datasetFinal.csv";

    public static final String FINAL_CSV_PATH = "/home/vvicario/Documents/projects/ids/FoodDataset.csv";

    public static final String FATSECRET_FOODS_GET_V2_URL = "https://platform.fatsecret.com/rest/server.api?format=json&method=food.get.v2&food_id=";

    public static final String FATSECRET_FOODS_SEARCH_URL = "https://platform.fatsecret.com/rest/server.api?format=json&method=foods.search&&max_results=50";

    public static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ1MjZBMkFCNkQ0MkQ5REIwMjBEMThBRDMxRTE5MTdCMUUzMjg2RTUiLCJ0eXAiOiJhdCtqd3QiLCJ4NXQiOiJSU2FpcTIxQzJkc0NEUml0TWVHUmV4NHlodVUifQ.eyJuYmYiOjE1OTY3Mjg1NjksImV4cCI6MTU5NjgxNDk2OSwiaXNzIjoiaHR0cHM6Ly9vYXV0aC5mYXRzZWNyZXQuY29tIiwiYXVkIjoiYmFzaWMiLCJjbGllbnRfaWQiOiI1ZTYyMWEwNGQ0ODU0N2ZlOWVkMDJmYzI3MjI1ZThhOCIsInNjb3BlIjpbImJhc2ljIl19.atfxwNgnkqX4KmEja-BhBj8IlQJA2NWLVgBv5i5_8Yj6wmveb0km-Z7wLCAt9bBkSrXOiaPGhsIqNmkTYk6wkxSggHlbZBFaU_HbSBHgeR1AnGx4aqLi89Czdr5Nu-Lys_mN9lymYSC3PeuuxESa-3y_3kDvIeJoj0z0m2FRYgxnd9J36g7Us_gNWaldXU3G26dRFdVi7cY93vxgY2jyi430Nl_uyyOyUnaC1lwoc2a473C-bU5c65LXTYi0XQTTC8mQXAtxW65Cbbei1kEMifUH0v3bBYWgueDSKJFJc6dYi6PvUwhaGEGtajgBGDUQae_KBPyXnQ7CRdNpy6Gy_g";

    public static final String[] CATEGORIES = {"fish", "meats", "sauces", "spices", "cream","salads", "soups", "cheese", "milk", "dairy", "candies", "sweets", "desserts", "fast", "drinks","vegetables", "fruits","pasta"};

    public static final String AUTHORIZATION = "Authorization";

    public static final String CATEGORY = "category";

    public static final String FOOD_ID = "food_id";

    public static final String FOOD_NAME = "food_name";

    public static final String CALCIUM = "calcium";

    public static final String CALORIES = "calories";

    public static final String CARBOHYDRATE = "carbohydrate";

    public static final String CHOLESTEROL = "cholesterol";

    public static final String FAT = "fat";

    public static final String FIBER = "fiber";

    public static final String IRON = "iron";

    public static final String MEASUREMENT_DESCRIPTION = "measurement_description";

    public static final String METRIC_SERVING_AMOUNT = "metric_serving_amount";

    public static final String METRIC_SERVING_UNIT = "metric_serving_unit";

    public static final String MONOUNSATURATED_FAT = "monounsaturated_fat";

    public static final String NUMBER_OF_UNITS = "number_of_units";

    public static final String POLYUNSATURATED_FAT = "polyunsaturated_fat";

    public static final String POTASSIUM = "potassium";

    public static final String PROTEIN = "protein";

    public static final String SATURATED_FAT = "saturated_fat";

    public static final String SERVING_DESCRIPTION = "serving_description";

    public static final String SERVING_ID = "serving_id";

    public static final String SERVING_URL = "serving_url";

    public static final String SODIUM = "sodium";

    public static final String SUGAR = "sugar";

    public static final String VITAMIN_A = "vitamin_a";

    public static final String VITAMIN_C = "vitamin_c";

    public static final String[] HEADERS = {
            CATEGORY,
            FOOD_ID,
            FOOD_NAME,
            CALCIUM,
            CALORIES,
            CARBOHYDRATE,
            CHOLESTEROL,
            FAT,
            FIBER,
            IRON,
            MEASUREMENT_DESCRIPTION,
            METRIC_SERVING_AMOUNT,
            METRIC_SERVING_UNIT,
            MONOUNSATURATED_FAT,
            NUMBER_OF_UNITS,
            POLYUNSATURATED_FAT,
            POTASSIUM,
            PROTEIN,
            SATURATED_FAT,
            SERVING_DESCRIPTION,
            SERVING_ID,
            SERVING_URL,
            SODIUM,
            SUGAR,
            VITAMIN_A,
            VITAMIN_C
    };

    /**
     * Filter by serving -> 100 g
     *
     * @param servingDetails
     * @param food
     * @return A list with all the serving details
     */
    public static String[] createFoodLine(JsonElement servingDetails, JsonObject food, String category) {
        if (servingDetails.getAsJsonObject().get("serving_description").toString().contains("100 g")) {
            JsonObject serving = servingDetails.getAsJsonObject();
            return new String[]{
                    category,
                    getValue(food.get(FOOD_ID)),
                    getValue(food.get(FOOD_NAME)),
                    getValue(serving.get(CALCIUM)),
                    getValue(serving.get(CALORIES)),
                    getValue(serving.get(CARBOHYDRATE)),
                    getValue(serving.get(CHOLESTEROL)),
                    getValue(serving.get(FAT)),
                    getValue(serving.get(FIBER)),
                    getValue(serving.get(IRON)),
                    getValue(serving.get(MEASUREMENT_DESCRIPTION)),
                    getValue(serving.get(METRIC_SERVING_AMOUNT)),
                    getValue(serving.get(METRIC_SERVING_UNIT)),
                    getValue(serving.get(MONOUNSATURATED_FAT)),
                    getValue(serving.get(NUMBER_OF_UNITS)),
                    getValue(serving.get(POLYUNSATURATED_FAT)),
                    getValue(serving.get(POTASSIUM)),
                    getValue(serving.get(PROTEIN)),
                    getValue(serving.get(SATURATED_FAT)),
                    getValue(serving.get(SERVING_DESCRIPTION)),
                    getValue(serving.get(SERVING_ID)),
                    getValue(serving.get(SERVING_URL)),
                    getValue(serving.get(SODIUM)),
                    getValue(serving.get(SUGAR)),
                    getValue(serving.get(VITAMIN_A)),
                    getValue(serving.get(VITAMIN_C))
            };
        }
        return null;
    }

    /**
     * Create a final CSV with no duplicated information
     */
    public static void main(String[] args) throws Exception  {
        CSVWriter writer = new CSVWriter(new FileWriter(FoodUtils.FINAL_CSV_PATH));
        BufferedReader csvReader = new BufferedReader(new FileReader(FILE_PATH));
        Set<String> ids = new HashSet<>();
        String row = "";
        writer.writeNext(HEADERS);
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            String id = data[1].replace("\"","");
            if(!ids.contains(id)) {
                ids.add(id);
                writer.writeNext(data);
            }
        }
        writer.flush();
        writer.close();
        csvReader.close();
    }

    /**
     * check if value is not null and remove double quotes
     */
    private static String getValue(JsonElement value) {
        return value != null && StringUtils.isNotEmpty(value.toString()) ? value.toString().replace("\"", "") : "";
    }

}
