;; WARNING: This file was generated from umm-cmn-json-schema.json. Do not manually modify.
(ns cmr.umm-spec.models.umm-common-models
   "Defines UMM Common clojure records."
 (:require [cmr.common.dev.record-pretty-printer :as record-pretty-printer]))

;; Describes the relevant platforms used to acquire the data in the collection. Platform type
;; vocabulary is controlled and includes Spacecraft, Aircraft, Vessel, Buoy, Platform, Station,
;; Network, Human, etc.
(defrecord PlatformType
  [
   ;; The most relevant platform type.
   Type

   ShortName

   LongName

   ;; Platform-specific characteristics, e.g., Equator Crossing Time, Inclination Angle, Orbital
   ;; Period. The characteristic names must be unique on this platform; however the names do not
   ;; have to be unique across platforms.
   Characteristics

   Instruments
  ])
(record-pretty-printer/enable-record-pretty-printing PlatformType)

;; Information describing the scientific endeavor(s) with which the collection is associated.
;; Scientific endeavors include campaigns, projects, interdisciplinary science investigations,
;; missions, field experiments, etc.
(defrecord ProjectType
  [
   ;; The unique identifier by which a project or campaign/experiment is known. The campain/project
   ;; is the scientific endeavor associated with the acquisition of the collection. Collections may
   ;; be associated with multiple campaigns.
   ShortName

   ;; The expanded name of the campaign/experiment (e.g. Global climate observing system).
   LongName

   ;; The name of the campaign/experiment (e.g. Global climate observing system).
   Campaigns

   ;; The starting date of the campaign.
   StartDate

   ;; The ending data of the campaign.
   EndDate
  ])
(record-pretty-printer/enable-record-pretty-printing ProjectType)

;; Information about the device used to measure or record data in this collection, including direct
;; human observation. In cases where instruments have a single sensor or the instrument and sensor
;; are used synonymously (e.g. AVHRR), both Instrument and Sensor should be recorded. The Sensor
;; information is represented in a separate section.
(defrecord InstrumentType
  [
   ShortName

   LongName

   ;; Instrument-specific characteristics, e.g., Wavelength, SwathWidth, Field of View. The
   ;; characteristic names must be unique on this instrument; however the names do not have to be
   ;; unique across instruments.
   Characteristics

   ;; The expanded name of the primary sensory instrument. (e.g. Advanced Spaceborne Thermal
   ;; Emission and Reflective Radiometer, Clouds and the Earth's Radiant Energy System, Human
   ;; Observation).
   Technique

   ;; Number of instruments used on the instrument when acquiring the granule data.
   NumberOfInstruments

   ComposedOf

   ;; The operation mode applied on the instrument when acquiring the granule data.
   OperationalModes
  ])
(record-pretty-printer/enable-record-pretty-printing InstrumentType)

;; Information about a collection with horizontal spatial coverage.
(defrecord HorizontalSpatialDomainType
  [
   ;; The appropriate numeric or alpha code used to identify the various zones in the collection's
   ;; grid coordinate system.
   ZoneIdentifier

   Geometry
  ])
(record-pretty-printer/enable-record-pretty-printing HorizontalSpatialDomainType)

;; Contains the excluded boundaries from the GPolygon.
(defrecord ExclusiveZoneType
  [
   Boundaries
  ])
(record-pretty-printer/enable-record-pretty-printing ExclusiveZoneType)

;; Information about a two-dimensional tiling system related to this collection.
(defrecord TilingIdentificationSystemType
  [
   TilingIdentificationSystemName

   Coordinate1

   Coordinate2
  ])
(record-pretty-printer/enable-record-pretty-printing TilingIdentificationSystemType)

;; Specifies the geographic and vertical (altitude, depth) coverage of the data.
(defrecord SpatialExtentType
  [
   ;; Denotes whether the collection's spatial coverage requires horizontal, vertical, horizontal
   ;; and vertical, orbit, or vertical and orbit in the spatial domain and coordinate system
   ;; definitions.
   SpatialCoverageType

   HorizontalSpatialDomain

   VerticalSpatialDomains

   OrbitParameters

   GranuleSpatialRepresentation
  ])
(record-pretty-printer/enable-record-pretty-printing SpatialExtentType)

;; Defines the contact information of a data center or data contact.
(defrecord ContactInformationType
  [
   ;; A URL associated with the contact, e.g., the home page for the DAAC which is responsible for
   ;; the collection.
   RelatedUrls

   ;; Time period when the contact answers questions or provides services.
   ServiceHours

   ;; Supplemental instructions on how or when to contact the responsible party.
   ContactInstruction

   ;; Mechanisms of contacting.
   ContactMechanisms

   ;; Contact addresses.
   Addresses
  ])
(record-pretty-printer/enable-record-pretty-printing ContactInformationType)

(defrecord ContactGroupType
  [
   ;; This is the roles of the data contact.
   Roles

   ;; Uuid of the data contact.
   Uuid

   ;; This is the contact person or group that is not affiliated with the data centers.
   NonDataCenterAffiliation

   ;; This is the contact information of the data contact.
   ContactInformation

   ;; This is the contact group name.
   GroupName
  ])
(record-pretty-printer/enable-record-pretty-printing ContactGroupType)

;; Defines a data center which is either an organization or institution responsible for
;; distributing, archiving, or processing the data, etc.
(defrecord DataCenterType
  [
   ;; This is the roles of the data center.
   Roles

   ;; This is the short name of the data center.
   ShortName

   ;; This is the long name of the data center.
   LongName

   ;; Uuid of the data center.
   Uuid

   ;; This is the contact groups of the data center.
   ContactGroups

   ;; This is the contact persons of the data center.
   ContactPersons

   ;; This is the contact information of the data center.
   ContactInformation
  ])
(record-pretty-printer/enable-record-pretty-printing DataCenterType)

;; Describes media options, size, data format, and fees involved in distributing or accessing the
;; data.
(defrecord DistributionType
  [
   ;; The distribution media for the collection data.
   DistributionMedia

   ;; A list of file sizes indicating a single exact or approximate, or range of distribution sizes.
   Sizes

   ;; The distribution format of the collection data (e.g., HDF, netCDF).
   DistributionFormat

   ;; The fee for ordering the collection data. The fee is entered as a number, in US Dollars.
   Fees
  ])
(record-pretty-printer/enable-record-pretty-printing DistributionType)

;; Building block text fields used to construct the recommended language for citing the collection
;; in professional scientific literature. The citation language constructed from these fields
;; references the collection itself, and is not designed for listing bibliographic references of
;; scientific research articles arising from search results. A list of references related to the
;; research results should be in the Publication Reference element.
(defrecord ResourceCitationType
  [
   ;; The name of the data series, or aggregate data of which the data is a part.
   SeriesName

   ;; The name of the organization(s) or individual(s) with primary intellectual responsibility for
   ;; the collection's development.
   Creator

   ;; The name of the city (and state or province and country if needed) where the collection was
   ;; made available for release.
   ReleasePlace

   ;; The title of the collection; this is the same as the collection Entry Title.
   Title

   ;; The name of the individual or organization that made the collection available for release.
   Publisher

   ;; The date when the collection was made available for release.
   ReleaseDate

   ;; The online resource related to the landing page of the collection.
   OnlineResource

   ;; The volume or issue number of the publication (if applicable).
   IssueIdentification

   ;; The individual(s) responsible for changing the data in the collection.
   Editor

   ;; The mode in which the data are represented, e.g. atlas, image, profile, text, etc.
   DataPresentationForm

   ;; The version of the collection.
   Version

   ;; Additional free-text citation information.
   OtherCitationDetails
  ])
(record-pretty-printer/enable-record-pretty-printing ResourceCitationType)

;; This element describes the digital object identifier.
(defrecord DoiType
  [
   ;; The authority who created the DOI.
   Authority

   ;; The Digitial Object Identifier.
   DOI
  ])
(record-pretty-printer/enable-record-pretty-printing DoiType)

;; Stores the start and end date/time of a collection.
(defrecord RangeDateTimeType
  [
   ;; The time when the temporal coverage period being described began.
   BeginningDateTime

   ;; The time when the temporal coverage period being described ended.
   EndingDateTime
  ])
(record-pretty-printer/enable-record-pretty-printing RangeDateTimeType)

(defrecord BoundingRectangleType
  [
   WestBoundingCoordinate

   NorthBoundingCoordinate

   EastBoundingCoordinate

   SouthBoundingCoordinate
  ])
(record-pretty-printer/enable-record-pretty-printing BoundingRectangleType)

(defrecord LineType
  [
   Points
  ])
(record-pretty-printer/enable-record-pretty-printing LineType)

;; Method for contacting the data contact. A contact can be available via phone, email, Facebook, or
;; Twitter.
(defrecord ContactMechanismType
  [
   ;; This is the method type for contacting the responsible party - phone, email, Facebook, or
   ;; Twitter.
   Type

   ;; This is the contact phone number, email address, Facebook address, or Twitter handle
   ;; associated with the contact method.
   Value
  ])
(record-pretty-printer/enable-record-pretty-printing ContactMechanismType)

;; Enables specification of Earth science keywords related to the collection. The Earth Science
;; keywords are chosen from a controlled keyword hierarchy maintained in the Keyword Management
;; System (KMS).
(defrecord ScienceKeywordType
  [
   Category

   Topic

   Term

   VariableLevel1

   VariableLevel2

   VariableLevel3

   DetailedVariable
  ])
(record-pretty-printer/enable-record-pretty-printing ScienceKeywordType)

;; Additional unique attributes of the collection, beyond those defined in the UMM model, which the
;; data provider deems useful for end-user understanding of the data in the collection. Additional
;; attributes are also called Product Specific Attributes (PSAs) or non-core attributes. Examples
;; are HORIZONTALTILENUMBER, VERTICALTILENUMBER.
(defrecord AdditionalAttributeType
  [
   ;; Identifies a namespace for the additional attribute name.
   Group

   ;; The standard unit of measurement for the additional attribute. For example, meters, hertz.
   ParameterUnitsOfMeasure

   ;; An estimate of the accuracy of the values of the additional attribute. For example, for AVHRR:
   ;; Measurement error or precision-measurement error or precision of a data product parameter.
   ;; This can be specified in percent or the unit with which the parameter is measured.
   ParameterValueAccuracy

   ;; The smallest unit increment to which the additional attribute value is measured.
   MeasurementResolution

   ;; The minimum value of the additional attribute over the whole collection.
   ParameterRangeBegin

   ;; Describes the method used for determining the parameter value accuracy that is given for this
   ;; additional attribute.
   ValueAccuracyExplanation

   ;; Value of the additional attribute if it is the same for all granules across the collection. If
   ;; the value of the additional attribute may differ by granule, leave this collection-level value
   ;; blank.
   Value

   Name

   ;; Free-text description of the additional attribute.
   Description

   ;; The date this additional attribute information was updated.
   UpdateDate

   ;; The maximum value of the additional attribute over the whole collection.
   ParameterRangeEnd

   ;; Data type of the values of the additional attribute.
   DataType
  ])
(record-pretty-printer/enable-record-pretty-printing AdditionalAttributeType)

;; Information about any constraints for accessing the data set. This includes any special
;; restrictions, legal prerequisites, limitations and/or warnings on obtaining the data set.
(defrecord AccessConstraintsType
  [
   ;; Free-text description of the constraint. In DIF, this field is called Access_Constraint. In
   ;; ECHO, this field is called RestrictionComment. Examples of text in this field are Public,
   ;; In-house, Limited. Additional detailed instructions on how to access the collection data may
   ;; be entered in this field.
   Description

   ;; Numeric value that is used with Access Control Language (ACLs) to restrict access to this
   ;; collection. For example, a provider might specify a collection level ACL that hides all
   ;; collections with a value element set to 15. In ECHO, this field is called RestrictionFlag.
   ;; This field does not exist in DIF.
   Value
  ])
(record-pretty-printer/enable-record-pretty-printing AccessConstraintsType)

(defrecord VerticalSpatialDomainType
  [
   ;; Describes the type of the area of vertical space covered by the collection locality.
   Type

   ;; Describes the extent of the area of vertical space covered by the collection. Must be
   ;; accompanied by an Altitude Encoding Method description. The datatype for this attribute is the
   ;; value of the attribute VerticalSpatialDomainType. The unit for this attribute is the value of
   ;; either DepthDistanceUnits or AltitudeDistanceUnits.
   Value
  ])
(record-pretty-printer/enable-record-pretty-printing VerticalSpatialDomainType)

(defrecord GeometryType
  [
   CoordinateSystem

   Points

   BoundingRectangles

   GPolygons

   Lines
  ])
(record-pretty-printer/enable-record-pretty-printing GeometryType)

;; Information about the instrument excluding fields used in the top level instrument element
(defrecord InstrumentChildType
  [
   ShortName

   LongName

   ;; Instrument-specific characteristics, e.g., Wavelength, SwathWidth, Field of View. The
   ;; characteristic names must be unique on this instrument; however the names do not have to be
   ;; unique across instruments.
   Characteristics

   ;; The expanded name of the primary sensory instrument. (e.g. Advanced Spaceborne Thermal
   ;; Emission and Reflective Radiometer, Clouds and the Earth's Radiant Energy System, Human
   ;; Observation).
   Technique
  ])
(record-pretty-printer/enable-record-pretty-printing InstrumentChildType)

;; The longitude and latitude values of a spatially referenced point in degrees.
(defrecord PointType
  [
   Longitude

   Latitude
  ])
(record-pretty-printer/enable-record-pretty-printing PointType)

;; Describes key bibliographic citations pertaining to the data.
(defrecord PublicationReferenceType
  [
   ;; The date of the publication.
   PublicationDate

   ;; Additional free-text reference information about the publication.
   OtherReferenceDetails

   ;; The name of the series of the publication.
   Series

   ;; The title of the publication in the bibliographic citation.
   Title

   ;; The Digital Object Identifier (DOI) of the publication.
   DOI

   ;; The publication pages that are relevant.
   Pages

   ;; The edition of the publication.
   Edition

   ;; The report number of the publication.
   ReportNumber

   ;; The publication volume number.
   Volume

   ;; The publisher of the publication.
   Publisher

   ;; The online resource related to the bibliographic citation.
   OnlineResource

   ;; The ISBN of the publication.
   ISBN

   ;; The author of the publication.
   Author

   ;; The issue of the publication.
   Issue

   ;; The pubication place of the publication.
   PublicationPlace
  ])
(record-pretty-printer/enable-record-pretty-printing PublicationReferenceType)

;; Used to identify other services, collections, visualizations, granules, and other metadata types
;; and resources that are associated with or dependent on the this collection, including
;; parent-child relationships.
(defrecord MetadataAssociationType
  [
   ;; The type of association between this collection metadata record and the target metadata
   ;; record. Choose type from the drop-down list.
   Type

   ;; Free-text description of the association between this collection record and the target
   ;; metadata record.
   Description

   ;; Shortname of the target metadata record that is associated with this collection record.
   EntryId

   ;; The version of the target metadata record that is associated with this collection record.
   Version
  ])
(record-pretty-printer/enable-record-pretty-printing MetadataAssociationType)

;; Represents a data file size.
(defrecord FileSizeType
  [
   ;; The size of the data.
   Size

   ;; Unit of information, together with Size determines total size in bytes of the data.
   Unit
  ])
(record-pretty-printer/enable-record-pretty-printing FileSizeType)

;; Defines the minimum and maximum value for one dimension of a two dimensional coordinate system.
(defrecord TilingCoordinateType
  [
   MinimumValue

   MaximumValue
  ])
(record-pretty-printer/enable-record-pretty-printing TilingCoordinateType)

(defrecord GPolygonType
  [
   Boundary

   ExclusiveZone
  ])
(record-pretty-printer/enable-record-pretty-printing GPolygonType)

;; A boundary is set of points connected by straight lines representing a polygon on the earth. It
;; takes a minimum of three points to make a boundary. Points must be specified in counter-clockwise
;; order and closed (the first and last vertices are the same).
(defrecord BoundaryType
  [
   Points
  ])
(record-pretty-printer/enable-record-pretty-printing BoundaryType)

(defrecord ContactPersonType
  [
   ;; This is the roles of the data contact.
   Roles

   ;; Uuid of the data contact.
   Uuid

   ;; This is the contact person or group that is not affiliated with the data centers.
   NonDataCenterAffiliation

   ;; This is the contact information of the data contact.
   ContactInformation

   ;; First name of the individual.
   FirstName

   ;; Middle name of the individual.
   MiddleName

   ;; Last name of the individual.
   LastName
  ])
(record-pretty-printer/enable-record-pretty-printing ContactPersonType)

;; Specifies the date and its type.
(defrecord DateType
  [
   ;; This is the date that an event associated with the collection or its metadata occurred.
   Date

   ;; This is the type of event associated with the date. For example, Creation, Last Revision. Type
   ;; is chosen from a picklist.
   Type
  ])
(record-pretty-printer/enable-record-pretty-printing DateType)

;; This entity is used to define characteristics.
(defrecord CharacteristicType
  [
   ;; The name of the characteristic attribute.
   Name

   ;; Description of the Characteristic attribute.
   Description

   ;; The value of the Characteristic attribute.
   Value

   ;; Units associated with the Characteristic attribute value.
   Unit

   ;; The datatype of the Characteristic/attribute.
   DataType
  ])
(record-pretty-printer/enable-record-pretty-printing CharacteristicType)

;; Information which describes the temporal range or extent of a specific collection.
(defrecord TemporalExtentType
  [
   ;; This attribute tells the system and ultimately the end user how temporal coverage is specified
   ;; for the collection. Choices are Single Date Time, Range Date Time, and Periodic Date Time.
   TemporalRangeType

   ;; The precision (position in number of places to right of decimal point) of seconds used in
   ;; measurement.
   PrecisionOfSeconds

   ;; Setting the Ends At Present Flag to 'True' indicates that a data collection which covers,
   ;; temporally, a discontinuous range, currently ends at the present date. Setting the Ends at
   ;; Present flag to 'True' eliminates the need to continuously update the Range Ending Time for
   ;; collections where granules are continuously being added to the collection inventory.
   EndsAtPresentFlag

   ;; Stores the start and end date/time of a collection.
   RangeDateTimes

   SingleDateTimes

   ;; Temporal information about a collection having granules collected at a regularly occurring
   ;; period. Information includes the start and end dates of the period, duration unit and value,
   ;; and cycle duration unit and value.
   PeriodicDateTimes
  ])
(record-pretty-printer/enable-record-pretty-printing TemporalExtentType)

;; Describes the online resource pertaining to the data.
(defrecord OnlineResourceType
  [
    ;; The URL of the website related to the online resource.
    Linkage

    ;; The protocol of the linkage for the online resource.
    Protocol

    ;; The application protocol of the online resource.
    ApplicationProtocol

    ;; The name of the online resource.
    Name

    ;; The description of the online resource.
    Description

    ;; The function of the online resource.
    Function
   ])
(record-pretty-printer/enable-record-pretty-printing OnlineResourceType)

;; Represents Internet sites that contain information related to the data, as well as related
;; Internet sites such as project home pages, related data archives/servers, metadata extensions,
;; online software packages, web mapping services, and calibration/validation data.
(defrecord RelatedUrlType
  [
   ;; Description of the web page at this URL.
   Description

   ;; An array of keywords describing the relation of the online resource to this resource.
   Relation

   ;; The URL for the relevant web page (e.g., the URL of the responsible organization's home page,
   ;; the URL of the collection landing page, the URL of the download site for the collection).
   URLs

   ;; The mime type of files downloaded from this site (e.g., pdf, doc, zip, tiff, jpg, readme).
   MimeType

   ;; The estimated or average size of a file downloaded from this site.
   FileSize
  ])
(record-pretty-printer/enable-record-pretty-printing RelatedUrlType)

;; Orbit parameters for the collection used by the Orbital Backtrack Algorithm.
(defrecord OrbitParametersType
  [
   ;; Width of the swath at the equator in Kilometers.
   SwathWidth

   ;; Orbital period in decimal minutes.
   Period

   ;; Inclination of the orbit. This is the same as (180-declination) and also the same as the
   ;; highest latitude achieved by the satellite. Data Unit: Degree.
   InclinationAngle

   ;; Indicates the number of orbits.
   NumberOfOrbits

   ;; The latitude start of the orbit relative to the equator. This is used by the backtrack search
   ;; algorithm to treat the orbit as if it starts from the specified latitude. This is optional and
   ;; will default to 0 if not specified.
   StartCircularLatitude
  ])
(record-pretty-printer/enable-record-pretty-printing OrbitParametersType)

;; Information about Periodic Date Time collections, including the name of the temporal period in
;; addition to the start and end dates, duration unit and value, and cycle duration unit and value.
(defrecord PeriodicDateTimeType
  [
   ;; The name given to the recurring time period. e.g. 'spring - north hemi.'
   Name

   ;; The date (day and time) of the first occurrence of this regularly occurring period which is
   ;; relevant to the collection coverage.
   StartDate

   ;; The date (day and time) of the end occurrence of this regularly occurring period which is
   ;; relevant to the collection coverage.
   EndDate

   ;; The unit specification for the period duration.
   DurationUnit

   ;; The number of PeriodDurationUnits in the RegularPeriodic period. e.g. the RegularPeriodic
   ;; event 'Spring-North Hemi' might have a PeriodDurationUnit='MONTH' PeriodDurationValue='3'
   ;; PeriodCycleDurationUnit='YEAR' PeriodCycleDurationValue='1' indicating that Spring-North Hemi
   ;; lasts for 3 months and has a cycle duration of 1 year. The unit for the attribute is the value
   ;; of the attribute PeriodDurationValue.
   DurationValue

   ;; The unit specification of the period cycle duration.
   PeriodCycleDurationUnit

   PeriodCycleDurationValue
  ])
(record-pretty-printer/enable-record-pretty-printing PeriodicDateTimeType)

;; This entity contains the physical address details for the contact.
(defrecord AddressType
  [
   ;; An address line for the street address, used for mailing or physical addresses of
   ;; organizations or individuals who serve as contacts for the collection.
   StreetAddresses

   ;; The city portion of the physical address.
   City

   ;; The state or province portion of the physical address.
   StateProvince

   ;; The country of the physical address.
   Country

   ;; The zip or other postal code portion of the physical address.
   PostalCode
  ])
(record-pretty-printer/enable-record-pretty-printing AddressType)
