package com.avioconsulting.mule.linter.dsl


import com.avioconsulting.mule.linter.model.rule.Rule
import com.avioconsulting.mule.linter.model.rule.RuleSet
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder

import static groovy.lang.Closure.DELEGATE_ONLY

class MuleLinterDsl {

    RulesDsl rulesDsl

    void rules(final Closure closure) {
        RulesDsl dsl = new RulesDsl()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        rulesDsl = dsl
    }
}

class RulesDsl{

    protected final RuleSet rules = new RuleSet()
    static def rulesMap = [:]
    static {
        //This loads all Rule classes shipped with core.
        //TODO: Find a way to load all classes from external library that can have different package.
        Reflections rf = new Reflections(new ConfigurationBuilder()
                .forPackages("com.avioconsulting")
                .setExpandSuperTypes(false)
                .addScanners(Scanners.SubTypes))
        def rules = rf.getSubTypesOf(Rule.class)
        //Look for field named RULE_ID
        rules.each { rule -> {
            //TODO: Constructor argument names cannot be extracted from class.
            // Info not available unless code is compiled with --parameter javac option.
            // Depending on parameter names could be brittle to maintain. Change in constructor
            // arg name will break the script.
            // Should rules also use fields that can be used instead of constructor args?
            // Maybe we can build a dynamic delegate with field name properties.
            rulesMap.put(rule.RULE_ID, rule)
        }}
    }

   private def Rule createRule(rule, args) {
        if (args == null || args.length == 0) {
            return rule.newInstance()
        } else {
            return rule.newInstance(args[0].values().toArray())
        }
    }
    def propertyMissing(String name) {
        methodMissing(name, null)
    }
    def methodMissing(String name, args) {
        def rule = rulesMap.get(name)
        if(rule != null) {
            def ruleObj = createRule(rule, null)
            println "Creating rule " + name
            if(args != null && args.length > 0 ){
                Closure cl = args[0]
                cl.resolveStrategy = DELEGATE_ONLY
                cl.delegate = ruleObj
                cl()
            }
            rules.addRule(ruleObj);
        }
    }

}
