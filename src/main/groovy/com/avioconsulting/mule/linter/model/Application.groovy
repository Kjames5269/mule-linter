package com.avioconsulting.mule.linter.model

class Application {

    static final String APPLICATION_DOES_NOT_EXIST = 'Application directory does not exists: '
    static final String POM_FILE = 'pom.xml'
    static final String GITIGNORE_FILE = '.gitignore'

    File applicationPath
    List<PropertyFile> propertyFiles = []
    List<ConfigurationFile> configurationFiles = []
    PomFile pomFile
    String name
    GitIgnoreFile gitignoreFile
    MuleArtifact muleArtifact

    Application(File applicationPath) {
        this.applicationPath = applicationPath
        if (!this.applicationPath.exists()) {
            throw new FileNotFoundException( APPLICATION_DOES_NOT_EXIST + applicationPath.absolutePath)
        }
        pomFile = new PomFile(applicationPath, POM_FILE)
        gitignoreFile = new GitIgnoreFile(applicationPath, GITIGNORE_FILE)
        this.name = pomFile.artifactId

        loadPropertyFiles()
        loadConfigurationFiles()
        loadMuleArtifact()
    }

    void loadPropertyFiles() {
        File resourcePath = new File(applicationPath, 'src/main/resources')
        if (resourcePath.exists()) {
            resourcePath.eachDirRecurse { dir ->
                dir.eachFileMatch(~/.*.properties/) { file ->
                    propertyFiles.add(new PropertyFile(file))
                }
            }
        }
    }

    void loadConfigurationFiles() {
        File configurationPath = new File(applicationPath, 'src/main/mule')
        if (!configurationPath.exists()) {
            throw new FileNotFoundException( APPLICATION_DOES_NOT_EXIST + configurationPath.absolutePath)
        }

        configurationPath.eachFileMatch(~/.*.xml/){ file ->
            configurationFiles.add(new ConfigurationFile(file))
        }
        
        configurationPath.eachDirRecurse { dir ->
            dir.eachFileMatch(~/.*.xml/) { file ->
                configurationFiles.add(new ConfigurationFile(file))
            }
        }
    }

    void loadMuleArtifact() {
        muleArtifact = new MuleArtifact(applicationPath)
    }

    File getApplicationPath() {
        return applicationPath
    }

    PomFile getPomFile() {
        return pomFile
    }

    GitIgnoreFile getGitignoreFile() {
        return gitignoreFile
    }

    Boolean hasFile(String filename) {
        File file = new File(applicationPath, filename)
        return file.exists()
    }

    String getName() {
        return name
    }

    List<PropertyFile> getPropertyFiles() {
        return propertyFiles
    }

    MuleArtifact getMuleArtifact() {
        return muleArtifact
    }
}
