package com.xiaoluogo.goodtochat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by Administrator on 2016/4/27.
 */
public class NewFriendManager {
    private LitePalDB litePalDB;
    //    private DaoMaster.DevOpenHelper openHelper;
//    Context mContecxt;
    String uid = null;
    private static HashMap<String, NewFriendManager> daoMap = new HashMap<>();

    /**
     * 获取DB实例
     *
     * @param context
     * @return
     */
    public static NewFriendManager getInstance(Context context) {
        UserBean user = BmobUser.getCurrentUser(UserBean.class);
        String loginId = user.getObjectId();
        if (TextUtils.isEmpty(loginId)) {
            throw new RuntimeException("you must login.");
        }
        NewFriendManager dao = daoMap.get(loginId);
        if (dao == null) {
            dao = new NewFriendManager(context, loginId);
            daoMap.put(loginId, dao);
        }
        return dao;
    }

    private NewFriendManager(Context context, String uId) {
        clear();
//        this.mContecxt = context.getApplicationContext();
        this.uid = uId;
        String DBName = uId + ".dbLP";
//        this.litePalDB = LitePalDB.fromDefault(DBName);
        this.litePalDB = new LitePalDB(DBName, 4);
        litePalDB.addClassName(Friend.class.getName());
        litePalDB.addClassName(NewFriend.class.getName());
        litePalDB.addClassName(ChatDialog.class.getName());
        litePalDB.addClassName(Group2Chat.class.getName());
        litePalDB.addClassName(ChatMessage.class.getName());
        LitePal.use(litePalDB);
//        this.openHelper = new DaoMaster.DevOpenHelper(mContecxt, DBName, null);
    }

    /**
     * 清空资源
     */
    public void clear() {
//        if (openHelper != null) {
//            openHelper.close();
//            openHelper = null;
//            mContecxt = null;
        uid = null;
//        }
    }

//    private DaoSession openReadableDb() {
//        checkInit();
//        SQLiteDatabase db = openHelper.getReadableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        DaoSession daoSession = daoMaster.newSession();
//        return daoSession;
//    }
//
//    private DaoSession openWritableDb() {
//        checkInit();
//        SQLiteDatabase db = openHelper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        DaoSession daoSession = daoMaster.newSession();
//        return daoSession;
//    }
//
//    private void checkInit() {
//        if (openHelper == null) {
//            throw new RuntimeException("请初始化db");
//        }
//    }

    //-------------------------------------------------------------

    /**
     * 获取本地所有的邀请信息
     *
     * @return
     */
    public List<NewFriend> getAllNewFriend() {
//        NewFriendDao dao = openReadableDb().getNewFriendDao();
//        return dao.queryBuilder().orderDesc(NewFriendDao.Properties.Time).list();
        return DataSupport.findAll(NewFriend.class);
    }

    /**
     * 创建或更新新朋友信息
     *
     * @param info
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateNewFriend(NewFriend info) {
//        NewFriendDao dao = openWritableDb().getNewFriendDao();
//        NewFriend local = getNewFriend(info.getObjectId(), info.getTime());
        try {
            List<NewFriend> newFriends= DataSupport.findAll(NewFriend.class);
            boolean isExist = false;
            for(int i = 0; i< newFriends.size() ;i++ ){
                if(info.getObjectId() == newFriends.get(i).getObjectId()){
                    isExist = true;
                }
            }
            if (!isExist) {
                NewFriend newf = new NewFriend();
                newf = info;
                newf.save();
                return newf.getId();
            } else {
                return -1;
            }
        }catch(IllegalStateException e){
            L.e(e.getMessage());
            throw e;
        }
    }

    /**
     * 获取本地的好友请求
     *
     * @param uid
     * @return
     */
    private NewFriend getNewFriend(String uid) {
//        NewFriendDao dao = openReadableDb().getNewFriendDao();
//        return dao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(uid))
//                .where(NewFriendDao.Properties.Time.eq(time)).build().unique();
        return (NewFriend) DataSupport.where("objectId = ?", uid).find(NewFriend.class);
    }

    /**
     * 是否有新的好友邀请
     *
     * @return
     */
    public boolean hasNewFriendInvitation() {
        List<NewFriend> infos = getNoVerifyNewFriend();
        if (infos != null && infos.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取未读的好友邀请
     *
     * @return
     */
    public int getNewInvitationCount() {
        List<NewFriend> infos = getNoVerifyNewFriend();
        if (infos != null && infos.size() > 0) {
            return infos.size();
        } else {
            return 0;
        }
    }

    /**
     * 获取所有未读未验证的好友请求
     *
     * @return
     */
    private List<NewFriend> getNoVerifyNewFriend() {
//        NewFriendDao dao = openReadableDb().getNewFriendDao();
//        return dao.queryBuilder().where(NewFriendDao.Properties.Status.eq(Constants.STATUS_VERIFY_NONE))
//                .build().list();
        return DataSupport.select("status", String.valueOf(Constants.STATUS_VERIFY_NONE)).find(NewFriend.class);
    }

    /**
     * 批量更新未读未验证的状态为已读
     */
    public void updateBatchStatus() {
        List<NewFriend> infos = getNoVerifyNewFriend();
        if (infos != null && infos.size() > 0) {
            int size = infos.size();
//            List<NewFriend> all = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                NewFriend msg = infos.get(i);
                msg.setStatus(Constants.STATUS_VERIFY_READED);
//                all.add(msg);
                msg.update(msg.getId());
            }
//            insertBatchMessages(infos);
        }
    }

//    /**
//     * 批量插入消息
//     *
//     * @param msgs
//     */
//    public void insertBatchMessages(List<NewFriend> msgs) {
//        NewFriendDao dao = openWritableDb().getNewFriendDao();
//        dao.insertOrReplaceInTx(msgs);
//    }

    /**
     * 修改指定好友请求的状态
     *
     * @param friend
     * @param status
     * @return
     */
    public void updateNewFriend(NewFriend friend, int status) {
        friend.setStatus(status);
        friend.update(friend.getId());
    }

    /**
     * 删除指定的添加请求
     *
     * @param friend
     */
    public void deleteNewFriend(NewFriend friend) {
//        NewFriendDao dao = openWritableDb().getNewFriendDao();
//        dao.delete(friend);
        DataSupport.delete(NewFriend.class, friend.getId());
    }

}
