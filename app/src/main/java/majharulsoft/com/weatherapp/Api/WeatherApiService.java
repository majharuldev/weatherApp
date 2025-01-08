package majharulsoft.com.weatherapp.Api;

import majharulsoft.com.weatherapp.model.weatheresponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {


    @GET("data/2.5/weather")
    Call<weatheresponse> getWeatherData(
            @Query("q") String cityName,
            @Query("appid") String apiKey

    );
}
