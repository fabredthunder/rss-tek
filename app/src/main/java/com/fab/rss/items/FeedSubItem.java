package com.fab.rss.items;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fab.rss.R;
import com.fab.rss.RSSApp;
import com.fab.rss.utils.UtilsFunctions;
import com.fab.rss.utils.models.BaseResponse;
import com.fab.rss.utils.models.RSSResponse;
import com.fab.rss.utils.services.BaseApiService;
import com.fab.rss.utils.services.IApiService;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/9/17
 */
public class FeedSubItem extends AbstractItem<FeedSubItem, FeedSubItem.ViewHolder> {

    private RSSResponse item;

    private DeleteCallback callback;

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public FeedSubItem(RSSResponse item, DeleteCallback callback) {
        this.item = item;
        this.callback = callback;
    }

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

        viewHolder.title.setText(item.getTitle());
        viewHolder.comment.setText(item.getComment());

        /*viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFeed(item.getId(), viewHolder.getAdapterPosition());
            }
        });*/

        viewHolder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.more.getVisibility() == View.VISIBLE) {
                    viewHolder.more.setVisibility(View.GONE);
                    viewHolder.seeMore.setImageResource(R.drawable.ic_arrow);
                } else {
                    viewHolder.more.setVisibility(View.VISIBLE);
                    viewHolder.seeMore.setImageResource(R.drawable.ic_arrow_up);
                }
            }
        });

        KLog.i("Bind item");
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment)
        TextView comment;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.see_more)
        ImageView seeMore;
        @BindView(R.id.more)
        LinearLayout more;
        @BindView(R.id.list)
        RecyclerView recyclerView;

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

    private void deleteFeed(final String id, final int position) {
        if (UtilsFunctions.isNetworkAvailable()) {

            final BaseApiService baseService = new BaseApiService();
            IApiService service = baseService.create();
            Call<BaseResponse> call = service.deleteRSS(RSSApp.getToken(), id);

            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                    if (response.code() == 200) {
                        Toasty.success(RSSApp.getInstance(), "Feed successfully deleted").show();
                        callback.onDelete(position);
                    } else if (response.code() == 503) {
                        Toasty.warning(RSSApp.getInstance(), "Server timeout. Trying again ...").show();
                        deleteFeed(id, position);
                    } else
                        Toasty.warning(RSSApp.getInstance(), "Server error").show();
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    }

    public interface DeleteCallback {
        void onDelete(int position);
    }
}