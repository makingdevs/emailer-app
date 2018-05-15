pipeline {
  agent any

  stages {
    stage('Build App') {
      when {
        branch 'master'
      }
      steps{
        nodejs(nodeJSInstallationName: 'Node 10.1.0') {
          echo 'Updating bower'
          sh 'bower install'
        }
      }
    }
    stage('Test App') {
      when {
        branch 'master'
      }
      steps{
        echo 'Testing app'
        sh 'gradle clean test'
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
