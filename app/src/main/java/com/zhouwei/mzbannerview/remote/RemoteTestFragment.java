package com.zhouwei.mzbannerview.remote;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import com.zhouwei.mzbannerview.R;
import com.zhouwei.mzbannerview.http.Fault;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by zhouwei on 17/6/8.
 */

public class RemoteTestFragment extends Fragment implements View.OnClickListener{
   private MovieLoader mMovieLoader;
   private MZBannerView mMZBannerView;
   private TextView mBtnChange;
    private List<Movie> mMovies;
   private Handler mHandler = new Handler();
    public static RemoteTestFragment newInstance(){
        RemoteTestFragment fragment = new RemoteTestFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remote_test_layout,null);
        mMZBannerView = (MZBannerView) view.findViewById(R.id.my_banner);
        mBtnChange = (TextView) view.findViewById(R.id.btn_change);
        mBtnChange.setOnClickListener(this);
        view.findViewById(R.id.btn_clear).setOnClickListener(this);
        mMovieLoader = new MovieLoader();
        getMovieList();
        return view;
    }


    /**
     * 获取电影列表
     */
    private void getMovieList(){
        mMovieLoader.getMovie(0,10).subscribe(new Action1<List<Movie>>() {
            @Override
            public void call(List<Movie> movies) {
                mMovies = movies;
                Log.e("zhouwei","get data suceess");
                setBanner(movies);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("TAG","error message:"+throwable.getMessage());
                if(throwable instanceof Fault){
                    Fault fault = (Fault) throwable;
                    if(fault.getErrorCode() == 404){
                        //错误处理
                    }else if(fault.getErrorCode() == 500){
                        //错误处理
                    }else if(fault.getErrorCode() == 501){
                        //错误处理
                    }
                }
            }
        });

    }

    private void setBanner(List<Movie> movies){
        mMZBannerView.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("zw","onPageScrolled---->"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mMZBannerView.setPages(movies, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

        mMZBannerView.start();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMZBannerView.pause();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_change){
            List movies = new ArrayList(mMovies);
            movies.add(mMovies.get(0));
            movies.add(mMovies.get(1));
            if(movies.size()>2){
                mMZBannerView.setCanLoop(true);
            }else{
                mMZBannerView.setCanLoop(false);
            }
            setBanner(movies);
        }else if(v.getId() == R.id.btn_clear){
            List<Movie> movies = new ArrayList<>();
            movies.add(mMovies.get(0));
            movies.add(mMovies.get(1));
            mMZBannerView.setCanLoop(false);
            setBanner(movies);

        }
    }

    public static class BannerViewHolder implements MZViewHolder<Movie>{
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.remote_banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.remote_item_image);
            return view;
        }

        @Override
        public void onBind(Context context, int i, Movie movie) {
            Log.e("zhouwei","current position:"+i);
            Picasso.with(context).load(movie.images.large).into(mImageView);
        }
    }
}
