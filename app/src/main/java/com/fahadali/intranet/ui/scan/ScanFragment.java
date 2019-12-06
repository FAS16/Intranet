package com.fahadali.intranet.ui.scan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fahadali.intranet.R;
import com.fahadali.intranet.activities.AttendanceRegistrationActivity;
import com.fahadali.intranet.model.Post;
import com.fahadali.intranet.other.HttpService;
import com.fahadali.intranet.other.JsonPlacerHolderApi;
import com.google.gson.Gson;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private ScanViewModel scanViewModel;
    private static final String TAG = "ScanFragment";
    private Button registrationButton, sendRequset;
    private TextView retrofitTextView;
    private String url = "http://www.mocky.io/v2/5de91ca031000063006b173f";
    private Gson gson;
    private HttpService httpService;
    private Retrofit retrofit;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scanViewModel = ViewModelProviders.of(this).get(ScanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        Log.d(TAG, "onCreateView: called...");


        httpService = new HttpService();
        gson = new Gson();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlacerHolderApi jsonPlacerHolderApi = retrofit.create(JsonPlacerHolderApi.class);

        Call<List<Post>> call = jsonPlacerHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(!response.isSuccessful()) {
                    retrofitTextView.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for(Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    retrofitTextView.append(content);

                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                retrofitTextView.setText(t.getMessage());

            }
        });

        registrationButton = root.findViewById(R.id.registrationButton);
        retrofitTextView = root.findViewById(R.id.okHttp);
        registrationButton.setOnClickListener(this);
        retrofitTextView.setOnClickListener(this);
        sendRequset = root.findViewById(R.id.send_request);
        sendRequset.setOnClickListener(this);





//        String url = "https://jsonplaceholder.typicode.com/todos/1";
//
//        try {
//            retrofitTextView.setText(run(url));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
            return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        if(v == registrationButton) {
            Intent i = new Intent(getContext(), AttendanceRegistrationActivity.class);
            startActivity(i);
        }

        else if( v == sendRequset) {


        }
    }
}