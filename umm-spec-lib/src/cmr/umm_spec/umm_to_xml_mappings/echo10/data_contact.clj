(ns cmr.umm-spec.umm-to-xml-mappings.echo10.data-contact
  "Functions for generating ECHO10 XML contact elements from UMM data centers and contact persons."
  (:require [cmr.umm-spec.util :as u]
            [clojure.string :as str]))

(def umm-data-center-role->echo10-contact-organization-role
 {"ARCHIVER" "ARCHIVER"
  "DISTRIBUTOR" "DISTRIBUTOR"
  "ORIGINATOR" "Data Originator"
  "PROCESSOR" "PROCESSOR"})

(def umm-contact-person-role->echo10-contact-person-role
 {"Data Center Contact" "DATA CENTER CONTACT"
  "Technical Contact" "TECHNICAL CONTACT"
  "Science Contact" "Technical Contact for Science"
  "Investigator" "INVESTIGATOR"
  "Metadata Author" "METADATA AUTHOR"
  "User Services" "User Services"
  "Science Software Development" "Science Software Development Manager"})

(def default-echo10-contact-role
 "TECHNICAL CONTACT")

(defn country-with-default
 [country]
 (if country
   country
   "Unknown"))

;; ECHO10 has email and phone contact mechanisms. UMM Email goes to ECHO10 email. Facebook and Twitter
;; contact mechanisms are dropped. Everything else is considered phone.
(def echo10-non-phone-contact-mechanisms
 #{"Email" "Twitter" "Facebook"})

(defn join-street-addresses
  "UMM has multiple street addresses and ECHO10 has 1, so join the street addresses in UMM street
  addresses into 1 street address string"
  [street-addresses]
  (if street-addresses
   (str/join " " street-addresses)
   u/not-provided))

(defn- generate-emails
  "Returns ECHO10 organization contact emails from the UMM contact information contact mechanisms"
  [contact-information]
  (let [emails (filter #(= "Email" (:Type %)) (:ContactMechanisms contact-information))]
    (when (seq emails)
      [:OrganizationEmails
       (for [email emails]
         [:Email (:Value email)])])))

(defn- generate-phones
  "Returns ECHO10 organization contact phones from the UMM contact information contact mechanisms"
  [contact-information]
  (let [phones (remove #(contains? echo10-non-phone-contact-mechanisms (:Type %)) (:ContactMechanisms contact-information))]
    (when (seq phones)
      [:OrganizationPhones
       (for [phone phones]
         [:Phone
          [:Number (:Value phone)]
          [:Type (:Type phone)]])])))

(defn- generate-addresses
  "Returns ECHO10 organization contact addresses from the UMM contact information contact mechanisms"
  [contact-information]
  (let [addresses (:Addresses contact-information)]
    (when (seq addresses)
      [:OrganizationAddresses
        (for [address addresses]
          [:Address
            [:StreetAddress (join-street-addresses (:StreetAddresses address))]
            [:City (u/with-default (:City address))]
            [:StateProvince (u/with-default (:StateProvince address))]
            [:PostalCode (u/with-default (:PostalCode address))]
            [:Country (country-with-default (:Country address))]])])))

(defn- generate-contact-persons
  "Returns the ECHO10 Contact Person elements from the given UMM collection or data center"
  [c]
  (let [contact-persons (:ContactPersons c)]
    (when (seq contact-persons)
      [:ContactPersons
        (for [person contact-persons]
          [:ContactPerson
           [:FirstName (u/with-default (:FirstName person))]
           [:MiddleName (:MiddleName person)]
           [:LastName (u/with-default (:LastName person))]
           [:JobPosition (first
                          (map #(get umm-contact-person-role->echo10-contact-person-role %)
                           (:Roles person)))]])])))

(defn- generate-organization-contacts
  "Returns the ECHO10 Contact elements from the given UMM collection data centers.
  ECHO10 XML only supports one role per data center so for each UMM data center role, create
  a Contact - essentially creating a Data Center per role with the rest of the info the same"
  [c]
  (let [data-centers (if (seq (:DataCenters c))
                       (:DataCenters c)
                       [u/not-provided-data-center])]
    (for [center data-centers
          role (mapv #(get umm-data-center-role->echo10-contact-organization-role %)
                     (:Roles center))
          :let [contact-information (:ContactInformation center)]]
      [:Contact
       [:Role role]
       [:HoursOfService (:ServiceHours contact-information)]
       [:Instructions (:ContactInstruction contact-information)]
       [:OrganizationName (:ShortName center)]
       (generate-addresses contact-information)
       (generate-phones contact-information)
       (generate-emails contact-information)
       (generate-contact-persons center)])))

(defn generate-contacts
  "Returns the ECHO10 Contact elements from the given UMM"
  [c]
  [:Contacts
   (generate-organization-contacts c)
   (when (seq (:ContactPersons c))
     [:Contact
       [:Role default-echo10-contact-role]
       (generate-contact-persons c)])])
