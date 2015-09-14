package com.uyogist.uyogist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uyogist.uyogist.service.APIClient;
import com.uyogist.uyogist.service.UyoGistService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Custom Chat List Adapter
 * Created by oyewale on 3/31/15.
 */
public class GistAdapter extends RecyclerView.Adapter<GistAdapter.ViewHolder> {

    private static final String TAG = GistAdapter.class.getSimpleName();
    private final Context mContext;
    public List<Gist> gists = new ArrayList<>();
    private View loadingView;

    private UyoGistService service;
    private GistCallback callback;

    SimpleDateFormat sdf;

    public GistAdapter(Context context){
        mContext = context;
        this.loadingView = loadingView;
//        gists = Gist.getDummyGists(10);
        sdf = new SimpleDateFormat("yyyy-M-dd");
        service = APIClient.getUyoGistAPIService(context);
        callback = new GistCallback();
        Call<List<Gist>> gistCall = service.getGists();
        gistCall.enqueue(callback);
    }

    private class GistCallback implements Callback<List<Gist>> {

        @Override
        public void onResponse(Response<List<Gist>> response) {
            gists = response.body();
            notifyDataSetChanged();
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e(TAG, t.getMessage());
        }
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

        }

        public void bindConversation(final Gist gist){
            this.gist = gist;

            gistTextView.setText(gist.getGist());
            authorTextView.setText(gist.getAuthor());
            dateTextView.setText(sdf.format(new Date(gist.getCreatedAt() * 1000)));

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
