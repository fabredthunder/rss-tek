package com.fab.rss.items;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fab.rss.R;
import com.fab.rss.RSSApp;
import com.fab.rss.utils.models.RSS;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/9/17
 */
class SubFeedItem extends AbstractItem<SubFeedItem, SubFeedItem.ViewHolder> {

    private RSS item;

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    SubFeedItem(RSS item) {
        this.item = item;
    }

    @Override
    public int getType() {
        return R.id.subfeeditem_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_subfeed;
    }

    @Override
    public void bindView(final ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        viewHolder.title.setText(item.getTitle());

        Glide.with(RSSApp.getInstance())
                .load(item.getImage())
                .centerCrop()
                .into(viewHolder.image);

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RSSApp.getInstance().startActivity(browserIntent);
            }
        });

        KLog.i("Bind item");
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        private TextView title;
        @BindView(R.id.image)
        private ImageView image;
        @BindView(R.id.root)
        private LinearLayout root;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }

}