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
try {
    stage('build') {
      println('so far so good...')
    }
    stage('test') {
      println('A test has failed!')
      sh 'exit 1'
    }
  } catch(e) {
    // mark build as failed
    currentBuild.result = "FAILURE";

    // send slack notification
    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

    // throw the error
    throw e;
}

          }
        }
      }
    }
  