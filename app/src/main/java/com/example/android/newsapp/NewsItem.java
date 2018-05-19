package com.example.android.newsapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a single news item
 */
@AllArgsConstructor
@Getter
@Setter
public class NewsItem {

    private String title;
    private String section;
    private String date;
    private String url;
    private String author;

}
