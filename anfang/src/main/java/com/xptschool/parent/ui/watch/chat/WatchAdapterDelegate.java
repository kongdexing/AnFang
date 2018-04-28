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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.widget.audiorecorder.MediaPlayerManager;
import com.android.widget.view.CircularImageView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.model.BeanWChat;
import com.xptschool.parent.model.ContactTeacher;
import com.xptschool.parent.util.ChatUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * Created by dexing on 2017/5/10.
 * No1
 */

public class WatchAdapterDelegate extends BaseAdapterDelegate {

    private int viewType;
//    public AnimationDrawable animation;

    public WatchAdapterDelegate(Context context, int viewType) {
        super(context);
        this.viewType = viewType;
        this.mContext = context;
    }

    public int getViewType() {
        return viewType;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_watch, parent, false));
    }

    public void onBindViewHolder(List items, int position, RecyclerView.ViewHolder holder) {
        final BeanWChat chat = (BeanWChat) items.get(position);
        final MyViewHolder viewHolder = (MyViewHolder) holder;

        if (chat == null) {
            viewHolder.llChat.setVisibility(View.GONE);
            return;
        } else {
            viewHolder.llChat.setVisibility(View.VISIBLE);
        }
        viewHolder.txtContent.setVisibility(View.GONE);
        viewHolder.rlVoice.setVisibility(View.GONE);

        //判断是否为撤回
//        if (chat.getSendStatus() == ChatUtil.STATUS_RECALL) {
//            viewHolder.llRevert.setVisibility(View.VISIBLE);
//            viewHolder.txtRevert.setText("\"" + teacher.getName() + "\"撤回了一条消息");
//            return;
//        } else {
//            viewHolder.llRevert.setVisibility(View.GONE);
//        }

        if ((ChatUtil.TYPE_TEXT).equals(chat.getType())) {
            Log.i(TAG, "onBindViewHolder text:" + chat.getText());
            viewHolder.txtContent.setVisibility(View.VISIBLE);
            //聊天内容
            viewHolder.txtContent.setText(chat.getText());
        } else if ((ChatUtil.TYPE_AMR).equals(chat.getType())) {
            Log.i(TAG, "onBindViewHolder amr:" + chat.getFileName());
            //录音
            viewHolder.rlVoice.setVisibility(View.VISIBLE);

//            viewHolder.id_recorder_time.setText(chat.getSeconds() + "\"");

//            ViewGroup.LayoutParams lp = viewHolder.id_recorder_length.getLayoutParams();
//            lp.width = (int) (ChatUtil.getChatMinWidth(mContext) + (ChatUtil.getChatMaxWidth(mContext) / 60f) * Integer.parseInt(chat.getSeconds()));

            String amr_file = chat.getFileName();
            String fileName = amr_file.substring(amr_file.lastIndexOf('/') + 1);

            //下载音频文件
            FileDownloader.getImpl().create(amr_file)
                    .setListener(new FileDownloadListener() {

                        @Override
                        protected boolean isInvalid() {
                            return super.isInvalid();
                        }

                        @Override
                        protected void pending(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
//                updateDisplay(String.format("[pending] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
                        }

                        @Override
                        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
//                updateDisplay(String.format("[connected] id[%d] %s %B %d/%d", task.getId(), etag, isContinue, soFarBytes, totalBytes));
                            viewHolder.id_recorder_time.setText("正在下载...");
                        }

                        @Override
                        protected void progress(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
//                updateDisplay(String.format("[progress] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
                        }

                        @Override
                        protected void blockComplete(final BaseDownloadTask task) {
                        }

                        @Override
                        protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, int soFarBytes) {
                            super.retry(task, ex, retryingTimes, soFarBytes);
//                updateDisplay(String.format("[retry] id[%d] %s %d %d",
//                        task.getId(), ex, retryingTimes, soFarBytes));
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            viewHolder.id_recorder_time.setVisibility(View.GONE);
                            final String voicePath = task.getPath();

                            //点击播放
                            viewHolder.rlVoice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 声音播放动画
                                    if (viewHolder.img_recorder_anim != null) {
                                        viewHolder.img_recorder_anim.setBackgroundResource(R.drawable.adj_right);
                                    }

                                    SoundPlayHelper.getInstance().stopPlay();

                                    viewHolder.img_recorder_anim.setBackgroundResource(R.drawable.play_anim_right);
                                    AnimationDrawable animation = (AnimationDrawable) viewHolder.img_recorder_anim.getBackground();
                                    animation.start();

                                    Log.i(TAG, "onClick: teacher playSound " + voicePath);

                                    // 播放录音
                                    MediaPlayerManager.playSound(voicePath, new MediaPlayer.OnCompletionListener() {

                                        public void onCompletion(MediaPlayer mp) {
                                            //播放完成后修改图片
                                            viewHolder.img_recorder_anim.setBackgroundResource(R.drawable.adj_right);
                                        }
                                    });
                                }
                            });

                        }

                        @Override
                        protected void paused(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
//                updateDisplay(String.format("[paused] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
//                updateDisplay(String.format("############################## %d", (Integer) task.getTag()));
                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            viewHolder.id_recorder_time.setText("下载失败");
//                updateDisplay(Html.fromHtml(String.format("[error] id[%d] %s %s",
//                        task.getId(),
//                        e,
//                        FileDownloadUtils.getStack(e.getStackTrace(), false))));
//
//                updateDisplay(String.format("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! %d", (Integer) task.getTag()));
                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {
//                updateDisplay(String.format("[warn] id[%d]", task.getId()));
//                updateDisplay(String.format("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ %d", (Integer) task.getTag()));
                        }
                    })
                    .setPath(XPTApplication.getInstance().getCachePath() + "/" + fileName)
                    .setTag(fileName)
                    .start();

            viewHolder.error_file.setVisibility(View.GONE);
            viewHolder.img_recorder_anim.setTag(chat);
            SoundPlayHelper.getInstance().insertPlayView(viewHolder.img_recorder_anim);
//            Log.i(TAG, "onBindViewHolder: teacher playSoundViews size " + SoundPlayHelper.getInstance().getPlaySoundViewSize());
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgUser)
        CircularImageView imgUser;

        @BindView(R.id.llChat)
        LinearLayout llChat;

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

        @BindView(R.id.view_unRead)
        View view_unRead;

        @BindView(R.id.id_recorder_time)
        TextView id_recorder_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
