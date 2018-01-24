node {

    stage ('소스체크아웃') {
        checkout([$class: 'GitSCM',
            branches: [[name: '*/master']],
            doGenerateSubmoduleConfigurations: false, extensions: [],
            submoduleCfg: [],
            userRemoteConfigs: [[credentialsId: 'DevPro', url: 'https://devpro.ktds.co.kr/msa/oauth.git']]])
    }

    stage ('스크립팅'){
        sh 'ssh ec2-user@ip-172-31-2-237 "mkdir -p /home/ec2-user/oauth/log"'
        sh 'scp ./start.sh ec2-user@ip-172-31-2-237:/home/ec2-user/oauth'
        sh 'scp ./shutdown.sh ec2-user@ip-172-31-2-237:/home/ec2-user/oauth'
        sh 'ssh ec2-user@ip-172-31-2-237 "chmod a+x /home/ec2-user/oauth/*.sh"'
    }

    stage ('서버 정지'){
        sh 'ssh ec2-user@ip-172-31-2-237 "/home/ec2-user/oauth/shutdown.sh || true"'
    }

    stage ('빌드') {
        sh './gradlew clean build'
    }

    stage ('배포') {
        sh 'scp build/libs/oauth-1.0.0-RELEASE.jar ec2-user@ip-172-31-2-237:/home/ec2-user/oauth'
    }

    stage ('서버 시작') {
        sh 'ssh ec2-user@ip-172-31-2-237 "/home/ec2-user/oauth/start.sh /home/ec2-user/oauth/oauth-1.0.0-RELEASE.jar prd"'
    }

}