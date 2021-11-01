package com.avioconsulting.mule.linter.dsl

import static groovy.lang.Closure.DELEGATE_ONLY

class MuleLinterContext extends BaseContext {

    RulesContext rulesContext

    void rules(final Closure closure) {
        RulesContext rules = new RulesContext()
        closure.delegate = rules
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        rulesContext = rules
    }
}
