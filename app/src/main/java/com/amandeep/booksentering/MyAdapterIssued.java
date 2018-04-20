package com.amandeep.booksentering;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by simba on 6/27/16.
 */
public class MyAdapterIssued extends RecyclerView.Adapter<MyAdapterIssued.ViewHolder> {
    private List<Books> mBooks;
    private List<Books> worldpopulationlist;
    String author;
    String subject;
    String title;
    // Store the context for easy access
    private Context mContext;
    int datedifference;

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
                    .setTitle("Return?" )
                    .setMessage("Take the book from the student")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final String[] date1 = new String[1];
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
                            query.whereEqualTo("status","issued");
                            query.whereEqualTo("author", books.getAuthor());
                            query.whereEqualTo("id",books.getStatus());
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            for (ParseObject object : objects) {
                                                        object.put("status","returned");
                                                        date1[0] =object.getString("date");
                                                        object.saveInBackground();
                                            }
                                        }
                                    }
                                }
                            });
                            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Books");
                            query1.whereEqualTo("author", books.getAuthor());
                            query1.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            for (ParseObject object : objects) {
                                                object.put("NumberOfCopies",object.getInt("NumberOfCopies")+1);
                                                object.saveInBackground();
                                            }
                                        }
                                    }
                                }
                            });
                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Fee");
                            query2.whereEqualTo("user",books.getStatus());
                            query2.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {

                                        if (objects.size() > 0) {
                                         for (ParseObject object : objects) {
                                            datedifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"), date1[0], getCalculatedDate("dd/MM/yyyy", 16));
                                            final int h = object.getInt("amount")-(int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"), date1[0], getCalculatedDate("dd/MM/yyyy", 16));
                                            Toast.makeText(getContext(),h,Toast.LENGTH_LONG).show();
                                            object.put("amount",h);
                                            object.saveInBackground();
                                        }}
                                    }
                                    else
                                    {
                                        Toast.makeText(mContext,e.toString(),Toast.LENGTH_SHORT).show();
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

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // Pass in the contact array into the constructor
    public MyAdapterIssued(Context context, List<Books> lawyers) {
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
    public MyAdapterIssued.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(MyAdapterIssued.ViewHolder viewHolder, int position) {
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
        query.whereEqualTo("status","issued");
        final String finalCharText = charText;
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    mBooks.clear();

                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            if (finalCharText.length() == 0) {

                                Books book = new Books(object.getString("subject"), object.getString("title"), object.getString("author"), object.getInt("NumberOfCopies"), "0");
                                mBooks.add(book);
                                continue;
                            } else {
                                if (object.getString("subject").toLowerCase(Locale.getDefault()).equals(finalCharText) || object.getString("title").toLowerCase(Locale.getDefault()).equals(finalCharText) || object.getString("author").toLowerCase(Locale.getDefault()).equals(finalCharText) || object.getString("id").toLowerCase(Locale.getDefault()).equals(finalCharText)) {
                                    Books book = new Books(object.getString("subject"), object.getString("title"), object.getString("author"), object.getInt("NumberOfCopies"), "0");
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
