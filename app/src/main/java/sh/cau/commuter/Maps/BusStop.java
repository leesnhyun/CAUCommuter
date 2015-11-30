package sh.cau.commuter.Maps;

/**
 * Created by SH on 2015-11-30.
 */
public class BusStop {

    private String stnid;
    private String gpsX;
    private String gpsY;
    private String stnName;


    public BusStop(String stnid, String gpsX, String gpsY, String stnName){
        this.stnid = stnid;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.stnName = stnName;
    }

    public String getStnId(){
        return this.stnid;
    }

    public String getGpsXcord(){
        return this.gpsX;
    }

    public String getGpsYcord(){
        return this.gpsY;
    }

    public String getStnName(){
        return  this.stnName;
    }

}
