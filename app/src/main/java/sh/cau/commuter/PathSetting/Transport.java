package sh.cau.commuter.PathSetting;

/**
 * Created by SH on 2015-12-10.
 */
public class Transport {

    private String method;
    private String line;
    private String start;
    private String dest;

    public Transport(String method, String line ){
        this.method = method;
        this.line = line;
    }

    public Transport(String method, String line, String start, String dest ){
        this.method = method;
        this.line = line;
        this.start = start;
        this.dest = dest;
    }

    public String getMethod(){ return this.method; }
    public String getLine(){ return this.line; }
    public String getStart(){ return this.start; }
    public String getDest(){ return this.dest; }

}
