package com.walmart.instatrend;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class InstaStreamActivity extends AppCompatActivity {

    private static final String POPULAR_IMAGE_URL = "https://api.instagram.com/v1/media/popular?client_id=";
    private static final String CLIENT_ID = "8fee365e935d4805b3cfeb90c8c22459";
    private ArrayList<PostBean> postsArray = null;
    private PostAdapter aPost;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_stream);
        postsArray = new ArrayList<PostBean>();
        aPost = new PostAdapter(this, postsArray);
        ListView lvPosts = (ListView)findViewById(R.id.lvImages);
        lvPosts.setAdapter(aPost);
        getPhotos();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void getPhotos() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(POPULAR_IMAGE_URL + CLIENT_ID, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.i("DEBUG", response.toString());
                JSONArray responseJson = null;
                try {
                    responseJson = response.getJSONArray("data");
                    for (int i = 0; i < responseJson.length(); i++) {
                        JSONObject simpleImage = responseJson.getJSONObject(i);
                        PostBean image = new PostBean();
                        image.username = simpleImage.getJSONObject("user").getString("username");
                        image.profileImage = simpleImage.getJSONObject("user").getString("profile_picture");
                        image.caption = simpleImage.getJSONObject("caption").getString("text");
                        image.imageUrl = simpleImage.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        image.imageHeight = simpleImage.getJSONObject("images").getJSONObject("standard_resolution").getString("height");
                        image.likesCount = simpleImage.getJSONObject("likes").getInt("count");
                        postsArray.add(image);
                    }
                } catch (Exception e) {

                }
                aPost.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("DEBUG", responseString);

            }
        });
        if(swipeContainer!=null) {
            swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insta_stream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
