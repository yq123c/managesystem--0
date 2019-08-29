package yeqiu.springboot.shiroconf;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author LC
 *创建时间：2019年8月2日 下午3:24:30
 *密码加密
 */
public class PasswordHelper {
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    public static final String ALGORITHM_NAME = "md5"; // 基础散列算法
    public static final int HASH_ITERATIONS = 2; // 自定义散列次数
    /**
     * 加密
     */
    public String encryptPassword(String password) {
        // 随机字符串作为salt因子，实际参与运算的salt我们还引入其它干扰因子
        String salt = randomNumberGenerator.nextBytes().toHex();
        String newPassword = new SimpleHash(ALGORITHM_NAME, password,
                ByteSource.Util.bytes(salt), HASH_ITERATIONS).toHex();
        return newPassword;
    }
}
