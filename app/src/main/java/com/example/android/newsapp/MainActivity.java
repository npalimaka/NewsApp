package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    @BindView(R.id.list)
    ListView newsList;
    @BindView(R.id.empty_view)
    TextView emptyViewSet;
    @BindView(R.id.loading_spinner)
    ProgressBar progressBar;

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q=poland&limit=10&api-key=b523045f-f15b-4adf-9976-a0d26e4c5d49";
    private NewsAdapter newsAdapter;
    private final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        newsList.setEmptyView(emptyViewSet);
        newsAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = newsAdapter.getItem(position).getMUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        newsList.setAdapter(newsAdapter);
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnected()) {
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        }else{
            progressBar.setVisibility(View.GONE);
            emptyViewSet.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> news) {
        emptyViewSet.setText(R.string.no_news);
        progressBar.setVisibility(View.GONE);
        newsAdapter.clear();
        if(news != null && !news.isEmpty()){
            newsAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        newsAdapter.clear();
    }
}
