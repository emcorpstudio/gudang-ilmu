package emcorp.studio.sinergienerginegeri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import emcorp.studio.sinergienerginegeri.R;
import emcorp.studio.sinergienerginegeri.model.Donation;
import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.Tools;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import emcorp.studio.sinergienerginegeri.model.Donation;

public class AdapterListDonation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Donation> items = new ArrayList<>();

    private Context ctx;

    @LayoutRes
    private int layout_id;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Donation obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListDonation(Context context, List<Donation> items, @LayoutRes int layout_id) {
        this.items = items;
        ctx = context;
        this.layout_id = layout_id;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView subtitle;
        public TextView date;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            date = v.findViewById(R.id.date);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Donation n = items.get(position);
            view.title.setText(n.judul);
            view.subtitle.setText(n.full_name);
            view.date.setText(n.created_at);
//            Tools.displayImageOriginal(ctx, view.image, n.image);
            Tools.displayImageOriginal(ctx, view.image, Constant.PICT_DONASI_URL+n.foto);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener == null) return;
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}