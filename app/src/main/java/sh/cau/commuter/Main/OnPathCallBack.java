package sh.cau.commuter.Main;

/**
 * Created by SH on 2015-12-11.
 */
public interface OnPathCallBack {

    void success(int elapsed, int distance);
    void fail();

}
