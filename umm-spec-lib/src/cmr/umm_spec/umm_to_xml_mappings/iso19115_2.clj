(ns cmr.umm-spec.umm-to-xml-mappings.iso19115-2
  "Defines mappings from UMM records into ISO19115-2 XML."
  (:require [clojure.string :as str]
            [cmr.common.util :as util]
            [cmr.umm-spec.umm-to-xml-mappings.iso19115-2.spatial :as spatial]
            [cmr.common.xml.gen :refer :all]
            [cmr.umm-spec.util :as su :refer [char-string]]
            [cmr.umm-spec.date-util :as date-util]
            [cmr.umm-spec.iso-keywords :as kws]
            [cmr.umm-spec.umm-to-xml-mappings.iso19115-2.platform :as platform]
            [cmr.umm-spec.umm-to-xml-mappings.iso19115-2.tiling-system :as tiling]
            [cmr.umm-spec.umm-to-xml-mappings.iso19115-2.organizations-personnel :as org-per]
            [cmr.umm-spec.iso19115-2-util :as iso]
            [cmr.umm-spec.umm-to-xml-mappings.iso19115-2.distributions-related-url :as dru]
            [cmr.umm-spec.umm-to-xml-mappings.iso19115-2.additional-attribute :as aa]
            [cmr.umm-spec.umm-to-xml-mappings.iso19115-2.metadata-association :as ma]
            [cmr.umm-spec.location-keywords :as lk]))

(def iso19115-2-xml-namespaces
  {:xmlns:xs "http://www.w3.org/2001/XMLSchema"
   :xmlns:gmx "http://www.isotc211.org/2005/gmx"
   :xmlns:gss "http://www.isotc211.org/2005/gss"
   :xmlns:gco "http://www.isotc211.org/2005/gco"
   :xmlns:xsi "http://www.w3.org/2001/XMLSchema-instance"
   :xmlns:gmd "http://www.isotc211.org/2005/gmd"
   :xmlns:gmi "http://www.isotc211.org/2005/gmi"
   :xmlns:gml "http://www.opengis.net/gml/3.2"
   :xmlns:xlink "http://www.w3.org/1999/xlink"
   :xmlns:eos "http://earthdata.nasa.gov/schema/eos"
   :xmlns:srv "http://www.isotc211.org/2005/srv"
   :xmlns:gts "http://www.isotc211.org/2005/gts"
   :xmlns:swe "http://schemas.opengis.net/sweCommon/2.0/"
   :xmlns:gsr "http://www.isotc211.org/2005/gsr"})

(def iso-topic-categories
  #{"farming"
    "biota"
    "boundaries"
    "climatologyMeteorologyAtmosphere"
    "economy"
    "elevation"
    "environment"
    "geoscientificInformation"
    "health"
    "imageryBaseMapsEarthCover"
    "intelligenceMilitary"
    "inlandWaters"
    "location"
    "oceans"
    "planningCadastre"
    "society"
    "structure"
    "transportation"
    "utilitiesCommunication"})

(defn- generate-data-dates
  "Returns ISO XML elements for the DataDates of given UMM collection."
  [c]
  ;; Use a default value if none present in the UMM record
  (let [dates (or (:DataDates c) [{:Type "CREATE" :Date date-util/default-date-value}])]
    (for [date dates
          :let [type-code (get iso/iso-date-type-codes (:Type date))
                date-value (or (:Date date) date-util/default-date-value)]]
      [:gmd:date
       [:gmd:CI_Date
        [:gmd:date
         [:gco:DateTime date-value]]
        [:gmd:dateType
         [:gmd:CI_DateTypeCode {:codeList (str (:ngdc iso/code-lists) "#CI_DateTypeCode")
                                :codeListValue type-code} type-code]]]])))


(defn iso-topic-value->sanitized-iso-topic-category
  "Ensures an uncontrolled IsoTopicCategory value is on the schema-defined list or substitues a
  default value."
  [category-value]
  (get iso-topic-categories category-value "location"))

(def attribute-data-type-code-list
  "http://earthdata.nasa.gov/metadata/resources/Codelists.xml#EOS_AdditionalAttributeDataTypeCode")

(defn- generate-projects-keywords
  "Returns the content generator instructions for descriptive keywords of the given projects."
  [projects]
  (let [project-keywords (map iso/generate-title projects)]
    (kws/generate-iso19115-descriptive-keywords "project" project-keywords)))

(defn- generate-projects
  [projects]
  (for [proj projects]
    (let [{short-name :ShortName} proj]
      [:gmi:operation
       [:gmi:MI_Operation
        [:gmi:description
         (char-string (iso/generate-title proj))]
        [:gmi:identifier
         [:gmd:MD_Identifier
          [:gmd:code
           (char-string short-name)]]]
        [:gmi:status ""]
        [:gmi:parentOperation {:gco:nilReason "inapplicable"}]]])))

(defn- generate-publication-references
  [pub-refs]
  (for [pub-ref pub-refs
        ;; Title and PublicationDate are required fields in ISO
        :when (and (:Title pub-ref) (:PublicationDate pub-ref))]
    [:gmd:aggregationInfo
     [:gmd:MD_AggregateInformation
      [:gmd:aggregateDataSetName
       [:gmd:CI_Citation
        [:gmd:title (char-string (:Title pub-ref))]
        (when (:PublicationDate pub-ref)
          [:gmd:date
           [:gmd:CI_Date
            [:gmd:date
             [:gco:Date (second (re-matches #"(\d\d\d\d-\d\d-\d\d)T.*" (str (:PublicationDate pub-ref))))]]
            [:gmd:dateType
             [:gmd:CI_DateTypeCode
              {:codeList (str (:iso iso/code-lists) "#CI_DateTypeCode")
               :codeListValue "publication"} "publication"]]]])
        [:gmd:edition (char-string (:Edition pub-ref))]
        (when (:DOI pub-ref)
          [:gmd:identifier
           [:gmd:MD_Identifier
            [:gmd:code (char-string (get-in pub-ref [:DOI :DOI]))]
            [:gmd:description (char-string "DOI")]]])
        [:gmd:citedResponsibleParty
         [:gmd:CI_ResponsibleParty
          [:gmd:organisationName (char-string (:Author pub-ref))]
          [:gmd:role
           [:gmd:CI_RoleCode
            {:codeList (str (:ngdc iso/code-lists) "#CI_RoleCode")
             :codeListValue "author"} "author"]]]]
        [:gmd:citedResponsibleParty
         [:gmd:CI_ResponsibleParty
          [:gmd:organisationName (char-string (:Publisher pub-ref))]
          [:gmd:role
           [:gmd:CI_RoleCode
            {:codeList (str (:ngdc iso/code-lists) "#CI_RoleCode")
             :codeListValue "publisher"} "publication"]]]]
        [:gmd:series
         [:gmd:CI_Series
          [:gmd:name (char-string (:Series pub-ref))]
          [:gmd:issueIdentification (char-string (:Issue pub-ref))]
          [:gmd:page (char-string (:Pages pub-ref))]]]
        [:gmd:otherCitationDetails (char-string (:OtherReferenceDetails pub-ref))]
        [:gmd:ISBN (char-string (:ISBN pub-ref))]]]
      [:gmd:associationType
       [:gmd:DS_AssociationTypeCode
        {:codeList (str (:ngdc iso/code-lists) "#DS_AssociationTypeCode")
         :codeListValue "Input Collection"} "Input Collection"]]]]))

(defn extent-description-string
  "Returns the ISO extent description string (a \"key=value,key=value\" string) for the given UMM-C
  collection record."
  [c]
  (let [vsd (first (-> c :SpatialExtent :VerticalSpatialDomains))
        temporal (first (:TemporalExtents c))
        m {"VerticalSpatialDomainType" (:Type vsd)
           "VerticalSpatialDomainValue" (:Value vsd)
           "SpatialCoverageType" (-> c :SpatialExtent :SpatialCoverageType)
           "SpatialGranuleSpatialRepresentation" (-> c :SpatialExtent :GranuleSpatialRepresentation)
           "Temporal Range Type" (:TemporalRangeType temporal)}]
    (str/join "," (for [[k v] m
                        :when (some? v)]
                    (str k "=" (str/replace v #"[,=]" ""))))))

(defn umm-c-to-iso19115-2-xml
  "Returns the generated ISO19115-2 xml from UMM collection record c."
  [c]
  (let [platforms (platform/platforms-with-id (:Platforms c))
        organizations (:Organizations c)]
    (xml
      [:gmi:MI_Metadata
       iso19115-2-xml-namespaces
       [:gmd:fileIdentifier (char-string (:EntryTitle c))]
       [:gmd:language (char-string "eng")]
       [:gmd:characterSet
        [:gmd:MD_CharacterSetCode {:codeList (str (:ngdc iso/code-lists) "#MD_CharacterSetCode")
                                   :codeListValue "utf8"} "utf8"]]
       [:gmd:hierarchyLevel
        [:gmd:MD_ScopeCode {:codeList (str (:ngdc iso/code-lists) "#MD_ScopeCode")
                            :codeListValue "series"} "series"]]
       (if-let [responsibilities (org-per/responsibility-by-role (:Personnel c) "POINTOFCONTACT")]
         (for [responsibility responsibilities]
           [:gmd:contact
            (org-per/generate-responsible-party responsibility)])
         [:gmd:contact {:gco:nilReason "missing"}])
       [:gmd:dateStamp
        [:gco:DateTime "2014-08-25T15:25:44.641-04:00"]]
       [:gmd:metadataStandardName (char-string "ISO 19115-2 Geographic Information - Metadata Part 2 Extensions for imagery and gridded data")]
       [:gmd:metadataStandardVersion (char-string "ISO 19115-2:2009(E)")]
       (spatial/coordinate-system-element c)
       [:gmd:identificationInfo
        [:gmd:MD_DataIdentification
         [:gmd:citation
          [:gmd:CI_Citation
           [:gmd:title (char-string (:EntryTitle c))]
           (generate-data-dates c)
           [:gmd:edition (char-string (:Version c))]
           [:gmd:identifier
            [:gmd:MD_Identifier
             [:gmd:code (char-string (:ShortName c))]
             [:gmd:version (char-string (:Version c))]]]
           (for [responsibility (org-per/responsibility-by-role (:Organizations c) "ORIGINATOR")]
             [:gmd:citedResponsibleParty
              (org-per/generate-responsible-party responsibility)])]]
         [:gmd:abstract (char-string (or (:Abstract c) su/not-provided))]
         [:gmd:purpose {:gco:nilReason "missing"} (char-string (:Purpose c))]
         [:gmd:status
          (when-let [collection-progress (:CollectionProgress c)]
            [:gmd:MD_ProgressCode
             {:codeList (str (:ngdc iso/code-lists) "#MD_ProgressCode")
              :codeListValue (str/lower-case collection-progress)}
             collection-progress])]
         (for [responsibility (org-per/responsibility-by-role (:Organizations c) "POINTOFCONTACT")]
           [:gmd:pointOfContact
            (org-per/generate-responsible-party responsibility)])
         (dru/generate-browse-urls c)
         (generate-projects-keywords (:Projects c))
         (kws/generate-iso19115-descriptive-keywords
           "theme" (map kws/science-keyword->iso-keyword-string (:ScienceKeywords c)))
         (kws/generate-iso19115-descriptive-keywords "place"
                                                     (lk/location-keywords->spatial-keywords
                                                      (:LocationKeywords c)))
         (kws/generate-iso19115-descriptive-keywords "temporal" (:TemporalKeywords c))
         (kws/generate-iso19115-descriptive-keywords nil (:AncillaryKeywords c))
         (platform/generate-platform-keywords platforms)
         (platform/generate-instrument-keywords platforms)
         [:gmd:resourceConstraints
          [:gmd:MD_LegalConstraints
           [:gmd:useLimitation (char-string (:UseConstraints c))]
           [:gmd:useLimitation
            [:gco:CharacterString (str "Restriction Comment:" (-> c :AccessConstraints :Description))]]
           [:gmd:otherConstraints
            [:gco:CharacterString (str "Restriction Flag:" (-> c :AccessConstraints :Value))]]]]
         (ma/generate-non-source-metadata-associations c)
         (generate-publication-references (:PublicationReferences c))
         [:gmd:language (char-string (or (:DataLanguage c) "eng"))]
         (for [topic-category (:ISOTopicCategories c)]
           [:gmd:topicCategory
            [:gmd:MD_TopicCategoryCode (iso-topic-value->sanitized-iso-topic-category topic-category)]])
         [:gmd:extent
          [:gmd:EX_Extent {:id "boundingExtent"}
           [:gmd:description
            [:gco:CharacterString (extent-description-string c)]]
           (tiling/tiling-system-elements c)
           (spatial/spatial-extent-elements c)
           (spatial/generate-orbit-parameters c)
           (for [temporal (:TemporalExtents c)
                 rdt (:RangeDateTimes temporal)]
             [:gmd:temporalElement
              [:gmd:EX_TemporalExtent
               [:gmd:extent
                [:gml:TimePeriod {:gml:id (su/generate-id)}
                 [:gml:beginPosition (:BeginningDateTime rdt)]
                 (if (:EndsAtPresentFlag temporal)
                   [:gml:endPosition {:indeterminatePosition "now"}]
                   [:gml:endPosition (su/nil-to-empty-string (:EndingDateTime rdt))])]]]])
           (for [temporal (:TemporalExtents c)
                 date (:SingleDateTimes temporal)]
             [:gmd:temporalElement
              [:gmd:EX_TemporalExtent
               [:gmd:extent
                [:gml:TimeInstant {:gml:id (su/generate-id)}
                 [:gml:timePosition date]]]]])]]
         [:gmd:processingLevel
          [:gmd:MD_Identifier
           [:gmd:code (char-string (-> c :ProcessingLevel :Id))]
           [:gmd:description (char-string (-> c :ProcessingLevel :ProcessingLevelDescription))]]]]]
       (aa/generate-additional-attributes (:AdditionalAttributes c))
       [:gmd:contentInfo
        [:gmd:MD_ImageDescription
         [:gmd:attributeDescription ""]
         [:gmd:contentType ""]
         [:gmd:processingLevelCode
          [:gmd:MD_Identifier
           [:gmd:code (char-string (-> c :ProcessingLevel :Id))]
           [:gmd:description (char-string (-> c :ProcessingLevel :ProcessingLevelDescription))]]]]]
       (dru/generate-distributions c)
       [:gmd:dataQualityInfo
        [:gmd:DQ_DataQuality
         [:gmd:scope
          [:gmd:DQ_Scope
           [:gmd:level
            [:gmd:MD_ScopeCode
             {:codeList (str (:ngdc iso/code-lists) "#MD_ScopeCode")
              :codeListValue "series"}
             "series"]]]]
         [:gmd:report
          [:gmd:DQ_AccuracyOfATimeMeasurement
           [:gmd:measureIdentification
            [:gmd:MD_Identifier
             [:gmd:code
              (char-string "PrecisionOfSeconds")]]]
           [:gmd:result
            [:gmd:DQ_QuantitativeResult
             [:gmd:valueUnit ""]
             [:gmd:value
              [:gco:Record {:xsi:type "gco:Real_PropertyType"}
               [:gco:Real (:PrecisionOfSeconds (first (:TemporalExtents c)))]]]]]]]
         (when-let [quality (:Quality c)]
           [:gmd:report
            [:gmd:DQ_QuantitativeAttributeAccuracy
             [:gmd:evaluationMethodDescription (char-string quality)]
             [:gmd:result {:gco:nilReason "missing"}]]])
         [:gmd:lineage
          [:gmd:LI_Lineage
           [:gmd:processStep
            [:gmd:LI_ProcessStep
             [:gmd:description {:gco:nilReason "unknown"}]
             (for [responsibility (org-per/responsibility-by-role (:Organizations c) "PROCESSOR")]
               [:gmd:processor
                (org-per/generate-responsible-party responsibility)])]]
           (ma/generate-source-metadata-associations c)]]]]
       [:gmi:acquisitionInformation
        [:gmi:MI_AcquisitionInformation
         (platform/generate-instruments platforms)
         (generate-projects (:Projects c))
         (platform/generate-platforms platforms)]]])))
