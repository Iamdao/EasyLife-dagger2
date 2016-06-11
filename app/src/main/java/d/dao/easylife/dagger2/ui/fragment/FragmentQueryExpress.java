package d.dao.easylife.dagger2.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import d.dao.easylife.dagger2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQueryExpress extends Fragment {


    public FragmentQueryExpress() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_query_express, container, false);
    }

}
