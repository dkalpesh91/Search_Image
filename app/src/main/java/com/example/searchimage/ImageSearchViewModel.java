package com.example.searchimage;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageSearchViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<ArrayList<Data>> liveData;

    //we will call this method to get the data
    public LiveData<ArrayList<Data>> getHeroes(String pageno,String searchKey) {
        //if the list is null
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            //we will load it asynchronously from server in this method
            loadHeroes(pageno,searchKey);
        } else {
            loadHeroes(pageno,searchKey);
        }

        //finally we will return the list
        return liveData;
    }


    //This method is using Retrofit to get the JSON data from URL
    private void loadHeroes(String pageno,String searchKey) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<JsonElement> call = api.getData(pageno,searchKey, "Client-ID 137cda6b5008a7c");


        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                //finally we are setting the list to our MutableLiveData
                JSONObject jobjresponse = null;
                Data dataObj = new Data();
                ArrayList<Data> dataArrayList = new ArrayList<>();
                try {

                    jobjresponse = new JSONObject(response.body().toString());
                    String status = jobjresponse.getString("status");
                    if (status != null && status.equalsIgnoreCase("200")) {
                        JSONArray data = jobjresponse.getJSONArray("data");
                        if (null != data) {
                            for (int i = 0; i < data.length(); i++) {
                                dataObj = new Data();
                                JSONObject jsonObject = data.getJSONObject(i);
                                dataObj.setParentidID(jsonObject.getString("id"));
                                JSONArray images = null;
                                try {
                                    images = jsonObject.getJSONArray("images");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (null != images) {
                                    for (int j = 0; j < images.length(); j++) {
                                        JSONObject jsonObjectImages = images.getJSONObject(j);
                                        dataObj.setID(jsonObjectImages.getString("id"));
                                        dataObj.setLink(jsonObjectImages.getString("link"));
                                        try {
                                            if(null != jsonObjectImages.getString("title")) {
                                                dataObj.setName(jsonObjectImages.getString("title"));
                                            }else{
                                                dataObj.setName("Image Search");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    dataArrayList.add(dataObj);
                                }

                            }
                        }
                        liveData.setValue(dataArrayList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }


}
