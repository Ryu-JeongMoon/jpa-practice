package org.example.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@SpringBootApplication
public class BasicShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicShopApplication.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		//hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5Module;
	}
}

/*
Global 설정으로 jackson-datatype-hibernate 라이브러리를 이용해
Lazy Loading 을 강제로 끌고 오게 하는데 이럴라면 Lazy Loading 거는 의미가 없다

Hibernate5Module 을 사용하면 Proxy 인 애들은 안 뿌리고
들어있는 애들만 뿌리게 해준다
 */