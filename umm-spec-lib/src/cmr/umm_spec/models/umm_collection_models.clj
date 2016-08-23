;; WARNING: This file was generated from umm-c-json-schema.json. Do not manually modify.
(ns cmr.umm-spec.models.umm-collection-models
   "Defines UMM-C clojure records."
 (:require [cmr.common.dev.record-pretty-printer :as record-pretty-printer]))

(defrecord UMM-C
  [
   ;; Information required to properly cite the collection in professional scientific literature.
   ;; This element provides information for constructing a citation for the item itself, and is not
   ;; designed for listing bibliographic references of scientific research articles arising from
   ;; search results. A list of references related to the research results should be in the
   ;; Publication Reference element. A DOI that specifically identifies the landing page for the
   ;; collection is listed here.
   CollectionCitations

   ;; Controlled hierarchical keywords used to specify the spatial location of the collection. The
   ;; controlled vocabulary for spatial keywords is maintained in the Keyword Management System
   ;; (KMS). The Spatial Keyword hierarchy includes one or more of the following layers: Category
   ;; (e.g., Continent), Type (e.g. Africa), Subregion1 (e.g., Central Africa), Subregion2 (e.g.,
   ;; Cameroon), and Subregion3. DetailedLocation exists outside the hierarchy.
   LocationKeywords

   ;; Dates related to activities involving the metadata record itself. For example, Future Review
   ;; date is the date that the metadata record is scheduled to be reviewed.
   MetadataDates

   ;; This is deprecated and will be removed. Use LocationKeywords instead. Controlled hierarchical
   ;; keywords used to specify the spatial location of the collection. The controlled vocabulary for
   ;; spatial keywords is maintained in the Keyword Management System (KMS). The Spatial Keyword
   ;; hierarchy includes one or more of the following layers: Location_Category (e.g., Continent),
   ;; Location_Type (e.g. Africa), Location_Subregion1 (e.g., Central Africa), Location_Subregion2
   ;; (e.g., Cameroon), and Location_Subregion3.
   SpatialKeywords

   ;; Identifies the topic categories from the EN ISO 19115-1:2014 Geographic Information – Metadata
   ;; – Part 1: Fundamentals (http://www.isotc211.org/) Topic Category Code List that pertain to
   ;; this collection, based on the Science Keywords associated with the collection. An ISO Topic
   ;; Category is a high-level thematic classification to assist in the grouping of and search for
   ;; available collections.
   ISOTopicCategories

   ;; The short name associated with the collection.
   ShortName

   ;; A brief description of the collection or service the metadata represents.
   Abstract

   ;; The language used in the metadata record.
   MetadataLanguage

   ;; Formerly called Internal Directory Name (IDN) Node (IDN_Node). This element has been used
   ;; historically by the GCMD internally to identify association, responsibility and/or ownership
   ;; of the dataset, service or supplemental information. Note: This field only occurs in the DIF.
   ;; When a DIF record is retrieved in the ECHO10 or ISO 19115 formats, this element will not be
   ;; translated.
   DirectoryNames

   ;; Suggested usage or purpose for the collection data or service.
   Purpose

   ;; Name of the two-dimensional tiling system for the collection. Previously called
   ;; TwoDCoordinateSystem.
   TilingIdentificationSystems

   ;; Describes key bibliographic citations pertaining to the collection.
   PublicationReferences

   ;; This element describes any data/service related URLs that include project home pages,
   ;; services, related data archives/servers, metadata extensions, direct links to online software
   ;; packages, web mapping services, links to images, or other data.
   RelatedUrls

   ;; Dates related to activities involving the collection data. For example, Creation date is the
   ;; date that the collection data first entered the data archive system.
   DataDates

   ;; Information about the personnel responsible for this collection and its metadata.
   ContactPersons

   ;; Allows the author to constrain access to the collection. This includes any special
   ;; restrictions, legal prerequisites, limitations and/or warnings on obtaining collection data.
   ;; Some words that may be used in this element's value include: Public, In-house, Limited, None.
   ;; The value field is used for special ACL rules (Access Control Lists
   ;; (http://en.wikipedia.org/wiki/Access_control_list)). For example it can be used to hide
   ;; metadata when it isn't ready for public consumption.
   AccessConstraints

   SpatialExtent

   ;; Information about the personnel groups responsible for this collection and its metadata.
   ContactGroups

   ;; The data’s distinctive attributes of the collection (i.e. attributes used to describe the
   ;; unique characteristics of the collection which extend beyond those defined).
   AdditionalAttributes

   ;; Controlled Science Keywords describing the collection. The controlled vocabulary for Science
   ;; Keywords is maintained in the Keyword Management System (KMS).
   ScienceKeywords

   ;; Free text description of the quality of the collection data. Description may include: 1)
   ;; succinct description of the quality of data in the collection; 2) Any quality assurance
   ;; procedures followed in producing the data in the collection; 3) indicators of collection
   ;; quality or quality flags - both validated or invalidated; 4) recognized or potential problems
   ;; with quality; 5) established quality control mechanisms; and 6) established quantitative
   ;; quality measurements.
   Quality

   ;; The title of the collection or service described by the metadata.
   EntryTitle

   ;; Describes media options, size, data format, and fees involved in distributing the data from
   ;; this collection.
   Distributions

   ;; Describes the production status of the data set. There are three choices: PLANNED refers to
   ;; data sets to be collected in the future and are thus unavailable at the present time. For
   ;; Example: The Hydro spacecraft has not been launched, but information on planned data sets may
   ;; be available. IN WORK refers to data sets currently in production or data that is continuously
   ;; being collected or updated. For Example: data from the AIRS instrument on Aqua is being
   ;; collected continuously. COMPLETE refers to data sets in which no updates or further data
   ;; collection will be made. For Example: Nimbus-7 SMMR data collection has been completed.
   CollectionProgress

   ;; For paleoclimate or geologic data, PaleoTemporalCoverage is the length of time represented by
   ;; the data collected. PaleoTemporalCoverage should be used when the data spans time frames
   ;; earlier than yyyy-mm-dd = 0001-01-01.
   PaleoTemporalCoverages

   ;; The reference frame or system in which altitudes (elevations) are given. The information
   ;; contains the datum name, distance units and encoding method, which provide the definition for
   ;; the system. This field also stores the characteristics of the reference frame or system from
   ;; which depths are measured. The additional information in the field is geometry reference data
   ;; etc.
   SpatialInformation

   ;; Identifies the collection as a Science Quality collection or a non-science-quality collection
   ;; such as a Near-Real-Time collection.
   CollectionDataType

   ;; Designed to protect privacy and/or intellectual property by allowing the author to specify how
   ;; the collection may or may not be used after access is granted. This includes any special
   ;; restrictions, legal prerequisites, terms and conditions, and/or limitations on using the item.
   ;; Providers may request acknowledgement of the item from users and claim no responsibility for
   ;; quality and completeness. Note: Use Constraints describe how the item may be used once access
   ;; has been granted; and is distinct from Access Constraints, which refers to any constraints in
   ;; accessing the item.
   UseConstraints

   ;; One or more words or phrases that describe the temporal resolution of the dataset.
   TemporalKeywords

   ;; Allows authors to provide words or phrases outside of the controlled Science Keyword
   ;; vocabulary, to further describe the collection.
   AncillaryKeywords

   ;; The identifier for the processing level of the collection (e.g., Level0, Level1A).
   ProcessingLevel

   ;; Information about the relevant platform(s) used to acquire the data in the collection.
   ;; Platform types are controlled in the Keyword Management System (KMS), and include Spacecraft,
   ;; Aircraft, Vessel, Buoy, Platform, Station, Network, Human, etc.
   Platforms

   ;; The name of the scientific program, field campaign, or project from which the data were
   ;; collected. This element is intended for the non-space assets such as aircraft, ground systems,
   ;; balloons, sondes, ships, etc. associated with campaigns. This element may also cover a long
   ;; term project that continuously creates new data sets — like MEaSUREs from ISCCP and NVAP or
   ;; CMARES from MISR. Project also includes the Campaign sub-element to support multiple campaigns
   ;; under the same project.
   Projects

   ;; The Version of the collection.
   Version

   ;; This class contains attributes which describe the temporal range of a specific collection.
   ;; Temporal Extent includes a specification of the Temporal Range Type of the collection, which
   ;; is one of Range Date Time, Single Date Time, or Periodic Date Time
   TemporalExtents

   ;; Information about the data centers responsible for this collection and its metadata.
   DataCenters

   ;; This element is used to identify other services, collections, visualizations, granules, and
   ;; other metadata types and resources that are associated with or dependent on the data described
   ;; by the metadata. This element is also used to identify a parent metadata record if it exists.
   ;; This usage should be reserved for instances where a group of metadata records are subsets that
   ;; can be better represented by one parent metadata record, which describes the entire set. In
   ;; some instances, a child may point to more than one parent. The EntryId is the same as the
   ;; element described elsewhere in this document where it contains and ID, and Version.
   MetadataAssociations

   ;; Describes the language used in the preparation, storage, and description of the collection. It
   ;; is the language of the collection data themselves. It does not refer to the language used in
   ;; the metadata record (although this may be the same language).
   DataLanguage
  ])
(record-pretty-printer/enable-record-pretty-printing UMM-C)

;; For paleoclimate or geologic data, PaleoTemporalCoverage is the length of time represented by the
;; data collected. PaleoTemporalCoverage should be used when the data spans time frames earlier than
;; yyyy-mm-dd = 0001-01-01.
(defrecord PaleoTemporalCoverageType
  [
   ;; Hierarchy of terms indicating units of geologic time, i.e., eon (e.g, Phanerozoic), era (e.g.,
   ;; Cenozoic), period (e.g., Paleogene), epoch (e.g., Oligocene), and stage or age (e.g,
   ;; Chattian).
   ChronostratigraphicUnits

   ;; A string indicating the number of years furthest back in time, including units, e.g., 100 Ga.
   ;; Units may be Ga (billions of years before present), Ma (millions of years before present), ka
   ;; (thousands of years before present) or ybp (years before present).
   StartDate

   ;; A string indicating the number of years closest to the present time, including units, e.g., 10
   ;; ka. Units may be Ga (billions of years before present), Ma (millions of years before present),
   ;; ka (thousands of years before present) or ybp (years before present).
   EndDate
  ])
(record-pretty-printer/enable-record-pretty-printing PaleoTemporalCoverageType)

;; This element defines a mapping to the GCMD KMS hierarchical location list. It replaces
;; SpatialKeywords. Each tier must have data in the tier above it.
(defrecord LocationKeywordType
  [
   ;; Top-level controlled keyword hierarchical level that contains the largest general location
   ;; where the collection data was taken from.
   Category

   ;; Second-tier controlled keyword hierarchical level that contains the regional location where
   ;; the collection data was taken from
   Type

   ;; Third-tier controlled keyword heirarchical level that contains the regional sub-location where
   ;; the collection data was taken from
   Subregion1

   ;; Fourth-tier controlled keyword heirarchical level that contains the regional sub-location
   ;; where the collection data was taken from
   Subregion2

   ;; Fifth-tier controlled keyword heirarchical level that contains the regional sub-location where
   ;; the collection data was taken from
   Subregion3

   ;; Uncontrolled keyword heirarchical level that contains the specific location where the
   ;; collection data was taken from. Exists outside the heirarchy.
   DetailedLocation
  ])
(record-pretty-printer/enable-record-pretty-printing LocationKeywordType)

(defrecord LocalCoordinateSystemType
  [
   ;; The information provided to register the local system to the Earth (e.g. control points,
   ;; satellite ephemeral data, and inertial navigation data).
   GeoReferenceInformation

   ;; A description of the Local Coordinate System and geo-reference information.
   Description
  ])
(record-pretty-printer/enable-record-pretty-printing LocalCoordinateSystemType)

(defrecord ChronostratigraphicUnitType
  [
   Eon

   Era

   Epoch

   Stage

   DetailedClassification

   Period
  ])
(record-pretty-printer/enable-record-pretty-printing ChronostratigraphicUnitType)

;; This element contains the Processing Level Id and the Processing Level Description
(defrecord ProcessingLevelType
  [
   ;; Description of the meaning of the Processing Level Id, e.g., the Description for the Level4
   ;; Processing Level Id might be 'Model output or results from analyses of lower level data'
   ProcessingLevelDescription

   ;; An identifier indicating the level at which the data in the collection are processed, ranging
   ;; from Level0 (raw instrument data at full resolution) to Level4 (model output or analysis
   ;; results). The value of Processing Level Id is chosen from a controlled vocabulary.
   Id
  ])
(record-pretty-printer/enable-record-pretty-printing ProcessingLevelType)

(defrecord GeographicCoordinateSystemType
  [
   ;; Units of measure used for the geodetic latitude and longitude resolution values (e.g., decimal
   ;; degrees).
   GeographicCoordinateUnits

   ;; The minimum difference between two adjacent latitude values in the Geographic Coordinate
   ;; System, expressed in Geographic Coordinate Units of measure, expressed as a two-digit decimal
   ;; number, e.g., 0.01
   LatitudeResolution

   ;; The minimum difference between two adjacent longitude values in the Geographic Coordinate
   ;; System, expressed in Geographic Coordinate Units of measure, expressed as a two-digit decimal
   ;; number, e.g., 0.01
   LongitudeResolution
  ])
(record-pretty-printer/enable-record-pretty-printing GeographicCoordinateSystemType)

;; The reference frame or system from which altitude or depths are measured. The term 'altitude' is
;; used instead of the common term 'elevation' to conform to the terminology in Federal Information
;; Processing Standards 70-1 and 173. The information contains the datum name, distance units and
;; encoding method, which provide the definition for the system.
(defrecord VerticalSystemDefinitionType
  [
   ;; The identification given to the level surface taken as the surface of reference from which
   ;; measurements are compared.
   DatumName

   ;; The units in which measurements are recorded.
   DistanceUnits

   ;; The means used to encode measurements.
   EncodingMethod

   ;; The minimum distance possible between two adjacent values, expressed in distance units of
   ;; measure for the collection.
   Resolutions
  ])
(record-pretty-printer/enable-record-pretty-printing VerticalSystemDefinitionType)

(defrecord GeodeticModelType
  [
   ;; The identification given to the reference system used for defining the coordinates of points.
   HorizontalDatumName

   ;; Identification given to established representation of the Earth's shape.
   EllipsoidName

   ;; Radius of the equatorial axis of the ellipsoid.
   SemiMajorAxis

   ;; The ratio of the Earth's major axis to the difference between the major and the minor.
   DenominatorOfFlatteningRatio
  ])
(record-pretty-printer/enable-record-pretty-printing GeodeticModelType)

;; This entity stores the reference frame or system from which altitudes (elevations) are measured.
;; The information contains the datum name, distance units and encoding method, which provide the
;; definition for the system. This table also stores the characteristics of the reference frame or
;; system from which depths are measured. The additional information in the table are geometry
;; reference data etc.
(defrecord SpatialInformationType
  [
   VerticalCoordinateSystem

   HorizontalCoordinateSystem

   ;; Denotes whether the spatial coverage of the collection is horizontal, vertical, horizontal and
   ;; vertical, orbit, or vertical and orbit.
   SpatialCoverageType
  ])
(record-pretty-printer/enable-record-pretty-printing SpatialInformationType)

(defrecord HorizontalCoordinateSystemType
  [
   GeodeticModel

   GeographicCoordinateSystem

   LocalCoordinateSystem
  ])
(record-pretty-printer/enable-record-pretty-printing HorizontalCoordinateSystemType)

;; Formerly called Internal Directory Name (IDN) Node (IDN_Node). This element has been used
;; historically by the GCMD internally to identify association, responsibility and/or ownership of
;; the dataset, service or supplemental information. Note: This field only occurs in the DIF. When a
;; DIF record is retrieved in the ECHO10 or ISO 19115 formats, this element will not be translated.
(defrecord DirectoryNameType
  [
   ShortName

   LongName
  ])
(record-pretty-printer/enable-record-pretty-printing DirectoryNameType)

(defrecord VerticalCoordinateSystemType
  [
   AltitudeSystemDefinition

   DepthSystemDefinition
  ])
(record-pretty-printer/enable-record-pretty-printing VerticalCoordinateSystemType)