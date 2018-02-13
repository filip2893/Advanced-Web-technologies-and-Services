/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.klijenti;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPodaci;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPrognoza;

/**
 *
 * @author fstrahij
 */
public class OWMKlijent {

    String apiKey;
    OWMRESTHelper helper;
    Client client;
    public String adresa;

    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

    /**
     * Dohvaća podatke o vremenu
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public MeteoPodaci getRealTimeWeather(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Current_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();

            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue() * 1000));
            mp.setSunSet(new Date(jo.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue() * 1000));

            adresa = jo.getJsonObject("sys").getJsonString("country").toString() + ", " + jo.getJsonString("name").toString();

            mp.setTemperatureValue(new Double(jo.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
            mp.setTemperatureMin(new Double(jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
            mp.setTemperatureMax(new Double(jo.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
            mp.setTemperatureUnit("celsius");

            mp.setHumidityValue(new Double(jo.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
            mp.setHumidityUnit("%");

            mp.setPressureValue(new Double(jo.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
            mp.setPressureUnit("hPa");

            mp.setWindSpeedValue(new Double(jo.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
            mp.setWindSpeedName("");

            if (jo.getJsonObject("wind").getJsonNumber("deg") == null) {
                mp.setWindDirectionValue(1.1f);
            } else {
                mp.setWindDirectionValue(new Double(jo.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
            }

            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");

            mp.setCloudsValue(jo.getJsonObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setPrecipitationMode("");

            mp.setWeatherNumber(jo.getJsonArray("weather").getJsonObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJsonArray("weather").getJsonObject(0).getString("icon"));

            mp.setLastUpdate(new Date(jo.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));
            return mp;

        } catch (Exception ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * dohvaća prognoze za IoT
     *
     * @param id
     * @param latitude
     * @param longitude
     * @return
     */
    public MeteoPrognoza[] getWeatherForecast(int id, String latitude, String longitude) {
        List<MeteoPrognoza> mprl = new ArrayList<>();
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Forecast_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        JsonReader reader = Json.createReader(new StringReader(odgovor));

        JsonObject jo = reader.readObject();
        JsonArray jsonProg = jo.getJsonArray("list");
        MeteoPrognoza[] mpr = null;
        for (int i = 0; i < jsonProg.size(); i++) {
            try {
                JsonObject lista = jsonProg.getJsonObject(i);

                MeteoPodaci mp = new MeteoPodaci();

                /*if (!lista.getJsonObject("sys").getJsonNumber("sunrise").toString().isEmpty()) {
                    mp.setSunRise(new Date(lista.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue() * 1000));
                }
                if (!lista.getJsonObject("sys").getJsonNumber("sunset").toString().isEmpty()) {
                    mp.setSunSet(new Date(lista.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue() * 1000));
                }   */
                mp.setTemperatureValue(new Double(lista.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
                mp.setTemperatureMin(new Double(lista.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
                mp.setTemperatureMax(new Double(lista.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
                mp.setTemperatureUnit("celsius");

                mp.setHumidityValue(new Double(lista.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
                mp.setHumidityUnit("%");

                mp.setPressureValue(new Double(lista.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
                mp.setPressureUnit("hPa");

                mp.setWindSpeedValue(new Double(lista.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
                mp.setWindSpeedName("");

                if (lista.getJsonObject("wind").getJsonNumber("deg") == null) {
                    mp.setWindDirectionValue(1.1f);
                } else {
                    mp.setWindDirectionValue(new Double(lista.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
                }

                mp.setWindDirectionCode("");
                mp.setWindDirectionName("");

                mp.setCloudsValue(lista.getJsonObject("clouds").getInt("all"));
                mp.setCloudsName(lista.getJsonArray("weather").getJsonObject(0).getString("description"));
                mp.setPrecipitationMode("");

                mp.setWeatherNumber(lista.getJsonArray("weather").getJsonObject(0).getInt("id"));
                mp.setWeatherValue(lista.getJsonArray("weather").getJsonObject(0).getString("description"));
                mp.setWeatherIcon(lista.getJsonArray("weather").getJsonObject(0).getString("icon"));

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                mp.setLastUpdate(format.parse(lista.getString("dt_txt")));

                int dan = (int) lista.getJsonNumber("dt").bigDecimalValue().longValue() * 1000;

                mprl.add(new MeteoPrognoza(id, dan, mp));
                mpr = new MeteoPrognoza[mprl.size()];
                int j = 0;

                for (MeteoPrognoza mpro : mprl) {
                    if (mpro.getId() == id) {
                        mpr[j++] = mpro;
                    }
                }

            } catch (ParseException ex) {
                Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return mpr;
        // todo preligodi programski kod za preuzimanje meteo prognoza od web servisa
        /*mprl.add(new MeteoPrognoza(1, 1, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(1, 2, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(1, 3, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(1, 4, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(1, 5, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(2, 1, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(2, 2, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(2, 3, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(2, 4, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));
        mprl.add(new MeteoPrognoza(2, 5, new MeteoPodaci(new Date(), new Date(),19.7f, 10.0f, 22.1f, "C", 55.0f, "%", 1001.2f, "hPa", 0.0f, "", 0.0f, "", "", 5, "sivo nebo", "ok", 0.0f, "", "mm/m2", 7, "ok", "", new Date())));*/

    }
}
