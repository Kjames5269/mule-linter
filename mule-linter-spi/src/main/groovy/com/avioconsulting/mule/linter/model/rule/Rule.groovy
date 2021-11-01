package com.avioconsulting.mule.linter.model.rule

import com.avioconsulting.mule.linter.model.Application

abstract class Rule {

    String ruleId
    String ruleName
    RuleSeverity severity = RuleSeverity.MINOR
    RuleType ruleType = RuleType.CODE_SMELL

    Rule(String ruleId, String ruleName, RuleSeverity severity, RuleType ruleType) {
        this.ruleId = ruleId
        this.ruleName = ruleName
        this.severity = severity
        this.ruleType = ruleType
    }
    Rule(String ruleId, String ruleName) {
        this(ruleId, ruleName, RuleSeverity.CRITICAL, RuleType.CODE_SMELL)
    }

    /**
     * All rule classes should implement this method to return a fully configurable instance of the Rule.
     * Groovy DSL depends on this method to acquire fully configurable and executable.
     * Default implementation will instantiate rule using default constructor, if available.
     * For any non-default constructor based rules, those classes should implement this method and provide
     * instance using provided params.
     * @return
     */
    static def Rule createRule(Class<? extends Rule> ruleClass, Map<String, Object> params) {
        return ruleClass.newInstance()
    }

    String getRuleName() {
        return ruleName
    }

    void setRuleName(String ruleName) {
        this.ruleName = ruleName
    }

    void setSeverity(RuleSeverity severity) {
        this.severity = severity
    }

    void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType
    }

    abstract List<RuleViolation> execute(Application application)

}
