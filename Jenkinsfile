
timestamps {
    def artifactId = 'fpmock'
    def fasitUsername = ''
    def fasitPassword = ''

    withCredentials([[$class          : 'UsernamePasswordMultiBinding', credentialsId: 'd890bcb9-d823-4662-83dc-b3ed100b98b9',
                      usernameVariable: 'SAVEDUSERNAME', passwordVariable: 'SAVEDPASSWORD']]) {
        fasitUsername = env.SAVEDUSERNAME
        fasitPassword = env.SAVEDPASSWORD
    }

    properties([disableConcurrentBuilds(), parameters([
            string(defaultValue: '', description: '', name: 'miljo'),
            string(defaultValue: '', description: '', name: 'version'),
            booleanParam(defaultValue: false, description: '', name: 'deploy')])
    ])

    node('DOCKER1') {
        try {
            env.LANG = "nb_NO.UTF-8"

            stage("INIT") {
                printStage("Init")
                env.JAVA_HOME = "${tool 'jdk-1.8'}"
                env.PATH = "${tool 'maven-3.3.9'}/bin:${env.PATH}"
                step([$class: 'WsCleanup'])
                checkout scm
                if (params.deploy) {
                    sh 'mvn -U -B versions:set -DnewVersion=' + params.version + ' -DgenerateBackupPoms=false'
                }
            }

            stage("BUILD") {
                printStage("Build")
                info("Build")
                configFileProvider(
                        [configFile(fileId: 'navMavenSettingsUtenProxy', variable: 'MAVEN_SETTINGS')]) {
                    mavenProps=" -Dfile.encoding=UTF-8 -Djava.security.egd=file:///dev/urandom "
                    sh 'mvn -U -B -s $MAVEN_SETTINGS ' + mavenProps + ' clean install'
                }
            }

            stage("UPLOAD") {
                if (params.deploy) {
                    printStage("Upload")
                    info("Upload")
                    configFileProvider(
                            [configFile(fileId: 'navMavenSettingsUtenProxy', variable: 'MAVEN_SETTINGS')]) {
                        mavenProps = " -Dfile.encoding=UTF-8 -Djava.security.egd=file:///dev/urandom "
                        sh 'mvn -U -B -s $MAVEN_SETTINGS ' + mavenProps + ' deploy'
                    }
                }
            }

            stage("VALIDATE") {
                if (params.deploy) {
                    printStage("Validate")
                    info("Validate")
                    sh 'naisd validate -o'
                }
            }

            stage("CONFIG") {
                if (params.deploy) {
                    printStage("Config");
                    info("Config")
                    sh 'naisd upload -u deployment -p d3pl0y -a ' + artifactId + ' -v ' + params.version
                }
            }

            stage('DEPLOY') {
                if (params.deploy) {
                    printStage("Deploy")
                    info("Deploy")
                    if (params.miljo?.trim()) {
                        sh 'naisd deploy -u ' + fasitUsername + ' -a ' + artifactId + ' -e ' + params.miljo + ' -p ' + fasitPassword + ' -v ' + params.version + ' -n ' + params.miljo

                    } else {
                        sh 'naisd deploy -u ' + fasitUsername + ' -a ' + artifactId + ' -p ' + fasitPassword + ' -v ' + params.version
                    }
                }
            }
        } catch (error) {
            emailext(
                    subject: "[AUTOMAIL] Feilet jobb ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                    body: "<p>Hei,<br><br>kan du ta en titt på jenkins jobben om det er noe du kan fikse.<br>" +
                            "<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a><br><br>" +
                            "Tusen takk på forhånd,<br>Miljø</p>",
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'],
                                         [$class: 'CulpritsRecipientProvider']]
            )
            throw error
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
