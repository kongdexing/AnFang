package com.xptschool.parent.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.adapter.BaseRecycleAdapter;
import com.xptschool.parent.adapter.RecyclerViewHolderBase;
import com.xptschool.parent.bean.BeanShop;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ShopListAdapter extends BaseRecycleAdapter {

    private List<BeanShop> beanShops = new ArrayList<>();

    public ShopListAdapter(Context context) {
        super(context);
        Log.i(TAG, "HomeWorkParentAdapter: ");
    }

    public void refreshData(List<BeanShop> listShops) {
        Log.i(TAG, "refreshData: " + listShops.size());
        beanShops = listShops;
    }

    public void appendData(List<BeanShop> listShops) {
        Log.i(TAG, "refreshData: ");
        beanShops.addAll(listShops);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i(TAG, "showData: " + position);
        final ViewHolder mHolder = (ViewHolder) holder;
        final BeanShop work = beanShops.get(position);
        mHolder.txtShopName.setText(work.getShop_name());
        mHolder.txtDes.setText(work.getDescribe());
        try {
            double price = Double.parseDouble(work.getPrice());
            if (0 >= price) {
                mHolder.txtPrice.setVisibility(View.GONE);
            } else {
                mHolder.txtPrice.setText(work.getPrice());
            }
        } catch (Exception ex) {
            mHolder.txtPrice.setVisibility(View.GONE);
        }
        mHolder.txtAddress.setText(work.getAddress());

        ImageLoader.getInstance().displayImage(work.getImage(),
                new ImageViewAware(mHolder.goods_img), CommonUtil.getDefaultImageLoaderOption());

        mHolder.llhomeworkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra(ExtraKey.SHOP_GOODS, work);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + beanShops.size());
        return beanShops.size();
    }

    class ViewHolder extends RecyclerViewHolderBase {

        private Unbinder unbinder;

        @BindView(R.id.llhomeworkItem)
        LinearLayout llhomeworkItem;

        @BindView(R.id.goods_img)
        ImageView goods_img;

        @BindView(R.id.txtShopName)
        TextView txtShopName;

        @BindView(R.id.txtDes)
        TextView txtDes;

        @BindView(R.id.txtPrice)
        TextView txtPrice;

        @BindView(R.id.txtAddress)
        TextView txtAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }

}
