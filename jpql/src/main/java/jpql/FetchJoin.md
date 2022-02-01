Fetch Join 특징과 한계

페치 조인 대상에는 별칭을 줄 수 없다<br>
hibernate 는 가능하지만 표준에 없으므로 가급적 사용하지 말 것

둘 이상의 컬렉션은 페치 조인할 수 없다<br>
데이터 뻥튀기 문제

컬렉션 페치 조인 시 페이징 API 사용 불가<br>
일대일 / 다대일의 경우에는 페치 조인해도 데이터 수가 변하지 않으므로 가능하지만 일대다 / 다대다의 경우에는 데이터 뻥튀기 되고 여기다가 페이징 걸면<br>
teamA - member1<br>
teamA - member2<br>
teamA - member3<br>
teamB - member4<br>
teamB - member5 ...<br>

형식으로 결과가 나오는데 2개만 보고 싶다할 때 우리가 원하는 결과는 teamA, teamB 에 대한 정보일테지만
teamA - member1 / member2 에 대한 정보만 나오게 된다 <br>
특히 이는 hibernate 가 경고 로그 뿜으면서 메모리에서 처리해버리기 때문에 저장된 데이터가 아주 많은 경우에 애플리케이션 죽을수도 있음