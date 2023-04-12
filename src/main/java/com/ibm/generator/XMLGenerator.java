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
package com.ibm.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class XMLGenerator {

    class TableColumn  {
          List<String> cells;

        @Override
        public String toString() {
            return "TableColumn [cells=" + cells + "]";
        }          
    }

    private static final String BASE_PATH = ".";
    private List<TableColumn> tableColumns;
    private int tableRows;

    public void readCsvFiles() {
        String csvFolder = BASE_PATH + "/excel";
       final File folder = new File(csvFolder);
       final List<File> filenames = listFilesForFolder(folder);

       Iterator<File> iter = filenames.iterator();
       
       while (iter.hasNext()) {
          tableColumns = new ArrayList<TableColumn>();
          tableRows = 0;
          File file = iter.next();
          readCsv(file.getAbsolutePath());
          generateDMN(file.getName());
       }
    }

    private List<File> listFilesForFolder(final File folder) {

        List<File> filenames = new LinkedList<File>(); 
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                if(fileEntry.getName().contains(".csv"))
                    filenames.add(fileEntry);
                    System.out.println(fileEntry);
            }
        }
        return filenames;
    }

    private void readCsv(String csvFile) {
        String line = "";
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            int row = 0;

            // Iterate over each row in decision table
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(csvSplitBy);

                int d = 0; 
                // Iterate over each input col
                while (d < data.length) {
                    
                    if (tableColumns.size() < d+1) {
                        TableColumn tableCol = new TableColumn();
                        tableCol.cells = new ArrayList<String>();
                        tableCol.cells.add(data[d]);
                        tableColumns.add(tableCol);
                    } else {
                       TableColumn tableCol = tableColumns.get(d);
                       tableCol.cells.add(data[d]);
                    }
                    d++;
                }

                System.out.println(tableColumns);

                row++;
            }

            tableRows = row-1;

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Rows read from CSV: " + tableRows);

    }

    private void generateDMN(String filename) {

        filename = filename.replace("csv", "dmn");
        String dmnFile = BASE_PATH + "/generated/" + filename;

        try {
            // create a new DMN table in XML format
            int i;
            StringBuilder dmnTable = new StringBuilder();

            generateOutputName(dmnTable);
            generateHeadings(dmnTable);

            for (i = 1; i <= tableRows; i++) {
                int col = 0; 

                String ruleId = "_" + UUID.randomUUID().toString();
                dmnTable.append("<dmn:rule id=\"" + ruleId + "\">\n");

                // Iterate over each input col
                while (col < tableColumns.size()-1) {
                    TableColumn inputCol = tableColumns.get(col);

                    String inputXml = inputCol.cells.get(i);

                    inputXml = inputXml.replace(">", "&gt;");
                    inputXml = inputXml.replace("<", "&lt;");

                    String inputUuid = "_" + UUID.randomUUID().toString();

                    dmnTable.append("<dmn:inputEntry id=\"" + inputUuid + "\">\n");
                    dmnTable.append("<dmn:text>" + inputXml + "</dmn:text>\n");
                    dmnTable.append("</dmn:inputEntry>\n");

                    col++;
                }

                // OutputCol is always last column
                TableColumn outputCol = tableColumns.get(col);
                String outputXML = outputCol.cells.get(i);
                String outputUuid = "_" + UUID.randomUUID().toString();
                
                dmnTable.append("<dmn:outputEntry id=\"" + outputUuid + "\">\n");
                dmnTable.append("<dmn:text>\"" + outputXML + "\"</dmn:text>\n");
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

    private void generateHeadings(StringBuilder dmnTable) {

        int col = 0;

        // Iterate over each input col
        while (col < tableColumns.size()-1) {
            TableColumn inputCol = tableColumns.get(col);

            String columnName = inputCol.cells.get(0);

            dmnTable.append("<dmn:input id=\"_B5F7CC81-05CD-4589-B4EA-3C8C450C387C\">\r\n" + 
            "        <dmn:inputExpression id=\"_F85D9764-2343-4949-9B00-BBFF1F816D62\" typeRef=\"string\">\r\n" + 
            "          <dmn:text>" + columnName + "</dmn:text>\r\n" + 
            "        </dmn:inputExpression>\r\n" + 
            "      </dmn:input>");

            col++;
        }
        
        dmnTable.append("<dmn:output id=\"_3EC92DDC-D347-4CAC-86ED-4BE76DFB30E0\"/>\r\n" + 
        "      <dmn:annotation name=\"annotation-1\"/>");

    }

    private void generateOutputName(StringBuilder dmnTable) {

        TableColumn outputCol = tableColumns.get(tableColumns.size()-1);

        String columnName = outputCol.cells.get(0);

        dmnTable.append("  <dmn:decision id=\"_BBA8FB3E-8E56-41E2-A9D5-80FECE01097C\" name=\"" + columnName +"\">\r\n" + 
        "    <dmn:extensionElements/>\r\n" + 
        "    <dmn:variable id=\"_B6D36E32-4CF5-47E4-A5B9-81F4B9C01119\" name=\"" + columnName +"\" typeRef=\"string\"/>\r\n" + 
        "    <dmn:decisionTable id=\"_7CCC4E09-15AD-4108-A087-287BAACE06B3\" hitPolicy=\"FIRST\" preferredOrientation=\"Rule-as-Row\">");
    }

	private static final String DMN_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<dmn:definitions xmlns:dmn=\"http://www.omg.org/spec/DMN/20180521/MODEL/\" xmlns=\"https://www.drools.org/kie-dmn/Breathabililty\" xmlns:feel=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" xmlns:kie=\"http://www.drools.org/kie/dmn/1.2\" xmlns:dmndi=\"http://www.omg.org/spec/DMN/20180521/DMNDI/\" xmlns:di=\"http://www.omg.org/spec/DMN/20180521/DI/\" xmlns:dc=\"http://www.omg.org/spec/DMN/20180521/DC/\" id=\"_D22C5EC8-4E20-46AD-A8F0-5909A7F4A319\" name=\"Breathability\" typeLanguage=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" namespace=\"https://www.drools.org/kie-dmn/Breathabililty\">\r\n" + 
			"  <dmn:extensionElements/>\r\n";

            private static final String DMN_TAIL = "    </dmn:decisionTable>\r\n" + 
			"  </dmn:decision>\r\n" + 
			"  <dmndi:DMNDI>\r\n" + 
			"    <dmndi:DMNDiagram id=\"_29F4A98A-07F6-46C3-BB7E-007BB02F7B94\" name=\"DRG\">\r\n" + 
			"      <di:extension>\r\n" + 
			"        <kie:ComponentsWidthsExtension>\r\n" + 
			"          <kie:ComponentWidths dmnElementRef=\"_7CCC4E09-15AD-4108-A087-287BAACE06B3\">\r\n" + 
			"            <kie:width>50</kie:width>\r\n" + 
			"            <kie:width>100</kie:width>\r\n" + 
			"            <kie:width>100</kie:width>\r\n" + 
			"            <kie:width>100</kie:width>\r\n" + 
			"            <kie:width>100</kie:width>\r\n" + 
			"          </kie:ComponentWidths>\r\n" + 
			"        </kie:ComponentsWidthsExtension>\r\n" + 
			"      </di:extension>\r\n" + 
			"      <dmndi:DMNShape id=\"dmnshape-drg-_BBA8FB3E-8E56-41E2-A9D5-80FECE01097C\" dmnElementRef=\"_BBA8FB3E-8E56-41E2-A9D5-80FECE01097C\" isCollapsed=\"false\">\r\n" + 
			"        <dmndi:DMNStyle>\r\n" + 
			"          <dmndi:FillColor red=\"255\" green=\"255\" blue=\"255\"/>\r\n" + 
			"          <dmndi:StrokeColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n" + 
			"          <dmndi:FontColor red=\"0\" green=\"0\" blue=\"0\"/>\r\n" + 
			"        </dmndi:DMNStyle>\r\n" + 
			"        <dc:Bounds x=\"565\" y=\"184\" width=\"100\" height=\"50\"/>\r\n" + 
			"        <dmndi:DMNLabel/>\r\n" + 
			"      </dmndi:DMNShape>\r\n" + 
			"    </dmndi:DMNDiagram>\r\n" + 
			"  </dmndi:DMNDI>\r\n" + 
			"</dmn:definitions>";

            
    public static void main(String[] args) {

        XMLGenerator xmlGenerator = new XMLGenerator();

        xmlGenerator.readCsvFiles();
    }

}
