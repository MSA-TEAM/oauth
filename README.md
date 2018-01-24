# AI Developer Portal OAuth Server 

## Spring Profile

- prd : 운영 환경 프로파일
- dev : 개발 환경 프로파일
- ide : 개발 모드 프로파일 

## RSA Key Generation

1. keytool -genkeypair \
       -alias ktds \
       -keyalg RSA \
       -dname "CN=Auth,OU=,O=ktds,L=Seoul,S=Seoul,C=KR" \
       -keypass new1234! \
       -keystore ktds.jks \
       -storepass new1234!
1. keytool -export -keystore ./ktds.jks -alias ktds -file ktds.cer
1. openssl x509 -inform der -in ./ktds.cer -pubkey -noout