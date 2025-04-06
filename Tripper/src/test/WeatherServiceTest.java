package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTest {

    @Test
    void testParseWeatherJson() {
        String json = """
        {
          "name": "Dublin",
          "main": {
            "temp": 12.34,
            "feels_like": 10.5,
            "humidity": 80
          },
          "weather": [
            {
              "description": "light rain",
              "icon": "10d"
            }
          ]
        }
        """;

        Gson gson = new Gson();
        WeatherResponse response = gson.fromJson(json, WeatherResponse.class);

        assertEquals("Dublin", response.getName());
        assertEquals(12.34, response.getMain().getTemp());
        assertEquals("light rain", response.getWeather().get(0).getDescription());
    }
}
