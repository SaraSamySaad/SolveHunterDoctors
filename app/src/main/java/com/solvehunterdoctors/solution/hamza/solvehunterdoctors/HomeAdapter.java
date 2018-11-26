package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by C.M on 24/10/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context mCtx;
    private List<PostsData> PostsList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;

    JSONObject message;
    JSONObject messageInfo;

    private final String PUSH_URL = "https://fcm.googleapis.com/fcm/send";

    public HomeAdapter(Context mCtx, List<PostsData> postsList) {
        this.mCtx = mCtx;
        PostsList = postsList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.home_item, null);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        final PostsData postsData = PostsList.get(PostsList.size() - 1 - position);
        holder.body.setText(postsData.getBody());
        holder.likes.setText(String.valueOf(postsData.getLikes()));
        holder.comments.setText("comments (" + postsData.getComments() + ")");
        //get uploaded by data
        myRef.child("Users").child(postsData.getUploadedById()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    myRef.child("Doctors").child(postsData.getUploadedById()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            holder.userName.setText(dataSnapshot.child("name").getValue().toString());
                            if (!dataSnapshot.child("image").getValue().equals("")) {
                                Glide.with(mCtx).load(dataSnapshot.child("image").getValue()).into(holder.userImage);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    holder.userName.setText(dataSnapshot.child("name").getValue().toString());
                    if (!dataSnapshot.child("image").getValue().equals("")) {
                        Glide.with(mCtx).load(dataSnapshot.child("image").getValue()).into(holder.userImage);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("Likes").child(postsData.getId()).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    for (Drawable drawable : holder.likes.getCompoundDrawables()) {
                        if (drawable != null) {
                            DrawableCompat.setTint(drawable, ContextCompat.getColor(mCtx, R.color.colorAccent));
                        }
                    }
                } else {
                    for (Drawable drawable : holder.likes.getCompoundDrawables()) {
                        if (drawable != null) {
                            DrawableCompat.setTint(drawable, ContextCompat.getColor(mCtx, R.color.colorPrimaryDark));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("likesError", String.valueOf(postsData.getLikes()));
                myRef.child("Likes").child(postsData.getId()).child(mAuth.getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    //add user id in fire base like & likes++
                                    myRef.child("Likes").child(postsData.getId()).child(mAuth.getCurrentUser().getUid()).setValue("true")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    myRef.child("acceptedPosts").child(postsData.getId()).child("likes").setValue(postsData.getLikes() + 1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    postsData.setLikes(postsData.getLikes() + 1);
                                                                    holder.likes.setText(String.valueOf(postsData.getLikes()));

                                                                    myRef.child("PushTokens/" + postsData.getUploadedById()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            String userToken = (String) dataSnapshot.getValue();

                                                                            message = new JSONObject();
                                                                            messageInfo = new JSONObject();

                                                                            try {
                                                                                messageInfo.put("title", "New Like");
                                                                                messageInfo.put("message", "Your Post have new like");
                                                                                //messageInfo.put("image-url", "https://lh3.googleusercontent.com/JrGwExTVGhm24PWMa6mjFFPXMmE1n-LnBtRC1_jtV_gmKiVrt9hVYPoZQPC9e66FBA=h900");
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            try {
                                                                                message.put("to", userToken);
                                                                                message.put("data", messageInfo);
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            AndroidNetworking.initialize(holder.itemView.getContext());

                                                                            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                                                                    .addNetworkInterceptor(new StethoInterceptor()).build();

                                                                            AndroidNetworking.initialize(holder.itemView.getContext(), okHttpClient);


                                                                            AndroidNetworking.post(PUSH_URL)
                                                                                    .addJSONObjectBody(message)
                                                                                    .addHeaders("Authorization", "key=AAAAsEXz4wA:APA91bHV_ac2eQUUdokYPWhDEQSCx0D42J_uHxrH5MaZM3_86NNs0-GrPnXdtwAnst0D4sgrrMinVK5KzgdstdYilPOUNksxcX0KgngB49yf7bz7Y347g1lcEiNscpRhRCIHqZx8yQNc")
                                                                                    .addHeaders("Content-Type", "application/json")
                                                                                    .setPriority(Priority.MEDIUM)
                                                                                    .build()
                                                                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                                                                        @Override
                                                                                        public void onResponse(JSONObject response) {

                                                                                        }

                                                                                        @Override
                                                                                        public void onError(ANError anError) {
                                                                                        }
                                                                                    });


                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    for (Drawable drawable : holder.likes.getCompoundDrawables()) {
                                                                        if (drawable != null) {
                                                                            DrawableCompat.setTint(drawable, ContextCompat.getColor(mCtx, R.color.colorPrimaryDark));
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }
                                            });
                                }
                                // else for unlike & likes--
                                else {
                                    myRef.child("Likes").child(postsData.getId()).child(mAuth.getCurrentUser().getUid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            myRef.child("acceptedPosts").child(postsData.getId()).child("likes").setValue(postsData.getLikes() - 1)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            postsData.setLikes(postsData.getLikes() - 1);
                                                            holder.likes.setText(String.valueOf(postsData.getLikes()));
                                                            for (Drawable drawable : holder.likes.getCompoundDrawables()) {
                                                                if (drawable != null) {
                                                                    DrawableCompat.setTint(drawable, ContextCompat.getColor(mCtx, R.color.colorAccent));
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(mCtx, AllCommentsActivity.class);
                mainIntent.putExtra("postBody", postsData.getBody());
                mainIntent.putExtra("postUploadedId", postsData.getUploadedById());
                mainIntent.putExtra("postId", postsData.getId());
                mCtx.startActivity(mainIntent);

            }
        });
        holder.addComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    myRef.child("Comments").child(postsData.getId()).push().setValue(new CommentData(holder.addComment.getText().toString(), mAuth.getCurrentUser().getUid()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final String comment = holder.addComment.getText().toString();
                                    holder.addComment.setText("");

                                    myRef.child("acceptedPosts").child(postsData.getId()).child("comments").setValue(postsData.getComments() + 1)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    postsData.setComments(postsData.getComments() + 1);
                                                    holder.comments.setText(String.valueOf(postsData.getComments()));

                                                    myRef.child("PushTokens/" + postsData.getUploadedById()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String userToken = (String) dataSnapshot.getValue();

                                                            message = new JSONObject();
                                                            messageInfo = new JSONObject();

                                                            try {
                                                                messageInfo.put("title", "New Comment");
                                                                messageInfo.put("message", comment);
                                                                //messageInfo.put("image-url", "https://lh3.googleusercontent.com/JrGwExTVGhm24PWMa6mjFFPXMmE1n-LnBtRC1_jtV_gmKiVrt9hVYPoZQPC9e66FBA=h900");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            try {
                                                                message.put("to", userToken);
                                                                message.put("data", messageInfo);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }


                                                            AndroidNetworking.initialize(holder.itemView.getContext());

                                                            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                                                    .addNetworkInterceptor(new StethoInterceptor()).build();

                                                            AndroidNetworking.initialize(holder.itemView.getContext(), okHttpClient);


                                                            AndroidNetworking.post(PUSH_URL)
                                                                    .addJSONObjectBody(message)
                                                                    .addHeaders("Authorization", "key=AAAAsEXz4wA:APA91bHV_ac2eQUUdokYPWhDEQSCx0D42J_uHxrH5MaZM3_86NNs0-GrPnXdtwAnst0D4sgrrMinVK5KzgdstdYilPOUNksxcX0KgngB49yf7bz7Y347g1lcEiNscpRhRCIHqZx8yQNc")
                                                                    .addHeaders("Content-Type", "application/json")
                                                                    .setPriority(Priority.MEDIUM)
                                                                    .build()
                                                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {

                                                                        }

                                                                        @Override
                                                                        public void onError(ANError anError) {
                                                                        }
                                                                    });


                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            });
                                }
                            });
                    InputMethodManager imm = (InputMethodManager) mCtx.getSystemService(mCtx.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.addComment.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return PostsList.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView body;
        TextView likes;
        TextView comments;
        TextView userName;
        ImageView userImage;
        EditText addComment;

        public HomeViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.body);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            userName = itemView.findViewById(R.id.user_name);
            userImage = itemView.findViewById(R.id.user_image);
            addComment = itemView.findViewById(R.id.add_comment);
        }
    }
}
