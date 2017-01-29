package com.fab.rss.items;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fab.rss.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.ramotion.foldingcell.FoldingCell;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/9/17
 */
public class FeedItem extends AbstractItem<FeedItem, FeedItem.ViewHolder> {

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    @Override
    public int getType() {
        return R.id.feeditem_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_feed;
    }

    @Override
    public void bindView(final ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        viewHolder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.foldingCell.toggle(false);
            }
        });

        KLog.i("Bind item");
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.folding_cell)
        FoldingCell foldingCell;

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