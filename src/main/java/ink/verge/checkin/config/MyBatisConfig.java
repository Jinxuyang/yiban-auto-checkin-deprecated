package ink.verge.checkin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("ink.verge.yiban_auto_checkin.mbg.mapper")
public class MyBatisConfig {
}
