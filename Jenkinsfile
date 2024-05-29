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
					groupId : "QA",
					version : "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
					repository : "${RELEASE_REPO}",
					credentialsId : "${NEXUS_LOGIN}",
					artifacts : [
						[
							artifactId : 'TestReaction',
							classifier : '',
							file : 'TestReaction' + version + '.jar',
							type : 'jar'
							
						]
					]
					
				)
			}
		}
	}
}