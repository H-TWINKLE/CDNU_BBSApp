package com.twinkle.cdnubbs.bmob;


import android.util.Log;
import android.widget.EditText;

import com.twinkle.cdnubbs.entity.Comment;
import com.twinkle.cdnubbs.entity.Post;
import com.twinkle.cdnubbs.entity.User;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class BmobProxy {

    private static BmobProxy instance = new BmobProxy();

    private BmobProxy() {
    }

    public static BmobProxy getInstance() {
        return instance;
    }

    public void bmobLogin(String admin, String pass) {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(admin);
        bmobUser.setPassword(pass);
        bmobUser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    if (toLoginListener != null) {
                        toLoginListener.onLoginSuccess();
                    }
                } else {
                    if (toLoginListener != null) {
                        toLoginListener.onLoginFailure(e.toString());
                    }
                }
            }
        });
    }

    public interface toLoginListener {
        void onLoginSuccess();

        void onLoginFailure(String tip);
    }

    ;

    private toLoginListener toLoginListener;

    public void setToLoginListener(BmobProxy.toLoginListener toLoginListener) {
        this.toLoginListener = toLoginListener;
    }

    public void bmobUpdateInfo(String text, int ii) {
        User user = new User();
        if (ii == 2) {
            user.setName(text);
        } else {
            if (onInfoUpdateListener != null) {
                onInfoUpdateListener.onInfoUpdateFailure("");
            }
            return;
        }
        user.update(BmobUser.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (onInfoUpdateListener != null) {
                        onInfoUpdateListener.onInfoUpdateSuccess();
                    }
                } else {
                    if (onInfoUpdateListener != null) {
                        onInfoUpdateListener.onInfoUpdateFailure(e.getMessage());
                    }
                }
            }
        });
    }


    public interface onInfoUpdateListener {
        void onInfoUpdateSuccess();

        void onInfoUpdateFailure(String tip);
    }


    private onInfoUpdateListener onInfoUpdateListener;

    public void setOnInfoUpdate(BmobProxy.onInfoUpdateListener onInfoUpdateListener) {
        this.onInfoUpdateListener = onInfoUpdateListener;
    }


    public void bmobUploadPic(File file) {
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobUploadPicListener != null) {
                        bmobUploadPicListener.onUploadPicSuccess(bmobFile);
                    }
                } else {

                    if (bmobUploadPicListener != null) {
                        bmobUploadPicListener.onUploadPicFailure(e.getMessage());
                    }

                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }


    public interface bmobUploadPicListener {
        void onUploadPicSuccess(BmobFile bmobFile);

        void onUploadPicFailure(String tip);
    }

    private bmobUploadPicListener bmobUploadPicListener;

    public void setBmobUploadPicListener(BmobProxy.bmobUploadPicListener bmobUploadPicListener) {
        this.bmobUploadPicListener = bmobUploadPicListener;
    }

    public void bmobUpdatePic(String text) {
        User bmobUser = BmobUser.getCurrentUser(User.class);
        bmobUser.setPic(text);
        bmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                    if (onPicUpdateListener != null) {
                        onPicUpdateListener.onPicUpdateSuccess();
                    }
                } else {
                    if (onPicUpdateListener != null) {
                        onPicUpdateListener.onPicUpdateFailure(e.getMessage());
                    }
                }
            }
        });
    }


    public interface onPicUpdateListener {
        void onPicUpdateSuccess();

        void onPicUpdateFailure(String tip);
    }

    private onPicUpdateListener onPicUpdateListener;

    public void setOnPicUpdateListener(BmobProxy.onPicUpdateListener onPicUpdateListener) {
        this.onPicUpdateListener = onPicUpdateListener;
    }


    public void bmobRevisePass(String pass) {
        User bmobUser = BmobUser.getCurrentUser(User.class);
        bmobUser.setPassword(pass);
        bmobUser.setPass(pass);
        bmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (onPassReviseListener != null) {
                        onPassReviseListener.onPassReviseSuccess();
                    }
                } else {
                    if (onPassReviseListener != null) {
                        onPassReviseListener.onPassReviseFailure(e.getMessage());
                    }
                }
            }
        });
    }


    public interface onPassReviseListener {
        void onPassReviseSuccess();

        void onPassReviseFailure(String tip);
    }

    private onPassReviseListener onPassReviseListener;

    public void setOnPassReviseListener(BmobProxy.onPassReviseListener onPassReviseListener) {
        this.onPassReviseListener = onPassReviseListener;
    }


    public void bmobGetOnePost(String Object_id) {
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.getObject(Object_id, new QueryListener<Post>() {

            @Override
            public void done(Post object, BmobException e) {
                if (e == null) {
                    if (getOnePostListener != null) {
                        getOnePostListener.onGetOnePostSuccess(object);
                    }
                } else {
                    if (getOnePostListener != null) {
                        getOnePostListener.onGetOnePostFailure(e.getMessage());
                    }
                }
            }

        });
    }


    public interface getOnePostListener {
        void onGetOnePostSuccess(Post post);

        void onGetOnePostFailure(String text);
    }


    private getOnePostListener getOnePostListener;

    public void setGetOnePostListener(BmobProxy.getOnePostListener getOnePostListener) {
        this.getOnePostListener = getOnePostListener;
    }


    public void bmobGetUserPost(String objectId) {
        BmobQuery<User> innerQuery = new BmobQuery<User>();
        String[] friendIds = {objectId};//好友的objectId数组
        innerQuery.addWhereContainedIn("objectId", Arrays.asList(friendIds));
        //查询帖子
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereMatchesQuery("author", "_User", innerQuery);
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (onBombGetUserPostListener != null) {
                        onBombGetUserPostListener.onGetUserPostSuccess(object);
                    }
                } else {
                    if (onBombGetUserPostListener != null) {
                        onBombGetUserPostListener.onGetUserPostFailure(e.getMessage());
                    }
                }
            }
        });
    }

    public interface onBombGetUserPostListener {
        void onGetUserPostSuccess(List<Post> list);

        void onGetUserPostFailure(String text);

    }


    private onBombGetUserPostListener onBombGetUserPostListener;

    public void setOnBombGetUserPostListener(BmobProxy.onBombGetUserPostListener onBombGetUserPostListener) {
        this.onBombGetUserPostListener = onBombGetUserPostListener;
    }

    public void bmobGetPost() {
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.include("author");
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (onGetPostListener != null) {
                        onGetPostListener.onGetPostSuccess(object);
                    }

                } else {
                    if (onGetPostListener != null) {
                        onGetPostListener.onGetPostFailure(e.getMessage());
                    }
                }
            }
        });
    }


    public interface onGetPostListener {
        void onGetPostSuccess(List<Post> list);

        void onGetPostFailure(String text);
    }

    private onGetPostListener onGetPostListener;

    public void setOnGetPostListener(BmobProxy.onGetPostListener onGetPostListener) {
        this.onGetPostListener = onGetPostListener;
    }


    public void addPostComment(String postId, String content) {
        Post post = new Post();
        post.setObjectId(postId);
        final Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(BmobUser.getCurrentUser(User.class));
        comment.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    if (onAddPostCommentListener != null) {
                        onAddPostCommentListener.onAddPostCommentListenerSuccess("发送成功！");
                    }
                } else {
                    if (onAddPostCommentListener != null) {
                        onAddPostCommentListener.onAddPostCommentListenerFailure(e.getMessage());
                    }
                }
            }

        });
    }

    public interface onAddPostCommentListener {
        void onAddPostCommentListenerSuccess(String text);

        void onAddPostCommentListenerFailure(String text);
    }


    private onAddPostCommentListener onAddPostCommentListener;

    public void setOnAddPostCommentListener(BmobProxy.onAddPostCommentListener onAddPostCommentListener) {
        this.onAddPostCommentListener = onAddPostCommentListener;
    }

    public void getPostComment(String id) {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        Post post = new Post();
        post.setObjectId(id);
        query.addWhereEqualTo("post", new BmobPointer(post));
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("author");
        query.order("-createdAt");
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {
                if (e == null) {
                    if (onGetPostCommentListener != null) {
                        onGetPostCommentListener.onGetPostCommentListenerSuccess(objects);
                    }
                } else {
                    onGetPostCommentListener.onGetPostCommentListenerFailure(e.getMessage());
                }

            }
        });
    }

    public interface onGetPostCommentListener {
        void onGetPostCommentListenerSuccess(List<Comment> list);

        void onGetPostCommentListenerFailure(String text);
    }

    private onGetPostCommentListener onGetPostCommentListener;


    public void setOnGetPostCommentListener(BmobProxy.onGetPostCommentListener onGetPostCommentListener) {
        this.onGetPostCommentListener = onGetPostCommentListener;
    }

    public void bmobPraise(String postId) {
        Post post = new Post();
        post.setObjectId(postId);
        BmobRelation relation = new BmobRelation();//将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
        relation.add(BmobUser.getCurrentUser(User.class));//将当前用户添加到多对多关联中
        post.setPraise(relation);//多对多关联指向`post`的`likes`字段
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (onBmobPraiseListener != null) {
                        onBmobPraiseListener.onBmobPraiseListenerSuccess("点赞成功！");
                    }
                } else {
                    if (onBmobPraiseListener != null) {
                        onBmobPraiseListener.onBmobPraiseListenerFailure(e.getMessage());
                    }
                }
            }
        });
    }

    public interface onBmobPraiseListener {
        void onBmobPraiseListenerSuccess(String text);

        void onBmobPraiseListenerFailure(String text);
    }

    private onBmobPraiseListener onBmobPraiseListener;

    public void setOnBmobPraiseListener(BmobProxy.onBmobPraiseListener onBmobPraiseListener) {
        this.onBmobPraiseListener = onBmobPraiseListener;
    }

    public void getListMess() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        BmobQuery<Post> innerQuery = new BmobQuery<Post>();
        innerQuery.addWhereEqualTo("author", BmobUser.getCurrentUser(User.class));
        query.addWhereMatchesQuery("post", "Post", innerQuery);
        query.include("author,post");
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {
                if (e == null) {
                    if (onGetListMessListener != null) {
                        onGetListMessListener.onGetListMessListenerSuccess(objects);
                    }
                } else {
                    if (onGetListMessListener != null) {
                        onGetListMessListener.onGetListMessListenerFailure(e.getMessage());
                    }
                }
            }
        });
    }

    public interface onGetListMessListener {
        void onGetListMessListenerSuccess(List<Comment> list);

        void onGetListMessListenerFailure(String text);
    }


    private onGetListMessListener onGetListMessListener;

    public void setOnGetListMessListener(BmobProxy.onGetListMessListener onGetListMessListener) {
        this.onGetListMessListener = onGetListMessListener;
    }


    public void getListComment() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("author", new BmobPointer(BmobUser.getCurrentUser(User.class)));
        query.include("author,post.author");
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {
                if (e == null) {
                    if (onGetListCommentListener != null) {
                        onGetListCommentListener.onGetListCommentListenerSuccess(objects);
                    }
                } else {
                    if (onGetListCommentListener != null) {
                        onGetListCommentListener.onGetListCommentListenerFailure(e.getMessage());
                    }
                }
            }
        });

    }

    public interface onGetListCommentListener {
        void onGetListCommentListenerSuccess(List<Comment> list);

        void onGetListCommentListenerFailure(String text);
    }


    private onGetListCommentListener onGetListCommentListener;


    public void setOnGetListCommentListener(BmobProxy.onGetListCommentListener onGetListCommentListener) {
        this.onGetListCommentListener = onGetListCommentListener;
    }


    public void getListPraise() {
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("praise", new BmobPointer(BmobUser.getCurrentUser(User.class)));
        query.include("author");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (onGetListPraiseListener != null) {
                        onGetListPraiseListener.onGetListPraiseListenerSuccess(object);
                    }
                } else {
                    if (onGetListPraiseListener != null) {
                        onGetListPraiseListener.onGetListPraiseListenerFailure(e.getMessage());
                    }
                }
            }

        });
    }

    public interface onGetListPraiseListener {
        void onGetListPraiseListenerSuccess(List<Post> list);

        void onGetListPraiseListenerFailure(String text);
    }


    private onGetListPraiseListener onGetListPraiseListener;


    public void setOnGetListPraiseListener(BmobProxy.onGetListPraiseListener onGetListPraiseListener) {
        this.onGetListPraiseListener = onGetListPraiseListener;
    }

    public void bmobDeletePraise(String postId) {
        Post post = new Post();
        post.setObjectId(postId);

        BmobRelation relation = new BmobRelation();
        relation.remove(BmobUser.getCurrentUser(User.class));
        post.setPraise(relation);
        post.update(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (onDeletePraiseListener != null) {
                        onDeletePraiseListener.onDeletePraiseListenerSuccess("取消点赞成功！");
                    }
                } else {
                    if (onDeletePraiseListener != null) {
                        onDeletePraiseListener.onDeletePraiseListenerFailure(e.getMessage());
                    }
                }
            }

        });

    }


    public interface onDeletePraiseListener {
        void onDeletePraiseListenerSuccess(String text);

        void onDeletePraiseListenerFailure(String text);
    }

    private onDeletePraiseListener onDeletePraiseListener;

    public void setOnDeletePraiseListener(BmobProxy.onDeletePraiseListener onDeletePraiseListener) {
        this.onDeletePraiseListener = onDeletePraiseListener;
    }


    public void bmobCheckIsPraise(String postId) {
        // 查询喜欢这个帖子的所有用户，因此查询的是用户表
        BmobQuery<User> query = new BmobQuery<User>();
        Post post = new Post();
        post.setObjectId(postId);
        //likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("praise", new BmobPointer(post));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                   if(bmobCheckIsPraiseListener!=null){
                       bmobCheckIsPraiseListener.bmobCheckIsPraiseListenerListener(object);
                   }
                } else {
                    if(bmobCheckIsPraiseListener!=null){
                        bmobCheckIsPraiseListener.bmobCheckIsPraiseListenerFailure(e.getMessage());
                    }
                }
            }

        });
    }

    public interface  bmobCheckIsPraiseListener{
        void bmobCheckIsPraiseListenerListener(List<User> list);
        void bmobCheckIsPraiseListenerFailure(String text);
    }

    private bmobCheckIsPraiseListener bmobCheckIsPraiseListener;

    public void setBmobCheckIsPraiseListener(BmobProxy.bmobCheckIsPraiseListener bmobCheckIsPraiseListener) {
        this.bmobCheckIsPraiseListener = bmobCheckIsPraiseListener;
    }
}
