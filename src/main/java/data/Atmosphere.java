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
package data;

import org.kie.api.project.KieActivator;

import com.fasterxml.jackson.annotation.JsonProperty;

@KieActivator
public class Atmosphere {

    @JsonProperty("pressure")
    private int pressure;

    @JsonProperty("carbonMonoxide")
    private int carbonDioxide;

    @JsonProperty("carbonMonoxide")
    private int carbonMonoxide;

    @JsonProperty("methane")
    private int methane;

    @JsonProperty("oxygen")
    private int oxygen;

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getCarbonDioxide() {
        return carbonDioxide;
    }

    public void setCarbonDioxide(int carbonDioxide) {
        this.carbonDioxide = carbonDioxide;
    }

    public int getCarbonMonoxide() {
        return carbonMonoxide;
    }

    public void setCarbonMonoxide(int carbonMonoxide) {
        this.carbonMonoxide = carbonMonoxide;
    }

    public int getMethane() {
        return methane;
    }

    public void setMethane(int methane) {
        this.methane = methane;
    }

    public int getOxygen() {
        return oxygen;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

}
