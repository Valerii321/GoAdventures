pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-11'
      args '--network=test'
    }

  }
  stages {
    stage('Build') {
      steps {
        git(url: 'https://github.com/Valerii321/GoAdventures', branch: 'develop')
        sh 'cd server/goadventures/ && mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        withSonarQubeEnv('Sonar') {
          sh 'cd server/goadventures && mvn sonar:sonar'
          }
        sh 'cd server/goadventures/ && mvn -B -DskipTests clean package'
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