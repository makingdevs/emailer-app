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
          env.BRANCH_NAME == 'master' || env.BRANCH_NAME == "stage"
        }
      }
      steps{
        echo 'Building app'
        sh 'gradle clean shadowJar -x test'
      }
    }

    stage('Transfer Jar'){
      when {
        expression {
          env.BRANCH_NAME == 'master' || env.BRANCH_NAME == "stage"
        }
      }
      steps{
        echo 'Transferring the jar'
        sh "scp ${env.WORKSPACE}/build/libs/app.jar centos@54.210.224.219:/home/centos/wars/emailer/stage/app.jar"
      }
    }

    stage('Deploy App'){
      when {
        expression {
          env.BRANCH_NAME == 'master' || env.BRANCH_NAME == "stage"
        }
      }
      environment {
        ENVIRONMENT = "${env.BRANCH_NAME == 'master' ? 'development' : env.BRANCH_NAME}"
      }
      steps{
        echo 'Execute sh to build and deploy in Kubernetes'
        sh "ssh centos@54.210.224.219 sh /home/centos/deployEmailer.sh 1.0.${env.BUILD_NUMBER}, ${env.ENVIRONMENT}"
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
