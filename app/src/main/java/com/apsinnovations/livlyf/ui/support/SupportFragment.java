package com.apsinnovations.livlyf.ui.support;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.apsinnovations.livlyf.R;
import com.google.android.material.textfield.TextInputEditText;


public class SupportFragment extends Fragment {
    ImageView imgWhatsapp, imgGmail, imgCall;
    Button btnSubmit;
    EditText etxtFeedback;
    TextInputEditText txtSubject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        imgWhatsapp = view.findViewById(R.id.imgWhatsapp);
        imgGmail = view.findViewById(R.id.imgGmail);
        imgCall = view.findViewById(R.id.imgCall);
        btnSubmit = view.findViewById(R.id.btnSubmitFeed);
        etxtFeedback = view.findViewById(R.id.txtFeedback);
        txtSubject = view.findViewById(R.id.txtSubject);

        imgWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = "+91 8847231095";
                String url = "https://api.whatsapp.com/send?phone=" + contact;

                try {
                    PackageManager pm = getActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getContext(), "WhatsApp not Installed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        });

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "+91 8847231095"));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
                } else {
                    startActivity(callIntent);
                }
            }
        });

        imgGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent Email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"amrit220399@gmail.com"});
                Email.putExtra(Intent.EXTRA_SUBJECT, "Your Queries");
                Email.putExtra(Intent.EXTRA_TEXT, "Type here..." + "");
                try {
                    startActivity(Email);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "Mail account not configured", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Thanks for the Response!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "+91 8847231095"));
                startActivity(callIntent);
            } else {
                Toast.makeText(getContext(), "Please enable permission from Settings to make a CALL!", Toast.LENGTH_LONG).show();
            }
        }
    }
}