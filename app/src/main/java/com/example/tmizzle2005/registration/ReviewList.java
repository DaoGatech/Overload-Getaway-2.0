package com.example.tmizzle2005.registration;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReviewList extends ArrayAdapter<ReviewItem> {
 
private ArrayList<ReviewItem> ist = new ArrayList<ReviewItem>();
@SuppressWarnings("unused")
private Context context;
@SuppressWarnings("unused")
private int size;
 
public ReviewList(ArrayList<ReviewItem> objs, Context ctx, int size) {
    super(ctx,R.layout.listview_layout,objs);
    for(int i = 0; i < size; i++) {
    	ist.add(objs.get(i));
    }
    this.size = size;
    this.context = ctx;
}
 
public View getView(int position, View view, ViewGroup parent) {
     
   
	 if (view == null) {
         LayoutInflater inflater = LayoutInflater.from(parent.getContext());
         view = inflater.inflate(R.layout.reviewlayout, parent, false);
     }

     final ReviewItem t  = ist.get(position);
     
     TextView textView1 = (TextView) view.findViewById(R.id.classname);
     textView1.setText("" + t.getname() + "-" + t.getRemaining());
     textView1.setTypeface(null, Typeface.BOLD);
     TextView textView3 = (TextView) view.findViewById(R.id.remaining);
     textView3.setText("Remaining Seats: " + t.getserial());
     
     return view;
 }

}

