pipeline {
  agent {
    docker {
      image 'node:latest'
      args '-p 3000:3000'
    }

  }
  stages {
    stage('st1') {
      steps {
        git(url: 'https://github.com/Valerii321/GoAdventures', branch: 'develop')
      }
    }
  }
  environment {
    build = '.'
  }
}