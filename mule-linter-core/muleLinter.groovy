
mule_linter {
    rules {
        JENKINS_EXISTS
        GIT_IGNORE
        MUNIT_VERSION {
            version= '3.3.50'
        }
//        //This argument order must match with constructor param order. Too technical.
//        COMPONENT_COUNT {
//            component: "logger"
//            namespace: "http://www.mulesoft.org/schema/mule/core"
//            maxCount: 1
//        }
    }
}

/*
mule_linter {
    rules {
        JENKINS_EXISTS {
            A 1
            B "Test"
            C "ABCD"
            E true
        }
//        addRule RULE.JENKINS_EXISTS
//        addRule RULE.GIT_IGNORE
//        addRule RULE.CONFIG_FILE_NAMING,[
//            format: CaseFormat.KEBAB_CASE
//        ]
//        addRule RULE.FLOW_SUBFLOW_NAMING,[
//            format : CaseFormat.PASCAL_CASE
//        ]
    }
}
*/