package com.bmuschko.consumer

import spock.lang.Specification
import spock.lang.Subject

class DefaultAccountManagerIntegrationTest extends Specification {
    
    @Subject DefaultAccountManager defaultAccountManager = new DefaultAccountManager()
    
    def "can credit money to existing balance"() {
        when:
        def newBalance = defaultAccountManager.credit(1L, new BigDecimal(456))
        
        then:
        newBalance > new BigDecimal(456)
    }
}