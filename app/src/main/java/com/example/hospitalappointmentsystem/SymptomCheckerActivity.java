package com.example.hospitalappointmentsystem;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.ChatAdapter;
import com.example.hospitalappointmentsystem.model.ChatMessage;
import com.example.hospitalappointmentsystem.network.SymptomApiClient;

import java.util.ArrayList;
import java.util.List;

public class SymptomCheckerActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private ProgressBar progressBar;

    private ChatAdapter adapter;
    private final List<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker);

        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.progressBar);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messages);
        rvChat.setAdapter(adapter);

        addMessage(
                "Hello! I'm your AI Health Assistant.\n\nDescribe your symptoms and I'll suggest possible causes and the appropriate specialist.\n\nThis is not a medical diagnosis.",
                false
        );

        btnSend.setOnClickListener(v -> {

            String text = etMessage.getText().toString().trim();

            if (TextUtils.isEmpty(text))
                return;

            addMessage(text, true);
            etMessage.setText("");

            sendMessage(text);

        });
    }

    private void addMessage(String text, boolean isUser) {

        messages.add(new ChatMessage(text, isUser));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);

    }

    private void sendMessage(String symptom) {

        progressBar.setVisibility(View.VISIBLE);

        SymptomApiClient.getResponse(symptom, new SymptomApiClient.Callback() {

            @Override
            public void onSuccess(String response) {

                runOnUiThread(() -> {

                    progressBar.setVisibility(View.GONE);
                    addMessage(response, false);

                });

            }

            @Override
            public void onError(String error) {

                runOnUiThread(() -> {

                    progressBar.setVisibility(View.GONE);
                    addMessage("Error: " + error, false);

                });

            }

        });

    }
}