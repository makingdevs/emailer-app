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

    stage('Deploy Docker Machine development') {
      when {
        expression {
          env.BRANCH_NAME == "master"
        }
      }
      steps{
        sh "ssh ec2-user@34.206.149.172 sh /home/ec2-user/deployApps.sh ${env.VERSION} development emailer 8081 8000"
      }
    }

    stage('Deploy Docker Machine stage') {
      when {
        expression {
          env.BRANCH_NAME == "stage"
        }
      }
      steps{
        sh "ssh ec2-user@34.206.149.172 sh /home/ec2-user/deployApps.sh ${env.VERSION} stage emailer 8082 8000"
      }
    }

    stage('Deploy Docker Machine production') {
      when {
        expression {
          env.BRANCH_NAME == "production"
        }
      }
      steps{
        sh "ssh ec2-user@34.206.149.172 sh /home/ec2-user/deployApps.sh ${env.VERSION} production emailer 8083 8000"
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }
}
