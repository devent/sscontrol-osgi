/**
 * Builds and deploys the project.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0.0
 * @version 1.2.0
 */
pipeline {

    options {
        buildDiscarder(logRotator(numToKeepStr: "3"))
        disableConcurrentBuilds()
        timeout(time: 60, unit: "MINUTES")
    }

    agent {
        label 'maven-3-jdk-8'
    }

    stages {

		/**
		* The stage will checkout the current branch.
		*/
        stage("Checkout Build") {
            steps {
                container('maven') {
                    checkout scm
                }
            }
        }

		/**
		* The stage will compile and deploy the generated site on all branches.
		*/
        stage('Compile and deploy Site') {
            steps {
                container('maven') {
                    withCredentials([string(credentialsId: 'gpg-key-passphrase', variable: 'GPG_PASSPHRASE')]) {
                    configFileProvider([configFile(fileId: 'gpg-key', variable: 'GPG_KEY_FILE')]) {
                    configFileProvider([configFile(fileId: 'maven-settings-global', variable: 'MAVEN_SETTINGS')]) {
                        withMaven() {
                            sh '/setup-gpg.sh'
	                        sh '/setup-ssh.sh'
                            sh '$MVN_CMD -s $MAVEN_SETTINGS clean install site:site site:deploy'
                        }
                	} } }
                }
            }
        }

		/**
		* The stage will perform the SonarQube analysis on all branches.
		*/
        stage('SonarQube Analysis') {
            steps {
                container('maven') {
					withSonarQubeEnv('sonarqube') {
	                    configFileProvider([configFile(fileId: 'maven-settings-global', variable: 'MAVEN_SETTINGS')]) {
	                        withMaven() {
	                            sh '$MVN_CMD -s $MAVEN_SETTINGS sonar:sonar'
	                        }
	                    }
	            	}
                }
            }
        }

		/**
		* The stage will deploy the artifacts to the private repository.
		*/
        stage('Deploy to Private') {
            steps {
                container('maven') {
                    withCredentials([string(credentialsId: 'gpg-key-passphrase', variable: 'GPG_PASSPHRASE')]) {
                    configFileProvider([configFile(fileId: 'gpg-key', variable: 'GPG_KEY_FILE')]) {
                	configFileProvider([configFile(fileId: 'maven-settings-global', variable: 'MAVEN_SETTINGS')]) {
                    	withMaven() {
                            sh '/setup-gpg.sh'
                        	sh '$MVN_CMD -s $MAVEN_SETTINGS -B deploy'
                    	}
                    } } }
                }
            }
        } // stage

		/**
		* The stage will perform a release from the develop branch.
		*/
        stage('Release to Private') {
    		when {
		        branch 'develop'
		        expression {
		        	// skip stage if it is triggered by maven release.
					return !sh(script: "git --no-pager log -1 --pretty=%B", returnStdout: true).contains('[maven-release-plugin]')
				}
			}
            steps {
                container('maven') {
                    withCredentials([string(credentialsId: 'gpg-key-passphrase', variable: 'GPG_PASSPHRASE')]) {
                    configFileProvider([configFile(fileId: 'gpg-key', variable: 'GPG_KEY_FILE')]) {
                	configFileProvider([configFile(fileId: 'maven-settings-global', variable: 'MAVEN_SETTINGS')]) {
                    	withMaven() {
                            sh '/setup-gpg.sh'
	                        sh '/setup-ssh.sh'
                    	    sh 'git checkout develop && git pull origin develop'
                        	sh '$MVN_CMD -s $MAVEN_SETTINGS -B release:prepare'
                        	sh '$MVN_CMD -s $MAVEN_SETTINGS -B release:perform'
                    	}
                    } } }
                }
            }
        } // stage

		/**
		* The stage will deploy the artifacts and the generated site to the public repository from the master branch.
		*/
        stage('Publish to Public') {
    		when {
		        branch 'master'
			}
            steps {
                container('maven') {
                    withCredentials([string(credentialsId: 'gpg-key-passphrase', variable: 'GPG_PASSPHRASE')]) {
                    configFileProvider([configFile(fileId: 'gpg-key', variable: 'GPG_KEY_FILE')]) {
                	configFileProvider([configFile(fileId: 'maven-settings-global', variable: 'MAVEN_SETTINGS')]) {
                    	withMaven() {
                            sh '/setup-gpg.sh'
                            sh '$MVN_CMD -s $MAVEN_SETTINGS -Posssonatype -B deploy'
                    	}
                    } } }
                }
            }
        } // stage

    } // stages

    post {
        success {
	        script {
	        	pom = readMavenPom file: 'pom.xml'
	            manager.createSummary("document.png").appendText("<a href='${env.JAVADOC_URL}/${pom.groupId}/${pom.artifactId}/${pom.version}/'>View Maven Site</a>", false)
	        }
        }

    } // post
}
