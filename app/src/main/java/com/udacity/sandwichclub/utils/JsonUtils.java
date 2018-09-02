package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) throws JSONException {
        JSONObject sandwichJSON = new JSONObject(json);
        JSONObject sandwichName = sandwichJSON.getJSONObject("name");
        Sandwich assembledSandwich = new Sandwich();

        assembledSandwich.setMainName(sandwichName.getString("mainName"));
        assembledSandwich.setPlaceOfOrigin(sandwichJSON.getString("placeOfOrigin"));
        assembledSandwich.setDescription(sandwichJSON.getString("description"));
        assembledSandwich.setImage(sandwichJSON.getString("image"));
        assembledSandwich.setAlsoKnownAs(JSArrayToList(sandwichName.getJSONArray("alsoKnownAs")));
        assembledSandwich.setIngredients(JSArrayToList(sandwichJSON.getJSONArray("ingredients")));


        return assembledSandwich;
    }

    private static ArrayList<String> JSArrayToList(JSONArray arInputArray) throws JSONException {

        if (arInputArray == null) { return null;}

        ArrayList<String> newList = new ArrayList<String>();

        for (int i=0, n=arInputArray.length(); i<n; i++){
            newList.add(arInputArray.get(i).toString());
        }

        return newList;
    }
}
