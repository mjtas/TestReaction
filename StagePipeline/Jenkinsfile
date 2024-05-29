pipeline {
	agent any
	tools {
		maven "MAVEN_HOME"
		jdk "Java_Home"
	}
	environment {
		SNAP_REPO = 'testreaction-snapshot'
		NEXUS_USER = 'admin'
		NEXUS_PASS = 'admin'
		RELEASE_REPO = 'testreaction-release'
		CENTRAL_REPO = 'testreaction-maven-central'
		NEXUSIP = '54.79.243.87'
		NEXUSPORT = '8081'
		NEXUS_GRP_REPO = 'testreaction-maven-group'
		NEXUS_LOGIN = 'nexuslogin'
		SONARSERVER = 'sonarserver'
  	    SONARSCANNER = 'sonarscanner'
  	    registryCredential = 'ecr.ap-southeast-2:awscreds'
  	    appRegistry = '527358489163.dkr.ecr.ap-southeast-2.amazonaws.com/test-reaction-img'
  	    testReactionRegistry = 'https://527358489163.dkr.ecr.ap-southeast-2.amazonaws.com'
	}
	stages {
		stage ('Build') {
			steps { 
				sh 'mvn clean install'
			}
			post {
				success {
					echo "Now Archiving."
					archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
				}
		    }
		}
		stage ('Test') {
			steps {
				sh 'mvn test'
			}
		}
		stage ('Sonar Analysis') {
			environment  {
				scannerHome =tool "${SONARSCANNER}"
			}
			steps {
				withSonarQubeEnv("${SONARSERVER}")
				{
				sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=testreaction \
                   		-Dsonar.projectName=testreaction-repo \
                   		-Dsonar.projectVersion=1.0 \
                   		-Dsonar.sources=target/ "
				}
			}
		}
		stage ('Quality Gate') {
			steps {
				timeout (time : 5, unit : 'HOURS') {
					waitForQualityGate abortPipeline :true
				}
			}
		}
		stage ('Upload Artifact') {
			steps { 
				nexusArtifactUploader (
					nexusVersion : 'nexus3',
					protocol : 'http',
					nexusUrl : "${NEXUSIP}:${NEXUSPORT}",
					groupId : "com.example",
					version : "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
					repository : "${RELEASE_REPO}",
					credentialsId : "${NEXUS_LOGIN}",
					artifacts : [
						[
							artifactId : 'TestReaction',
							classifier : '',
							file : 'target/TestReaction-1.0.jar',
							type : 'jar'
							
						]
					]
					
				)
			}
		}
		stage('Build App image'){
            steps{
                script{
                    dockerImage = docker.build( appRegistry + ":$BUILD_NUMBER", "./Docker-files/app/multistage/")
                }
            }
        }
        stage('Upload App Image'){
            steps{
                script{
                    docker.withRegistry( vprofileRegistry, registryCredential ) {
                        dockerImage.push("$BUILD_NUMBER")
                        dockerImage.push('latest')
                    }
                }
            }
        }
	}
}