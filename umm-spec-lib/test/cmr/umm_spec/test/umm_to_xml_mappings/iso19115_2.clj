(ns cmr.umm-spec.test.umm-to-xml-mappings.iso19115-2
  "Tests to verify that ISO19115-2 is generated correctly."
  (:require [cmr.umm-spec.umm-to-xml-mappings.iso19115-2 :as iso]
            [cmr.umm-spec.xml-to-umm-mappings.iso19115-2 :as parser]
            [cmr.umm-spec.xml-to-umm-mappings.iso19115-2.additional-attribute :as aa]
            [cmr.umm-spec.models.umm-collection-models :as coll]
            [clojure.test :refer :all]
            [clojure.data.xml :as x]
            [clojure.java.io :as io]
            [cmr.common.util :refer [are3]]
            [cmr.common.xml :as xml]
            [cmr.common.xml.xslt :as xslt]
            [cmr.umm-spec.umm-spec-core :as core]
            [cmr.umm-spec.test.location-keywords-helper :as lkt]))

(def iso-no-use-constraints "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<gmi:MI_Metadata xmlns:gmi=\"http://www.isotc211.org/2005/gmi\" xmlns:eos=\"http://earthdata.nasa.gov/schema/eos\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns:gmd=\"http://www.isotc211.org/2005/gmd\" xmlns:gml=\"http://www.opengis.net/gml/3.2\" xmlns:gmx=\"http://www.isotc211.org/2005/gmx\" xmlns:gsr=\"http://www.isotc211.org/2005/gsr\" xmlns:gss=\"http://www.isotc211.org/2005/gss\" xmlns:gts=\"http://www.isotc211.org/2005/gts\" xmlns:srv=\"http://www.isotc211.org/2005/srv\" xmlns:swe=\"http://schemas.opengis.net/sweCommon/2.0/\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">
   <!--Other Properties, all:0, coi:0,ii:0,si:0,pli:0,pri:0,qi:0,gi:0,ci:0,dk:0,pcc:0,icc:0,scc:0-->
   <gmd:fileIdentifier>
      <gco:CharacterString>gov.nasa.echo:A minimal valid collection V 1</gco:CharacterString>
   </gmd:fileIdentifier>
   <gmd:language>
      <gco:CharacterString>eng</gco:CharacterString>
   </gmd:language>
   <gmd:characterSet>
      <gmd:MD_CharacterSetCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_CharacterSetCode\" codeListValue=\"utf8\">utf8</gmd:MD_CharacterSetCode>
   </gmd:characterSet>
   <gmd:hierarchyLevel>
      <gmd:MD_ScopeCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_ScopeCode\" codeListValue=\"series\">series</gmd:MD_ScopeCode>
   </gmd:hierarchyLevel>
   <gmd:contact gco:nilReason=\"missing\" />
   <gmd:dateStamp>
      <gco:DateTime>2016-07-06T18:15:49.058-04:00</gco:DateTime>
   </gmd:dateStamp>
   <gmd:metadataStandardName>
      <gco:CharacterString>ISO 19115-2 Geographic Information - Metadata Part 2 Extensions for imagery and gridded data</gco:CharacterString>
   </gmd:metadataStandardName>
   <gmd:metadataStandardVersion>
      <gco:CharacterString>ISO 19115-2:2009(E)</gco:CharacterString>
   </gmd:metadataStandardVersion>
   <gmd:identificationInfo>
      <gmd:MD_DataIdentification>
         <gmd:citation>
            <gmd:CI_Citation>
               <gmd:title>
                  <gco:CharacterString>MINIMAL &gt; A minimal valid collection</gco:CharacterString>
               </gmd:title>
               <gmd:date>
                  <gmd:CI_Date>
                     <gmd:date>
                        <gco:DateTime>1999-12-31T19:00:00-05:00</gco:DateTime>
                     </gmd:date>
                     <gmd:dateType>
                        <gmd:CI_DateTypeCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode\" codeListValue=\"revision\">revision</gmd:CI_DateTypeCode>
                     </gmd:dateType>
                  </gmd:CI_Date>
               </gmd:date>
               <gmd:date>
                  <gmd:CI_Date>
                     <gmd:date>
                        <gco:DateTime>1999-12-31T19:00:00-05:00</gco:DateTime>
                     </gmd:date>
                     <gmd:dateType>
                        <gmd:CI_DateTypeCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode\" codeListValue=\"creation\">creation</gmd:CI_DateTypeCode>
                     </gmd:dateType>
                  </gmd:CI_Date>
               </gmd:date>
               <gmd:edition>
                  <gco:CharacterString>1</gco:CharacterString>
               </gmd:edition>
               <gmd:identifier>
                  <gmd:MD_Identifier>
                     <gmd:code>
                        <gco:CharacterString>MINIMAL</gco:CharacterString>
                     </gmd:code>
                     <gmd:description>
                        <gco:CharacterString>A minimal valid collection</gco:CharacterString>
                     </gmd:description>
                  </gmd:MD_Identifier>
               </gmd:identifier>
               <gmd:otherCitationDetails>
                  <gco:CharacterString />
               </gmd:otherCitationDetails>
            </gmd:CI_Citation>
         </gmd:citation>
         <gmd:abstract>
            <gco:CharacterString>A minimal valid collection</gco:CharacterString>
         </gmd:abstract>
         <gmd:purpose gco:nilReason=\"missing\" />
         <gmd:language>
            <gco:CharacterString>eng</gco:CharacterString>
         </gmd:language>
         <gmd:characterSet>
            <gmd:MD_CharacterSetCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_CharacterSetCode\" codeListValue=\"utf8\">utf8</gmd:MD_CharacterSetCode>
         </gmd:characterSet>
         <gmd:extent>
            <gmd:EX_Extent id=\"boundingExtent\">
               <gmd:description gco:nilReason=\"unknown\" />
            </gmd:EX_Extent>
         </gmd:extent>
         <gmd:supplementalInformation />
         <gmd:processingLevel>
            <gmd:MD_Identifier>
               <gmd:code>
                  <gco:CharacterString />
               </gmd:code>
               <gmd:description>
                  <gco:CharacterString />
               </gmd:description>
            </gmd:MD_Identifier>
         </gmd:processingLevel>
      </gmd:MD_DataIdentification>
   </gmd:identificationInfo>
   <gmd:distributionInfo>
      <gmd:MD_Distribution>
         <gmd:distributor>
            <gmd:MD_Distributor>
               <gmd:distributorContact gco:nilReason=\"missing\" />
               <gmd:distributionOrderProcess>
                  <gmd:MD_StandardOrderProcess>
                     <gmd:fees gco:nilReason=\"missing\" />
                  </gmd:MD_StandardOrderProcess>
               </gmd:distributionOrderProcess>
               <gmd:distributorTransferOptions>
                  <gmd:MD_DigitalTransferOptions />
               </gmd:distributorTransferOptions>
            </gmd:MD_Distributor>
         </gmd:distributor>
      </gmd:MD_Distribution>
   </gmd:distributionInfo>
   <gmd:dataQualityInfo>
      <gmd:DQ_DataQuality>
         <gmd:scope>
            <gmd:DQ_Scope>
               <gmd:level>
                  <gmd:MD_ScopeCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_ScopeCode\" codeListValue=\"series\">series</gmd:MD_ScopeCode>
               </gmd:level>
            </gmd:DQ_Scope>
         </gmd:scope>
         <gmd:lineage>
            <gmd:LI_Lineage>
               <gmd:processStep>
                  <gmi:LE_ProcessStep>
                     <gmd:description gco:nilReason=\"unknown\" />
                  </gmi:LE_ProcessStep>
               </gmd:processStep>
            </gmd:LI_Lineage>
         </gmd:lineage>
      </gmd:DQ_DataQuality>
   </gmd:dataQualityInfo>
   <gmd:metadataMaintenance>
      <gmd:MD_MaintenanceInformation>
         <gmd:maintenanceAndUpdateFrequency>
            <gmd:MD_MaintenanceFrequencyCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_MaintenanceFrequencyCode\" codeListValue=\"irregular\">irregular</gmd:MD_MaintenanceFrequencyCode>
         </gmd:maintenanceAndUpdateFrequency>
         <gmd:maintenanceNote>
            <gco:CharacterString>Translated from ECHO using ECHOToISO.xsl Version: 1.32 (Dec. 9, 2015)</gco:CharacterString>
         </gmd:maintenanceNote>
      </gmd:MD_MaintenanceInformation>
   </gmd:metadataMaintenance>
   <gmi:acquisitionInformation>
      <gmi:MI_AcquisitionInformation />
   </gmi:acquisitionInformation>
</gmi:MI_Metadata>")

(def iso-with-use-constraints
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
  <gmi:MI_Metadata xmlns:gmi=\"http://www.isotc211.org/2005/gmi\" xmlns:eos=\"http://earthdata.nasa.gov/schema/eos\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns:gmd=\"http://www.isotc211.org/2005/gmd\" xmlns:gml=\"http://www.opengis.net/gml/3.2\" xmlns:gmx=\"http://www.isotc211.org/2005/gmx\" xmlns:gsr=\"http://www.isotc211.org/2005/gsr\" xmlns:gss=\"http://www.isotc211.org/2005/gss\" xmlns:gts=\"http://www.isotc211.org/2005/gts\" xmlns:srv=\"http://www.isotc211.org/2005/srv\" xmlns:swe=\"http://schemas.opengis.net/sweCommon/2.0/\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">
     <!--Other Properties, all:0, coi:0,ii:0,si:0,pli:0,pri:0,qi:0,gi:0,ci:0,dk:0,pcc:0,icc:0,scc:0-->
     <gmd:fileIdentifier>
        <gco:CharacterString>gov.nasa.echo:A minimal valid collection V 1</gco:CharacterString>
     </gmd:fileIdentifier>
     <gmd:language>
        <gco:CharacterString>eng</gco:CharacterString>
     </gmd:language>
     <gmd:characterSet>
        <gmd:MD_CharacterSetCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_CharacterSetCode\" codeListValue=\"utf8\">utf8</gmd:MD_CharacterSetCode>
     </gmd:characterSet>
     <gmd:hierarchyLevel>
        <gmd:MD_ScopeCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_ScopeCode\" codeListValue=\"series\">series</gmd:MD_ScopeCode>
     </gmd:hierarchyLevel>
     <gmd:contact gco:nilReason=\"missing\" />
     <gmd:dateStamp>
        <gco:DateTime>2016-07-06T18:15:49.058-04:00</gco:DateTime>
     </gmd:dateStamp>
     <gmd:metadataStandardName>
        <gco:CharacterString>ISO 19115-2 Geographic Information - Metadata Part 2 Extensions for imagery and gridded data</gco:CharacterString>
     </gmd:metadataStandardName>
     <gmd:metadataStandardVersion>
        <gco:CharacterString>ISO 19115-2:2009(E)</gco:CharacterString>
     </gmd:metadataStandardVersion>
     <gmd:identificationInfo>
        <gmd:MD_DataIdentification>
           <gmd:citation>
              <gmd:CI_Citation>
                 <gmd:title>
                    <gco:CharacterString>MINIMAL &gt; A minimal valid collection</gco:CharacterString>
                 </gmd:title>
                 <gmd:date>
                    <gmd:CI_Date>
                       <gmd:date>
                          <gco:DateTime>1999-12-31T19:00:00-05:00</gco:DateTime>
                       </gmd:date>
                       <gmd:dateType>
                          <gmd:CI_DateTypeCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode\" codeListValue=\"revision\">revision</gmd:CI_DateTypeCode>
                       </gmd:dateType>
                    </gmd:CI_Date>
                 </gmd:date>
                 <gmd:date>
                    <gmd:CI_Date>
                       <gmd:date>
                          <gco:DateTime>1999-12-31T19:00:00-05:00</gco:DateTime>
                       </gmd:date>
                       <gmd:dateType>
                          <gmd:CI_DateTypeCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode\" codeListValue=\"creation\">creation</gmd:CI_DateTypeCode>
                       </gmd:dateType>
                    </gmd:CI_Date>
                 </gmd:date>
                 <gmd:edition>
                    <gco:CharacterString>1</gco:CharacterString>
                 </gmd:edition>
                 <gmd:identifier>
                    <gmd:MD_Identifier>
                       <gmd:code>
                          <gco:CharacterString>MINIMAL</gco:CharacterString>
                       </gmd:code>
                       <gmd:description>
                          <gco:CharacterString>A minimal valid collection</gco:CharacterString>
                       </gmd:description>
                    </gmd:MD_Identifier>
                 </gmd:identifier>
                 <gmd:otherCitationDetails>
                    <gco:CharacterString />
                 </gmd:otherCitationDetails>
              </gmd:CI_Citation>
           </gmd:citation>
           <gmd:abstract>
              <gco:CharacterString>A minimal valid collection</gco:CharacterString>
           </gmd:abstract>
           <gmd:purpose gco:nilReason=\"missing\" />
           <gmd:resourceConstraints>
              <gmd:MD_LegalConstraints>
                 <gmd:useLimitation>
                    <gco:CharacterString>Restriction Comment: Dummy Comment</gco:CharacterString>
                 </gmd:useLimitation>
                 <gmd:otherConstraints>
                    <gco:CharacterString>Restriction Flag:0</gco:CharacterString>
                 </gmd:otherConstraints>
              </gmd:MD_LegalConstraints>
           </gmd:resourceConstraints>
           <gmd:language>
              <gco:CharacterString>eng</gco:CharacterString>
           </gmd:language>
           <gmd:characterSet>
              <gmd:MD_CharacterSetCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_CharacterSetCode\" codeListValue=\"utf8\">utf8</gmd:MD_CharacterSetCode>
           </gmd:characterSet>
           <gmd:extent>
            <gmd:EX_Extent id=\"boundingExtent\">
              <gmd:description>
                <gco:CharacterString>SpatialCoverageType=Horizontal, SpatialGranuleSpatialRepresentation=CARTESIAN, Temporal Range Type=Continuous Range, Time Type=UTC</gco:CharacterString>
              </gmd:description>
              <gmd:geographicElement>
                <gmd:EX_GeographicBoundingBox id=\"foo\">
                  <gmd:westBoundLongitude>
                    <gco:Decimal>-109.0</gco:Decimal>
                  </gmd:westBoundLongitude>
                  <gmd:eastBoundLongitude>
                    <gco:Decimal>11.0</gco:Decimal>
                  </gmd:eastBoundLongitude>
                  <gmd:southBoundLatitude>
                    <gco:Decimal>57.0</gco:Decimal>
                  </gmd:southBoundLatitude>
                  <gmd:northBoundLatitude>
                    <gco:Decimal>85.0</gco:Decimal>
                  </gmd:northBoundLatitude>
                </gmd:EX_GeographicBoundingBox>
              </gmd:geographicElement>
            </gmd:EX_Extent>
           </gmd:extent>
           <gmd:supplementalInformation />
           <gmd:processingLevel>
              <gmd:MD_Identifier>
                 <gmd:code>
                    <gco:CharacterString />
                 </gmd:code>
                 <gmd:description>
                    <gco:CharacterString />
                 </gmd:description>
              </gmd:MD_Identifier>
           </gmd:processingLevel>
        </gmd:MD_DataIdentification>
     </gmd:identificationInfo>
     <gmd:distributionInfo>
        <gmd:MD_Distribution>
           <gmd:distributor>
              <gmd:MD_Distributor>
                 <gmd:distributorContact gco:nilReason=\"missing\" />
                 <gmd:distributionOrderProcess>
                    <gmd:MD_StandardOrderProcess>
                       <gmd:fees gco:nilReason=\"missing\" />
                    </gmd:MD_StandardOrderProcess>
                 </gmd:distributionOrderProcess>
                 <gmd:distributorTransferOptions>
                    <gmd:MD_DigitalTransferOptions />
                 </gmd:distributorTransferOptions>
              </gmd:MD_Distributor>
           </gmd:distributor>
        </gmd:MD_Distribution>
     </gmd:distributionInfo>
     <gmd:dataQualityInfo>
        <gmd:DQ_DataQuality>
           <gmd:scope>
              <gmd:DQ_Scope>
                 <gmd:level>
                    <gmd:MD_ScopeCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_ScopeCode\" codeListValue=\"series\">series</gmd:MD_ScopeCode>
                 </gmd:level>
              </gmd:DQ_Scope>
           </gmd:scope>
           <gmd:lineage>
              <gmd:LI_Lineage>
                 <gmd:processStep>
                    <gmi:LE_ProcessStep>
                       <gmd:description gco:nilReason=\"unknown\" />
                       <gmi:processingInformation>
                        <eos:EOS_Processing>
                          <gmi:identifier/>
                          <eos:otherPropertyType>
                            <gco:RecordType xlink:href=\"http://earthdata.nasa.gov/metadata/schema/eos/1.0/eos.xsd#xpointer(//element[@name='AdditionalAttributes'])\">Echo Additional Attributes</gco:RecordType>
                          </eos:otherPropertyType>
                          <eos:otherProperty>
                            <gco:Record>
                              <eos:AdditionalAttributes>
                                <eos:AdditionalAttribute>
                                  <eos:reference>
                                    <eos:EOS_AdditionalAttributeDescription>
                                      <eos:type>
                                        <eos:EOS_AdditionalAttributeTypeCode codeList=\"http://earthdata.nasa.gov/metadata/resources/Codelists.xml#EOS_AdditionalAttributeTypeCode\" codeListValue=\"processingInformation\">processingInformation</eos:EOS_AdditionalAttributeTypeCode>
                                      </eos:type>
                                      <eos:name>
                                        <gco:CharacterString>SIPSMetGenVersion</gco:CharacterString>
                                      </eos:name>
                                      <eos:description>
                                        <gco:CharacterString>The version of the SIPSMetGen software used to produce the metadata file for this granule</gco:CharacterString>
                                      </eos:description>
                                      <eos:dataType>
                                        <eos:EOS_AdditionalAttributeDataTypeCode codeList=\"http://earthdata.nasa.gov/metadata/resources/Codelists.xml#EOS_AdditionalAttributeDataTypeCode\" codeListValue=\"STRING\">STRING</eos:EOS_AdditionalAttributeDataTypeCode>
                                      </eos:dataType>
                                      <eos:measurementResolution>
                                        <gco:CharacterString>Measurement resolution</gco:CharacterString>
                                      </eos:measurementResolution>
                                      <eos:parameterRangeBegin>
                                        <gco:CharacterString>Parameter begin</gco:CharacterString>
                                      </eos:parameterRangeBegin>
                                      <eos:parameterRangeEnd>
                                        <gco:CharacterString>Parameter End</gco:CharacterString>
                                      </eos:parameterRangeEnd>
                                      <eos:parameterUnitsOfMeasure>
                                        <gco:CharacterString>Units of Measure</gco:CharacterString>
                                      </eos:parameterUnitsOfMeasure>
                                      <eos:parameterValueAccuracy>
                                        <gco:CharacterString>Parameter Value Accuracy</gco:CharacterString>
                                      </eos:parameterValueAccuracy>
                                      <eos:valueAccuracyExplanation>
                                        <gco:CharacterString>Value Accuracy Explanation</gco:CharacterString>
                                      </eos:valueAccuracyExplanation>
                                    </eos:EOS_AdditionalAttributeDescription>
                                  </eos:reference>
                                  <eos:value>
                                    <gco:CharacterString>A Value</gco:CharacterString>
                                  </eos:value>
                                </eos:AdditionalAttribute>
                              </eos:AdditionalAttributes>
                            </gco:Record>
                          </eos:otherProperty>
                        </eos:EOS_Processing>
                      </gmi:processingInformation>
                    </gmi:LE_ProcessStep>
                 </gmd:processStep>
              </gmd:LI_Lineage>
           </gmd:lineage>
        </gmd:DQ_DataQuality>
     </gmd:dataQualityInfo>
     <gmd:metadataMaintenance>
        <gmd:MD_MaintenanceInformation>
           <gmd:maintenanceAndUpdateFrequency>
              <gmd:MD_MaintenanceFrequencyCode codeList=\"http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_MaintenanceFrequencyCode\" codeListValue=\"irregular\">irregular</gmd:MD_MaintenanceFrequencyCode>
           </gmd:maintenanceAndUpdateFrequency>
           <gmd:maintenanceNote>
              <gco:CharacterString>Translated from ECHO using ECHOToISO.xsl Version: 1.32 (Dec. 9, 2015)</gco:CharacterString>
           </gmd:maintenanceNote>
        </gmd:MD_MaintenanceInformation>
     </gmd:metadataMaintenance>
     <gmi:acquisitionInformation>
        <gmi:MI_AcquisitionInformation />
     </gmi:acquisitionInformation>
  </gmi:MI_Metadata>")

(def constraints-path [:identificationInfo :MD_DataIdentification :resourceConstraints])

(deftest iso-constraints
  (testing "Use constraints"
   (are3 [umm-map expected-iso]
     (let [expected-iso-parsed (x/parse-str expected-iso)
           expected-iso-constraints (xml/element-at-path expected-iso-parsed constraints-path)
           generated-iso (iso/umm-c-to-iso19115-2-xml (coll/map->UMM-C umm-map))
           generated-iso-parsed (x/parse-str generated-iso)
           generated-iso-constraints (xml/element-at-path generated-iso-parsed constraints-path)]
       (is (= expected-iso-constraints generated-iso-constraints)))

    "No use constraints"
    {} iso-no-use-constraints

    "With use constraints"
    {:AccessConstraints {:Description "Dummy Comment" :Value 0}} iso-with-use-constraints)))

(deftest data-quality-info-additional-attributes
  (testing "additional attributes that should go to dataQualityInfo section are written out correctly"
    (let [parsed (#'parser/parse-iso19115-xml (lkt/setup-context-for-test lkt/sample-keyword-map)
                                              iso-with-use-constraints)
          ;; all the parsed additional attributes are from dataQualityInfo and we use it as the expected value
          expected-additional-attributes (:AdditionalAttributes parsed)
          generated-iso (iso/umm-c-to-iso19115-2-xml parsed)
          ;; parse out the dataQualtiyInfo additional attributes
          parsed-additional-attributes (#'aa/parse-data-quality-info-additional-attributes generated-iso)]
      ;; validate against xml schema
      (is (empty? (core/validate-xml :collection :iso19115 generated-iso)))
      (is (not (empty? parsed-additional-attributes)))
      (is (= expected-additional-attributes parsed-additional-attributes)))))

(deftest granule-spatial-representation
  (testing "granule spatial representation is parsed correctly"
    (let [parsed (#'parser/parse-iso19115-xml (lkt/setup-context-for-test lkt/sample-keyword-map)
                                              iso-with-use-constraints)
          gran-spatial-representation (get-in parsed [:SpatialExtent :GranuleSpatialRepresentation])]
      (is (= "CARTESIAN" gran-spatial-representation)))))
