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
        def notifySlack(String buildStatus = 'STARTED') {
        
          // Build status of null means success.
          buildStatus = buildStatus ?: 'SUCCESS'
          def color
          if (buildStatus == 'STARTED') {
              color = '#D4DADF'
          } else if (buildStatus == 'SUCCESS') {
              color = '#BDFFC3'
          } else if (buildStatus == 'UNSTABLE') {
              color = '#FFFE89'
          } else {
              color = '#FF9FA1'
          }
          def msg = "${buildStatus}: `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}"
          slackSend(color: color, message: msg)
    }
        node {
            try {
                notifySlack()
                sh 'runbuild'
            } catch (e) {
                currentBuild.result = 'FAILURE'
                throw e
            } finally {
                notifySlack(currentBuild.result)
            }
          }
        }
      }
    }
  }