package tk.twpooi.tuetue;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by tw on 2016-08-16.
 */
public class ParticipantSelectListCustomAdapter extends RecyclerView.Adapter<ParticipantSelectListCustomAdapter.ViewHolder> {

    // UI
    private Context context;
    private View mainView;

    // Data
    public ArrayList<HashMap<String, String>> list;

    private ParticipantSelectListActivity activity;


    // 생성자
    public ParticipantSelectListCustomAdapter(Context context, ArrayList<HashMap<String,String>> list, View view, ParticipantSelectListActivity activity) {
        this.context = context;
        this.list = list;
        this.mainView = view;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_select_list_custom_item,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HashMap<String,String> noticeData = list.get(position);
        final int pos = position;
        final String userId = noticeData.get("userId");
        String select = noticeData.get("select");

        String nickname = noticeData.get("nickname");
        String img = noticeData.get("img");

        holder.tv_nickname.setText(nickname);
        Picasso.with(context)
                .load(img)
                .transform(new CropCircleTransformation())
                .into(holder.img_profile);

        holder.showInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });

        if("0".equals(select)){
            holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }else{
            holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.selectIndex(pos);
            }
        });

    }

    private void setClipBoardLink(Context context , String link){

        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", link);
        clipboardManager.setPrimaryClip(clipData);
        showSnackbar("클립보드에 저장되었습니다.");

    }

    private void showSnackbar(String msg){
        Snackbar snackbar = Snackbar.make(mainView, msg, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_color));
        snackbar.show();
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


    public abstract class ScrollListener extends RecyclerView.OnScrollListener {
        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }

            if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                scrolledDistance += dy;
            }
            // 여기까지 툴바 숨기기
        }

        public abstract void onHide();
        public abstract void onShow();

    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout root;
        ImageView img_profile;
        TextView tv_nickname;
        TextView tv_email;
        RelativeLayout showInfoBtn;


        public ViewHolder(View v) {
            super(v);
            root = (RelativeLayout) v.findViewById(R.id.root);
            img_profile = (ImageView)v.findViewById(R.id.rl_profile_img);
            tv_nickname = (TextView) v.findViewById(R.id.nickname);
            tv_email = (TextView) v.findViewById(R.id.email);
            showInfoBtn = (RelativeLayout)v.findViewById(R.id.showInfoBtn);
        }
    }

}
