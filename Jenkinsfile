timestamps {
    def artifactId = 'fpmock'
    def fasitUsername = ''
    def fasitPassword = ''
    def revision = ''
    def version = ''
    def sha = ''

    def gitPushStash = ''

    withCredentials([[$class          : 'UsernamePasswordMultiBinding', credentialsId: 'd890bcb9-d823-4662-83dc-b3ed100b98b9',
                      usernameVariable: 'SAVEDUSERNAME', passwordVariable: 'SAVEDPASSWORD']]) {
        fasitUsername = env.SAVEDUSERNAME
        fasitPassword = env.SAVEDPASSWORD
    }

    withCredentials([[$class          : 'UsernamePasswordMultiBinding', credentialsId: 'd2742a0e-53d7-485f-b30a-cfcb05b8fab3',
                      usernameVariable: 'SAVEDUSERNAME', passwordVariable: 'SAVEDPASSWORD']]) {
        gitPushStash = 'git push http://' + env.SAVEDUSERNAME + ':' + env.SAVEDPASSWORD + '@stash.devillo.no/scm/vedfp/vl-beregning.git'
    }

    properties([disableConcurrentBuilds(), parameters([
            string(defaultValue: '', description: '', name: 'miljo'),
            booleanParam(defaultValue: false, description: 'Deployment til NAIS.', name: 'deploy')
        ])
    ])

    node('DOCKER1') {
        try {
            env.LANG = "nb_NO.UTF-8"

            stage("INIT") {
                printStage("Init")
                env.JAVA_HOME = "${tool 'jdk-1.8'}"
                env.PATH = "${tool 'maven-3.5.3'}/bin:${env.PATH}"
                step([$class: 'WsCleanup'])
                checkout scm

                revision = sh(returnStdout: true, script: 'cat .mvn/version').trim()
                commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                timestamp = new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC"))
                sha = '_' + timestamp + '_' + commitHash
                version = ' -Drevision="' + revision + '" -Dchangelist="" -Dsha1="' + sha + '" '

                println"-------------"
                println("Versjon: " + revision + sha)
                println("miljø: " + params.miljo)
                println"-------------"
            }

            stage("BUILD") {
                printStage("Build")
                    info("Build: " + revision + sha)
                    configFileProvider(
                            [configFile(fileId: 'navMavenSettingsUtenProxy', variable: 'MAVEN_SETTINGS')]) {

                        mavenProps = " -Dfile.encoding=UTF-8 -Djava.security.egd=file:///dev/urandom "
                        sh 'mvn -B -s $MAVEN_SETTINGS ' + version + ' -DinstallAtEnd=true -DdeployAtEnd=true ' + mavenProps + ' clean install'
                    }
            }

            stage("VALIDATE") {
                printStage("Validate")
                info("Validate: " + revision + sha)
                sh 'naisd validate -o'
            }

            stage("UPLOAD") {
                if (params.deploy) {
                    printStage("Upload")
                    info("Upload: " + revision + sha)
                    configFileProvider(
                            [configFile(fileId: 'navMavenSettingsUtenProxy', variable: 'MAVEN_SETTINGS')]) {
                        mavenProps = " -Dfile.encoding=UTF-8 -Djava.security.egd=file:///dev/urandom "
                        sh 'mvn -U -B -s $MAVEN_SETTINGS ' + version + mavenProps + ' deploy'
                    }
                }
            }

            stage("CONFIG") {
                if (params.deploy) {
                    printStage("Config")
                    info("Config: " + revision + sha)
                    sh 'naisd upload -u fpdeployer -p GnPuBD5xSWs7tZmE -a ' + artifactId + ' -v ' + revision + sha
                }
            }

            stage('DEPLOY') {
                if (params.deploy) {
                    printStage("Deploy")
                    info("Deploy: " + revision + sha)
                    sh 'naisd deploy -u ' + fasitUsername + ' -a ' + artifactId + ' -e ' + params.miljo + ' -p ' + fasitPassword + ' -v ' + revision + sha + ' -n ' + params.miljo + ' | awk "!/password/"'
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
