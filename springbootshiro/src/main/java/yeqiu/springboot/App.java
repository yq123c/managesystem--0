package yeqiu.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot启动类
 * @author 陆昌
 * @time 2019年6月21日上午10:02:36
 * 说明：
 */
@SpringBootApplication
@MapperScan("yeqiu.*.dao")
public class App 
{
    public static void main( String[] args ){
        SpringApplication.run(App.class, args);
    }
}
