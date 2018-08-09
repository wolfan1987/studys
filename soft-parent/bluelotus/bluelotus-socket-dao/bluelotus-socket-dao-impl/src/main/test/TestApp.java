import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuqiyang on 2017/9/20。
 */
public class TestApp {

    public static void main(String[] args) {

        Map<String, Boolean> map = new HashMap<>();

        Boolean resutl = false;

        map.put("key",resutl);

        resutl = true;

        System.out.println("map = " + map.get("key"));


    }
}

