package com.example.magazinonline.MainPart.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.magazinonline.Classes.User;
import com.example.magazinonline.MainPart.Activities.Home;
import com.example.magazinonline.MainPart.ViewModels.HomeViewModel;
import com.example.magazinonline.R;

public class SendProducerEmailFragment extends Fragment {
    private HomeViewModel viewModel;
    private User selectedProducer;
    private ImageView goBack;
    private EditText mailSubjectField;
    private EditText mailMessageField;
    private Button sendButton;

    public SendProducerEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_producer_message, container,
                false);

        setVariables(view);
        setOnClickListeners();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // se ascunde toolbar-ul din activitatea parinte
        ((Home) requireActivity()).hideToolbar();
    }

    private void setVariables(View v) {
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        selectedProducer = viewModel.getSelectedProducer();
        goBack = v.findViewById(R.id.send_message_go_back);
        mailSubjectField = v.findViewById(R.id.send_message_subject);
        mailMessageField = v.findViewById(R.id.send_message_message);
        sendButton = v.findViewById(R.id.send_message_send_button);
    }

    private void setOnClickListeners() {
        goBack.setOnClickListener(view -> requireActivity().onBackPressed());

        sendButton.setOnClickListener(view -> {
            String[] to = new String[]{selectedProducer.getEmail()};
            String subject = String.valueOf(mailSubjectField.getText()).trim();
            String message = String.valueOf(mailMessageField.getText()).trim();

            sendEmail(to, subject, message);
        });
    }

    // metoda pentru trimiterea mail-ului folosind intent
    private void sendEmail(String[] to, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,
                requireActivity().getResources().getString(R.string.choose_email_client)));
    }
}