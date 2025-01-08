package majharulsoft.com.weatherapp.cilent;

import com.google.gson.Gson;

import majharulsoft.com.weatherapp.Api.WeatherApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


   // private static final String BASE_URL = "https://www.meteosource.com/api/";
    private static final String BASE_URL = "https://api.openweathermap.org/";

    private static RetrofitClient instance;
    private Retrofit retrofit;

    private RetrofitClient() {
        Gson gson = new Gson();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    public static RetrofitClient getInstance() {

        if (instance == null) {
            instance = new RetrofitClient();
        }

        return instance;
    }

    public WeatherApiService getApi() {

        return retrofit.create(WeatherApiService.class);
    }








}
