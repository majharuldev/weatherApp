package majharulsoft.com.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import majharulsoft.com.weatherapp.cilent.RetrofitClient;
import majharulsoft.com.weatherapp.model.weatheresponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    EditText cityInput;
    Button search;
    TextView show;

    String cityName;
    private static final String API_KEY = "ae294e6708dd9d0cbb68482a0095f3b6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        show = findViewById(R.id.weather);
        cityInput = findViewById(R.id.cityName);
        search = findViewById(R.id.search);



        SharedPreferences sharedPreferences = getSharedPreferences("WeatherApp", MODE_PRIVATE);
        String lastSearchedCity = sharedPreferences.getString("last_searched_city", "");

        // If there is a city saved, set it in the EditText
        if (!lastSearchedCity.isEmpty()) {
            cityInput.setText(lastSearchedCity);
            fetchWeatherData(lastSearchedCity); // Optionally fetch weather for the last searched city
        }

        // Click Listener for Search Button
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = cityInput.getText().toString().trim(); // Get the input text
                if (!cityName.isEmpty()) {
                    // Save the last searched city to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("last_searched_city", cityName);
                    editor.apply();  // Commit changes

                    fetchWeatherData(cityName); // Fetch weather data for the entered city
                } else {
                    show.setText("Please enter a city name."); // Display error message
                }
            }
        });
    }



    private void fetchWeatherData(String cityName) {
        RetrofitClient.getInstance().getApi().getWeatherData(cityName, API_KEY).enqueue(new Callback<weatheresponse>() {
            @Override
            public void onResponse(Call<weatheresponse> call, Response<weatheresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    weatheresponse weatherResponse = response.body();
                    Log.d("Weather", "City: " + weatherResponse.getName());
                    Log.d("Weather", "Temperature: " + weatherResponse.getMain().getTemp() + " K");

                    float temp = (float) (weatherResponse.getMain().getTemp() - 273.15);
                    String humi = String.valueOf(weatherResponse.getMain().getHumidity());
                    String feel = String.valueOf(weatherResponse.getMain().getFeelsLike() - 273.15); // Convert Kelvin to Celsius
                    String wind = String.valueOf(weatherResponse.getWind().getSpeed());

                    String weatherInfo = "City: " + weatherResponse.getName() + "\n" +
                            "Temperature: " + temp + "°C\n" +
                            "Humidity: " + humi + "%\n" +
                            "Feels Like: " + feel + "°C\n" +
                            "Wind Speed: " + wind + " m/s";

                    // Set the formatted text to the TextView
                    show.setText(weatherInfo);

                } else {
                    Log.e("Weather", "Response failed: " + response.message());
                    show.setText("City not found. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<weatheresponse> call, Throwable t) {
                Log.e("Weather", "API Call failed: " + t.getMessage());
                show.setText("Failed to fetch data. Please check your connection.");
            }
        });


    }

}