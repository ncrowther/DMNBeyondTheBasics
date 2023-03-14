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
public class PlanetInfo {

    Surface surface;
    Atmosphere atmosphere;

    public class Surface {
        @JsonProperty("pressure")
        private int pressure;

        @JsonProperty("temperature")
        private int temperature;

        @JsonProperty("gravity")
        private int gravity;

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int surfacePressure) {
            this.pressure = surfacePressure;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getGravity() {
            return gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }

        @Override
        public String toString() {
            return "Surface [surfacePressure=" + pressure + ", temperature=" + temperature + ", gravity="
                    + gravity + "]";
        }

    }

    public class Atmosphere {

        @JsonProperty("carbonDioxide")
        private int carbonDioxide;

        @JsonProperty("methane")
        private int methane;

        @JsonProperty("oxygen")
        private int oxygen;

        public int getCarbonDioxide() {
            return carbonDioxide;
        }

        public void setCarbonDioxide(int carbonDioxide) {
            this.carbonDioxide = carbonDioxide;
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

        @Override
        public String toString() {
            return "Atmosphere [carbonDioxide=" + carbonDioxide + ", methane="
                    + methane + ", oxygen=" + oxygen + "]";
        }
    }

    public PlanetInfo(Surface surface, Atmosphere atmosphere) {
        this.surface = surface;
        this.atmosphere = atmosphere;
    }

    @Override
    public String toString() {
        return "PlanetInfo [surface=" + surface + ", atmosphere=" + atmosphere + "]";
    }

}