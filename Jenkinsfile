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
          sh 'coffee --compile src/main/resources/webroot/coffee'
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
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        echo 'Building app'
        sh 'gradle clean shadowJar -x test'
      }
    }

    stage('Download Config'){
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        dir("configFiles"){
          sh "git clone -b ${env.BRANCH_NAME}-new --single-branch git@bitbucket.org:techmindsmx/config-emailer.git ."
        }
      }
    }

    stage('Preparing build Image Docker'){
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        sh 'cp configFiles/conf.json .'
        dir("folderDocker"){
          sh "git clone git@github.com:makingdevs/Java-Jar-Docker.git ."
        }
        sh 'mv folderDocker/* .'
        sh 'mv build/libs/app.jar .'
      }
    }

    stage('Build image docker') {
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      steps{
        script {
          docker.withTool('Docker') {
            docker.withRegistry('https://752822034914.dkr.ecr.us-east-1.amazonaws.com/emailer', 'ecr:us-east-1:techminds-aws') {
              def customImage = docker.build("emailer:${env.VERSION}", '--build-arg URL_WAR=app.jar --build-arg FILE_NAME_CONFIGURATION=conf.json --build-arg PATH_NAME_CONFIGURATION=/root/emailer/ .')
              customImage.push()
            }
          }
        }
      }
    }

    stage('Deploy Kube') {
      when {
        expression {
          env.BRANCH_NAME in ["master","stage","production"]
        }
      }
      environment {
        ENVIRONMENT = "${env.BRANCH_NAME == 'master' ? 'development' : env.BRANCH_NAME}"
      }
      steps{
        sh "ssh ec2-user@34.200.152.121 sh /home/ec2-user/deployApp.sh ${env.VERSION} ${env.ENVIRONMENT} emailer"
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
