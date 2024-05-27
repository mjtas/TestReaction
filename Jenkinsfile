pipeline {
	agent any
	tools {
		maven "MAVEN_HOME"
		jdk "Java_Home"
	}
	environment {
		SNAP_REPO = 'profile-snapshot'
		NEXUS_USER = 'admin'
		NEXUS_PASS = 'admin'
		RELEASE_REPO = 'profile-release'
		CENTRAL_REPO = 'pro-maven-central'
		NEXUSIP = 'http://54.79.243.87/'
		NEXUSPORT = '8081'
		NEXUS_GRP_REPO = 'pro-maven-group'
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
					archiveArtifacts artifacts : '**/*.war'
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
				sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=vprofile \
                   		-Dsonar.projectName=vprofile-repo \
                   		-Dsonar.projectVersion=1.0 \
                   		-Dsonar.sources=src/ \
                   		-Dsonar.java.binaries=target/test-classes/com/visualpathit/account/controllerTest/ \
                   		-Dsonar.junit.reportsPath=target/surefire-reports/ \
                   		-Dsonar.jacoco.reportsPath=target/jacoco.exec \
                   		-Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml''' 
				}
			}
		}
		/* stage ('Quality Gate') {
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
							artifactId : 'vproapp',
							classifier : '',
							file : 'target/vprofile-v2.war',
							type : 'war'
							
						]
					]
					
				)
			}
		} */
	}
}