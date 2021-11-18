package com.example.foundeat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foundeat.R;

public class ChoiceDialog extends DialogFragment {

    private OnChoiceListener listener;

    private Button camBtn, galleryBtn;

    public ChoiceDialog() {
        // Required empty public constructor
    }

    public static ChoiceDialog newInstance() {
        ChoiceDialog fragment = new ChoiceDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choice_dialog, container, false);
        camBtn = view.findViewById(R.id.camBtn);
        galleryBtn = view.findViewById(R.id.galleryBtn);


        camBtn.setOnClickListener(v->{
            listener.onChoice(0);
            dismiss();
        });
        galleryBtn.setOnClickListener(v->{
            listener.onChoice(1);
            dismiss();
        });
        return view;
    }

    public void setListener(OnChoiceListener listener){
        this.listener = listener;
    }

    public interface OnChoiceListener{
        void onChoice(int choice);
    }
}