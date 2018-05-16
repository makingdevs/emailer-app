pipeline {
  agent any

  tools {
    gradle "Gradle 2.10"
  }

  environment {
    VERSION = "${UUID.randomUUID().toString().replace('-','')[0..6]}"
  }

  stages {

    stage('Update Assets') {
      steps{
        nodejs(nodeJSInstallationName: 'Node 10.1.0') {
          echo 'Updating bower'
          sh 'bower install'
        }
      }
    }
    stage('Test App') {
      steps{
        echo 'Testing app'
        sh 'gradle clean test'
      }
    }

    stage('Build App') {
      when {
        expression {
          env.BRANCH == 'master' || env.BRANCH == "stage"
        }
      }
      steps{
        echo 'Building app'
        sh 'gradle clean shadowJar -x test'
      }
    }

    stage('Transfer Jar'){
      when {
        branch 'master'
      }
      steps{
        echo 'Transferring the jar'
        sh "scp ${env.WORKSPACE}/build/libs/app.jar centos@54.210.224.219:/home/centos/wars/emailer/stage/app.jar"
      }
    }

    stage('Deploy App'){
      when {
        branch 'master'
      }
      steps{
        echo 'Execute sh to build and deploy in Kubernetes'
        sh "ssh centos@54.210.224.219 sh /home/centos/deployEmailer.sh ${env.VERSION}"
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
