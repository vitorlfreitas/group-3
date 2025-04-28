package com.tripper.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * WeatherResponse is a class that represents the response from a weather API.
 * It contains information about the weather forecast, including the city, list of forecasts,
 * and other related data.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 * 
 * @see <a href="https://openweathermap.org/api">OpenWeatherMap API</a>
 */
@Setter
@Getter
public class WeatherResponse {

    private String cod;
    private int cnt;
    private List<Forecast> list;
    private City city;

    @Setter
    @Getter
    public static class Forecast {
        private long dt;
        private MainData main;
        private List<WeatherInfo> weather;
        private Clouds clouds;
        private Wind wind;
        private String dt_txt;

    }

    @Setter
    @Getter
    public static class MainData {

        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;

    }

    @Setter
    @Getter
    public static class WeatherInfo {

        private int id;
        private String main;
        private String description;
        private String icon;

    }

    @Setter
    @Getter
    public static class Clouds {

        private int all;

    }

    @Setter
    @Getter
    public static class Wind {

        private double speed;
        private int deg;

    }

    @Setter
    @Getter
    public static class City {

        private String name;
        private Coord coord;

    }

    @Setter
    @Getter
    public static class Coord {

        private double lat;
        private double lon;

    }
}
