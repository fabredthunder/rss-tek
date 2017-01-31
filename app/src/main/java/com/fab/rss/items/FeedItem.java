package com.fab.rss.items;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fab.rss.R;
import com.fab.rss.RSSApp;
import com.fab.rss.utils.UtilsFunctions;
import com.fab.rss.utils.models.BaseResponse;
import com.fab.rss.utils.models.FeedRSS;
import com.fab.rss.utils.models.RSSResponse;
import com.fab.rss.utils.services.BaseApiService;
import com.fab.rss.utils.services.IApiService;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/9/17
 */
public class FeedItem extends AbstractItem<FeedItem, FeedItem.ViewHolder> {

    private RSSResponse item;

    private DeleteCallback callback;

    private Activity activity;

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public FeedItem(RSSResponse item, DeleteCallback callback, Activity activity) {
        this.item = item;
        this.callback = callback;
        this.activity = activity;
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

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.myDialog))
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this feed?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFeed(item.getId(), viewHolder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        viewHolder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.more.getVisibility() == View.VISIBLE) {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.more.setVisibility(GONE);
                    viewHolder.seeMore.setImageResource(R.drawable.ic_arrow);
                    viewHolder.fastItemAdapter.clear();

                } else {
                    viewHolder.more.setVisibility(View.VISIBLE);
                    viewHolder.seeMore.setImageResource(R.drawable.ic_arrow_up);
                    getItems(viewHolder);
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
        private TextView comment;
        @BindView(R.id.title)
        private TextView title;
        @BindView(R.id.delete)
        private ImageView delete;
        @BindView(R.id.see_more)
        private ImageView seeMore;
        @BindView(R.id.more)
        private LinearLayout more;
        @BindView(R.id.list)
        private RecyclerView recyclerView;
        @BindView(R.id.progress)
        private ProgressBar progressBar;

        FastItemAdapter<SubFeedItem> fastItemAdapter;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            fastItemAdapter = new FastItemAdapter<>();
            recyclerView.setLayoutManager(new LinearLayoutManager(RSSApp.getInstance()));
            recyclerView.setItemAnimator(new AlphaCrossFadeAnimator());
            recyclerView.setAdapter(fastItemAdapter);
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

    private void getItems(final ViewHolder viewHolder) {

        final BaseApiService baseService = new BaseApiService();
        IApiService service = baseService.create();
        Call<FeedRSS> call = service.getRSS(RSSApp.getToken(), item.getId());

        call.enqueue(new Callback<FeedRSS>() {
            @Override
            public void onResponse(Call<FeedRSS> call, Response<FeedRSS> response) {

                viewHolder.progressBar.setVisibility(GONE);

                if (response.code() == 200) {

                    for (int i = 0; i < response.body().getFeed().length; ++i)
                        viewHolder.fastItemAdapter.add(new SubFeedItem(response.body().getFeed()[i]));

                } else {
                    viewHolder.more.setVisibility(GONE);
                    viewHolder.seeMore.setImageResource(R.drawable.ic_arrow);
                    Toasty.warning(RSSApp.getInstance(), "Server error").show();
                }
            }

            @Override
            public void onFailure(Call<FeedRSS> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}