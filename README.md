# Kotlin, React을 이용한 Project(Simple Blog)
> Simple Blog는 게시판 기능을 중심으로 구현한 간단한 블로그 프로젝트입니다.<br>
> Spring Boot, JPA, JWT, Spring Security를 활용해 개발하였으며, JWT 기반으로 사용자 인증을 구현했습니다.<br>
> 이전 프로젝트는 Java로 개발했지만, 이번에는 Kotlin을 사용해 간결한 문법, nullable 타입, null 안전성, 그리고 Java 코드와의 호환성 등 Kotlin의 장점을 활용했습니다.<br>
또한 기존의 서버 사이드 렌더링(JSP, Thymeleaf) 방식이 아닌 클라이언트 사이드 렌더링을 위해 React를 도입했습니다.
> 이를 통해 React와 Kotlin 간의 페이지 상호작용 방식을 고민하며 구현했습니다.<br>
<img width="2215" height="2378" alt="Image" src="https://github.com/user-attachments/assets/54ce2446-b878-4518-bcdc-6b0faa2bd715" />

## 목차
* [프로젝트 개요](#프로젝트-개요)
* [사용 기술](#사용-기술)
  * [백엔드](#백엔드)
  * [프론트엔드](#프론트엔드)
* [설계](#구조-설계)
  * [DB 설계](#DB-설계)
  * [API 설계](#API-설계)
* [개발내용](#개발내용)
* [실행화면](#실행화면)
* [후기](#후기)

## 프로젝트 개요
* 프로젝트 명 : Simple Blog
* 개발 인원 : 1명
* 개발 기간 : 2025.06.20 ~ 2025.08.11
* 주요 기능 :
  * 사용자 관리
    * Spring Security + JWT 기반 로그인/로그아웃
    * 회원 CRUD 기능
    * 유효성 검사<br>
      - 회원 가입 시 이메일 · 닉네임 중복 검사, 비밀번호 4글자 이상 체크<br>
      - 수정 시 닉네임 중복 검사, 비밀번호 변경 시 4글자 이상 · 변경안할 시 0글자)
  * 블로그
    * 게시판 CRUD 기능
    * 댓글 CRUD 기능
    * 페이징 처리
    * 게시글 목록 조회(회원 별 글 목록/전체 글 목록)
  * 추가 기능
    * 최근 가입 회원 목록(메인 화면)
    * 최근 게시 글 목록(메인 화면)
    * 로그인/비로그인 상태에 따른 UI · 기능 접근 제어
    
## 사용 기술
### 백엔드
* 주요 프레임워크 / 라이브러리
  * Spring Boot 3.5.0
  * Spring Data JPA
  * Kotlin JDSL
  * Spring Security
  * JWT
 
* Build Tool
  * Gradle Kotlin DSL (build.gradle.kts)
  * Spring Dependency Management Plugin
  * Java 17
 
* DataBase
  * MariaDB
  * JPA (Hibernate)
  * P6Sp

### 프론트엔드
* HTML/CSS
* React
* JavaScript
* Tailwind

## 구조 설계
### DB 설계
* 회원 테이블(테이블 명 : Member) <br>
<img width="527" height="306" alt="Image" src="https://github.com/user-attachments/assets/8ba3918c-533e-469a-a046-b8a4dbdfb378" /><br>
* 게시판 테이블(테이블 명 : Post) <br>
<img width="523" height="270" alt="Image" src="https://github.com/user-attachments/assets/8decdfae-272f-43e0-954d-942972291429" /><br>
* 댓글 테이블(테이블 명 : Comment)<br>
<img width="528" height="271" alt="Image" src="https://github.com/user-attachments/assets/f2bc29ed-2b0c-46f9-8cd1-7f5414b25e87" /><br>


### API 설계
* 회원 관련 API<br>
<img width="579" height="533" alt="Image" src="https://github.com/user-attachments/assets/0f0f8926-881a-4e2d-be13-0865986faf53" /><br>
* 게시판 관련 API<br>
<img width="578" height="286" alt="Image" src="https://github.com/user-attachments/assets/feb3a464-a3e0-408e-863e-edd47cedaba5" /><br>
* 댓글 관련 API<br>
<img width="579" height="182" alt="Image" src="https://github.com/user-attachments/assets/8e83e3b1-99da-47bb-8c19-f68ff600c385" /><br>


## 개발내용
* 백엔드 구현
  * [Spring Security와 JWT를 활용한 회원 인증·인가 구현](https://luckygirljinny.tistory.com/442)
  * [백엔드 유효성 검사 구현](https://luckygirljinny.tistory.com/444)
  * [백엔드 접근 권한 제어](https://luckygirljinny.tistory.com/446)
  * 게시판·댓글·회원 CRUD 기능 구현
    * [게시판 CRUD 기능 구현](https://luckygirljinny.tistory.com/449)
    * [댓글 CRUD 기능 구현](https://luckygirljinny.tistory.com/450)
    * [회원 CRUD 기능 구현](https://luckygirljinny.tistory.com/448)
  * [Kotlin(Spring Boot)과 React 연동 방법](https://luckygirljinny.tistory.com/452)
  * [RESTful API 설계 및 구현](https://luckygirljinny.tistory.com/454)
  * [Kotlin JDSL을 활용한 동적 쿼리 작성](https://luckygirljinny.tistory.com/456)
  * [전역 예외 처리 및 예외 클래스 관리](https://luckygirljinny.tistory.com/457)
  * [응답 표준화(공통 API Response 구조 설계)](https://luckygirljinny.tistory.com/458)
  * [CORS 정책 설정](https://luckygirljinny.tistory.com/459)
  * [데이터베이스 설계 및 최적화(ERD 설계, 인덱스, N+1 문제 해결)](https://luckygirljinny.tistory.com/460)
 
* 프론트엔드 구현
  * [프론트엔드 유효성 검사 로직 구현](https://luckygirljinny.tistory.com/444)
  * [React 컴포넌트를 활용한 페이징 처리](https://luckygirljinny.tistory.com/451)
  * [프론트엔드 접근 권한 제어](https://luckygirljinny.tistory.com/445)
    

## 실행화면
<details>
<summary>메인페이지(게시판 목록)</summary>
  <br>
  <strong>메인 페이지 </strong>
  <ul>
    <li> 메인 페이지에서는 최신 게시글 목록, 최근 가입 멤버의 목록이 보입니다.</li>
  </ul>
 <img width="2215" height="2378" alt="Image" src="https://github.com/user-attachments/assets/7e076a3d-a0e6-4921-adc7-97ab7470638e" /><br>
</details>

<details>
<summary>게시판 관련 화면</summary>
  <br>
  <strong>1. 글 작성 화면</strong>
  <ul>
    <li> 로그인 한 사용자만 이용이 가능합니다.</li>
    <li> header에 글 쓰기 버튼을 통해 글 쓰기 Form을 통해 접근할 수 있습니다.</li>
    <li> 글 작성 시에는 제목과 내용이 입력되어야지만 작성이 가능하며 작성 완료가 되면 작성한 글의 상세페이지로 넘어갑니다.</li>
  </ul>
  ![Image](https://github.com/user-attachments/assets/794739ea-69c0-4013-bbdd-f8aafb69741f)
  <br>

  <br>
  <strong>2. 글 상세 화면</strong>
  <ul>
    <li> 글 목록 또는 글 작성 후에 상세 페이지를 통해 접근 가능하며, 비로그인 회원도 이용 가능 합니다.</li>
    <li> 글 상세페이지에는 작성된 글과 내용이 보이며, 글 작성자일 경우에는 해당 글 '수정', '삭제' 버튼이 보입니다. </li>
  </ul>
  ![Image](https://github.com/user-attachments/assets/e49852d7-647a-42d9-8eaa-49a5ad49ddd7)
  <br>

  <br>
  <strong>3. 글 목록 화면</strong>
  <ul>
    <li> 목록 페이지는 비로그인 회원도 이용 가능합니다.</li>
    <li> 목록은 회원별 블로그에서 목록을 볼 수 도 있으며, 메뉴에서 '전체 글' 버튼을 통해 전체 회원이 쓴 글을 볼 수도 있습니다. </li>
    <li> 회원 블로그일 경우 상단에 해당 회원의 '닉네임님의 블로그'라 안내하며, 목록에는 제목과 내용이 보입니다.</li>
    <li> 전체 회원글 목록일 경우 상단에 '전체 회원 게시물'이라 써져 있으며, 목록에는 제목, 내용, 글 작성자의 블로그로 이동 할 수 있는 버튼이 보입니다.</li>
    <li> 목록은 페이징처리하였습니다.</li>
  </ul>
  ![Image](https://github.com/user-attachments/assets/c9e74a03-9941-460d-89e8-2a289096973b)
  <hr>
  ![Image](https://github.com/user-attachments/assets/3bef6d87-15fc-4ffa-913d-3e452b48235f)
  <br>

  <br>
  <strong>4. 글 수정 화면</strong>
  <ul>
    <li> 글 수정 Form 페이지는 글 상세페이지에서 '수정'버튼을 통해 접근할 수 있습니다.</li>
    <li> 글 수정 페이지는 글을 작성한 본인만 수정이 가능합니다.</li>
    <li> 글 수정이 완료되면 글 상세페이지로 이동합니다.</li>
  </ul>
  ![Image](https://github.com/user-attachments/assets/2ff5a919-a2a9-43f3-94a5-66c85d629909)
  <br>

  <br>
  <strong>5. 글 삭제 화면</strong>
  <ul>
    <li> 글 삭제는 글 상세 화면에서 '삭제'버튼을 통해 삭제가 가능합니다.</li>
    <li> 글 삭제는 글 작성자 본인만 가능합니다.</li>
    <li> 글 삭제 완료 후에는 목록으로 이동합니다.</li>
  </ul>
  <img width="739" height="446" alt="Image" src="https://github.com/user-attachments/assets/c083839f-b9c3-4ed2-8d22-35278f6e3735" />
  <br>
  
</details>

<details>
<summary>댓글 관련 화면</summary>
  <br>
  <strong>1. 댓글 작성 화면</strong>
  <ul>
    <li> 댓글은 글 상세 화면에서 작성 가능하며, 로그인한 사용자만 댓글 쓰기가 가능합니다.</li>
    <li> 댓글 작성 후에는 댓글을 작성한 게시물의 상세 화면으로 이동하며, 작성된 댓글은 게시물 상세 화면 하단에 목록에서 볼 수 있습니다.</li>
  </ul>
   ![Image](https://github.com/user-attachments/assets/8a53b654-428f-4a95-aef9-6717b4029acf)
   <br>

  <br>
  <strong>2. 댓글 목록 화면</strong>
  <ul>
    <li> 댓글 목록은 비로그인 사용자도 볼 수 있습니다.</li>
    <li> 댓글 목록은 게시글 상세 화면 하단에 확인 가능합니다.</li>
    <li> 댓글 목록에서 본인이 쓴 댓글에는 '수정', '삭제'버튼이 보입니다.</li>
  </ul>
   ![Image](https://github.com/user-attachments/assets/e3f71f56-1c17-4ca2-812e-0efffbe459c6)
   <br>

  <br>
  <strong>3. 댓글 수정 화면</strong>
  <ul>
    <li> 댓글 수정은 본인이 쓴 댓글만 수정이 가능합니다.</li>
    <li> 댓글 목록에서 '수정'버튼을 눌러 수정이 가능하며, '수정'버튼을 누르면 해당 댓글 있는 자리에 textarea로 변경되어 수정이 가능합니다.</li>
    <li> 댓글 수정이 완료되면 댓글 수정한 게시물의 상세 화면으로 이동합니다.</li>
  </ul>
   ![Image](https://github.com/user-attachments/assets/aa32c829-6f81-4133-bcd9-a0eaaeb9bad3)
   <br>

  <br>
  <strong>4. 댓글 삭제 화면</strong>
  <ul>
    <li> 댓글 삭제는 본인이 쓴 댓글만 삭제가 가능합니다.</li>
    <li> 댓글 삭제가 완료가되면 댓글 삭제한 게시물의 상세 화면으로 이동합니다.</li>
  </ul>
   <img width="743" height="574" alt="Image" src="https://github.com/user-attachments/assets/ad511a3f-e40a-4987-a9be-6aa8cbb2e1ab" />
   <br>
  
 <br>
 
</details>

<details>
<summary>사용자 관련 화면</summary>
  <br>
  <strong>1. 회원 가입 화면</strong>
  <ul>
    <li> header에 '회원가입'버튼을 통해 회원가입 Form 화면으로 이동이 가능합니다. </li>
    <li> 이메일, 닉네임, 비밀번호를 입력해야지만 회원 가입이 가능하며, 가입 시 이메일, 닉네임 중복이아니고, 비밀번호는 4자리 이상이어야지마 가입할 수 있도록 했습니다.</li>
    <li> 가입이 완료되면 로그인 form 화면으로 이동합니다.</li>
  </ul>
  <img width="1112" height="377" alt="Image" src="https://github.com/user-attachments/assets/9cbd00c5-5959-4efb-95b6-d3f8211459e6" />
  <br>

  <br>
  <strong>2. 로그인 화면</strong>
  <ul>
    <li> header에 '로그인'버튼을 통해 로그인 Form 화면으로 이동이 가능합니다.</li>
    <li> 이메일과 비밀번호를 입력해야지만 로그인이 가능합니다.</li>
    <li> 로그인이 완료되면 메인 화면으로 이동합니다.</li>
  </ul>
  <img width="1111" height="274" alt="Image" src="https://github.com/user-attachments/assets/aa12ffed-35d2-46a8-86ce-0cbd506b9d43" />
  <br>

  <br>
  <strong>3. 회원 정보 수정 화면</strong>
  <ul>
    <li> 로그인을 하면 header에 '내 정보'버튼이 보이는데 '내 정보'버튼을 통해 회원 정보 수정 form 화면으로 이동하며, input에는 이메일과 닉네임 정보가 보입니다.</li>
    <li> 닉네임과 비밀번호만 변경이 되고 이메일은 수정 불가합니다.</li>
    <li> 닉네임 변경시에는 중복이 아니어아니여야지만 변경이 되며, 비밀번호 변경 시에는 4글자 이상이어야지만 변경이됩니다.</li>
    <li> 닉네임만 변경하고 비밀번호 변경을 아니할 때는 비밀번호는 빈 칸으로 놔두고 변경하면 됩니다.</li>
    <li> 변경 후에는 변경된 정보를 가지고 정보 수정 form으로 화면이 보입니다.</li>
  </ul>
  <img width="1111" height="377" alt="Image" src="https://github.com/user-attachments/assets/4ac652e4-7ef5-4f76-91b6-49f0184cfdff" />
  <br>

  <br>
  <strong>4. 회원 탈퇴 화면</strong>
  <ul>
    <li> 회원 수정 form 하단에 회원 탈퇴 버튼을 통해 탈퇴 화면으로 이동합니다.</li>
    <li> 탈퇴 화면에는 탈퇴 시 안내사항에 동의 후에 탈퇴가 가능하며 탈퇴가 완료된 후에는 메인 화면으로 이동합니다.</li>
  </ul>
  <img width="1112" height="239" alt="Image" src="https://github.com/user-attachments/assets/e70758d2-c737-4a7a-9bab-5a7155ba917e" />
  <br>
  
</details>



## 후기
이번 프로젝트는 기존에 진행했었던 Java 기반의 JSP, Thymeleaf 프로젝트와 달리, Kotlin과 React를 도입하여 진행했다는 점에서 큰 변화가 있었습니다.<br>
이전에는 서버와 화면이 강하게 결합된 JSP, Thymeleaf 방식을 사용했지만, 이번에는 프론트엔드와 백엔드를 완전히 분리하여 RESTful한 API를 기반으로 통신하도록 설계했습니다.<br><br>

프로젝트를 시작하기 전, 먼저 Kotlin을 학습하여 Java와 높은 호환성을 유지하면서도 더 간결하고 안전한 문법을 익혔습니다. 예를 들어, data class를 이용해 DTO를 간단히 정의하거나, 널 안정성(Null Safety)을 통해 런타임 오류를 줄이는 등의 장점을 실제 코드에 적용했습니다. Java에서 사용하던 라이브러리와 스프링 부트 설정을 그대로 활용할 수 있어, 새로운 언어를 도입하면서도 기존 경험을 살릴 수 있었습니다.<br>
이후 React를 학습하여 프론트엔드 개발을 진행했는데, 기존 서버 렌더링 방식과 달리 컴포넌트 기반 개발을 경험하면서 화면 구성과 데이터 관리의 분리를 체감할 수 있었습니다. API 호출과 UI 컴포넌트를 명확하게 분리해 유지보수성을 높였고, 필요한 데이터만 가져와 렌더링하는 효율적인 구조를 만들었습니다. 예를 들어, 메인 페이지에서 최근 회원 목록과 게시글 목록을 각각 독립된 API로 호출하고, MembersItem과 같은 재사용 가능한 UI 컴포넌트를 구성했습니다.<br><br>

또한, 지난 프로젝트에서는 RESTful 구조를 지키지 못했던 점이 아쉬웠기에, 이번에는 URL 설계, HTTP 메서드 사용, 응답 형식을 일관성 있게 관리하는 데 특히 신경을 썼습니다. React 측에서도 API 호출 로직을 memberApi, postApi 등으로 모듈화하여 화면 로직과 데이터 로직을 분리하는 구조를 적용했습니다. 이를 통해 프로젝트 구조가 명확해지고, 기능 확장 시에도 변경 범위를 최소화할 수 있었습니다.<br>

협업 경험을 살리기 위해 GitHub 연동에도 신경을 썼습니다. 지난번 팀 프로젝트에서 GitHub를 사용한 경험이 있었기에, 이번에는 혼자서도 체계적인 버전 관리를 해보고자 했습니다. 주요 기능이 완성될 때마다 커밋을 작성해 업로드하며, 변경 내역을 기록하고 필요 시 이전 버전으로 쉽게 되돌릴 수 있도록 했습니다.<br><br>

이번 프로젝트는 새로운 기술 스택을 도입하면서도 기존 경험과 장점을 잘 접목시킨 좋은 경험이었으며, 학습–적용–리팩터링의 과정을 통해 기술적 역량과 프로젝트 관리 능력을 함께 향상시킬 수 있었습니다.
