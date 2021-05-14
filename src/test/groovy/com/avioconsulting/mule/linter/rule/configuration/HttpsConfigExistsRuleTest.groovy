package com.avioconsulting.mule.linter.rule.configuration

import com.avioconsulting.mule.linter.TestApplication
import com.avioconsulting.mule.linter.model.Application
import com.avioconsulting.mule.linter.model.rule.Rule
import com.avioconsulting.mule.linter.model.rule.RuleSeverity
import com.avioconsulting.mule.linter.model.rule.RuleViolation
import spock.lang.Specification

@SuppressWarnings(['MethodName', 'MethodReturnTypeRequired', 'GStringExpressionWithinString',
        'StaticFieldsBeforeInstanceFields'])
class HttpsConfigExistsRuleTest extends Specification {

    private final TestApplication testApp = new TestApplication()
    private Application app

    def setup() {
        testApp.initialize()
        testApp.addPom()
        testApp.addConfig()
    }

    def cleanup() {
        testApp.remove()
    }

    def 'Test App Live Config Test'() {
        given:
        Rule rule = new HttpsConfigExistsRule()

        when:
        Application app = new Application(testApp.appDir)
        List<RuleViolation> violations = rule.execute(app)

        then:
        violations.size() == 0
    }

    def '1 HTTPS configurations with no TLS configuration'() {
        given:
        testApp.addFile('src/main/mule/global-config.xml', new File('src/test/resources/BAD_CONFIG_1.xml').text)
        Rule rule = new HttpsConfigExistsRule()

        when:
        Application app = new Application(testApp.appDir)
        List<RuleViolation> violations = rule.execute(app)

        then:
        violations.size() == 1 && violations[0].rule.getSeverity() == RuleSeverity.BLOCKER

    }

    def '0 HTTPS configurations incorrectly configured'() {
        given:
        testApp.addFile('src/main/mule/global-config.xml', new File('src/test/resources/GOOD_CONFIG_1.xml').text)
        Rule rule = new HttpsConfigExistsRule()

        when:
        Application app = new Application(testApp.appDir)
        List<RuleViolation> violations = rule.execute(app)

        then:
        violations.size() == 0

    }

}
