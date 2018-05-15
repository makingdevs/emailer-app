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

    stage('Transfer Jar'){
      when {
        branch 'master'
      }
      steps{
        echo 'Transferring the jar'
        sh "scp ${env.WORKSPACE}/build/libs/${env.WORKSPACE}-1.0-fat.jar centos@54.210.224.219:/home/centos/wars/emailer/stage/app.jar"
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
