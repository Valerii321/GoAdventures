pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-11'
      args '--network=jenkins_local'
    }

  }
  stages {
    stage('get git') {
      steps {
        git(url: 'https://github.com/Valerii321/GoAdventures', branch: 'develop')        
      }
    }
    stage('build & SonarQube analysis') {
      steps {        
        withSonarQubeEnv('Sonar') {
//          sh 'cd server/goadventures/ && mvn install -Dmaven.test.skip=true'
          sh 'cd server/goadventures/ && ${mvnCmd} clean package sonar:sonar -Dsonar.host.url=http://localhost:9000 -DskipTests=true'
//          sh 'cd server/goadventures/ && mvn clean package sonar:sonar -Dsonar.host.url=http://127.0.0.1:9000 -DskipTests=true"'
          }
        }
      }
    }
  post {
    failure {
      slackSend(color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

    }

    success {
      slackSend(color: '#008000', message: "GOOD Result: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

    }

  }
}