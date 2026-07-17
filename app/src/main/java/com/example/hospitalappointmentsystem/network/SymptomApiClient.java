package com.example.hospitalappointmentsystem.network;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SymptomApiClient {

    private static final String API_KEY = "YOUR_GEMINI_API_KEY";

    private static final String URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public interface Callback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void getResponse(String symptomText, Callback callback) {

        try {

            JSONObject part = new JSONObject();
            part.put("text",
                    "You are an AI medical assistant.\n" +
                            "Suggest possible causes based on symptoms.\n" +
                            "Recommend the appropriate medical specialist.\n" +
                            "Do NOT provide a final diagnosis.\n\n" +
                            "Symptoms: " + symptomText);

            JSONArray parts = new JSONArray();
            parts.put(part);

            JSONObject content = new JSONObject();
            content.put("parts", parts);

            JSONArray contents = new JSONArray();
            contents.put(content);

            JSONObject body = new JSONObject();
            body.put("contents", contents);

            RequestBody requestBody = RequestBody.create(
                    body.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call,
                                       @NonNull Response response) throws IOException {

                    String result = response.body() != null
                            ? response.body().string()
                            : "";

                    if (!response.isSuccessful()) {
                        callback.onError("HTTP " + response.code() + "\n" + result);
                        return;
                    }

                    try {

                        JSONObject json = new JSONObject(result);

                        JSONArray candidates = json.getJSONArray("candidates");

                        JSONObject candidate = candidates.getJSONObject(0);

                        JSONObject content = candidate.getJSONObject("content");

                        JSONArray responseParts = content.getJSONArray("parts");

                        String reply = responseParts
                                .getJSONObject(0)
                                .getString("text");

                        callback.onSuccess(reply);

                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }
}