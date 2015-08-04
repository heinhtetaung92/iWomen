package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.undp_iwomen.iwomen.R;

/**
 * Created by khinsandar on 7/29/15.
 */
public class ResourcesFragment extends Fragment {
    public static final String ARG_MENU_INDEX = "index";

    private Context mContext;


    public ResourcesFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment, container, false);


        //int index = getArguments().getInt(ARG_MENU_INDEX);
        //String text = String.format("Menu at index %s", index);
        /*((TextView) rootView.findViewById(R.id.textView)).setText(text);
        getActivity().setTitle(text);*/


        return rootView;
    }


}

