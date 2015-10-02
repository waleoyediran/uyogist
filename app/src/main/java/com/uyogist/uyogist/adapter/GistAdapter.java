package com.uyogist.uyogist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uyogist.uyogist.model.Gist;
import com.uyogist.uyogist.R;
import com.uyogist.uyogist.service.APIClient;
import com.uyogist.uyogist.service.UyoGistService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Custom Chat List Adapter
 * Created by oyewale on 3/31/15.
 */
public class GistAdapter extends RecyclerView.Adapter<GistAdapter.ViewHolder> {

    private static final String TAG = GistAdapter.class.getSimpleName();
    private final Context mContext;
    public List<Gist> gists = new ArrayList<>();
    private View loadingView;

    SimpleDateFormat sdf;

    public GistAdapter(Context context, View loadingView) {
        mContext = context;
        sdf = new SimpleDateFormat("yyyy-M-dd");
        this.loadingView = loadingView;
        UyoGistService service = APIClient.getUyoGistAPIService(context);

        //TODO: Load Gist From Server
        gists = Gist.getDummyGists(10);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_gist_item, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Gist gist = gists.get(position);
        holder.bindConversation(gist);
    }

    @Override
    public int getItemCount() {
        if (gists != null)
            return gists.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Gist gist;

        private TextView gistTextView;
        private TextView authorTextView;
        private ImageView gistImageView;
        private TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            gistTextView = (TextView) itemView.findViewById(R.id.gist);
            authorTextView = (TextView) itemView.findViewById(R.id.author);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
            gistImageView = (ImageView) itemView.findViewById(R.id.img);

        }

        public void bindConversation(final Gist gist){
            this.gist = gist;

            gistTextView.setText(gist.getGist());
            authorTextView.setText(gist.getAuthor());
            dateTextView.setText(sdf.format(new Date(gist.getCreatedAt() * 1000)));

            if (gist.getImageUrl() != null){
                Picasso.with(mContext)
                        .load(gist.getImageUrl())
                        .resize(150, 50)
                        .centerCrop()
                        .into(gistImageView);
            }


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick");

            if (gist == null){
                return;
            }
        }
    }
}
