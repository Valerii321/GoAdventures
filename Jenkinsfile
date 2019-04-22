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
        sh 'cd server'
        sh 'ls -la'
        sh 'mvn clean spring-boot:run'
        sh 'mvn -B -DskipTests clean package'
      }
    }
    stage('Test') {
      steps {
        sh 'cd server/goadventures/'
        sh 'mvn test'
        sh 'mvn package -Dmaven.test.skip=true'
      }
    }
  }
}