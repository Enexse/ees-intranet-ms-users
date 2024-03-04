pipeline {
    agent any
    tools {
        maven 'maven-3.9.6'
    }
    environment {
        PATH = "/usr/local/bin:$PATH"
        repourl = "${REGISTRY_URL}/${PROJECT_ID}/${ARTIFACT_REGISTRY}"
    }
    stages {
        stage('Checkout Stage') {
            steps {
            //git credentialsId: 'bitbucket', url: 'https://EnExSe@bitbucket.org/enexse/ees-intranet-ms-users.git', branch: 'feature/dockerization'
            checkout([$class: 'GitSCM',
                            branches: [[name: '*/main']],
                            extensions: [],
                            userRemoteConfigs: [[credentialsId: 'git',
                            url: 'https://github.com/Enexse/ees-intranet-ms-users.git']]])
                sh 'mvn clean install -DskipTests=true'
            }
        }
        // stage('Build Docker Image Stage') {
        //     steps {
        //         sh 'docker build -t enexse/ees-ms-users .'
        //     }
        // }
        // stage('Push Docker Image Stage') {
        //     steps {
        //         sh 'docker login -u enexse -p Softwares@1234*'
        //         sh 'docker push enexse/ees-ms-users'
        //     }
        // }
        stage('Deploy to GKE Stage') {
            steps {
                //sh "sed -i 's/tagversion/${env.PROJECT_ID}/g' k8s/deployment.yaml"
                //sh "sed -i 's|enexse|${repourl}|g' k8s/deployment.yaml"
                //sh "kubectl apply -f k8s/deployment.yaml  --context ${env.CLUSTER}"
                step([$class: 'KubernetesEngineBuilder',
                    projectId: env.PROJECT_ID,
                    clusterName: env.CLUSTER,
                    location: env.ZONE,
                    manifestPattern: 'k8s/deployment.yaml',
                    credentialsId: "6c693da8-d3e8-4725-ba84-c388635b84ed", //env.PROJECT_ID,
                    verifyDeployments: true])
            }
        }
    }
}