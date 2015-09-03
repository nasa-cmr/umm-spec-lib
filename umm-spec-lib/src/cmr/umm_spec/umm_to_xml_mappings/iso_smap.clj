(ns cmr.umm-spec.umm-to-xml-mappings.iso-smap
  "Defines mappings from UMM records into ISO SMAP XML."
  (:require [clojure.string :as str]
            [cmr.umm-spec.umm-to-xml-mappings.iso-util :refer [gen-id]]
            [cmr.umm-spec.umm-to-xml-mappings.dsl :refer :all]
            [cmr.umm-spec.iso-smap-utils :as util]))

(def iso-smap-xml-namespaces
  {:xmlns:gmd "http://www.isotc211.org/2005/gmd"
   :xmlns:gco "http://www.isotc211.org/2005/gco"
   :xmlns:gmi "http://www.isotc211.org/2005/gmi"
   :xmlns:gml "http://www.opengis.net/gml/3.2"
   :xmlns:gmx "http://www.isotc211.org/2005/gmx"
   :xmlns:gsr "http://www.isotc211.org/2005/gsr"
   :xmlns:gss "http://www.isotc211.org/2005/gss"
   :xmlns:gts "http://www.isotc211.org/2005/gts"
   :xmlns:srv "http://www.isotc211.org/2005/srv"
   :xmlns:xlink "http://www.w3.org/1999/xlink"
   :xmlns:xsi "http://www.w3.org/2001/XMLSchema-instance"})

(defn generate-keyword
  "Returns ISO SMAP keyword content for an XPath context containing a UMM Platform/Instrument/etc.
  record."
  [{[record] :context}]
  (util/smap-keyword-str record))

(defn- date-mapping
  "Returns the date element mapping for the given name and date value in string format."
  [date-name value]
  [:gmd:date
   [:gmd:CI_Date
    [:gmd:date
     [:gco:DateTime value]]
    [:gmd:dateType
     [:gmd:CI_DateTypeCode {:codeList "http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode"
                            :codeListValue date-name} date-name]]]])

(defn- generate-collection-progress
  "Returns content generator instruction for the CollectionProgress field."
  [xpath-context]
  (when-let [collection-progress (-> xpath-context :context first :CollectionProgress)]
    [:gmd:MD_ProgressCode
     {:codeList "http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#MD_ProgressCode"
      :codeListValue (str/lower-case collection-progress)}
     collection-progress]))

(def umm-c-to-iso-smap-xml
  [:gmd:DS_Series
   iso-smap-xml-namespaces
   [:gmd:composedOf {:gco:nilReason "inapplicable"}]
   [:gmd:seriesMetadata
    [:gmi:MI_Metadata
     [:gmd:language (char-string "eng")]
     [:gmd:contact {:xlink:href "#alaskaSARContact"}]
     [:gmd:dateStamp
      [:gco:Date "2013-01-02"]]
     [:gmd:identificationInfo
      [:gmd:MD_DataIdentification
       [:gmd:citation
        [:gmd:CI_Citation
         [:gmd:title (char-string "SMAP Level 1A Parsed Radar Instrument Telemetry")]
         (date-mapping "revision" "2000-12-31T19:00:00-05:00")

         [:gmd:identifier
          [:gmd:MD_Identifier
           [:gmd:code (char-string-from "/EntryId")]
           [:gmd:description [:gco:CharacterString "The ECS Short Name"]]]]

         [:gmd:identifier
          [:gmd:MD_Identifier
           [:gmd:code (char-string-from "/Version")]
           [:gmd:description [:gco:CharacterString "The ECS Version ID"]]]]]]
       [:gmd:abstract (char-string-from "/Abstract")]
       [:gmd:purpose {:gco:nilReason "missing"} (char-string-from "/Purpose")]
       [:gmd:status generate-collection-progress]
       [:gmd:descriptiveKeywords
        [:gmd:MD_Keywords
         (for-each "/Platforms"
           [:gmd:keyword
            [:gco:CharacterString generate-keyword]])
         (for-each "/Platforms/Instruments"
           [:gmd:keyword
            [:gco:CharacterString generate-keyword]])]]
       [:gmd:language (char-string-from "/DataLanguage")]
       [:gmd:extent
        [:gmd:EX_Extent
         (for-each "/TemporalExtents/RangeDateTimes"
           [:gmd:temporalElement
            [:gmd:EX_TemporalExtent
             [:gmd:extent
              [:gml:TimePeriod {:gml:id gen-id}
               [:gml:beginPosition (xpath "BeginningDateTime")]
               [:gml:endPosition (xpath "EndingDateTime")]]]]])
         (for-each "/TemporalExtents/SingleDateTimes"
           [:gmd:temporalElement
            [:gmd:EX_TemporalExtent
             [:gmd:extent
              [:gml:TimeInstant {:gml:id gen-id}
               [:gml:timePosition (xpath ".")]]]]])]]]]
     [:gmd:identificationInfo
      [:gmd:MD_DataIdentification
       [:gmd:citation
        [:gmd:CI_Citation
         [:gmd:title (char-string "DataSetId")]
         (date-mapping "revision" "2000-12-31T19:00:00-05:00")]]
       [:gmd:abstract (char-string "DataSetId")]
       [:gmd:aggregationInfo
        [:gmd:MD_AggregateInformation
         [:gmd:aggregateDataSetIdentifier
          [:gmd:MD_Identifier
           [:gmd:code (char-string-from "/EntryTitle")]]]
         [:gmd:associationType
          [:gmd:DS_AssociationTypeCode {:codeList "http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#DS_AssociationTypeCode"
                                        :codeListValue "largerWorkCitation"}
           "largerWorkCitation"]]]]
       [:gmd:language (char-string "eng")]]]]]])

