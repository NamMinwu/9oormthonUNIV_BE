pipeline {
    environment {
        DOCKER_IMAGE = 'minwu1234/9roomthonuniv:latest' // Docker Hub 이미지 이름
        SSH_SERVER = 'ec2-prod' // SSH 서버 이름 (Publish over SSH 플러그인에서 설정한 이름)
        DEPLOY_DIR = '/home/ubuntu/' // Spring 서버에서 Docker Compose를 실행할 디렉토리
        ENV_FILE_CREDENTIALS_ID = "9roomuniv-env"
        APP_YML_CREDENTIALS_ID = "application-file"
        KUBE_MANIFESTS_DIR = 'k8s/'
        CONFIGMAP_CREDENTIALS_ID = "k8s-configmap-file"   // 추가
        SECRET_CREDENTIALS_ID = "k8s-secret-file"         // 추가
    }
    agent any



    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'main', credentialsId: 'github-credentials', url: 'https://github.com/NamMinwu/9oormthonUNIV_BE'
            }
        }


        stage('Inject Configuration Files') {
            steps {
                script {
                    withCredentials([
                        file(credentialsId: ENV_FILE_CREDENTIALS_ID, variable: 'ENV_FILE_PATH'),
                        file(credentialsId: APP_YML_CREDENTIALS_ID, variable: 'APP_YML_PATH'),
                        file(credentialsId: CONFIGMAP_CREDENTIALS_ID, variable: 'CONFIGMAP_PATH'),
                        file(credentialsId: SECRET_CREDENTIALS_ID, variable: 'SECRET_PATH'),

                    ]) {
                        // .env 파일 Jenkins 루트에 복사
                        sh 'cp "$ENV_FILE_PATH" "$WORKSPACE/.env"'

                        // application.yml을 클론된 프로젝트 안으로 복사
                        sh 'cp "$APP_YML_PATH" "$WORKSPACE/src/main/resources/application.yml"'

                        // ConfigMap, Secret 매니페스트를 작업공간에 복사
                        sh 'cp "$CONFIGMAP_PATH" "$WORKSPACE/k8s/configmap.yaml"'
                        sh 'cp "$SECRET_PATH" "$WORKSPACE/k8s/secret.yaml"'
                    }
                }
            }
        }




        stage('Gradle Build') {
            steps {
                sh './gradlew build -x test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Docker Hub에서 최신 이미지를 pull
                    withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                        sh "docker push ${DOCKER_IMAGE}"
                        sh "docker logout"
                    }
                }
            }
        }

        stage('Transfer Files to EC2 Server and Deploy') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        // 1. .env 파일 전송
                        sshPublisher(publishers: [
                            sshPublisherDesc(
                                configName: "${SSH_SERVER}",
                                transfers: [
                                    // ConfigMap 매니페스트 전송
                                    sshTransfer(
                                        sourceFiles: "${KUBE_MANIFESTS_DIR}/**",
                                        remoteDirectory: "k8s/",
                                        removePrefix: "${KUBE_MANIFESTS_DIR}",
                                        execCommand: """
                                            kubectl apply -f /home/ubuntu/k8s/secret.yaml && \
                                            kubectl apply -f /home/ubuntu/k8s/configmap.yaml && \
                                            kubectl apply -f /home/ubuntu/k8s/mysql/pvc.yml && \
                                            kubectl apply -f /home/ubuntu/k8s/mysql/deployment.yml && \
                                            kubectl apply -f /home/ubuntu/k8s/mysql/service.yml && \
                                            kubectl apply -f /home/ubuntu/k8s/app/deployment.yml && \
                                            kubectl apply -f /home/ubuntu/k8s/app/service.yml
                                        """
                                    )
                                ],
                                usePromotionTimestamp: false,
                                verbose: true
                            )
                        ])
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment succeeded!'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}