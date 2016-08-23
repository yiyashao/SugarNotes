package com.techosoft.idea.sugarnote.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.techosoft.idea.sugarnote.model.Reading;

/**
 * Created by david on 2016/8/23.
 */
public class CloudAgent extends ContextWrapper {

    private MyConst mConst;
    private MyHelper myHelper;

    public CloudAgent(Context base){ //use contextWrapper to handle the pass of context
        super(base);
        //initialize objects
        mConst = new MyConst();
        AVOSCloud.initialize(this, mConst.CLOUD_KEY_01, mConst.CLOUD_KEY_02); //initilize the cloud service
        myHelper = new MyHelper(base);
    }



/*    public void saveWantItemInGiveList(String itemCloudId, int userId){
        final AVObject giveListLink = new AVObject(mConst.TABLE_GIVE_BOX);
        giveListLink.put(mConst.GIVE_BOX_GIVER_ID, userId); //int value
        giveListLink.put(mConst.GIVE_BOX_WANT_ITEM_OBJID, itemCloudId); //String value
        try{
            giveListLink.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        // 存储成功
                        myHelper.displayToast("item saved into Give Box");
                        // 保存成功之后，objectId 会自动从服务端加载到本地
                    } else {
                        Log.d(mConst.LOG_TAG, "failed save item to Give Box");
                        // 失败的话，请检查网络环境以及 SDK 配置是否正确
                    }
                }
            });
        }catch (Exception e){
            Log.d(mConst.LOG_TAG, "failed save item to Give Box with Exception " + e.toString() );
        }

    }*/



    public void saveBloodReading(Reading reading){
        final AVObject readingRecord = new AVObject(mConst.TABLE_BLOOD_RECORD);
        readingRecord.put(mConst.BLOOD_RECORD_READING, reading.reading);
        readingRecord.put(mConst.BLOOD_RECORD_NOTE, reading.note);
        readingRecord.put(mConst.BLOOD_RECORD_TIME, reading.timeStamp);
        readingRecord.put(mConst.BLOOD_RECORD_UNIT, reading.unit);
        readingRecord.put(mConst.BLOOD_RECORD_USERID, reading.userId);
        readingRecord.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 存储成功
                    Log.d(mConst.LOG_TAG, "want item saved with objId: " + readingRecord.getObjectId());
                    // 保存成功之后，objectId 会自动从服务端加载到本地
                } else {
                    Log.d(mConst.LOG_TAG, "failed save want_item");
                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                }
            }
        });
    }

   /* public void getAvObject(){
        AVQuery<AVObject> query = new AVQuery<>("Todo");
        query.whereEqualTo("number", 2);
        // 如果这样写，第二个条件将覆盖第一个条件，查询只会返回 priority = 1 的结果
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                List<AVObject> priorityEqualsZeroTodos = list;// 符合 priority = 0 的 Todo 数组
                Log.d("LeanCloud", "ok, found some records " + list.size());
                AVObject oneItem = list.get(0);
                setMessageValue(oneItem.get("content").toString());
            }
        });
    }*/


}