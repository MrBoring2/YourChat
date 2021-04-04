package com.example.yourchat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatScroolListener extends RecyclerView.OnScrollListener  {
    private int lastDy = 0;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            final LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            int pos = lastDy > 0 ? lm.findLastVisibleItemPosition() : lm.findFirstVisibleItemPosition();
            recyclerView.smoothScrollToPosition(pos);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        lastDy = dy;
    }
}
