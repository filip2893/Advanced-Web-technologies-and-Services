/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.klijenti;

import java.util.Date;

/**
 *
 * @author fstrahij
 */
public class MeteoPodaci {

    private Date sunRise;
    private Date sunSet;

    private Float temperatureValue;
    private Float temperatureMin;
    private Float temperatureMax;
    private String temperatureUnit;

    private Float humidityValue;
    private String humidityUnit;

    private Float pressureValue;
    private String pressureUnit;

    private Float windSpeedValue;
    private String windSpeedName;
    private Float windDirectionValue;
    private String windDirectionCode;
    private String windDirectionName;

    private int cloudsValue;
    private String cloudsName;

    private String visibility;

    private Float precipitationValue;
    private String precipitationMode;
    private String precipitationUnit;

    private int weatherNumber;
    private String weatherValue;
    private String weatherIcon;
    private Date lastUpdate;

    public MeteoPodaci() {
    }

    public MeteoPodaci(Date sunRise, Date sunSet, Float temperatureValue, Float temperatureMin, Float temperatureMax, String temperatureUnit, Float humidityValue, String humidityUnit, Float pressureValue, String pressureUnit, Float windSpeedValue, String windSpeedName, Float windDirectionValue, String windDirectionCode, String windDirectionName, int cloudsValue, String cloudsName, String visibility, Float precipitationValue, String precipitationMode, String precipitationUnit, int weatherNumber, String weatherValue, String weatherIcon, Date lastUpdate) {
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.temperatureValue = temperatureValue;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.temperatureUnit = temperatureUnit;
        this.humidityValue = humidityValue;
        this.humidityUnit = humidityUnit;
        this.pressureValue = pressureValue;
        this.pressureUnit = pressureUnit;
        this.windSpeedValue = windSpeedValue;
        this.windSpeedName = windSpeedName;
        this.windDirectionValue = windDirectionValue;
        this.windDirectionCode = windDirectionCode;
        this.windDirectionName = windDirectionName;
        this.cloudsValue = cloudsValue;
        this.cloudsName = cloudsName;
        this.visibility = visibility;
        this.precipitationValue = precipitationValue;
        this.precipitationMode = precipitationMode;
        this.precipitationUnit = precipitationUnit;
        this.weatherNumber = weatherNumber;
        this.weatherValue = weatherValue;
        this.weatherIcon = weatherIcon;
        this.lastUpdate = lastUpdate;
    }

    public Date getSunRise() {
        return sunRise;
    }

    public void setSunRise(Date sunRise) {
        this.sunRise = sunRise;
    }

    public Date getSunSet() {
        return sunSet;
    }

    public void setSunSet(Date sunSet) {
        this.sunSet = sunSet;
    }

    public Float getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(Float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public Float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Float getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public Float getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(Float humidityValue) {
        this.humidityValue = humidityValue;
    }

    public String getHumidityUnit() {
        return humidityUnit;
    }

    public void setHumidityUnit(String humidityUnit) {
        this.humidityUnit = humidityUnit;
    }

    public Float getPressureValue() {
        return pressureValue;
    }

    public void setPressureValue(Float pressureValue) {
        this.pressureValue = pressureValue;
    }

    public String getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(String pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public Float getWindSpeedValue() {
        return windSpeedValue;
    }

    public void setWindSpeedValue(Float windSpeedValue) {
        this.windSpeedValue = windSpeedValue;
    }

    public String getWindSpeedName() {
        return windSpeedName;
    }

    public void setWindSpeedName(String windSpeedName) {
        this.windSpeedName = windSpeedName;
    }

    public Float getWindDirectionValue() {
        return windDirectionValue;
    }

    public void setWindDirectionValue(Float windDirectionValue) {
        this.windDirectionValue = windDirectionValue;
    }

    public String getWindDirectionCode() {
        return windDirectionCode;
    }

    public void setWindDirectionCode(String windDirectionCode) {
        this.windDirectionCode = windDirectionCode;
    }

    public String getWindDirectionName() {
        return windDirectionName;
    }

    public void setWindDirectionName(String windDirectionName) {
        this.windDirectionName = windDirectionName;
    }

    public int getCloudsValue() {
        return cloudsValue;
    }

    public void setCloudsValue(int cloudsValue) {
        this.cloudsValue = cloudsValue;
    }

    public String getCloudsName() {
        return cloudsName;
    }

    public void setCloudsName(String cloudsName) {
        this.cloudsName = cloudsName;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Float getPrecipitationValue() {
        return precipitationValue;
    }

    public void setPrecipitationValue(Float precipitationValue) {
        this.precipitationValue = precipitationValue;
    }

    public String getPrecipitationMode() {
        return precipitationMode;
    }

    public void setPrecipitationMode(String precipitationMode) {
        this.precipitationMode = precipitationMode;
    }

    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

    public void setPrecipitationUnit(String precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }

    public int getWeatherNumber() {
        return weatherNumber;
    }

    public void setWeatherNumber(int weatherNumber) {
        this.weatherNumber = weatherNumber;
    }

    public String getWeatherValue() {
        return weatherValue;
    }

    public void setWeatherValue(String weatherValue) {
        this.weatherValue = weatherValue;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
