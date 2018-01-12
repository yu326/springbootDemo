package com.example.demo.sdk;

import com.example.demo.sdk.model.PostParameter;
import com.example.demo.sdk.model.WeiboException;
import com.example.demo.sdk.util.WeiboConfig;

/**
 * Created by koreyoshi on 2017/12/13.
 */
public class Yu extends Weibo {
    private static final long serialVersionUID = 4742830953302255953L;

    public Yu(String access_token) {
        this.access_token = access_token;
    }

    /**
     * get User's weibo lsit
     *
     * @param uid
     * @return
     * @throws WeiboException
     */
    public String getUserWeiboListById(String uid, int count, int page) throws WeiboException {
        return client.get(
                WeiboConfig.getValue("baseURL") + "statuses/user_timeline_batch.json",
                new PostParameter[]{new PostParameter("uids", uid), new PostParameter("count", count), new PostParameter("page", page)},
                access_token).toString();
    }

    /**
     * get a  Detailed weibo's data
     *
     * @param ids           IDs of the searched posts, should be less than 50, separated by half-width comma.
     * @param trimUser      Returned value for User. 0 for returning complete User value, 1 for just returning uid. Default as 0.
     * @param isGetLongText The switch for whether returning the full text if the number of text is greater than 140. 0: only return the text within 140 letters, 1:return the full text in the field longText. Default as 0.
     * @return
     * @throws WeiboException
     */
    public String getDetailedWeiboData(String ids, int trimUser, int isGetLongText) throws WeiboException {
        return client.get(
                WeiboConfig.getValue("baseURL") + "statuses/show_batch/biz.json",
                new PostParameter[]{new PostParameter("ids", ids), new PostParameter("trim_user", trimUser), new PostParameter("isGetLongText", isGetLongText)},
                access_token).toString();
    }


    /**
     *  search for posts containing a certain keyword.
     *  @http://open.weibo.com/wiki/C/2/search/statuses/limited-en
     *  notice
     *  关键词内容支持与、或、非。最好是5个词以内，并且最好一次使用一种语法；
     *  与：空格即可，如A B
     *  或：~表示，如A~B~C
     *  非：空格-，比如A -B，表示出现A,不出现B
     *  混用情况：(A B)~(C D)表示同时出现A,B或同时出现C,D， A B -C表示同时出现A,B但不出现C
     *  与、或、非的关键词不能包含除字母、数字外的其他特殊符号；
     *
     * @param dup            是否排重（不显示相似数据），0：否、1：是，默认为1。
     * @param antispam      是否反垃圾（不显示低质量数据），0：否、1：是，默认为1。
     * @param count          每页返回的数量，最小10，最大50。（默认返回10条）
     * @param starttime     搜索范围起始时间，取值为时间戳，单位为秒。
     * @param endtime       搜索范围结束时间，取值为时间戳，单位为秒。
     * @return
     * @throws WeiboException
     */
    public String getDataByKeyWord(String keyWord,int dup, int antispam, int count, long starttime, long endtime,int page) throws WeiboException {
        return client.get(
                WeiboConfig.getValue("baseURL") + "search/statuses/limited.json",
                new PostParameter[]{new PostParameter("q", keyWord),new PostParameter("dup", dup),new PostParameter("antispam", antispam),new PostParameter("count", count),new PostParameter("starttime", starttime),new PostParameter("endtime", endtime),new PostParameter("page", page)},
                access_token).toString();
    }


}
