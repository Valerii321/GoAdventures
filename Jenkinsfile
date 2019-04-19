pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-12-alpine'
      args '-p 8080:8080'
    }

  }
  stages {
    stage('st1') {
      steps {
        git(url: 'https://github.com/Valerii321/GoAdventures', branch: 'develop')
      }
    }
    stage('test') {
      steps {
        sh 'cd client'
        sh 'npm install'
      }
    }
  }
  environment {
    command = 'mvn clean spring-boot:run'
  }
}