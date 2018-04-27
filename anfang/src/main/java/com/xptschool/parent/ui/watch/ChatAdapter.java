package com.xptschool.parent.ui.watch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.xptschool.parent.model.BeanWChat;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.watch.chat.ParentAdapterDelegate;
import com.xptschool.parent.ui.watch.chat.WatchAdapterDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * 微聊适配器
 */
public class ChatAdapter extends RecyclerView.Adapter {

    private String TAG = ChatAdapter.class.getSimpleName();
    private List<BeanWChat> listChat = new ArrayList<>();
    private int VIEW_PARENT = 0;
    private int VIEW_WATCH = 1;
    private ParentAdapterDelegate parentAdapterDelegate;
    private WatchAdapterDelegate watchAdapterDelegate;

    public ChatAdapter(Context context) {
        parentAdapterDelegate = new ParentAdapterDelegate(context, VIEW_PARENT);
        watchAdapterDelegate = new WatchAdapterDelegate(context, VIEW_WATCH);
        SoundPlayHelper.getInstance().setPlaySoundViews(null);
    }

    @Override
    public int getItemViewType(int position) {
        return listChat.get(position).getIsSend() ? parentAdapterDelegate.getViewType() : watchAdapterDelegate.getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == parentAdapterDelegate.getViewType()) {
            return parentAdapterDelegate.onCreateViewHolder(parent);
        } else {
            return watchAdapterDelegate.onCreateViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        Log.i(TAG, "onBindViewHolder position:" + position + " viewType:" + viewType);
        if (viewType == parentAdapterDelegate.getViewType()) {
            //可重发
            parentAdapterDelegate.onBindViewHolder(listChat, position, holder, new OnItemResendListener());
        } else {
            watchAdapterDelegate.onBindViewHolder(listChat, position, holder);
        }
    }

    @Override
    public int getItemCount() {
        return listChat == null ? 0 : listChat.size();
    }

    public void appendData(List<BeanWChat> chats) {
        if (listChat.size() == 0) {
            listChat = chats;
        } else {
            listChat.addAll(chats);
        }
        notifyDataSetChanged();
    }

    // 添加数据
    public void addData(BeanWChat chat) {
        Log.i(TAG, "addData: " + chat.getChatId());
        listChat.add(0, chat);
        notifyItemInserted(0);
    }

    private boolean isExist(String chatId) {
        for (int i = 0; i < listChat.size(); i++) {
            if (listChat.get(i).getChatId().equals(chatId)) {
                return true;
            }
        }
        return false;
    }

//    public void updateData(BeanWChat chat) {
//        if (!isExist(chat.getChatId())) {
//            addData(chat);
//            return;
//        }
//        GreenDaoHelper.getInstance().updateChat(chat);
//        Log.i(TAG, "updateData: ");
//        for (int i = 0; i < listChat.size(); i++) {
//            if (listChat.get(i).getChatId().equals(chat.getChatId())) {
//                Log.i(TAG, "updateData chatId : " + chat.getChatId() + "  position:" + i);
//                listChat.set(i, chat);
//                notifyItemChanged(i);
//                break;
//            }
//        }
//    }

    /**
     * 重新发送
     */
    public class OnItemResendListener {
        void onResend(BeanWChat chat, int position) {
//            chat.setSendStatus(ChatUtil.STATUS_RESENDING);
//            updateData(chat);
//            chat.onReSendChatToMessage();
        }
    }

}
