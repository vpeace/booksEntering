package com.amandeep.booksentering;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksDetails extends Fragment {

    private static final String TAG = "BooksDetails";
    ArrayList<Books> books;
    String[] name = new String[20];
    AutoCompleteTextView name_tv;
    SwipeRefreshLayout mSwipeRefreshLayout;
    View progressView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.activity_books_details, container, false);
         progressView= view.findViewById(R.id.progressBar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

        final RecyclerView rvLawyer = (RecyclerView) view.findViewById(R.id.rv);
        books = new ArrayList<>();
        name_tv = (AutoCompleteTextView) view.findViewById(R.id.add_lawyerBtn);
        // Create adapter passing in the sample user data
        final MyAdapter adapter = new MyAdapter(getActivity(), books);

        name_tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.filter(s.toString().toLowerCase(Locale.getDefault()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
        query.whereEqualTo("status","pending");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    name[0] = "";
                    books.clear();
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            Books book = new Books(object.getString("subject"), object.getString("title"), object.getString("author"), object.getInt("NumberOfCopies"), object.getString("id"));
                            books.add(book);
                            name[0] = name[0] + object.getString("title") + "," + object.getString("subject") + "," + object.getString("author") + ","+ object.getString("id")+ ",";

                        }
                        if (progressView != null) {
                            progressView.setVisibility(View.GONE);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (name[0].contains("null"))
                            setAdapter(name[0].substring(4));
                        else
                            setAdapter(name[0]);
                    }
                }
            }
        });
        //rvLawyer.setItemAnimator(new SlideInUpAnimator());
        // Attach the adapter to the recyclerview to populate items
        rvLawyer.setAdapter(adapter);

        // Set layout manager to position the items
        rvLawyer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
                query.whereEqualTo("status","pending");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            name[0] = "";
                            books.clear();
                            if (objects.size() > 0) {
                                for (ParseObject object : objects) {
                                    Books book = new Books(object.getString("subject"), object.getString("title"), object.getString("author"), object.getInt("NumberOfCopies"), object.getString("id"));
                                    books.add(book);
                                    name[0] = name[0] + object.getString("title") + "," + object.getString("subject") + "," + object.getString("author") + ","+ object.getString("id")+ ",";

                                }
                                if (progressView != null) {
                                    progressView.setVisibility(View.GONE);
                                }
                                mSwipeRefreshLayout.setRefreshing(false);
                                if (name[0].contains("null"))
                                    setAdapter(name[0].substring(4));
                                else
                                    setAdapter(name[0]);
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"No records available",Toast.LENGTH_SHORT).show();
                                if (progressView != null) {
                                    progressView.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });
                //rvLawyer.setItemAnimator(new SlideInUpAnimator());
                // Attach the adapter to the recyclerview to populate items
                rvLawyer.setAdapter(adapter);

                // Set layout manager to position the items
                rvLawyer.setLayoutManager(new LinearLayoutManager(getActivity()));
                // That's all!
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
        query.whereEqualTo("status","pending");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    name[0] = "";
                    books.clear();
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            Books book = new Books(object.getString("subject"), object.getString("title"), object.getString("author"), object.getInt("NumberOfCopies"), object.getString("id"));
                            books.add(book);
                            name[0] = name[0] + object.getString("title") + "," + object.getString("subject") + "," + object.getString("author") + ","+ object.getString("id")+ ",";

                        }
                        if (progressView != null) {
                            progressView.setVisibility(View.GONE);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (name[0].contains("null"))
                            setAdapter(name[0].substring(4));
                        else
                            setAdapter(name[0]);
                    }
                }
            }
        });
        super.onResume();
    }

    public void setAdapter(String name) {
        String names[] = new String[10];

        names = name.split(",");

        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, names);

        name_tv.setAdapter(adapter1);
        name_tv.setThreshold(1);

    }

}