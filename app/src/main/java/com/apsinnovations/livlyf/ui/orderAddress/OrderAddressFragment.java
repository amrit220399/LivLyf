package com.apsinnovations.livlyf.ui.orderAddress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.apsinnovations.livlyf.R;


public class OrderAddressFragment extends Fragment {
    TextView txtReceiver, txtAddress, txtLandMark, txtPinCode, txtContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_address, container, false);
        txtReceiver = view.findViewById(R.id.OAF_txtReceiver);
        txtAddress = view.findViewById(R.id.OAF_txtAddress);
        txtLandMark = view.findViewById(R.id.OAF_txtLandmark);
        txtPinCode = view.findViewById(R.id.OAF_txtPinCode);
        txtContact = view.findViewById(R.id.OAF_txtContact);
        return view;
    }
}