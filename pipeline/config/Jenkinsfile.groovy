node(){

    //String jdkTool = tool name: 'JDK8', type: 'hudson.model.JDK'
    String repo = 'https://github.com/alextoderita/toderita.com.git'

    checkout(repo)
    createDockerImage("my_tomcat")
    runDockerContainer("tomcat")
}

private void checkout(String repo){
    stage('checkout'){
        println "Checking out code"
        checkout([
            $class: 'GitSCM',
            branches: [[name: '*/master']],
            userRemoteConfigs: [[
                credentialsId: 'toderita-com-git',
                url: repo
            ]]
        ])
    }
}

private void createDockerImage( String imageName ){
	stage('Create Docker image'){
		sh 'cd infra/docker && sudo docker build -t my_tomcat .'
	}
}

private void runDockerContainer(String containerName){
	stage('Run Docker container'){
		sh 'sudo docker run --security-opt=apparmor:unconfined --security-opt seccomp:unconfined --privileged -p 9999:8080 -d --name tomcat my_tomcattt'
	}
}
