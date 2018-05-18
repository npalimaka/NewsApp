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

    private String mTitle;
    private String mSection;
    private String mDate;
    private String mUrl;

}
