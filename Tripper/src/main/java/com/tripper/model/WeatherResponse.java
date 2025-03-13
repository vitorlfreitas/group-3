package main.java.com.tripper.model;

import java.util.List;

/**
 * This class represents a response from the OpenWeatherMap API
 */
public class WeatherResponse {
    private String cod;
    private int cnt;
    private List<Forecast> list;
    private City city;

    // Getters and setters
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<Forecast> getList() {
        return list;
    }

    public void setList(List<Forecast> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public static class Forecast {
        private long dt;
        private MainData main;
        private List<WeatherInfo> weather;
        private Clouds clouds;
        private Wind wind;
        private String dt_txt;

        // Getters and setters

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public MainData getMain() {
            return main;
        }

        public void setMain(MainData main) {
            this.main = main;
        }

        public List<WeatherInfo> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherInfo> weather) {
            this.weather = weather;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }
    }

    public static class MainData {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;

        // Getters and setters
        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }

    public static class WeatherInfo {
        private int id;
        private String main;
        private String description;
        private String icon;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class Clouds {

        private int all;

        // Getters and setters
        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }
    }

    public static class Wind {

        private double speed;
        private int deg;

        // Getters and setters
        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public int getDeg() {
            return deg;
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }
    }

    public static class City {

        private String name;
        private Coord coord;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coord getCoord() {
            return coord;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }
    }

    public static class Coord {

        private double lat;
        private double lon;

        // Getters and setters

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}
