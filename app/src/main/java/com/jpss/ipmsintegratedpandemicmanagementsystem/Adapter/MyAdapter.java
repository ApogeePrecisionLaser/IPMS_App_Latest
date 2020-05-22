package com.jpss.ipmsintegratedpandemicmanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.ObjectModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ObjectModel> listState;
    ArrayList<String> myList=new ArrayList<String>();
    private MyAdapter myAdapter;
    private boolean isFromView = false;
    ArrayList<String>getvalue=new ArrayList<>();
    ArrayList <String> checkedValue=new ArrayList<>();
    String val;
    public MyAdapter(Context context, List<ObjectModel> objects){
        this.mContext=context;
        this.listState=(ArrayList<ObjectModel>)objects;
        this.myAdapter = this;

    }
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.spinner_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(listState.get(position).getTitle());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(listState.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Integer pos = (Integer) holder.checkBox.getTag();
                if(isChecked){
                    holder.checkBox.setChecked(isChecked);
                    checkedValue.add(listState.get(position).getTitle()) ;

                } else {
                    checkedValue.remove(listState.get(position).getTitle());
                }
                /*holder.checkBox.setChecked(isChecked);
                listState.get(position).setSelected(isChecked);
                getvalue.add(listState.get(position).getTitle());
                DatabaseOperation databaseOperation=new DatabaseOperation(mContext);
                databaseOperation.open();
                myList =databaseOperation.getsymtomsid(getvalue);
                System.out.println(myList);*/
            }
        });

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listState.get(position).isSelected())
                {
                    listState.get(position).setSelected(false);
                    holder.checkBox.setChecked(false);

                }
                else
                {    listState.get(position).setSelected(true);
                    holder.checkBox.setChecked(true);
                }
            }
        });

    }
    public ArrayList<String> getList(){
        return checkedValue;
    }

    @Override
    public int getItemCount() {
        return listState.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox checkBox;
        public ViewHolder( View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}
