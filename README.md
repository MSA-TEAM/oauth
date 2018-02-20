# OAuth Server 

## Spring Profile

- prd : 운영 환경 프로파일
- dev : 개발 환경 프로파일
- ide : 개발 모드 프로파일 

## RSA Key Generation

1. keytool -genkeypair \
       -alias sicc \
       -keyalg RSA \
       -dname "CN=Auth,OU=,O=sicc,L=Seoul,S=Seoul,C=KR" \
       -keypass new1234! \
       -keystore sicc.jks \
       -storepass new1234!
1. keytool -export -keystore ./sicc.jks -alias sicc -file sicc.cer
1. openssl x509 -inform der -in ./sicc.cer -pubkey -noout