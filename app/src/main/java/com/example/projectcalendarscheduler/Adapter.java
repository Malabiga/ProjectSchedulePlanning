package com.example.projectcalendarscheduler;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private Context mContext;
    private List<Model> mData;

    public Adapter(Context mContext, List<Model> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.tvLocation.setText("Location: " + mData.get(position).getLocation());
        holder.tvTask.setText("Task: " + mData.get(position).getTask());
        holder.tvDuration.setText("Duration: " + mData.get(position).getDuration());
        holder.tvStartDate.setText("Start Date: " + mData.get(position).getStartDate());
        holder.tvEndDate.setText("End Date: " + mData.get(position).getEndDate());

        statusFunc(holder, position);
    }

    private void statusFunc(MyViewHolder holder,int position) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Date date = new Date();
                        String getStartDate = removeLastChar(mData.get(position).getStartDate());
                        String getStartTime = mData.get(position).getStartDate();
                        String getEndTime = mData.get(position).getEndDate();

                        CharSequence charDate = DateFormat.format("yyyy-MM-dd", date.getTime());

                        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                        Date currentDate = Calendar.getInstance().getTime();
                        long currentTime = currentDate.getTime();

                        try {
                            Date startParse = formatDate.parse(getStartDate);
                            long startDate = startParse.getTime();

                            CharSequence charTime = DateFormat.format("H:mm", date.getTime());
                            SimpleDateFormat formatTime = new SimpleDateFormat("H:mm");
                            Date dateTime = formatTime.parse((String) charTime);
                            long longTime = dateTime.getTime();

                            String startRem = getStartTime.substring(11);
                            String endRem = getEndTime.substring(11);

                            Date dateStart = formatTime.parse(startRem);
                            Date dateEnd = formatTime.parse(endRem);

                            long longStart = dateStart.getTime();
                            long longEnd = dateEnd.getTime();

                            if (charDate.equals(getStartDate)) {
                                if (longTime >= longStart && longTime <= longEnd) {
                                    holder.tvStatus.setText("Happening Now");
                                } else if (longTime >= longStart) {
                                    holder.tvStatus.setText("Done");
                                } else if (longTime <= longEnd) {
                                    holder.tvStatus.setText("Incoming Event");
                                }
                            } else if (currentTime < startDate) {
                                holder.tvStatus.setText("Incoming Event");
                            } else if (currentTime > startDate) {
                                holder.tvStatus.setText("Done");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String happening = holder.tvStatus.getText().toString();
                        if (happening.equals("Happening Now")) {
                            holder.tvStatus.setText("Happening Now");
                            holder.tvStatus.setTextColor(Color.GREEN);
                        } else if (happening.equals("Incoming Event")) {
                            holder.tvStatus.setText("Incoming Event");
                            holder.tvStatus.setTextColor(Color.BLUE);
                        } else if (happening.equals("Done")) {
                            holder.tvStatus.setText("Done Event");
                            holder.tvStatus.setTextColor(Color.RED);
                        }
                        statusFunc(holder,position);
                    }
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private String removeLastChar(String s) {
        return s.substring(0, s.length() - 6);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvLocation, tvTask, tvDuration, tvEndDate, tvStartDate, tvStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTask = itemView.findViewById(R.id.tvTask);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
