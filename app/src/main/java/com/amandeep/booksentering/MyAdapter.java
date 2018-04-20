package com.amandeep.booksentering;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by simba on 6/27/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Books> mBooks;
    private List<Books> worldpopulationlist;
    String author;
    String subject;
    String title;
    // Store the context for easy access
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView_subject, mTextView_title, mTextView_author, mTextView_noOfCopies, mTextView_status;
        View progressView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_subject = (TextView) itemView.findViewById(R.id.lawyer_position_tv);
            mTextView_title = (TextView) itemView.findViewById(R.id.lawyer_name_tv);
            mTextView_author = (TextView) itemView.findViewById(R.id.lawyer_study_tv);
            mTextView_noOfCopies = (TextView) itemView.findViewById(R.id.total_tv);
            mTextView_status = (TextView) itemView.findViewById(R.id.status_tv);
            progressView = itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            final Books books = mBooks.get(position);
            new AlertDialog.Builder(mContext)
                    .setTitle("Issue?" )
                    .setMessage("Issue the book to the student")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
                            query.whereEqualTo("status","pending");
                            query.whereEqualTo("author", books.getAuthor());
                            query.whereEqualTo("id",books.getStatus());
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            for (ParseObject object : objects) {
                                                if(object.getInt("Books")>2)
                                                {
                                                    Toast.makeText(mContext,"No more Books can be issued",Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    object.put("status", "issued");
                                                    object.put("date", getCalculatedDate("dd/MM/yyyy", 7));
                                                    object.saveInBackground();
                                                }
                                            }
                                        }

                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    // Pass in the contact array into the constructor
    public MyAdapter(Context context, List<Books> lawyers) {
        mBooks = lawyers;

        this.worldpopulationlist = new ArrayList<Books>();
        this.worldpopulationlist.addAll(mBooks);
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;


    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.find_a_lawyer_item_recycler_view, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Books lawyer = mBooks.get(position);

        // Set item views based on your views and data model
        TextView textView_subject = viewHolder.mTextView_subject;
        textView_subject.setText("Subject: " + lawyer.getSubject());
        TextView textView_title = viewHolder.mTextView_title;
        textView_title.setText("Title: " + lawyer.getTitle());
        TextView textView_author = viewHolder.mTextView_author;
        textView_author.setText("Author: " + lawyer.getAuthor());
        TextView textView_noOfCopies = viewHolder.mTextView_noOfCopies;
        View Pro = viewHolder.progressView;
        Pro.setVisibility(View.GONE);
        if (lawyer.getNumberOfCopies() > 0)
            textView_noOfCopies.setText("NO. Of Books: " + lawyer.getNumberOfCopies());
        else {
            textView_noOfCopies.setVisibility(View.GONE);
        }
        TextView textView_status = viewHolder.mTextView_status;
        if (lawyer.getStatus().equals("0")) {
            textView_status.setVisibility(View.GONE);
        } else {
            textView_status.setText("Status: " + lawyer.getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
        query.whereEqualTo("status","pending");
        final String finalCharText = charText;
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    mBooks.clear();

                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            if (finalCharText.length() == 0) {

                                Books book = new Books(object.getString("subject"), object.getString("title"), object.getString("author"), object.getInt("NumberOfCopies"), object.getString("id"));
                                mBooks.add(book);
                                continue;
                            } else {
                                if (object.getString("subject").toLowerCase(Locale.getDefault()).equals(finalCharText) || object.getString("title").toLowerCase(Locale.getDefault()).equals(finalCharText) || object.getString("author").toLowerCase(Locale.getDefault()).equals(finalCharText) || object.getString("id").toLowerCase(Locale.getDefault()).equals(finalCharText)) {
                                    Books book = new Books(object.getString("subject"), object.getString("title"), object.getString("author"), object.getInt("NumberOfCopies"), object.getString("id"));
                                    mBooks.add(book);
                                }
                            }
                        }

                    }
                }
            }
        });

        notifyDataSetChanged();
    }
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }
}
