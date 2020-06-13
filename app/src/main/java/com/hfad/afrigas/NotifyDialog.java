package com.hfad.afrigas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NotifyDialog extends AppCompatDialogFragment {
    private EditText notifyTitle;
    private EditText notifyContent;
    private NotifyDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Send Notification")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = notifyTitle.getText().toString();
                        String content = notifyContent.getText().toString();
                        listener.applyTexts(title, content);
                    }
                });
        notifyTitle = view.findViewById(R.id.notify_title);
        notifyTitle.setText("Dear Customer,");
        notifyContent = view.findViewById(R.id.notify_content);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NotifyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
            "must implement NotifyDialogListener");
        }

    }

    public interface NotifyDialogListener {
        void applyTexts(String title, String content);
    }

}
