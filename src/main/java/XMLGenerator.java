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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class XMLGenerator {

    private static final String BASE_PATH = "D:/IBAMOE/git/divideAndConquerDMN";
    private List<String> temperature = new ArrayList<>();
    private List<String> classification = new ArrayList<>();
    int rows = 0;

    private void readCsv() {
        String csvFile = BASE_PATH + "/docs/oxygenTable.csv";
        String line = "";
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(csvSplitBy);

                temperature.add(data[0]);
                classification.add(data[1]);

                // do something with the data
                System.out.println("Temperature: " + temperature + ", Classification: " + classification);

                rows++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Rows read from CSV: " + rows);
    }

    private void generateDMN() {

        String dmnFile = BASE_PATH + "/src/main/resources/org/acme/habitability/rules/Breathability.dmn";

        try {
            // create a new DMN table in XML format
            int i;
            StringBuilder dmnTable = new StringBuilder();

            for (i = 1; i < rows; i++) {
                // generate UUID and add to XML
                String ruleId = "_" + UUID.randomUUID().toString();
                String inputUuid = "_" + UUID.randomUUID().toString();
                String outputUuid = "_" + UUID.randomUUID().toString();
                String classif = classification.get(i);
                String temp = temperature.get(i);

                temp = temp.replace(">", "&gt;");
                temp= temp.replace("<", "&lt;");

                dmnTable.append("<dmn:rule id=\"" + ruleId + "\">\n");
                dmnTable.append("<dmn:inputEntry id=\"" + inputUuid + "\">\n");
                dmnTable.append("<dmn:text>" + temp + "</dmn:text>\n");
                dmnTable.append("</dmn:inputEntry>\n");

                dmnTable.append("<dmn:outputEntry id=\"" + outputUuid + "\">\n");
                dmnTable.append("<dmn:text>\"" + classif + "\"</dmn:text>\n");
                dmnTable.append("</dmn:outputEntry>\n");                

                dmnTable.append("<dmn:annotationEntry>\n");  
                dmnTable.append("<dmn:text/>\n"); 
                dmnTable.append("</dmn:annotationEntry>\n");             
                
                dmnTable.append("</dmn:rule>\n"); 
            }

            System.out.println("Rows writen to DMN table: " + i);

            String dmnModel = DMN_HEADER + "\n" + dmnTable + "\n" + DMN_TAIL;
            
            FileWriter myWriter = new FileWriter(dmnFile);
            myWriter.write(dmnModel);
            myWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        XMLGenerator xmlGenerator = new XMLGenerator();

        xmlGenerator.readCsv();
        xmlGenerator.generateDMN();
    }

    private static final String DMN_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
    + "<dmn:definitions xmlns:dmn=\"http://www.omg.org/spec/DMN/20180521/MODEL/\" xmlns=\"https://www.drools.org/kie-dmn/Breathabililty\" xmlns:feel=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" xmlns:kie=\"http://www.drools.org/kie/dmn/1.2\" xmlns:dmndi=\"http://www.omg.org/spec/DMN/20180521/DMNDI/\" xmlns:di=\"http://www.omg.org/spec/DMN/20180521/DI/\" xmlns:dc=\"http://www.omg.org/spec/DMN/20180521/DC/\" id=\"_D22C5EC8-4E20-46AD-A8F0-5909A7F4A319\" name=\"Breathability\" typeLanguage=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" namespace=\"https://www.drools.org/kie-dmn/Breathabililty\">\r\n"
    + "  <dmn:extensionElements/>\r\n"
    + "  <dmn:decisionService id=\"_BF054A6C-11AD-43CD-B885-C0E26BE53BEF\" name=\"BreathabiltyDS\">\r\n"
    + "    <dmn:extensionElements/>\r\n"
    + "    <dmn:variable id=\"_31CAB0AD-F412-43E6-9EE1-3287ECFAA963\" name=\"BreathabiltyDS\"/>\r\n"
    + "    <dmn:outputDecision href=\"#_EF9E2212-8793-4E4B-A629-2BB434D4C4C6\"/>\r\n"
    + "    <dmn:encapsulatedDecision href=\"#_BBA8FB3E-8E56-41E2-A9D5-80FECE01097C\"/>\r\n"
    + "    <dmn:encapsulatedDecision href=\"#_88B3F141-A317-4D10-846B-E79FD276EB02\"/>\r\n"
    + "    <dmn:inputData href=\"#_058E2D12-39AD-4DF1-BDD2-B8C122DFCE4C\"/>\r\n"
    + "    <dmn:inputData href=\"#_E30BDA4C-4B2E-4AB2-BE9A-C2FF579871BE\"/>\r\n"
    + "  </dmn:decisionService>\r\n"
    + "  <dmn:inputData id=\"_058E2D12-39AD-4DF1-BDD2-B8C122DFCE4C\" name=\"o2\">\r\n"
    + "    <dmn:extensionElements/>\r\n"
    + "    <dmn:variable id=\"_8632A7DE-A949-4CE8-B2B7-22D591EF9A8B\" name=\"o2\" typeRef=\"number\"/>\r\n"
    + "  </dmn:inputData>\r\n"
    + "  <dmn:inputData id=\"_E30BDA4C-4B2E-4AB2-BE9A-C2FF579871BE\" name=\"pressure\">\r\n"
    + "    <dmn:extensionElements/>\r\n"
    + "    <dmn:variable id=\"_C4E17E0D-0AFC-4379-BB8C-733D01E09950\" name=\"pressure\" typeRef=\"number\"/>\r\n"
    + "  </dmn:inputData>\r\n"
    + "  <dmn:decision id=\"_BBA8FB3E-8E56-41E2-A9D5-80FECE01097C\" name=\"Oxygen\">\r\n"
    + "    <dmn:extensionElements/>\r\n"
    + "    <dmn:variable id=\"_B6D36E32-4CF5-47E4-A5B9-81F4B9C01119\" name=\"Oxygen\" typeRef=\"string\"/>\r\n"
    + "    <dmn:informationRequirement id=\"_90C337FA-A41C-42B7-8AB3-15EECA03C212\">\r\n"
    + "      <dmn:requiredInput href=\"#_058E2D12-39AD-4DF1-BDD2-B8C122DFCE4C\"/>\r\n"
    + "    </dmn:informationRequirement>\r\n"
    + "    <dmn:decisionTable id=\"_7CCC4E09-15AD-4108-A087-287BAACE06B3\" hitPolicy=\"FIRST\" preferredOrientation=\"Rule-as-Row\">\r\n"
    + "      <dmn:input id=\"_3F11AC5C-4DF2-45DD-86D4-E9A2A4DDE316\">\r\n"
    + "        <dmn:inputExpression id=\"_0C0B962B-371D-44A7-91F7-00309A386787\" typeRef=\"number\">\r\n"
    + "          <dmn:text>o2</dmn:text>\r\n" + "        </dmn:inputExpression>\r\n" + "      </dmn:input>\r\n"
    + "      <dmn:output id=\"_3EC92DDC-D347-4CAC-86ED-4BE76DFB30E0\"/>\r\n"
    + "      <dmn:annotation name=\"annotation-1\"/>";

private static final String DMN_TAIL = "   </dmn:decisionTable>\r\n" + "  </dmn:decision>\r\n"
    + "  <dmn:decision id=\"_88B3F141-A317-4D10-846B-E79FD276EB02\" name=\"Surface Pressure\">\r\n"
    + "    <dmn:extensionElements/>\r\n"
    + "    <dmn:variable id=\"_AC7A4EDE-530D-47A1-B8B5-515CBA508468\" name=\"Surface Pressure\" typeRef=\"string\"/>\r\n"
    + "    <dmn:informationRequirement id=\"_67DFE25C-B79C-404F-965F-0C6A0C85579C\">\r\n"
    + "      <dmn:requiredInput href=\"#_E30BDA4C-4B2E-4AB2-BE9A-C2FF579871BE\"/>\r\n"
    + "    </dmn:informationRequirement>\r\n"
    + "    <dmn:decisionTable id=\"_9D10495A-DBC4-440C-92BD-334B6D90F452\" hitPolicy=\"FIRST\" preferredOrientation=\"Rule-as-Row\">\r\n"
    + "      <dmn:input id=\"_47234F4B-AF3E-47C5-B3D4-042D9AA5DB19\">\r\n"
    + "        <dmn:inputExpression id=\"_C5DBBFB6-324E-4F73-A088-C82F883D9F0D\" typeRef=\"number\">\r\n"
    + "          <dmn:text>pressure</dmn:text>\r\n" + "        </dmn:inputExpression>\r\n"
    + "      </dmn:input>\r\n" + "      <dmn:output id=\"_39548E45-C1A2-4BB8-880E-30EA61ACF8B7\"/>\r\n"
    + "      <dmn:annotation name=\"annotation-1\"/>\r\n"
    + "      <dmn:rule id=\"_65161505-32A5-4D26-98D8-C86B6A480632\">\r\n"
    + "        <dmn:inputEntry id=\"_65A4D7AC-6982-4917-A138-A250575C75F1\">\r\n"
    + "          <dmn:text>&gt;=1000</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:outputEntry id=\"_CF5DBDA4-058D-4FE7-BDB7-D67B6E1832BF\">\r\n"
    + "          <dmn:text>\"Optimal\"</dmn:text>\r\n" + "        </dmn:outputEntry>\r\n"
    + "        <dmn:annotationEntry>\r\n" + "          <dmn:text/>\r\n" + "        </dmn:annotationEntry>\r\n"
    + "      </dmn:rule>\r\n" + "      <dmn:rule id=\"_60BE359D-E7F5-4BD1-B7A9-D7BE07F0E6D4\">\r\n"
    + "        <dmn:inputEntry id=\"_F7566E6A-2D86-423D-9001-AFE86FFC1984\">\r\n"
    + "          <dmn:text>[100 .. 999]</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:outputEntry id=\"_F1DC048F-43C8-4CB3-A4F7-D148C71F8EF1\">\r\n"
    + "          <dmn:text>\"Bearable\"</dmn:text>\r\n" + "        </dmn:outputEntry>\r\n"
    + "        <dmn:annotationEntry>\r\n" + "          <dmn:text/>\r\n" + "        </dmn:annotationEntry>\r\n"
    + "      </dmn:rule>\r\n" + "      <dmn:rule id=\"_FCCE1273-E8BE-4A7E-BDAD-43F3E2F5E9B1\">\r\n"
    + "        <dmn:inputEntry id=\"_B1006570-7187-4E2D-A31B-5505067F5CAD\">\r\n"
    + "          <dmn:text>&lt;100</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:outputEntry id=\"_82DFA44E-B622-49FC-8D7A-F7620E19B270\">\r\n"
    + "          <dmn:text>\"Deadly\"</dmn:text>\r\n" + "        </dmn:outputEntry>\r\n"
    + "        <dmn:annotationEntry>\r\n" + "          <dmn:text/>\r\n" + "        </dmn:annotationEntry>\r\n"
    + "      </dmn:rule>\r\n" + "    </dmn:decisionTable>\r\n" + "  </dmn:decision>\r\n"
    + "  <dmn:decision id=\"_EF9E2212-8793-4E4B-A629-2BB434D4C4C6\" name=\"Breathability\">\r\n"
    + "    <dmn:extensionElements/>\r\n"
    + "    <dmn:variable id=\"_CB6837F7-F482-44FB-A8A7-3C0568E6D700\" name=\"Breathability\" typeRef=\"number\"/>\r\n"
    + "    <dmn:informationRequirement id=\"_54CA9720-18D8-43DC-B150-144685F5DC94\">\r\n"
    + "      <dmn:requiredDecision href=\"#_BBA8FB3E-8E56-41E2-A9D5-80FECE01097C\"/>\r\n"
    + "    </dmn:informationRequirement>\r\n"
    + "    <dmn:informationRequirement id=\"_1A99FCED-3BE1-4C47-AA44-0D38DE7E4C01\">\r\n"
    + "      <dmn:requiredDecision href=\"#_88B3F141-A317-4D10-846B-E79FD276EB02\"/>\r\n"
    + "    </dmn:informationRequirement>\r\n"
    + "    <dmn:decisionTable id=\"_BC9689BF-8416-4C24-829B-2A020832D5F4\" hitPolicy=\"FIRST\" preferredOrientation=\"Rule-as-Row\">\r\n"
    + "      <dmn:input id=\"_F53DA069-D558-4CE4-9435-F75BC9C548E8\">\r\n"
    + "        <dmn:inputExpression id=\"_331DFE23-4378-4218-802A-1502486B9E20\" typeRef=\"string\">\r\n"
    + "          <dmn:text>Oxygen</dmn:text>\r\n" + "        </dmn:inputExpression>\r\n"
    + "      </dmn:input>\r\n" + "      <dmn:input id=\"_37D93237-9FF8-4379-856B-1C6754E76A37\">\r\n"
    + "        <dmn:inputExpression id=\"_AA9872FA-A97E-4BAD-87E7-A085F35D53D6\" typeRef=\"string\">\r\n"
    + "          <dmn:text>Surface Pressure</dmn:text>\r\n" + "        </dmn:inputExpression>\r\n"
    + "      </dmn:input>\r\n" + "      <dmn:output id=\"_8485CAC8-CFF4-4404-B79B-EB5E68328240\"/>\r\n"
    + "      <dmn:annotation name=\"annotation-1\"/>\r\n"
    + "      <dmn:rule id=\"_73DB3AA3-DFA6-4074-8CB3-E1FE0D1C35C7\">\r\n"
    + "        <dmn:inputEntry id=\"_125AE261-64F0-4B87-A050-0959B23DA891\">\r\n"
    + "          <dmn:text>\"Optimal\"</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:inputEntry id=\"_0B8A48F5-45F2-497C-AC88-4B410C2C5FC5\">\r\n"
    + "          <dmn:text>\"Optimal\"</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:outputEntry id=\"_5BECC043-5CFC-48C6-9BC9-EF281853A68C\">\r\n"
    + "          <dmn:text>\"Optimal\"</dmn:text>\r\n" + "        </dmn:outputEntry>\r\n"
    + "        <dmn:annotationEntry>\r\n" + "          <dmn:text/>\r\n" + "        </dmn:annotationEntry>\r\n"
    + "      </dmn:rule>\r\n" + "      <dmn:rule id=\"_3B0A9CEB-4348-4269-8FBC-D1C824EAAFD3\">\r\n"
    + "        <dmn:inputEntry id=\"_98AEE776-8AFD-429F-A443-12CF92F7AF23\">\r\n"
    + "          <dmn:text>\"Bearable\"</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:inputEntry id=\"_6AB57FB2-DAF8-4BA2-91CB-E14BFA85C1ED\">\r\n"
    + "          <dmn:text>\"Bearable\"</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:outputEntry id=\"_A78165EE-4571-47E9-BD81-8B2F3D08F726\">\r\n"
    + "          <dmn:text>\"Bearable\"</dmn:text>\r\n" + "        </dmn:outputEntry>\r\n"
    + "        <dmn:annotationEntry>\r\n" + "          <dmn:text/>\r\n" + "        </dmn:annotationEntry>\r\n"
    + "      </dmn:rule>\r\n" + "      <dmn:rule id=\"_974C2DA9-118C-4CB9-9356-AF5F2465E82C\">\r\n"
    + "        <dmn:inputEntry id=\"_DE671230-B71C-4611-B881-779891B08BC7\">\r\n"
    + "          <dmn:text>\"Bearable\"</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:inputEntry id=\"_59A0B8A1-20B9-4545-8DEA-17191D1FC0C3\">\r\n"
    + "          <dmn:text>\"Optimal\"</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:outputEntry id=\"_683847B8-DC3B-4796-AFE6-42FC1ED4BE6E\">\r\n"
    + "          <dmn:text>\"Bearable\"</dmn:text>\r\n" + "        </dmn:outputEntry>\r\n"
    + "        <dmn:annotationEntry>\r\n" + "          <dmn:text/>\r\n" + "        </dmn:annotationEntry>\r\n"
    + "      </dmn:rule>\r\n" + "      <dmn:rule id=\"_E63FFE3A-32A6-4F1E-9B5E-4EC07A2FFCEC\">\r\n"
    + "        <dmn:inputEntry id=\"_B4A1A668-FB0E-4CC1-9D73-B055A0C79A89\">\r\n"
    + "          <dmn:text>\"Optimal\"</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:inputEntry id=\"_AE918F1F-E977-453A-A746-02DBF85B410C\">\r\n"
    + "          <dmn:text>\"Bearable\"</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:outputEntry id=\"_A9224124-6515-441F-B9CE-36B6EED573EC\">\r\n"
    + "          <dmn:text>\"Bearable\"</dmn:text>\r\n" + "        </dmn:outputEntry>\r\n"
    + "        <dmn:annotationEntry>\r\n" + "          <dmn:text/>\r\n" + "        </dmn:annotationEntry>\r\n"
    + "      </dmn:rule>\r\n" + "      <dmn:rule id=\"_5622DDC8-FCAB-4EE8-BD58-29CC8B13CEAC\">\r\n"
    + "        <dmn:inputEntry id=\"_B2228660-7AA8-479C-BB31-1A8F769B297D\">\r\n"
    + "          <dmn:text>-</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:inputEntry id=\"_57CA195B-8DB9-4811-9917-801A13452443\">\r\n"
    + "          <dmn:text>-</dmn:text>\r\n" + "        </dmn:inputEntry>\r\n"
    + "        <dmn:outputEntry id=\"_11C22175-8912-4386-B9CE-BA0FFCA25B1E\">\r\n"
    + "          <dmn:text>\"Deadly\"</dmn:text>\r\n" + "        </dmn:outputEntry>\r\n"
    + "        <dmn:annotationEntry>\r\n" + "          <dmn:text/>\r\n" + "        </dmn:annotationEntry>\r\n"
    + "      </dmn:rule>\r\n" + "    </dmn:decisionTable>\r\n" + "  </dmn:decision>\r\n" + "  <dmndi:DMNDI>\r\n"
    + "    <dmndi:DMNDiagram id=\"_29F4A98A-07F6-46C3-BB7E-007BB02F7B94\" name=\"DRG\">\r\n"
    + "      <di:extension>\r\n" + "        <kie:ComponentsWidthsExtension>\r\n"
    + "          <kie:ComponentWidths dmnElementRef=\"_7CCC4E09-15AD-4108-A087-287BAACE06B3\">\r\n"
    + "            <kie:width>50</kie:width>\r\n" + "            <kie:width>100</kie:width>\r\n"
    + "            <kie:width>100</kie:width>\r\n" + "            <kie:width>100</kie:width>\r\n"
    + "          </kie:ComponentWidths>\r\n"
    + "          <kie:ComponentWidths dmnElementRef=\"_9D10495A-DBC4-440C-92BD-334B6D90F452\">\r\n"
    + "            <kie:width>50</kie:width>\r\n" + "            <kie:width>100</kie:width>\r\n"
    + "            <kie:width>125</kie:width>\r\n" + "            <kie:width>100</kie:width>\r\n"
    + "          </kie:ComponentWidths>\r\n"
    + "          <kie:ComponentWidths dmnElementRef=\"_BC9689BF-8416-4C24-829B-2A020832D5F4\">\r\n"
    + "            <kie:width>50</kie:width>\r\n" + "            <kie:width>100</kie:width>\r\n"
    + "            <kie:width>121</kie:width>\r\n" + "            <kie:width>100</kie:width>\r\n"
    + "            <kie:width>100</kie:width>\r\n" + "          </kie:ComponentWidths>\r\n"
    + "        </kie:ComponentsWidthsExtension>\r\n" + "      </di:extension>\r\n"
    + "      <dmndi:DMNShape id=\"dmnshape-drg-_BF054A6C-11AD-43CD-B885-C0E26BE53BEF\" dmnElementRef=\"_BF054A6C-11AD-43CD-B885-C0E26BE53BEF\" isCollapsed=\"false\">\r\n"
    + "        <dmndi:DMNStyle>\r\n" + "          <dmndi:FillColor red=\"255\" green=\"255\" blue=\"255\"/>\r\n"
    + "          <dmndi:StrokeColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n"
    + "          <dmndi:FontColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n" + "        </dmndi:DMNStyle>\r\n"
    + "        <dc:Bounds x=\"565\" y=\"30\" width=\"258\" height=\"239\"/>\r\n"
    + "        <dmndi:DMNLabel/>\r\n" + "        <dmndi:DMNDecisionServiceDividerLine>\r\n"
    + "          <di:waypoint x=\"565\" y=\"130\"/>\r\n" + "          <di:waypoint x=\"823\" y=\"130\"/>\r\n"
    + "        </dmndi:DMNDecisionServiceDividerLine>\r\n" + "      </dmndi:DMNShape>\r\n"
    + "      <dmndi:DMNShape id=\"dmnshape-drg-_058E2D12-39AD-4DF1-BDD2-B8C122DFCE4C\" dmnElementRef=\"_058E2D12-39AD-4DF1-BDD2-B8C122DFCE4C\" isCollapsed=\"false\">\r\n"
    + "        <dmndi:DMNStyle>\r\n" + "          <dmndi:FillColor red=\"255\" green=\"255\" blue=\"255\"/>\r\n"
    + "          <dmndi:StrokeColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n"
    + "          <dmndi:FontColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n" + "        </dmndi:DMNStyle>\r\n"
    + "        <dc:Bounds x=\"559.5981308411215\" y=\"350.3421052631579\" width=\"100\" height=\"50\"/>\r\n"
    + "        <dmndi:DMNLabel/>\r\n" + "      </dmndi:DMNShape>\r\n"
    + "      <dmndi:DMNShape id=\"dmnshape-drg-_E30BDA4C-4B2E-4AB2-BE9A-C2FF579871BE\" dmnElementRef=\"_E30BDA4C-4B2E-4AB2-BE9A-C2FF579871BE\" isCollapsed=\"false\">\r\n"
    + "        <dmndi:DMNStyle>\r\n" + "          <dmndi:FillColor red=\"255\" green=\"255\" blue=\"255\"/>\r\n"
    + "          <dmndi:StrokeColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n"
    + "          <dmndi:FontColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n" + "        </dmndi:DMNStyle>\r\n"
    + "        <dc:Bounds x=\"726.2990654205607\" y=\"350.3421052631579\" width=\"100\" height=\"50\"/>\r\n"
    + "        <dmndi:DMNLabel/>\r\n" + "      </dmndi:DMNShape>\r\n"
    + "      <dmndi:DMNShape id=\"dmnshape-drg-_BBA8FB3E-8E56-41E2-A9D5-80FECE01097C\" dmnElementRef=\"_BBA8FB3E-8E56-41E2-A9D5-80FECE01097C\" isCollapsed=\"false\">\r\n"
    + "        <dmndi:DMNStyle>\r\n" + "          <dmndi:FillColor red=\"255\" green=\"255\" blue=\"255\"/>\r\n"
    + "          <dmndi:StrokeColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n"
    + "          <dmndi:FontColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n" + "        </dmndi:DMNStyle>\r\n"
    + "        <dc:Bounds x=\"574\" y=\"173\" width=\"100\" height=\"50\"/>\r\n"
    + "        <dmndi:DMNLabel/>\r\n" + "      </dmndi:DMNShape>\r\n"
    + "      <dmndi:DMNShape id=\"dmnshape-drg-_88B3F141-A317-4D10-846B-E79FD276EB02\" dmnElementRef=\"_88B3F141-A317-4D10-846B-E79FD276EB02\" isCollapsed=\"false\">\r\n"
    + "        <dmndi:DMNStyle>\r\n" + "          <dmndi:FillColor red=\"255\" green=\"255\" blue=\"255\"/>\r\n"
    + "          <dmndi:StrokeColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n"
    + "          <dmndi:FontColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n" + "        </dmndi:DMNStyle>\r\n"
    + "        <dc:Bounds x=\"698\" y=\"173\" width=\"100\" height=\"50\"/>\r\n"
    + "        <dmndi:DMNLabel/>\r\n" + "      </dmndi:DMNShape>\r\n"
    + "      <dmndi:DMNShape id=\"dmnshape-drg-_EF9E2212-8793-4E4B-A629-2BB434D4C4C6\" dmnElementRef=\"_EF9E2212-8793-4E4B-A629-2BB434D4C4C6\" isCollapsed=\"false\">\r\n"
    + "        <dmndi:DMNStyle>\r\n" + "          <dmndi:FillColor red=\"255\" green=\"255\" blue=\"255\"/>\r\n"
    + "          <dmndi:StrokeColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n"
    + "          <dmndi:FontColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n" + "        </dmndi:DMNStyle>\r\n"
    + "        <dc:Bounds x=\"636\" y=\"68\" width=\"100\" height=\"50\"/>\r\n"
    + "        <dmndi:DMNLabel/>\r\n" + "      </dmndi:DMNShape>\r\n"
    + "      <dmndi:DMNEdge id=\"dmnedge-drg-_90C337FA-A41C-42B7-8AB3-15EECA03C212\" dmnElementRef=\"_90C337FA-A41C-42B7-8AB3-15EECA03C212\">\r\n"
    + "        <di:waypoint x=\"609.5981308411215\" y=\"375.3421052631579\"/>\r\n"
    + "        <di:waypoint x=\"624\" y=\"223\"/>\r\n" + "      </dmndi:DMNEdge>\r\n"
    + "      <dmndi:DMNEdge id=\"dmnedge-drg-_67DFE25C-B79C-404F-965F-0C6A0C85579C\" dmnElementRef=\"_67DFE25C-B79C-404F-965F-0C6A0C85579C\">\r\n"
    + "        <di:waypoint x=\"776.2990654205607\" y=\"375.3421052631579\"/>\r\n"
    + "        <di:waypoint x=\"748\" y=\"223\"/>\r\n" + "      </dmndi:DMNEdge>\r\n"
    + "      <dmndi:DMNEdge id=\"dmnedge-drg-_54CA9720-18D8-43DC-B150-144685F5DC94\" dmnElementRef=\"_54CA9720-18D8-43DC-B150-144685F5DC94\">\r\n"
    + "        <di:waypoint x=\"624\" y=\"198\"/>\r\n" + "        <di:waypoint x=\"686\" y=\"118\"/>\r\n"
    + "      </dmndi:DMNEdge>\r\n"
    + "      <dmndi:DMNEdge id=\"dmnedge-drg-_1A99FCED-3BE1-4C47-AA44-0D38DE7E4C01-AUTO-TARGET\" dmnElementRef=\"_1A99FCED-3BE1-4C47-AA44-0D38DE7E4C01\">\r\n"
    + "        <di:waypoint x=\"748\" y=\"198\"/>\r\n" + "        <di:waypoint x=\"686\" y=\"118\"/>\r\n"
    + "      </dmndi:DMNEdge>\r\n" + "    </dmndi:DMNDiagram>\r\n" + "  </dmndi:DMNDI>\r\n"
    + "</dmn:definitions>";
        
}
