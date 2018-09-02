package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;
import org.w3c.dom.Text;

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

        Log.v ("String","URL: "+sandwich.getImage());
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
        TextView mAlsoKnownAsView = (TextView) findViewById(R.id.tv_AlsoKnownAs);
        TextView mPlaceOfOriginView = (TextView) findViewById(R.id.tv_Origin);
        TextView mDescriptionView = (TextView) findViewById(R.id.tv_Description);
        TextView mIngreditentsView= (TextView) findViewById(R.id.tv_Ingredients);
        RelativeLayout lMainDetailLayout = (RelativeLayout) findViewById(R.id.Main_Detail_Layout);

        mPlaceOfOriginView.setText(sandwich.getPlaceOfOrigin());
        mDescriptionView.setText(sandwich.getDescription());


        for (String s : sandwich.getAlsoKnownAs()) {
            if ( mAlsoKnownAsView.getText() == "" ) {mAlsoKnownAsView.setText(s);}
            else { mAlsoKnownAsView.append( "," + s ); }
        }

        for (String s : sandwich.getIngredients()) {
            if ( mIngreditentsView.getText() == "" ) { mIngreditentsView.setText(s); }
            else { mIngreditentsView.append( "\n" + s ); }
        }

        for (int i=0; i < lMainDetailLayout.getChildCount(); i++) {
            View childview = lMainDetailLayout.getChildAt(i);
            if (childview instanceof TextView){
                TextView thischildview = (TextView) childview;
                if (thischildview.length() < 2) {thischildview.setText(R.string.no_known_information);}
            }
        }
    }
}
