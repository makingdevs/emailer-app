pipeline {
  agent any

  stages {
    stage('Build App') {
      when {
        branch 'master'
      }
      steps{
        nodejs(nodeJSInstallationName: 'Node 10.1.0') {
          sh 'npm config ls'
        }
        withNPM(npmrcConfig:'conf-npmrc') {
          echo 'Updating bower'
          sh 'npm install bower'
          echo $(ls -a)
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
