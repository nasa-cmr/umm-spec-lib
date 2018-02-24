;; WARNING: This file was generated from umm-s-json-schema.json. Do not manually modify.
(ns cmr.umm-spec.models.umm-service-models
   "Defines UMM-S clojure records."
 (:require [cmr.common.dev.record-pretty-printer :as record-pretty-printer]))

(defrecord UMM-S
  [
   ;; This element contains important information about the universal resource locator (URL) for the
   ;; service.
   RelatedURL

   ;; This element contains important information about the coverage for the service.
   Coverage

   ;; A field needed for pattern substitution in OPeNDAP services
   OnlineAccessURLPatternSubstitution

   ;; The service provider, or organization, or institution responsible for developing, archiving,
   ;; and/or distributing the service, software, or tool.
   ServiceOrganizations

   ;; This element contains important information about the Unique Resource Locator for the service.
   ServiceOptions

   ;; This is the contact persons of the service.
   ContactPersons

   ;; Information about any constraints for accessing the service, software, or tool.
   AccessConstraints

   ;; This is the contact groups of the service.
   ContactGroups

   ;; Allows for the specification of Earth Science Service keywords that are representative of the
   ;; service, software, or tool being described. The controlled vocabulary for Service Keywords is
   ;; maintained in the Keyword Management System (KMS).
   ServiceKeywords

   ;; A field needed for pattern matching in OPeNDAP services
   OnlineAccessURLPatternMatch

   ;; Allows for the specification of Earth Science keywords that are representative of the service,
   ;; software, or tool being described. The controlled vocabulary for Science Keywords is
   ;; maintained in the Keyword Management System (KMS).
   ScienceKeywords

   ;; Information on how the item (service, software, or tool) may or may not be used after access
   ;; is granted. This includes any special restrictions, legal prerequisites, terms and conditions,
   ;; and/or limitations on using the item. Providers may request acknowledgement of the item from
   ;; users and claim no responsibility for quality and completeness.
   UseConstraints

   ;; The name of the service, software, or tool.
   Name

   ;; A brief description of the service.
   Description

   ;; The type of the service, software, or tool.
   Type

   ;; Words or phrases to further describe the service, software, or tool.
   AncillaryKeywords

   ;; Associates the satellite/platform that is supported by the service, software, or tool.
   Platforms

   ;; The edition or version of the service, software, or tool.
   Version

   ;; Information about the quality of the service, software, or tool, or any quality assurance
   ;; procedures followed in development.
   ServiceQuality

   ;; The long name of the service, software, or tool.
   LongName
  ])
(record-pretty-printer/enable-record-pretty-printing UMM-S)

;; This element describes the platform information.
(defrecord PlatformType
  [
   ;; The short name of the platform associated with the service.
   ShortName

   ;; The long name of the platform associated with the service.
   LongName

   ;; Associates the instrument/sensor that is supported by the service, software, or tool.
   Instruments
  ])
(record-pretty-printer/enable-record-pretty-printing PlatformType)

;; This element describes instrument information.
(defrecord InstrumentType
  [
   ;; The short name of the instrument associated with the service.
   ShortName

   ;; The long name of the instrument associated with the service.
   LongName
  ])
(record-pretty-printer/enable-record-pretty-printing InstrumentType)

(defrecord TimePointsType
  [
   ;; Time format representing time point of the temporal extent.
   TimeFormat

   ;; Time value of the time point of temporal extent.
   TimeValue

   ;; Description of the time value of the temporal extent.
   Description
  ])
(record-pretty-printer/enable-record-pretty-printing TimePointsType)

;; Defines the contact information of a service organization or service contact.
(defrecord ContactInformationType
  [
   ;; A URL associated with the contact, e.g., the home page for the service provider which is
   ;; responsible for the service.
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
   ;; This is the roles of the service contact.
   Roles

   ;; Uuid of the service contact.
   Uuid

   ;; This is the contact person or group that is not affiliated with the service organizations.
   NonServiceOrganizationAffiliation

   ;; This is the contact information of the service contact.
   ContactInformation

   ;; This is the contact group name.
   GroupName
  ])
(record-pretty-printer/enable-record-pretty-printing ContactGroupType)

;; Defines a service organization which is either an organization or institution responsible for
;; distributing, archiving, or processing the data via a service, etc.
(defrecord ServiceOrganizationType
  [
   ;; This is the roles of the service organization.
   Roles

   ;; This is the short name of the service organization.
   ShortName

   ;; This is the long name of the service organization.
   LongName

   ;; Uuid of the service organization.
   Uuid

   ;; This is the contact groups of the service organization.
   ContactGroups

   ;; This is the contact persons of the service organization.
   ContactPersons

   ;; This is the contact information of the service organization.
   ContactInformation
  ])
(record-pretty-printer/enable-record-pretty-printing ServiceOrganizationType)

;; Enables specification of Earth science service keywords related to the service. The Earth Science
;; Service keywords are chosen from a controlled keyword hierarchy maintained in the Keyword
;; Management System (KMS).
(defrecord ServiceKeywordType
  [
   ServiceCategory

   ServiceTopic

   ServiceTerm

   ServiceSpecificTerm
  ])
(record-pretty-printer/enable-record-pretty-printing ServiceKeywordType)

;; This element describes coverage information.
(defrecord CoverageType
  [
   ;; The temporal resolution of the coverage available from the service.
   TemporalResolution

   ;; Path relative to the root universal resource locator for the coverage.
   RelativePath

   ;; The unit of the temporal resolution of the coverage available from the service.
   TemporalResolutionUnit

   ;; The temporal extent of the coverage available from the service.
   CoverageTemporalExtent

   ;; The spatial resolution of the coverage available from the service.
   SpatialResolution

   ;; The unit of the spatial resolution of the coverage available from the service.
   SpatialResolutionUnit

   ;; The spatial extent of the coverage available from the service. These are coordinate pairs
   ;; which describe either the point, line string, or polygon representing the spatial extent. The
   ;; bounding box is described by the west, south, east and north ordinates
   CoverageSpatialExtent

   ;; The name of the coverage available from the service.
   Name

   ;; The type of the coverage available from the service.
   Type
  ])
(record-pretty-printer/enable-record-pretty-printing CoverageType)

;; Method for contacting the service contact. A contact can be available via phone, email, Facebook,
;; or Twitter.
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

(defrecord CoverageTemporalExtentType
  [
   ;; Points in time representing the temporal extent of the layer or coverage.
   CoverageTimePoints

   ;; Uuid of the temporal extent.
   Uuid
  ])
(record-pretty-printer/enable-record-pretty-printing CoverageTemporalExtentType)

;; The bounding box consists of min x, min y, max y and max y ordinates.
(defrecord BBoxType
  [
   ;; The minimum x ordinate of the bounding box.
   MinX

   ;; The minimum x ordinate of the bounding box.
   MinY

   ;; The maximum y ordinate of the bounding box.
   MaxX

   ;; The maximum y ordinate of the bounding box.
   MaxY
  ])
(record-pretty-printer/enable-record-pretty-printing BBoxType)

;; This object describes service quality, composed of the quality flag, the quality flagging system,
;; traceability and lineage.
(defrecord ServiceQualityType
  [
   ;; The quality flag for the service.
   QualityFlag

   ;; The quality traceability of the service.
   Traceability

   ;; The quality lineage of the service.
   Lineage
  ])
(record-pretty-printer/enable-record-pretty-printing ServiceQualityType)

;; The line string consists of two points: a start point and an end ppint.
(defrecord LineStringType
  [
   ;; The start point of the line string.
   StartPoint

   ;; The end point of the line string.
   EndPoint
  ])
(record-pretty-printer/enable-record-pretty-printing LineStringType)

(defrecord ContactPersonType
  [
   ;; This is the roles of the service contact.
   Roles

   ;; Uuid of the data contact.
   Uuid

   ;; This is the contact person or group that is not affiliated with the service organization.
   NonServiceOrganizationAffiliation

   ;; This is the contact information of the service contact.
   ContactInformation

   ;; First name of the individual.
   FirstName

   ;; Middle name of the individual.
   MiddleName

   ;; Last name of the individual.
   LastName
  ])
(record-pretty-printer/enable-record-pretty-printing ContactPersonType)

;; This object describes service options, data transformations and output formats.
(defrecord ServiceOptionsType
  [
   ;; This element is used to identify the list of supported subsetting requests.
   SubsetTypes

   ;; This element is used to identify the list of supported projections types.
   SupportedProjections

   ;; This element is used to identify the list of supported interpolation types.
   InterpolationTypes

   ;; This project element describes the list of names of the formats supported by the service.
   SupportedFormats
  ])
(record-pretty-printer/enable-record-pretty-printing ServiceOptionsType)

(defrecord CoverageSpatialExtentType
  [
   ;; Type of the spatial extent.
   Type

   ;; Uuid of the spatial extent.
   Uuid

   ;; The spatial extent of the layer or coverage described by a point.
   SpatialPoints

   ;; The spatial extent of the layer or coverage described by a line string.
   SpatialLineStrings

   ;; The spatial extent of the layer or coverage described by a bounding box.
   SpatialBoundingBox

   ;; The spatial extent of the layer or coverage described by a polygon.
   SpatialPolygons
  ])
(record-pretty-printer/enable-record-pretty-printing CoverageSpatialExtentType)

;; This entity contains the physical address details for the contact.
(defrecord AddressType
  [
   ;; An address line for the street address, used for mailing or physical addresses of
   ;; organizations or individuals who serve as contacts for the service.
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

;; The coordinates consist of a latitude and longitude.
(defrecord CoordinatesType
  [
   ;; The latitude of the point.
   Latitude

   ;; The longitude of the point.
   Longitude
  ])
(record-pretty-printer/enable-record-pretty-printing CoordinatesType)