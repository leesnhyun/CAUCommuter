package sh.cau.commuter.Model;

/**
 * Created by SH on 2015-11-29.
 */
public interface Constant {

    int TAB_LIMIT_COUNT = 5;
    String DATA_ADDR = "http://happs.gtz.kr/data/transit.db";
    String DB_FILE = "transit.db";

    /// Splash Activity ///
    String LOADING = "서버로부터 데이터를 다운로드 중입니다.";
    String LOADING_FAIL = "서버에 접속하지 못하였습니다. (네트워크를 확인해주십시오.)";

    /// Main Activity ///
    String PATH = "경로";
    String TAB_LIMIT_ERROR = "더 이상 경로를 추가할 수 없습니다.";


}
