package sh.cau.commuter.Maps;

/**
 * Created by SH on 2015-12-01.
 */
public class SubwayStation implements CommuteLocation {

    private String line;
    private String stnid;
    private String gpsX;
    private String gpsY;
    private String stnName;

    public SubwayStation(String stnid, String gpsX, String gpsY, String stnName, String line){
        this.stnid = stnid;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.stnName = stnName;
        this.line = line;
    }


    @Override
    public String getStnId() {
        return this.stnid;
    }

    @Override
    public String getGpsXcord() {
        return gpsX;
    }

    @Override
    public String getGpsYcord() {
        return gpsY;
    }

    @Override
    public String getStnName() {
        return stnName;
    }

    public String getLine(){

        switch(this.line){
            case "A" : return "공항철도";
            case "B" : return "분당선";
            case "E" : return "에버라인";
            case "G" : return "경춘선";
            case "I" : return "인천선";
            case "K" : return "경의.중앙선";
            case "S" : return "신분당선";
            case "SU" : return "수인선";
            case "U" : return "의정부경전철";
        }

        return this.line;
    }

}
