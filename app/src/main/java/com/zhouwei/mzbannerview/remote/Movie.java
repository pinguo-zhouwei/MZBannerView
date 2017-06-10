package com.zhouwei.mzbannerview.remote;

/**
 *
 * Created by zhouwei on 16/11/9.
 */

public class Movie {
    public Rate rating;
    public String title;
    public String collect_count;
    public String original_title;
    public String subtype;
    public String year;
    public MovieImage images;

    public static class Rate{
        public int max;
        public float average;
        public String stars;
        public int min;
    }

    public static class MovieImage{
        public String small;
        public String large;
        public String medium;
    }
}
