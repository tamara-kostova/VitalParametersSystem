package finki.vitalparameterssystem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import finki.vitalparameterssystem.service.DataService;
import okhttp3.*;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {
    private static final String PYTHON_SCRIPT_URL = "http://localhost:5000/analyze_temperature";

    @Override
    public String sendTemperatureToPython(List<Double> temperatureList) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(temperatureList);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request  = new Request.Builder().url(PYTHON_SCRIPT_URL).post(body).build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    @Override
    public String sendSaturationToPython(List<Double> saturationList) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(saturationList);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request  = new Request.Builder().url(PYTHON_SCRIPT_URL).post(body).build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    @Override
    public String sendRespirationRateToPython(List<Integer> respirationRateList) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(respirationRateList);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request  = new Request.Builder().url(PYTHON_SCRIPT_URL).post(body).build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    @Override
    public String sendPulseToPython(List<Integer> pulseList) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(pulseList);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request  = new Request.Builder().url(PYTHON_SCRIPT_URL).post(body).build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    @Override
    public String sendSystolicToPython(List<Integer> systolicList) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(systolicList);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request  = new Request.Builder().url(PYTHON_SCRIPT_URL).post(body).build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
