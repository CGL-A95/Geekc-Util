package idUtil;

/**
 * @desc id生成工具类
 * @author wan.changxuan
 */
public class IdPreUtil {

    //private static final UniqueId id = (UniqueId) JsonConfig.getJsonConfig().get("unique_seed_id");
    private static final UniqueId id = new UniqueId( 1);

    /**
     * 生成唯一字符串id
     * @return
     */
    public static String nextString(){
        return id.nextString();
    }

    /**
     * 生成唯一数字id
     * @return
     */
    public static Long nextInt(){
        return id.nextInt();
    }

}
