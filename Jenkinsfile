pipeline {
  agent any

  tools {
    gradle "Gradle 2.10"
  }

  environment {
    VERSION = "${UUID.randomUUID().toString().replace('-','')[0..6]}" 
  }

  stages {

    stage('Download Config'){
      steps{
        dir("configFiles"){
          sh "git clone git@bitbucket.org:techmindsmx/config-emailer.git ."
        }
      }
    }

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
      steps{
        echo 'Building app'
        sh 'gradle clean shadowJar -x test'
      }
    }

    stage('Preparing build Image Docker') {
      steps{
        sh 'cp configFiles/conf.json .'
        dir("folderDocker"){
          sh "git clone git@github.com:makingdevs/Java-Jar-Docker.git ."
        }
      }
    }

    stage('Build image docker') {
      docker.withTool('docker') {
        echo "Environment:"
      }
    }
    //  agent any
    //  steps {
    //    sh 'docker build -t shanem/spring-petclinic:latest .'
    //  }
    //}

    //stage('Transfer Jar'){
    //  steps{
    //    echo 'Transferring the jar'
    //    sh "scp ${env.WORKSPACE}/build/libs/app.jar centos@54.210.224.219:/home/centos/wars/emailer/stage/app.jar"
    //  }
    //}

    //stage('Deploy App'){
    //  environment {
    //    ENVIRONMENT = "${env.BRANCH_NAME == 'master' ? 'development' : env.BRANCH_NAME}"
    //  }
    //  steps{
    //    echo 'Execute sh to build and deploy in Kubernetes'
    //    sh "ssh centos@54.210.224.219 sh /home/centos/deployEmailer.sh ${env.VERSION} ${env.ENVIRONMENT}"
    //  }
    //}

  }

  post {
    always {
      cleanWs()
    }
  }
}
