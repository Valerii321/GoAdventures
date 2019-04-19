pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-11'
    }

  }
  stages {
    stage('repo') {
      steps {
        git(url: 'https://github.com/Valerii321/GoAdventures', branch: 'develop')
      }
    }
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