(ns cmr.umm-spec.xml-to-umm-mappings.echo10
  "Defines mappings from ECHO10 XML into UMM records"
  (:require
   [clojure.string :as str]
   [cmr.common.util :as util]
   [cmr.common.xml.parse :refer :all]
   [cmr.common.xml.simple-xpath :refer [select text]]
   [cmr.umm-spec.date-util :as date]
   [cmr.umm-spec.json-schema :as js]
   [cmr.umm-spec.location-keywords :as lk]
   [cmr.umm-spec.util :as u]
   [cmr.umm-spec.xml-to-umm-mappings.echo10.data-contact :as dc]
   [cmr.umm-spec.xml-to-umm-mappings.echo10.related-url :as ru]
   [cmr.umm-spec.xml-to-umm-mappings.echo10.spatial :as spatial]))

(defn parse-temporal
  "Returns seq of UMM temporal extents from an ECHO10 XML document."
  [doc]
  (for [temporal (select doc "/Collection/Temporal")]
    {:TemporalRangeType (value-of temporal "TemporalRangeType")
     :PrecisionOfSeconds (value-of temporal "PrecisionOfSeconds")
     :EndsAtPresentFlag (value-of temporal "EndsAtPresentFlag")
     :RangeDateTimes (for [rdt (select temporal "RangeDateTime")]
                       (fields-from rdt :BeginningDateTime :EndingDateTime))
     :SingleDateTimes (values-at temporal "SingleDateTime")
     :PeriodicDateTimes (for [pdt (select temporal "PeriodicDateTime")]
                          (fields-from pdt :Name :StartDate :EndDate :DurationUnit :DurationValue
                                       :PeriodCycleDurationUnit :PeriodCycleDurationValue))}))

(defn parse-characteristic
  "Returns a UMM characteristic record from an ECHO10 Characteristic element."
  [element]
  (fields-from element :Name :Description :DataType :Unit :Value))

(defn parse-characteristics
  "Returns a seq of UMM characteristic records from the element's child Characteristics."
  [el]
  (map parse-characteristic (select el "Characteristics/Characteristic")))

(defn parse-sensor
  "Returns a UMM Sensor record from an ECHO10 Sensor element."
  [sensor-element]
  (assoc (fields-from sensor-element :ShortName :LongName :Technique)
         :Characteristics (parse-characteristics sensor-element)))

(defn parse-instrument
  "Returns a UMM Instrument record from an ECHO10 Instrument element."
  [inst]
  (assoc (fields-from inst :ShortName :LongName :Technique :NumberOfSensors)
         :OperationalModes (values-at inst "OperationModes/OperationMode")
         :Characteristics (parse-characteristics inst)
         :Sensors (map parse-sensor (select inst "Sensors/Sensor"))))

(defn parse-metadata-association
  "Returns a UMM MetadataAssocation from an ECHO10 CollectionAsscociation element."
  [element]
  (let [version-id (value-of element "VersionId")
        assoc-type (value-of element "CollectionType")]
    {:EntryId (value-of element "ShortName")
     :Version (u/without-default version-id)
     :Type (some-> (u/without-default assoc-type)
                   str/upper-case)
     :Description (value-of element "CollectionUse")}))

(defn parse-metadata-associations
  "Returns a seq of UMM MetadataAssocations from an ECHO10 document."
  [doc]
  (map parse-metadata-association
       (select doc "/Collection/CollectionAssociations/CollectionAssociation")))

(defn parse-data-dates
  "Returns UMM DataDates seq from ECHO 10 XML document."
  [doc]
  (for [[date-type xpath] [["CREATE" "InsertTime"]
                           ["UPDATE" "LastUpdate"]
                           ["DELETE" "DeleteTime"]]
        :let [date-val (date/without-default (value-of doc (str "/Collection/" xpath)))]
        :when date-val]
    {:Type date-type
     :Date date-val}))

(defn parse-tiling
  "Returns a UMM TilingIdentificationSystem map from the given ECHO10 XML document."
  [doc]
  (for [sys-el (select doc "/Collection/TwoDCoordinateSystems/TwoDCoordinateSystem")]
    {:TilingIdentificationSystemName (u/without-default
                                       (value-of sys-el "TwoDCoordinateSystemName"))
     :Coordinate1 (fields-from (first (select sys-el "Coordinate1")) :MinimumValue :MaximumValue)
     :Coordinate2 (fields-from (first (select sys-el "Coordinate2")) :MinimumValue :MaximumValue)}))

(defn- parse-platforms
  "Parses platforms from the ECHO10 collection document."
  [doc]
  (for [plat (select doc "/Collection/Platforms/Platform")]
    {:ShortName (value-of plat "ShortName")
     :LongName (value-of plat "LongName")
     :Type (u/without-default-value-of plat "Type")
     :Characteristics (parse-characteristics plat)
     :Instruments (map parse-instrument (select plat "Instruments/Instrument"))}))

(defn- parse-metadata-dates
  "ECHO10 only has a revision date (UPDATE), so get that if applicable"
  [doc]
  (when-let [revision-date (date/parse-date-type-from-xml doc "Collection/RevisionDate" "UPDATE")]
    [revision-date]))

(defn- parse-science-keywords
  "Parse ECHO10 science keywords or use default if applicable"
  [doc apply-default?]
  (if-let [science-keywords (seq (select doc "/Collection/ScienceKeywords/ScienceKeyword"))]
    (for [sk science-keywords]
      {:Category (value-of sk "CategoryKeyword")
       :Topic (value-of sk "TopicKeyword")
       :Term (value-of sk "TermKeyword")
       :VariableLevel1 (value-of sk "VariableLevel1Keyword/Value")
       :VariableLevel2 (value-of sk "VariableLevel1Keyword/VariableLevel2Keyword/Value")
       :VariableLevel3 (value-of sk "VariableLevel1Keyword/VariableLevel2Keyword/VariableLevel3Keyword")
       :DetailedVariable (value-of sk "DetailedVariableKeyword")})
    (when apply-default?
      u/not-provided-science-keywords)))

(defn- parse-access-constraints
  "If both value and Description are nil, return nil.
  Otherwise, if Description is nil, assoc it with u/not-provided"
  [doc apply-default?]
  (let [access-constraints-record
        {:Description (value-of doc "/Collection/RestrictionComment")
         :Value (value-of doc "/Collection/RestrictionFlag")}]
    (when (seq (util/remove-nil-keys access-constraints-record))
      (update access-constraints-record :Description #(u/with-default % apply-default?)))))

(defn- parse-echo10-xml
  "Returns UMM-C collection structure from ECHO10 collection XML document."
  [context doc {:keys [apply-default?]}]
  {:EntryTitle (value-of doc "/Collection/DataSetId")
   :ShortName  (value-of doc "/Collection/ShortName")
   :Version    (value-of doc "/Collection/VersionId")
   :DataDates  (parse-data-dates doc)
   :MetadataDates (parse-metadata-dates doc)
   :Abstract   (value-of doc "/Collection/Description")
   :CollectionDataType (value-of doc "/Collection/CollectionDataType")
   :Purpose    (value-of doc "/Collection/SuggestedUsage")
   :CollectionProgress (value-of doc "/Collection/CollectionState")
   :AccessConstraints (parse-access-constraints doc apply-default?)
   :Distributions [{:DistributionFormat (value-of doc "/Collection/DataFormat")
                    :Fees (value-of doc "/Collection/Price")}]
   :TemporalKeywords (values-at doc "/Collection/TemporalKeywords/Keyword")
   :LocationKeywords (lk/spatial-keywords->location-keywords
                      (lk/get-spatial-keywords-maps context)
                      (values-at doc "/Collection/SpatialKeywords/Keyword"))
   :SpatialExtent    (spatial/parse-spatial doc apply-default?)
   :TemporalExtents  (or (seq (parse-temporal doc))
                         (when apply-default? u/not-provided-temporal-extents))
   :Platforms (or (seq (parse-platforms doc))
                  (when apply-default? u/not-provided-platforms))
   :ProcessingLevel {:Id (u/with-default (value-of doc "/Collection/ProcessingLevelId") apply-default?)
                     :ProcessingLevelDescription (value-of doc "/Collection/ProcessingLevelDescription")}
   :AdditionalAttributes (for [aa (select doc "/Collection/AdditionalAttributes/AdditionalAttribute")]
                           {:Name (value-of aa "Name")
                            :DataType (value-of aa "DataType")
                            :Description (u/with-default (value-of aa "Description") apply-default?)
                            :ParameterRangeBegin (value-of aa "ParameterRangeBegin")
                            :ParameterRangeEnd (value-of aa "ParameterRangeEnd")
                            :Value (value-of aa "Value")})
   :MetadataAssociations (parse-metadata-associations doc)
   :Projects (for [proj (select doc "/Collection/Campaigns/Campaign")]
               {:ShortName (value-of proj "ShortName")
                :LongName (value-of proj "LongName")
                :StartDate (value-of proj "StartDate")
                :EndDate (value-of proj "EndDate")})
   :TilingIdentificationSystems (parse-tiling doc)
   :RelatedUrls (ru/parse-related-urls doc apply-default?)
   :ScienceKeywords (parse-science-keywords doc apply-default?)
   :DataCenters (dc/parse-data-centers doc apply-default?)
   :ContactPersons (dc/parse-data-contact-persons doc apply-default?)})

(defn echo10-xml-to-umm-c
  "Returns UMM-C collection record from ECHO10 collection XML document. The :apply-default? option
  tells the parsing code to set the default values for fields when parsing the metadata into umm."
  [context metadata options]
  (js/parse-umm-c (parse-echo10-xml context metadata options)))
