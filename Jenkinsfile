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
          sh 'npm config ls'
          sh 'bower install'
        }
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
