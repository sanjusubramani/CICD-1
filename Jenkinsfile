pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master',
                url: 'https://github.com/sanjusubramani/CICD-1.git'
            }
        }

          stage('Unit Test') {
            steps {
             sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
             sh 'mvn clean package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                 sh 'mvn sonar:sonar'
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKERHUB_USERNAME',
                    passwordVariable: 'DOCKERHUB_PASSWORD'
                )]) {
                    sh '''
                    echo $DOCKERHUB_PASSWORD | docker login -u $DOCKERHUB_USERNAME --password-stdin
                    '''
                }
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t payment-app:v1 .'
            }
        }

        stage('Docker Push') {
            steps {
            sh '''
            docker tag payment-app:v1 sanju2024/payment-app:v1
            docker push sanju2024/payment-app:v1
            '''
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(credentials: ['ec2-key']) {
                   sh '''
                   ssh -o StrictHostKeyChecking=no ec2-user@10.10.10.20 << EOF
            
                   docker pull sanju2024/payment-app:v1

                   docker stop payment-app || true
                   docker rm payment-app || true 

                   docker run -d \
                   --restart always \
                   --name payment-app \
                   -p 8080:8080 \
                   sanju2024/payment-app:v1

                   EOF
                   ''' 
                }
            }
        }

        stage('Health Check') {
            steps {
                sshagent(credentials: ['ec2-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ec2-user@10.10.10.20 \
                    "curl -f http://localhost:8080/actuator/health"
                 '''
                }
            }
        }        
    }
}
