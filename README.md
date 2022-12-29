# 더 나이트 (The Knight)

## 1. 서비스 소개

---

### **서비스 개요**

- 자신의 손에 있는 무기의 정체를 숨기면서 상대 팀의 리더를 찾아 공격하는 턴제 심리 전략 게임
- 진행 기간 : 2022.10.11 ~ 2022.11.18 (6주)

### 기획 의도

- 두뇌/전략 게임을 직접 플레이 하고 싶어하는 사람들의 수요를 충족시켜주는
- 기존의 온라인/오프라인 진행 방식의 한계를 개선한
- 웹 기반 온라인 턴제 전략 게임

### 서비스 화면

**1. 게임 초기 화면 (게임설명, 로비, 랭킹)**
<img src=./img/intro.gif>

**2. 게임 대기방 (팀 선택 및 게임 시작)**
<img src=./img/room.gif>

**3. 무기 및 순서 선택**
<img src=./img/weapon.gif>

**4. 게임 플레이 화면 (공격, 방어, 의심 선택)**
<img src=./img/play.gif>

### 팀원 소개

<img src=./img/1.png>

## 2. 설계

---

### Architecture

<img src=./img/2.png>

### ERD

<img src=./img/3.png>

### 와이어 프레임

[https://www.figma.com/file/AryONRgFRD5QA2v8O7L0XO/%EC%9E%90%EC%9C%A8%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-team-library?t=hlLl3ZevFRoJ8pml-6](https://www.figma.com/file/AryONRgFRD5QA2v8O7L0XO/%EC%9E%90%EC%9C%A8%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-team-library?t=hlLl3ZevFRoJ8pml-6)

<img src=./img/4.png>

### API 명세

[https://www.notion.so/API-f4c5e99a8a2549c3b699c5926af7c7c7](https://www.notion.so/API-f4c5e99a8a2549c3b699c5926af7c7c7)

<img src=./img/5.png>

## 3. 기술 스택

---

### Frontend

<img src=./img/6.png>

- react : `18.2.0`
- react-redux : `8.0.4`
- redux-toolkit : `1.8.6`
- sockjs-client : `1.6.1`
- stompjs : `2.3.3`
- react-spring : `9.5.5`
- mui/material : `5.10.10`

### Backend

<img src=./img/7.png>

- Spring boot : `2.7.5`
- QueryDSL : `1.0.10`
- redisson : `3.18.0`
- sockJs-client : `1.5.1`
- stomp : `1.2`
- mariaDB : `10.9.3`
- redis : `7.0.5`

### Infra

<img src=./img/8.png>

- ubuntu : `20.04`
- jenkins : `2.361.2`
- docker : `20.10.21`
- nginx :  `1.22.1`

### IDE

<img src=./img/9.png>

## 4. 협업 도구

---

### Git

- Git Flow를 브랜치 전략으로 선정
- Develop 브랜치와 Master 브랜치에 MR을 Merge하면, GitLab Webhook이 발생하고 Jenkins를 이용하여 자동 배포 환경 구현
- Master 브랜치에 Merge되는 순간 Docker Image를 Run하여 자동 배포
- Git commit convention

```markdown
- ✨ feat : 새로운 기능 추가
- 🍋 modify : 약간의 코드 수정 (코드 리뷰 이후 등)
- 🐛 fix : 버그 수정
- 📝 docs : 문서 수정
- 💄 style : 코드 포매팅, 세미콜론 누락, 코드 변경이 없는 경우
- ♻ refactor : 코드 리펙토링 (로직 수정 X)
- ✅ test : 테스트 코드, 리펙토링 테스트 코드 추가
- 🔨 chore : 빌드 업무 수정, 패키지 매니저 수정
- 🔧 config: 환경설정 파일 추가 및 수정
```

### Jira

- 개발 일정 관리

<img src=./img/10.png>

<img src=./img/11.png>

### Notion

[https://www.notion.so/MAIN-83661bff425c486dbea9aa9ded3144e3](https://www.notion.so/The-Knight-83661bff425c486dbea9aa9ded3144e3)