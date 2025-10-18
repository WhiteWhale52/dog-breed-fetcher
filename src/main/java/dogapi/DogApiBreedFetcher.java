package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    final String API_URL = "https://dog.ceo/api";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    private static final String STATUS_CODE = "status_code";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.
        String url = String.format("%s/breed/%s/list", API_URL, breed.toLowerCase());

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response code: " + response.code());
            System.out.println("Response message: " + response.message());
            System.out.println("Is successful: " + response.isSuccessful());
            System.out.println("Body: " + response.body());

            if (response.body() == null) {
                throw new BreedNotFoundException(breed);
            }

            JSONObject responseBody = new JSONObject(response.body().string());

            // Check for success
            if (!responseBody.optString("status").equals("success")) {
                throw new BreedNotFoundException(breed);
            }

            JSONArray subBreeds = responseBody.getJSONArray("message");

            List<String> subBreedList = new ArrayList<>();
            for (int i = 0; i < subBreeds.length(); i++) {
                subBreedList.add(subBreeds.getString(i));
            }

            return subBreedList;

        }
        catch (IOException | BreedNotFoundException e) {
            throw new BreedNotFoundException(breed);
        }
    }

}
