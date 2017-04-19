package com.example.android.news_app;

import android.view.View;

/**
 * @author PriteshJ
 *         <p>
 *         Custom interface that needs to implemented by
 *         Recycler View to implement list view on click
 *         behavior
 */
@SuppressWarnings("ALL")
interface RecyclerViewItemClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}