package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView mAlsoKnownAsView = findViewById(R.id.tv_AlsoKnownAs);
        TextView mPlaceOfOriginView = findViewById(R.id.tv_Origin);
        TextView mDescriptionView = findViewById(R.id.tv_Description);
        TextView mIngreditentsView= findViewById(R.id.tv_Ingredients);
        RelativeLayout mMainDetailLayout = findViewById(R.id.Main_Detail_Layout);

        // Set basic strings
        mPlaceOfOriginView.setText(sandwich.getPlaceOfOrigin());
        mDescriptionView.setText(sandwich.getDescription());


        // Parse lists to display - "Aliases"
        for (String s : sandwich.getAlsoKnownAs()) {
            if ( mAlsoKnownAsView.getText() == "" ) {mAlsoKnownAsView.setText(s);}
            else { mAlsoKnownAsView.append( "\n" + s ); }
        }

        // Parse lists to display - "Ingredients"
        for (String s : sandwich.getIngredients()) {
            if ( mIngreditentsView.getText() == "" ) { mIngreditentsView.setText(s); }
            else { mIngreditentsView.append( "\n" + s ); }
        }

        // Set a "I don't know this" string on any text fields which have no information.  Also, change its color to a medium-gray.
        for (int i=0; i < mMainDetailLayout.getChildCount(); i++) {
            View childview = mMainDetailLayout.getChildAt(i);
            if (childview instanceof TextView){
                TextView thischildview = (TextView) childview;
                if (thischildview.length() < 2) {
                    thischildview.setText(R.string.no_known_information);
                    thischildview.setTextColor(Color.parseColor("#d8d8d8"));
                }
            }
        }
    }
}
