pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-11'
    }

  }
  stages {
    stage('Build') {
      steps {
        git(url: 'https://github.com/Valerii321/GoAdventures', branch: 'develop')
        sh 'cd server/goadventures/ && ls -la'
        sh 'cd server/goadventures/ && mvn install -Dmaven.test.skip=true'
        sh 'cd server/goadventures/ && mvn -B -DskipTests clean package'
        sh 'cd server/goadventures/ && mvn test -Dmaven.test.skip=true'
      }
    }
    stage('notification') {
      steps {
        slackSend(color: '#32CD32', message: "${env.JOB_NAME} Success, Changes: \n$changeString")
      }
    }
  }
}