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
        sh 'cd server/goadventures && mvn dependency:go-offline'
        sh 'cd server/goadventures && mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        withSonarQubeEnv('sonarqubee') {
          sh 'cd server/goadventures && mvn sonar:sonar'
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