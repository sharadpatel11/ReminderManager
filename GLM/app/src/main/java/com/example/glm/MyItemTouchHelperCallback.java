//package com.example.glm;
//
//import android.graphics.Color;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {
//
//    private ItemTouchHelperAdapter mAdapter;
//
//    public MyItemTouchHelperCallback(ItemTouchHelperAdapter adapter){
//        this.mAdapter = adapter;
//    }
//
//    @Override
//    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        final int swipeFlags = ItemTouchHelper.RIGHT;
//        return makeMovementFlags(dragFlags, swipeFlags);
//    }
//
//    @Override
//    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        return true;
//    }
//
//    @Override
//    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
//    }
//
//    @Override
//    public boolean isItemViewSwipeEnabled(){
//        return true;
//    }
//
//    @Override
//    public boolean isLongPressDragEnabled(){
//        return false;
//    }
//
//    /*
//    @Override
//    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState){
//        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
//            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
//        }
//        super.onSelectedChanged(viewHolder, actionState);
//    }
//
//     */
//}
