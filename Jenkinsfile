pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-12-alpine'
      args '-p 8080:8080'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'cd client'
        sh 'npm install'
        sh 'mvn clean spring-boot:run'
        sh 'mvn -B -DskipTests clean package'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }
  }
}