pipeline {
    agent any

    parameters {
        string(name: 'TAG', , defaultValue: '1.0.0', description: 'please enter the tag in following format 1.0.0')
        string(name: 'RELEASE_NAME', defaultValue: 'my-nginx-chart', description: 'Helm release name')
        string(name: 'CHART_NAME', defaultValue: './fraud_detection/application-chart', description: 'Path or name of Helm chart')
        string(name: 'NAMESPACE', defaultValue: 'default', description: 'Kubernetes namespace')
    }

    stages {

        stage('Connect to the cluster') {
            steps {
                sh 'gcloud container clusters get-credentials detection-workloads --zone us-central1-a --project rajatv'
            }
        }
        stage('Helm Version Check') {
            steps {
                sh 'helm version'
            }
        }

        stage('Deploy Helm Chart') {
            steps {
                script {
                    echo "Deploying Helm chart: ${params.CHART_NAME}"
                    echo "Using release name: ${params.RELEASE_NAME}"
                    echo "Namespace: ${params.NAMESPACE}"

                    // Deploy the Helm chart with the provided parameters
                    sh """
                    helm upgrade --install ${params.RELEASE_NAME} \
                                 ${params.CHART_NAME} \
                                 --namespace ${params.NAMESPACE} \
                                 --set image.tag=${params.TAG} \
                                 --wait
                    """
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    echo "Verifying the deployment in namespace ${params.NAMESPACE}"
                    // Verify that the pods are running in the desired namespace
                    sh "kubectl get pods -n ${params.NAMESPACE}"
                }
            }
        }

        stage('Patch the green deployment') {
            steps {
                script {
                      sh """
                      kubectl patch service green -p '{"spec":{"selector":{"app": "${params.TAG}"}}}'
                    

                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Helm deployment succeeded!'
        }
        failure {
            echo 'Helm deployment failed!'
        }
    }
}
