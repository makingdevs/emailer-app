pipeline {
  agent any

  stages {
    stage('Build App') {
      when {
        branch 'master'
      }
      steps{
        echo 'Updating bower'
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
