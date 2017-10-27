package com.example.joseph.walmartchallenge;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.joseph.walmartchallenge.model.Item;
import com.example.joseph.walmartchallenge.model.SearchResult;
import com.example.joseph.walmartchallenge.remote.WalmartData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rvItems;

    List<Item> itemList = new ArrayList<>();
    private ItemListAdapter itemListAdapter;
    private RecyclerView.LayoutManager layoutManager;

//    public static final String searchTest = "ipod";
    private EditText etSearch;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        rvItems = findViewById(R.id.rvItems);
        etSearch = findViewById(R.id.etSearch);

        layoutManager = new LinearLayoutManager(getApplicationContext());




    }

    public void search(View view) {

        final String search = etSearch.getText().toString();

        View currentFocus = this.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }

        WalmartData.search(search)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<SearchResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull SearchResult searchResult) {
                        itemList = searchResult.getItems();
//                        for (Item i: imageList){
//                            Log.d(TAG, "onNext: " + i.getTitle() + " " + i.getAuthor());
//                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        rvItems.setLayoutManager(layoutManager);
                        itemListAdapter = new ItemListAdapter(rvItems, activity, itemList);

                        itemListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                itemList.add(null);
                                itemListAdapter.notifyItemInserted(itemList.size() - 1);
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        itemList.remove(itemList.size() - 1);
                                        itemListAdapter.notifyItemRemoved(itemList.size());

                                        //Generating more data
//                                        int index = contacts.size();
//                                        int end = index + 10;
//                                        for (int i = index; i < end; i++) {
//                                            Contact contact = new Contact();
//                                            contact.setPhone(phoneNumberGenerating());
//                                            contact.setEmail("DevExchanges" + i + "@gmail.com");
//                                            contacts.add(contact);
//                                        }
//                                        contactAdapter.notifyDataSetChanged();
//                                        contactAdapter.setLoaded();

                                        getMoreSearch(search, itemList.size()+1);

                                    }
                                });
                            }
                        });
                        rvItems.setAdapter(itemListAdapter);
                    }
                });

    }


    public void getMoreSearch(String search, int start){
        WalmartData.searchMore(search, start)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<SearchResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull SearchResult searchResult) {

                        itemList.addAll(searchResult.getItems());

//                        itemList = searchResult.getItems();
//                        for (Item i: imageList){
//                            Log.d(TAG, "onNext: " + i.getTitle() + " " + i.getAuthor());
//                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");

                        itemListAdapter.notifyDataSetChanged();
                        itemListAdapter.setLoaded();

//                        itemListAdapter = new ItemListAdapter(rvItems, activity, itemList);
//                        rvItems.setLayoutManager(layoutManager);
//                        rvItems.setAdapter(itemListAdapter);

                    }
                });
    }

}
