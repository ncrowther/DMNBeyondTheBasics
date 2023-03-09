
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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLGenerator {

    private static final String BASE_PATH = "D:/A_Consultancy/techsales/2023/BelfiusInsurance/divideandconquer/docs"; 
    private List<String> temperature = new ArrayList<>();
    private List<String> classification = new ArrayList<>();
    int rows = 0;
    

    private void readCsv() {
        String csvFile = BASE_PATH + "/DivideAndConquer.oxygen.csv";
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

        try {
            // create a new XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().newDocument();

            // create a root element
            Element root = doc.createElement("rules");
            doc.appendChild(root);
            int i;
            for (i = 1; i < rows; i++) {
                // generate UUID and add to XML
                String ruleId = "_" + UUID.randomUUID().toString();
                Element ruleElement = doc.createElement("dmn:rule");
                ruleElement.setAttribute("id", ruleId);
                Node rule = root.appendChild(ruleElement);

                // generate input col 1 and add to XML
                String inputUuid = "_" + UUID.randomUUID().toString();
                Element inputEntry = doc.createElement("dmn:inputEntry");
                inputEntry.setAttribute("id", inputUuid);
                rule.appendChild(inputEntry);
                // generate data and add to XML
                Element textElement = doc.createElement("dmn:text");
                textElement.appendChild(doc.createTextNode("\"" + temperature.get(i) + "\""));
                inputEntry.appendChild(textElement);

                // generate output col 1 and add to XML
                String outputUuid = "_" + UUID.randomUUID().toString();
                Element outputEntry = doc.createElement("dmn:outputEntry");
                outputEntry.setAttribute("id", outputUuid);
                rule.appendChild(outputEntry);
                // generate data and add to XML
                textElement = doc.createElement("dmn:text");
                textElement.appendChild(doc.createTextNode("\"" + classification.get(i) + "\""));
                outputEntry.appendChild(textElement);

                // generate annotation entry
                Element annotationEntry = doc.createElement("dmn:annotationEntry");
                rule.appendChild(annotationEntry);
                // generate data and add to XML
                textElement = doc.createElement("dmn:text");
                textElement.appendChild(doc.createTextNode(""));
                annotationEntry.appendChild(textElement);
            }

            System.out.println("Rows writen to DMN table: " + i);

            // create a new XML transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // output the XML to the console or file
            DOMSource source = new DOMSource(doc);
            // StreamResult result = new StreamResult(System.out); // to print to console
            StreamResult result = new StreamResult(new File(BASE_PATH +"/output.xml")); 
            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        XMLGenerator xmlGenerator = new XMLGenerator();

        xmlGenerator.readCsv();
        xmlGenerator.generateDMN();
    }
}
