package cn.vpclub.youzanorder;

import cn.vpclub.spring.boot.cors.autoconfigure.CorsConfiguration;
import cn.vpclub.spring.boot.cors.autoconfigure.CorsProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties({CorsProperties.class})
@MapperScan("cn.vpclub.youzanorder.mapper*")
public class YouzanOrderCommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(YouzanOrderCommandApplication.class, args);
	}
	@Bean
	public CorsConfiguration corsConfiguration(){
		return new CorsConfiguration();
	}
}
