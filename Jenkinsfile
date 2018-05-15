pipeline {
  agent any

  tools {
    gradle "Gradle 2.10"
  }

  stages {

    stage('Update Assets') {
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

    stage('Build App') {
      when {
        branch 'master'
      }
      steps{
        echo 'Building app'
        sh 'gradle clean build -x test'
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
