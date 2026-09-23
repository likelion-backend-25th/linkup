# 실행 및 테스트 방법

## 테스트
```bash
# 앱과 DB 테스트 실행시
docker compose up -d

# DB만 필요할 겯우
docker compose up -d mysql

# 컨테이너 삭제시
docker compose down

# 볼륨 삭제시
docker volume rm linkup_mysql-data
```