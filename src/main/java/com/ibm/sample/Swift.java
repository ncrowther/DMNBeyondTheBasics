/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.sample;

import org.kie.api.project.KieActivator;

import com.fasterxml.jackson.annotation.JsonProperty;

@KieActivator
public class Swift {

    @JsonProperty("bic")
    private String bic;

    @JsonProperty("branch")
    private String branch;

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("payment")
    private int payment;

    public Swift(String bic, String branch, String messageType, String currency, int payment) {
        this.bic = bic;
        this.branch = branch;
        this.messageType = messageType;
        this.currency = currency;
        this.payment = payment;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Swift [bic=" + bic + ", branch=" + branch + ", messageType=" + messageType + ", currency=" + currency
                + ", payment=" + payment + "]";
    }

}