package com.avioconsulting.mule.linter.dsl

import static groovy.lang.Closure.DELEGATE_ONLY

abstract class MuleLinterScript extends Script {
    static MuleLinterContext muleLinter(@DelegatesTo(value = MuleLinterContext, strategy = DELEGATE_ONLY) final Closure closure) {
        final MuleLinterContext muleLinterContext = new MuleLinterContext()

        closure.delegate = muleLinterContext
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()

        return muleLinterContext
    }

}
