
timestamps {
    def deployVersion = ''
    def artifactId = ''
    def fasitUsername = ''
    def fasitPassword = ''

    withCredentials([[$class          : 'UsernamePasswordMultiBinding', credentialsId: 'ade43d98-326c-41ad-9a61-aefbf933e5d2',
                      usernameVariable: 'SAVEDUSERNAME', passwordVariable: 'SAVEDPASSWORD']]) {
        fasitUsername = env.SAVEDUSERNAME
        fasitPassword = env.SAVEDPASSWORD
    }

    node('DOCKER1') {

        env.LANG = "nb_NO.UTF-8"

        stage("Init") {
            printStage("Init")
            env.JAVA_HOME = "${tool 'jdk-1.8'}"
            env.PATH = "${tool 'maven-3.5.0'}/bin:${env.PATH}"
            step([$class: 'WsCleanup'])
            checkout scm
        }

        artifactId = readFile('pom.xml') =~ '<artifactId>(.+)</artifactId>'
        artifactId = artifactId[0][1]

        if (deployVersion.isEmpty()) {
            def version = readFile('pom.xml') =~ '<version>(.+)</version>'
            pomVersion = version[0][1]
            deployVersion = pomVersion
        }

        stage("BUILD") {
            printStage("Build")
            configFileProvider(
                    [configFile(fileId: 'navMavenSettings', variable: 'MAVEN_SETTINGS')]) {
                mavenProps=" -Dfile.encoding=UTF-8 -Djava.security.egd=file:///dev/urandom "
                sh 'mvn -U -B -s $MAVEN_SETTINGS ' + mavenProps + ' clean deploy -Dmaven.test.skip=true'
            }
        }

        stage("UPLOAD") {
            printStage("Validate");
            sh 'naisd validate -o'
            sh 'naisd upload -u deployment -p d3pl0y -a ' + artifactId + ' -v ' + deployVersion
        }

        stage('DEPLOY') {
            printStage("Deploy")
            //sh 'naisd deploy -u ' + fasitUsername + ' -a ' + artifactId + ' -e ' + env + ' -p ' + fasitPassword + ' -v ' + deployVersion
        }

        stage("UPLOAD") {

        }
    }
}

def info(msg) {
    ansiColor('xterm') {
        println "\033[45m\033[37m " + msg + " \033[0m"
    }
    currentBuild.description = msg
}
void printStage(stage) {
    ansiColor('xterm') {
        println "\033[46m Entered stage " + stage + " \033[0m"
    }
}