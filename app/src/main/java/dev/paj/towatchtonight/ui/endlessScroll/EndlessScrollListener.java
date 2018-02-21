package dev.paj.towatchtonight.ui.endlessScroll;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private EndlessScrollList parentFragment;

    private int lastVisibleItem = 0;
    private boolean loading = false;

    public EndlessScrollListener(EndlessScrollList parentFragment) {
        this.parentFragment = parentFragment;
    }

    public void onNetworkError() {
        loading = false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if(layoutManager instanceof GridLayoutManager) {
            lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        int allItems = layoutManager.getItemCount();
        int threshold = 4;

        if(dy > 0 && !loading) {
            if(lastVisibleItem + threshold >= allItems) {
                parentFragment.loadMoreDataToList();
                loading = true;
            }
        }
    }

    public void onLoadingFinished() {
        this.loading = false;
    }
}
