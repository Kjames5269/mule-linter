package com.avioconsulting.mule.linter.dsl

import com.avioconsulting.mule.linter.model.rule.RuleSet

import static groovy.lang.Closure.DELEGATE_ONLY

class RulesContext extends BaseContext {

    final RuleSet ruleSet = new RuleSet()

    def propertyMissing(String name) {
        methodMissing(name, null)
    }

    def methodMissing(String name, args) {
        def ruleClass = RulesLoader.getRuleClassById(name)
        def rule;
        if(ruleClass != null) {
            def ruleObj = new LinkedHashMap()
            println "Creating rule " + name
            if(args != null && args.length > 0 ){
                Closure cl = args[0]
                cl.resolveStrategy = DELEGATE_ONLY
                cl.delegate = ruleObj
                cl()
            }
            rule = ruleClass.createRule(ruleClass, ruleObj)
            ruleSet.addRule(rule);
        }
    }
}
