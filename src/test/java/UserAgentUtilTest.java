import com.qianfeng.UserAgentUtil;

/**
 *
 *
 */
public class UserAgentUtilTest {
    public static void main(String[] args) {
        System.out.println(UserAgentUtil.parserUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36"));
        System.out.println(UserAgentUtil.parserUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36"));

    }
}
