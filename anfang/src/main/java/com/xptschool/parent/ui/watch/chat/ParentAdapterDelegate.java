package com.xptschool.parent.ui.watch.chat;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.widget.audiorecorder.MediaPlayerManager;
import com.android.widget.view.CircularImageView;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.BeanWChat;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.watch.ChatAdapter;
import com.xptschool.parent.util.ChatUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * Created by dexing on 2017/5/10.
 * No1
 */

public class ParentAdapterDelegate extends BaseAdapterDelegate {

    private int viewType;
    public AnimationDrawable animation;

    public ParentAdapterDelegate(Context context, int viewType) {
        super(context);
        this.viewType = viewType;
        this.mContext = context;
    }

    public int getViewType() {
        return viewType;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_parent, parent, false));
    }

    public void onBindViewHolder(List items, final int position, RecyclerView.ViewHolder holder, final ChatAdapter.OnItemResendListener listener) {
        final BeanWChat chat = (BeanWChat) items.get(position);
        BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
        if (parent == null || chat == null) {
            return;
        }
        final MyViewHolder viewHolder = (MyViewHolder) holder;

        viewHolder.txtContent.setVisibility(View.GONE);
        viewHolder.rlVoice.setVisibility(View.GONE);

        if ((ChatUtil.TYPE_TEXT).equals(chat.getType())) {
            viewHolder.txtContent.setVisibility(View.VISIBLE);
            //聊天内容
            viewHolder.txtContent.setText(chat.getText());
        } else if ((ChatUtil.TYPE_AMR).equals(chat.getType())) {
            Log.i(TAG, "onBindViewHolder amr:" + chat.getFileName());
            //录音
            viewHolder.rlVoice.setVisibility(View.VISIBLE);

//            viewHolder.id_recorder_time.setText(chat.getSeconds() + "\"");
//
            ViewGroup.LayoutParams lp = viewHolder.id_recorder_length.getLayoutParams();
            lp.width = (int) (ChatUtil.getChatMinWidth(mContext) + (ChatUtil.getChatMaxWidth(mContext) / 60f) * Integer.parseInt(chat.getSeconds()));

            final File file = new File(chat.getFileName());
            if (!file.exists()) {
                viewHolder.error_file.setVisibility(View.VISIBLE);
            } else {
                viewHolder.error_file.setVisibility(View.GONE);
                RelativeLayout.LayoutParams voiceAnimLP = (RelativeLayout.LayoutParams) viewHolder.img_recorder_anim.getLayoutParams();
                voiceAnimLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                voiceAnimLP.setMargins(0, 0, 30, 0);
                viewHolder.img_recorder_anim.setTag(chat);

                SoundPlayHelper.getInstance().insertPlayView(viewHolder.img_recorder_anim);
                Log.i(TAG, "onBindViewHolder: parent playSoundViews size " + SoundPlayHelper.getInstance().getPlaySoundViewSize());

                //点击播放
                viewHolder.rlVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 声音播放动画
                        if (viewHolder.img_recorder_anim != null) {
                            viewHolder.img_recorder_anim.setBackgroundResource(R.drawable.adj);
                        }

                        SoundPlayHelper.getInstance().stopPlay();

                        viewHolder.img_recorder_anim.setBackgroundResource(R.drawable.play_anim);
                        animation = (AnimationDrawable) viewHolder.img_recorder_anim.getBackground();
                        animation.start();

                        Log.i(TAG, "onClick: parent playSound " + file.getPath());
                        // 播放录音
                        MediaPlayerManager.playSound(file.getPath(), new MediaPlayer.OnCompletionListener() {

                            public void onCompletion(MediaPlayer mp) {
                                Log.i(TAG, "onCompletion: ");
                                //播放完成后修改图片
                                viewHolder.img_recorder_anim.setBackgroundResource(R.drawable.adj);
                            }
                        });
                    }
                });
            }
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgUser)
        CircularImageView imgUser;

        @BindView(R.id.llContent)
        LinearLayout llContent;

        @BindView(R.id.txtContent)
        EmojiconTextView txtContent;

        @BindView(R.id.rlVoice)
        RelativeLayout rlVoice;

        @BindView(R.id.id_recorder_length)
        RelativeLayout id_recorder_length;

        @BindView(R.id.id_recorder_anim)
        View img_recorder_anim;

        @BindView(R.id.error_file)
        View error_file;

        @BindView(R.id.id_recorder_time)
        TextView id_recorder_time;

        @BindView(R.id.sendProgress)
        ProgressBar sendProgress;

        @BindView(R.id.llResend)
        LinearLayout llResend;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
