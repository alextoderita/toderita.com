def projectGitURL = "https://github.com/alextoderita/toderita.com.git"
def gitCredentialsId = "toderita-com-git"
def dockerImageName = "toderita-com-tomcat"
def dockerContainerName = "toderita-com"

node("master"){
    getSourceCode(projectGitURL, gitCredentialsId)
    buildDockerImage(dockerImageName)
    deployDockerContainer(dockerContainerName, dockerImageName)
    runInput()
    deleteDockerContainer(dockerContainerName)
}

def getSourceCode(projectGitURL, gitCredentialsId){
    stage("Git Checkout"){
        echo "Checking out the code from SCM"
        checkout([$class: 'GitSCM', branches: [[name: '*/master']],
            userRemoteConfigs: [[ credentialsId: "${gitCredentialsId}", url: "${projectGitURL}" ]]])
    }
}

def buildDockerImage(dockerImageName){
	stage("Build Docker image"){
		sh "sudo docker build -t ${dockerImageName} . --file infra/docker/Dockerfile"
	}
}

def deployDockerContainer(dockerContainerName, dockerImageName){
	stage("Deploy Docker container"){
		sh "sudo docker run --security-opt=apparmor:unconfined --security-opt seccomp:unconfined --privileged -p 9999:8080 -d --name ${dockerContainerName} ${dockerImageName}"
	}
}

def runInput(){
  stage("Approval Definition"){
	  input id: "approval-1", message: 'Check the application status... Is it running?', ok: "Approve"
  }
}

def deleteDockerContainer(dockerContainerName){
	stage("Remove Docker container"){
		sh "sudo docker rm --force ${dockerContainerName}"
	}
}
