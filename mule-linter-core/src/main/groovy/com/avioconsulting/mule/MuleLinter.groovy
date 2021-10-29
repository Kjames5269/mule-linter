package com.avioconsulting.mule

import com.avioconsulting.mule.linter.dsl.Dsl
import com.avioconsulting.mule.linter.dsl.MuleLinterDsl
import com.avioconsulting.mule.linter.model.Application
import com.avioconsulting.mule.linter.model.MuleApplication
import com.avioconsulting.mule.linter.model.ReportFormat
import com.avioconsulting.mule.linter.model.rule.Rule
import com.avioconsulting.mule.linter.model.rule.RuleExecutor
import com.avioconsulting.mule.linter.model.rule.RuleSet
import org.codehaus.groovy.control.CompilerConfiguration
import org.reflections.ReflectionUtils
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder

@SuppressWarnings(['All', 'GStringExpressionWithinString'])
class MuleLinter {

    Application app
    List<RuleSet> ruleSetList = []
    ReportFormat  outputFormat

    MuleLinter(File applicationDirectory, File ruleConfigFile, ReportFormat outputFormat) {
        this.app = new MuleApplication(applicationDirectory)
        ruleSetList = parseConfigurationFile(ruleConfigFile)
        this.outputFormat= outputFormat
    }

    List<RuleSet> parseConfigurationFile(File ruleConfigFile) {
        GroovyClassLoader gcl = new GroovyClassLoader()
        Class dynamicRules = gcl.parseClass(ruleConfigFile)
        RuleSet rules = dynamicRules.getRules()
        return [rules]
    }

    @SuppressWarnings('UnnecessaryObjectReferences')
    void runLinter() {

        // Create the executor
        RuleExecutor exe = this.buildLinterExecutor()

        // Display Results
        exe.displayResults(outputFormat,System.out)

    }

    @SuppressWarnings('UnnecessaryObjectReferences')
    RuleExecutor buildLinterExecutor() {

        // Create the executor
        RuleExecutor exe = new RuleExecutor(app, ruleSetList)

        // Execute
        exe.executeRules()

        return exe
    }

}
