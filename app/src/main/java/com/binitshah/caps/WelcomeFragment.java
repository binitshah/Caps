package com.binitshah.caps;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {

    int position;
    TextView title;
    TextView description;
    ImageView headerImage;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        title = (TextView) v.findViewById(R.id.welcome_title);
        description = (TextView) v.findViewById(R.id.welcome_description);
        headerImage = (ImageView) v.findViewById(R.id.header_image);
        Bundle bundle = getArguments();
        position = bundle.getInt("position");

        switch (position){ //todo set images and descriptions
            case 0:
                headerImage.setImageDrawable(getResources().getDrawable(R.drawable.image1));
                String title1 = getResources().getString(R.string.title1);
                title.setText(title1);
                break;
            case 1:
                headerImage.setImageDrawable(getResources().getDrawable(R.drawable.image2));
                String title2 = getResources().getString(R.string.title2);
                title.setText(title2);
                break;
            case 2:
                headerImage.setImageDrawable(getResources().getDrawable(R.drawable.image3));
                String title3 = getResources().getString(R.string.title3);
                title.setText(title3);
                break;
            case 3:
                headerImage.setImageDrawable(getResources().getDrawable(R.drawable.image4));
                String title4 = getResources().getString(R.string.title4);
                title.setText(title4);
                break;
            default:
                break;
        }

        return v;
    }

}
