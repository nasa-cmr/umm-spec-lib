(ns cmr.umm-spec.xml-to-umm-mappings.iso-smap
  "Defines mappings from ISO-SMAP XML to UMM records"
  (:require [cmr.umm-spec.xml-to-umm-mappings.dsl :refer :all]
            [cmr.umm-spec.xml-to-umm-mappings.add-parse-type :as apt]
            [cmr.umm-spec.json-schema :as js]
            [cmr.umm-spec.iso-smap-utils :as utils]))

(def metadata-base-xpath
  "/gmd:DS_Series/gmd:seriesMetadata/gmi:MI_Metadata")

(def md-identification-base-xpath
  (str metadata-base-xpath "/gmd:identificationInfo/gmd:MD_DataIdentification"))

(def short-name-identification-xpath
  (str md-identification-base-xpath
       "[gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier"
       "/gmd:description/gco:CharacterString='The ECS Short Name']"))

(def abstract-xpath
  (char-string-xpath short-name-identification-xpath "/gmd:abstract"))

(def purpose-xpath
  (char-string-xpath short-name-identification-xpath "/gmd:purpose"))

(def data-language-xpath
  (xpath (str short-name-identification-xpath
              "/gmd:language/gco:CharacterString")))

(def entry-title-xpath
  (xpath (str md-identification-base-xpath
              "[gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString='DataSetId']"
              "/gmd:aggregationInfo/gmd:MD_AggregateInformation/gmd:aggregateDataSetIdentifier"
              "/gmd:MD_Identifier/gmd:code/gco:CharacterString")))

(def entry-id-xpath
  (xpath (str md-identification-base-xpath
              "/gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier"
              "[gmd:description/gco:CharacterString='The ECS Short Name']"
              "/gmd:code/gco:CharacterString")))

(def version-xpath
  (xpath (str md-identification-base-xpath
              "/gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier"
              "[gmd:description/gco:CharacterString='The ECS Version ID']"
              "/gmd:code/gco:CharacterString")))


(def temporal-extent-xpath-str
  (str md-identification-base-xpath
       "/gmd:extent/gmd:EX_Extent/gmd:temporalElement/gmd:EX_TemporalExtent/gmd:extent"))

(def keywords-xpath-str
  (str md-identification-base-xpath
       "/gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword/gco:CharacterString"))

(def iso-smap-xml-to-umm-c
  (apt/add-parsing-types
    js/umm-c-schema
    (object {:EntryId entry-id-xpath
             :EntryTitle entry-title-xpath
             :Version version-xpath
             :Abstract abstract-xpath
             :Purpose purpose-xpath
             :DataLanguage data-language-xpath
             :Platforms (for-each keywords-xpath-str
                          (fn [xpath-context]
                            (let [keyword-str (->> xpath-context :context first :content (apply str))]
                              (utils/parse-platform keyword-str))))
             :TemporalExtents (for-each temporal-extent-xpath-str
                                (object {:RangeDateTimes (for-each "gml:TimePeriod"
                                                           (object {:BeginningDateTime (xpath "gml:beginPosition")
                                                                    :EndingDateTime    (xpath "gml:endPosition")}))
                                         :SingleDateTimes (select "gml:TimeInstant/gml:timePosition")}))})))
