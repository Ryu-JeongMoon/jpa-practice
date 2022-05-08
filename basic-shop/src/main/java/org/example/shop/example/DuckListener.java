package org.example.shop.example;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

public class DuckListener {

	@PrePersist
	private void prePersist(Object obj) {
		System.out.println("DuckListener.prePersist obj = " + obj);
	}

	@PostPersist
	private void postPersist(Object obj) {
		System.out.println("DuckListener.postPersist obj = " + obj);
	}
}

/*
Domain 에 직접 이벤트 리스너 처박는 것보다 책임의 분리를 생각하여
Listener 만들고 얘를 등록시키는 것이 훨씬 낫다
책임 분리와 유지보수!

특정 타입에만 적용되는 리스너라면 타입 명시하여 parameter 로 받을 수 있다
특정 타입이 아닌 경우에는 Object 로 받아서 처리
 */