package com.example.calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorSelectListener;

/**
 * Created by Adas on 24.03.2016.
 */
public class ColorsDetailsRecyclerViewAdapter extends RecyclerView.Adapter<ColorsDetailsRecyclerViewAdapter.Holder> {
    private LayoutInflater mInflater;
    private List<Integer> mColors = Collections.emptyList();
    private List<String> mDescriptions = Collections.emptyList();

    private Context mContext;

    public ColorsDetailsRecyclerViewAdapter(Context context, List<Integer> colors, List<String> descriptions) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mColors = colors;
        mDescriptions = descriptions;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_colors_adapter_layout, parent, false);
        return new Holder(view);
    }

       @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mDescription.setText(mDescriptions.get(position) + "");
        if (position == getItemCount() - 1 && position != 0) {
            holder.mDeleteColor.setBackgroundResource(android.R.drawable.ic_delete);
        } else holder.mDeleteColor.setBackgroundResource(android.R.drawable.ic_menu_edit);

        GradientDrawable bgShape = (GradientDrawable) holder.mCircle.getBackground();
        bgShape.setColor(mColors.get(position));
        bgShape.setStroke(2, Color.BLACK);

        // We have to deal with a note or a note list here, so
        // check for both types and process accordingly

    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    public void setData(List<Integer> colors, List<String> descriptions) {

        this.mColors = colors;
        this.mDescriptions = descriptions;
    }

    public void delete(int position) {
        mColors.remove(position);
        mDescriptions.remove(position);
        notifyItemRemoved(position);
    }

    public void notifyImageObtained() {
        notifyDataSetChanged();
    }

       public class Holder extends RecyclerView.ViewHolder {

        TextView mDescription, mDeleteColor;
        EditText mDescriptionEdit;
        ImageView mCircle;
        int newColor;

        public Holder(View itemView) {
            super(itemView);

            mDescription = (TextView) itemView.findViewById(R.id.color_description);
            mDescriptionEdit = (EditText) itemView.findViewById(R.id.color_description_edit);


            mDeleteColor = (TextView) itemView.findViewById(R.id.delete_color);
            mDescriptionEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDescriptionEdit.getVisibility() == View.VISIBLE) {
                        mDescriptionEdit.requestFocus();
                    }
                }
            });

            mDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDescription.setVisibility(View.GONE);
                    mDescriptionEdit.setVisibility(View.VISIBLE);
                    mDeleteColor.setBackgroundResource(android.R.drawable.checkbox_on_background);
                    mDeleteColor.setVisibility(View.VISIBLE);
                    mDescriptionEdit.setText(mDescription.getText());
                    mDescriptionEdit.requestFocus();
                    ((AddOrEditCalendar) mContext).anythingChanged = true;
                }
            });

            mDeleteColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mDescriptionEdit.getVisibility() == (View.VISIBLE)) {
                        ((AddOrEditCalendar) mContext).updateColor(getAdapterPosition(), mDescriptionEdit.getText().toString(), mColors.get(getAdapterPosition()));
                        mDescription.setText(mDescriptionEdit.getText());
                        mDescriptionEdit.setText("");
                        mDescriptionEdit.setVisibility(View.GONE);
                        mDescription.setVisibility(View.VISIBLE);
                        mDeleteColor.setBackgroundResource(android.R.drawable.ic_menu_edit);
                        if (getAdapterPosition() == getItemCount() - 1 && getAdapterPosition() != 0) {
                            mDeleteColor.setBackgroundResource(android.R.drawable.ic_delete);
                        }
                    } else {
                        if (getAdapterPosition() == getItemCount() - 1 && getAdapterPosition() != 0) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("REMOVING COLOR")
                                    .setMessage("Are you sure?")
                                    .setCancelable(true)
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    ((AddOrEditCalendar) mContext).deleteColor(getAdapterPosition());
                                                    notifyDataSetChanged();
                                                    dialog.dismiss();
                                                    ((AddOrEditCalendar) mContext).anythingChanged = true;

                                                }
                                            }
                                    );
                            AlertDialog alert = builder.create();
                            alert.show();


                        } else {
                            mDescription.setVisibility(View.GONE);
                            mDescriptionEdit.setVisibility(View.VISIBLE);
                            mDescriptionEdit.setText(mDescription.getText());
                            mDeleteColor.setBackgroundResource(android.R.drawable.checkbox_on_background);
                            mDeleteColor.setVisibility(View.VISIBLE);
                            mDescriptionEdit.requestFocus();
                            ((AddOrEditCalendar) mContext).anythingChanged = true;
                        }

                    }
                }
            });

            mCircle = (ImageView) itemView.findViewById(R.id.circle);

            mCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ChromaDialog.Builder()
                            .initialColor(mColors.get(getAdapterPosition()))
                            .onColorSelected(
                                    new ColorSelectListener() {
                                        @Override
                                        public void onColorSelected(int color) {

                                            mColors.set(getAdapterPosition(), color);
                                            notifyDataSetChanged();
                                            ((AddOrEditCalendar) mContext).anythingChanged = true;
                                        }
                                    })
                            .create()
                            .show(((FragmentActivity) mContext).getSupportFragmentManager(), "ChromaDialog");

                }
            });
        }

    }


}
