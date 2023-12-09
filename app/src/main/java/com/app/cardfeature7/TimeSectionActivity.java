package com.app.cardfeature7;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TimeSectionActivity extends DialogFragment {

    private Context context;
    private OnTimeSetListener listener;
    private int initialHour;
    private int initialMinute;

    public TimeSectionActivity(Context context, int initialHour, int initialMinute) {
        this.context = context;
        this.initialHour = initialHour;
        this.initialMinute = initialMinute;
    }

    public interface OnTimeSetListener {
        void onTimeSet(int hourOfDay, int minute);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnTimeSetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnTimeSetListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.time_picker, null);
        builder.setView(dialogView);

        final NumberPicker hourPicker = dialogView.findViewById(R.id.hourPicker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setValue(initialHour);

        final NumberPicker minutePicker = dialogView.findViewById(R.id.minutePicker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(initialMinute);

        Button okButton = dialogView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedHour = hourPicker.getValue();
                int selectedMinute = minutePicker.getValue();
                if (listener != null) {
                    listener.onTimeSet(selectedHour, selectedMinute);
                }
                dismiss();
            }
        });

        return builder.create();
    }
}