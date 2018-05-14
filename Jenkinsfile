pipeline {
  agent any

  stages {
    stage('Build App') {
      when {
        branch 'master'
      }
      steps{
        withNPM(npmrcConfig:'my-custom-npmrc') {
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
