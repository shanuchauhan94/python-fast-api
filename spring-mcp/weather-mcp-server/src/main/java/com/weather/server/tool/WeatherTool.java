package com.weather.server.tool;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
public class WeatherTool {

    @McpTool(name = "getWeather", description = "Get the current weather for a city")
    public String getWeather(@McpToolParam(description = "City name, for example Delhi or London") String city) {

        System.out.println("================WeatherTool getWeather ==========================");

        return String.format(
                "Current weather in %s: Temperature 32°C, feels like 34°C, " +
                "humidity 65%%, wind speed 12 km/h, condition Partly Cloudy.",
                city
        );
    }

    @McpTool(name = "getForecast", description = "Get the weather forecast for a city")
    public String getForecast(@McpToolParam(description = "City name, for example Delhi or London") String city) {

        System.out.println("================WeatherTool getForecast ==========================");

        return String.format(
                "5-day weather forecast for %s: " +
                "Today 32°C Partly Cloudy, " +
                "Tomorrow 34°C Sunny, " +
                "Day 3 33°C Cloudy, " +
                "Day 4 31°C Rainy, " +
                "Day 5 30°C Partly Cloudy.",
                city
        );
    }
}