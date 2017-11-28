package org.eurekaclinical.i2b2integration.webapp.config;

/*-
 * #%L
 * Eureka! Clinical I2b2 Integration Webapp
 * %%
 * Copyright (C) 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import com.google.inject.AbstractModule;
import com.google.inject.servlet.SessionScoped;
import org.eurekaclinical.i2b2integration.webapp.client.ServiceClientRouterTable;
import org.eurekaclinical.common.comm.clients.RouterTable;
import org.eurekaclinical.i2b2integration.client.EurekaClinicalI2b2IntegrationClient;
import org.eurekaclinical.standardapis.props.CasEurekaClinicalProperties;
import org.eurekaclinical.i2b2integration.webapp.props.WebappProperties;
import org.eurekaclinical.useragreement.client.EurekaClinicalUserAgreementClient;

/**
 * @author Andrew Post
 */
public class AppModule extends AbstractModule {

    private final WebappProperties properties;
    private final EurekaClinicalI2b2IntegrationClientProvider i2b2IntegrationClientProvider;
    private EurekaClinicalUserAgreementClientProvider userAgreementClientProvider;

    public AppModule(WebappProperties inProperties) {
        this.properties = inProperties;
        this.i2b2IntegrationClientProvider = new EurekaClinicalI2b2IntegrationClientProvider(inProperties.getServiceUrl());
        String userAgreementServiceUrl = inProperties.getUserAgreementServiceUrl();
        if (userAgreementServiceUrl != null) {
            this.userAgreementClientProvider = new EurekaClinicalUserAgreementClientProvider(userAgreementServiceUrl);
        }
    }

    @Override
    protected void configure() {
        bind(RouterTable.class).to(ServiceClientRouterTable.class).in(SessionScoped.class);
        bind(CasEurekaClinicalProperties.class).toInstance(this.properties);
        bind(EurekaClinicalI2b2IntegrationClient.class).toProvider(this.i2b2IntegrationClientProvider).in(SessionScoped.class);
        if (this.userAgreementClientProvider != null) {
            bind(EurekaClinicalUserAgreementClient.class).toProvider(this.userAgreementClientProvider).in(SessionScoped.class);
        }
    }
}
