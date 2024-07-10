package com.naz.organaiz;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class SeverPortCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {

        String port = System.getenv("X_ZOHO_CATALYST_LISTEN_PORT");
        int portToListen;
        if(port != null && !port.isEmpty()){
            portToListen = Integer.parseInt(port);
        } else {
            portToListen = 9000;
        }
        factory.setPort(portToListen);
    }
}
