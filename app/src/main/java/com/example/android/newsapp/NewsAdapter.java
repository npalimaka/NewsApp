package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This adapter binds all news data with its views within a list
 */
public class NewsAdapter extends ArrayAdapter<NewsItem> {

    @BindView(R.id.news_title)
    TextView newsTitle;
    @BindView(R.id.news_section)
    TextView newsSection;
    @BindView(R.id.date)
    TextView newsDate;
    @BindView(R.id.time)
    TextView newsTime;

    public NewsAdapter(@NonNull Context context, @NonNull List<NewsItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_list_item, parent, false);
            ButterKnife.bind(this, listItemView);
        }

        NewsItem singleNews = getItem(position);

        DateTime date = DateTime.parse(singleNews.getMDate());
        Date dateToDate = date.toDate();
        String formattedDate = formatDate(dateToDate);
        String formattedTime = formatTime(dateToDate);
        newsDate.setText(formattedDate);
        newsTime.setText(formattedTime);
        newsTitle.setText(singleNews.getMTitle());
        newsSection.setText(singleNews.getMSection());
        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
