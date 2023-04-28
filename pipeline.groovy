pipeline {
        agent any

  tools {
    maven '3.8.5'
  }

  environment {
    SONARQUBE_URL = 'http://172.17.0.3:9000'
    SONARQUBE_AUTH_TOKEN = credentials('sonarqube-auth-token')
    SONARQUBE_PROJECT_KEY = 'Roche'
    SNYK_TOKEN = credentials('snyk-token')
    DIR = "$WORKSPACE"

  }

  stages {
stage('trufflesecurity') {
  steps {
    sh 'docker run --platform linux/arm64 --rm -v "$PWD:/pwd" trufflesecurity/trufflehog:latest github --repo https://github.com/rajatvarade-globant/my-java-project.git --json > trufflehog-report.json'
  }
}

// stage('Publish Report') {
//   steps {
//     sh 'trufflehog --report -o trufflehog-report.html trufflehog-report.json'
//     publishHTML([
//       allowMissing: true,
//       alwaysLinkToLastBuild: false,
//       keepAll: true,
//       reportDir: '',
//       reportFiles: 'trufflehog-report.html',
//       reportName: 'Trufflehog Report',
//       reportTitles: 'Trufflehog Report'
//     ])
//   }
// }

    stage('Clone') {
      steps {
        // Get code from a GitHub repository
        git url: 'https://github.com/rajatvarade-globant/my-java-project.git', branch: 'master'
        echo "$DIR"
      }

    }
    stage('Build') {
      steps {
        // build the project
        sh 'mvn clean package'
      }
    }
    stage('Test') {
      steps {
        // run unit tests
        sh 'mvn test'
      }
      post {
        // publish JUnit test results
        always {
          // junit '/var/jenkins_home/workspace/git/docker-java-app/target/surefire-reports/TEST-com.mkyong.AppTest.xml'

          junit(allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml')
          sh 'mvn test'
        }
      }
    }
    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('SonarQube') { // replace with your SonarQube server name in Jenkins

          sh('mvn sonar:sonar -Dsonar.projectKey=$SONARQUBE_PROJECT_KEY -Dsonar.host.url=$SONARQUBE_URL -Dsonar.token=$SONARQUBE_AUTH_TOKEN')
        }
      }
    }
    stage('Build Image') {
      steps {
        sh "docker build -f Dockerfile -t myimage ."
      }
    }
    stage('Scanning Image') {
      steps {
        sysdigImageScan engineCredentialsId: 'sysdig-secure-api-credentials', imageName: "myimage"
      }
    }
    stage('Security Testing') {
        agent {
        docker {
            image 'snyk/snyk:maven'
            args '-v $DIR:/app'
        }
    }
            steps {
                sh 'snyk auth $SNYK_TOKEN'
                sh 'snyk test --json synk-report.json --fail-on high'
                publishHTML(target: [
                    allowMissing: true,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'synk-report',
                    reportFiles: 'synk-report.html',
                    reportName: 'Synk Report'
                ])
            }
        }



  }
}
