package com.example.shownews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.shownews.Api.APIClient;
import com.example.shownews.Api.ApiService;
import com.example.shownews.Models.Article;
import com.example.shownews.Models.News;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // API Key = "8cb757617e2649f886345895ca8addfd";

    private static final String API_KEY= "8cb757617e2649f886345895ca8addfd";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;
    private List<Article> articles =new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.recycler_view_main);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        onLoadingSwipeRefresh("");
    }


    public void LoadData(String keyword){

        swipeRefreshLayout.setRefreshing(true);

        ApiService  apiService = APIClient.getApiClient().create(ApiService.class);

        Call<News> call;

        if(keyword.length()>0) {

            call= apiService.fetchSearchedNews(keyword,"en","publishedAt",API_KEY);

        }else{

            call = apiService.fetchNews("in", API_KEY);
        }


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body().getArticles()!=null){

                    if(!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new Adapter(articles,MainActivity.this);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                    initListener();
                    swipeRefreshLayout.setRefreshing(false);


                }else{
                    Toast.makeText(MainActivity.this,"No Result FOund",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });


    }

    private void initListener() {

        adapter.setListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItaClick(View view, int position) {

                ImageView imageView = view.findViewById(R.id.image);
                Intent intent = new Intent(MainActivity.this, NewsDetail.class);

                Article article = articles.get(position);

                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img",  article.getUrlToImage());
                intent.putExtra("date",  article.getPublishedAt());
                intent.putExtra("source",  article.getSource().getName());
                intent.putExtra("author",  article.getAuthor());

                Pair<View, String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,
                        pair
                );


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, optionsCompat.toBundle());
                }else {
                    startActivity(intent);
                }



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item,menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_action_menu_item).getActionView();

        MenuItem searchMenuItem = menu.findItem(R.id.search_action_menu_item);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search News..");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if(query.length() > 2) {
                    onLoadingSwipeRefresh(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               // LoadData(newText);
                return false;

            }
        });

        searchMenuItem.getIcon().setVisible(false,false);
        return true;



    }

    @Override
    public void onRefresh() {

        LoadData("");

    }

   private void onLoadingSwipeRefresh(final String keyword){

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"Inon",Toast.LENGTH_SHORT).show();
                LoadData(keyword);

            }
        });
    }
}
