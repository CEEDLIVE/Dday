# Introduction

Toy Project
Author: ceedlive

## Development Environment
+ Windows 10
+ Android Studio 3.3.2
+ Gradle
+ GitHub
    + Repository: https://github.com/CEEDLIVE/Dday.git

## Test Devices
+ Galaxy S6

## Development Step (TODO)
1. 화면 구성  
- [ ] 확장 리스트뷰 화면 (스크롤뷰)  
- [ ] 상세(등록/수정) 화면  
    - [ ] 캘린더  
    - [ ] 디데이 계산  

2. 데이터 핸들링 (SharedPreferences 사용, 이후 SQLite, 외부 DB 연동 순으로 리팩토링 예정)  
- [ ] 조회  
    - [ ] 확장 리스트뷰 구현  
- [ ] 저장  
    - [ ] 데이터 유효성 체크  
- [ ] 수정  
    - [ ] 액티비티 전환, 액티비티 간 데이터 처리  
    - [ ] 데이터 유효성 체크  
- [ ] 삭제  
    - [ ] 다이얼로그 디스플레이  
        - [ ] 커스텀 다이얼로그 제작  
    - [ ] 확인 버튼 클릭 시 해당 디데이 일정 삭제  

3. 기타
- [ ] 앱 이름
- [ ] 앱 아이콘
- [ ] 스플래시 화면: 앱이 시작할 때 잠깐 표시되는 스플래시 화면
- [ ] 난독화
- [ ] 코드 리팩토링

4. 마무리
- [ ] 앱 배포/플레이 스토어 등록
    - [ ] 서명 키 생성


## Follow Field Naming Conventions

안드로이드 오픈소스는 아래와 같은 Naming Convention 을 따릅니다.

+ Non-public, non-static field names start with m. (public, static이 아닌 것에는 m을 붙여라.(m은 멤버변수의 m입니다.))
+ Static field names start with s. (static에는 s를 붙여라)
+ Other fields start with a lower case letter. (나머지 모든 필드에는 소문자로 시작한다.)
+ Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES. (public static final fields 에는 _를 붙히고 모든 문자를 대문자로 써라)

```java
//example
public class MyClass {
    public static final int SOME_CONSTANT = 42;
    public int publicField;
    private static MyClass sSingleton;
    int mPackagePrivate;
    private int mPrivate;
    protected int mProtected;
}
```

http://source.android.com/source/code-style.html#follow-field-naming-conventions 위주소에 들어가보시면 더 자세한 규칙들을 보실 수 있어요.