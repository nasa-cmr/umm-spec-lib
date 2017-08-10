(ns cmr.umm-spec.test.iso-shared
  "ISO 19115 specific expected conversion functionality"
  (:require
    [clojure.string :as string]
    [cmr.common.util :as util :refer [update-in-each]]
    [cmr.umm-spec.iso19115-2-util :as iso-util]
    [cmr.umm-spec.models.umm-collection-models :as umm-c]
    [cmr.umm-spec.models.umm-common-models :as cmn]
    [cmr.umm-spec.util :as su]
    [cmr.umm-spec.xml-to-umm-mappings.iso19115-2.data-contact :as xml-to-umm-data-contact]
    [cmr.umm-spec.xml-to-umm-mappings.iso-shared.iso-topic-categories :as iso-topic-categories])
  (:use
   [cmr.umm-spec.models.umm-collection-models]
   [cmr.umm-spec.models.umm-common-models]))

(defn- create-contact-person
  "Creates a contact person given the info of a creator, editor and publisher"
  [person]
  (when person
    (let [person-names (xml-to-umm-data-contact/parse-individual-name person nil)]
      (cmn/map->ContactPersonType
        {:Roles ["Technical Contact"]
         :FirstName (:FirstName person-names)
         :MiddleName (:MiddleName person-names)
         :LastName (:LastName person-names)}))))

(defn update-contact-persons-from-collection-citation
  "CollectionCitation introduces additional contact persons from creator, editor and publisher.
   They need to be added to the ContactPersons in the expected umm after converting the umm
   to the xml and back to umm.  Returns the updated ContactPersons."
  [contact-persons collection-citation]
  (let [{:keys [Creator Editor Publisher]} collection-citation
        creator-contact-person (create-contact-person Creator)
        editor-contact-person (when Editor
                                (assoc (create-contact-person Editor) :NonDataCenterAffiliation "editor"))
        publisher-contact-person (create-contact-person Publisher)]
    (remove nil?  (conj contact-persons
                        (util/remove-nil-keys creator-contact-person)
                        (util/remove-nil-keys editor-contact-person)
                        (util/remove-nil-keys publisher-contact-person)))))

(defn trim-collection-citation
  "Returns CollectionCitation with Creator, Editor, Publisher and ReleasePlace fields trimmed."
  [collection-citation]
  (let [{:keys [Creator Editor Publisher ReleasePlace]} collection-citation]
    (util/remove-nil-keys
      (assoc collection-citation
             :Creator (when Creator (string/trim Creator))
             :Editor (when Editor (string/trim Editor))
             :Publisher (when Publisher (string/trim Publisher))
             :ReleasePlace (when ReleasePlace (string/trim ReleasePlace))))))

(defn split-temporals
  "Returns a seq of temporal extents with a new extent for each value under key
  k (e.g. :RangeDateTimes) in each source temporal extent."
  [k temporal-extents]
  (reduce (fn [result extent]
            (if-let [values (get extent k)]
              (concat result (map #(assoc extent k [%])
                                  values))
              (conj (vec result) extent)))
          []
          temporal-extents))

(defn sort-by-date-type-iso
  "Returns temporal extent records to match the order in which they are generated in ISO XML."
  [extents]
  (let [ranges (filter :RangeDateTimes extents)
        singles (filter :SingleDateTimes extents)]
    (seq (concat ranges singles))))

(defn fixup-iso-ends-at-present
  "Updates temporal extents to be true only when they have both :EndsAtPresentFlag = true AND values
  in RangeDateTimes, otherwise nil."
  [temporal-extents]
  (for [extent temporal-extents]
    (let [ends-at-present (:EndsAtPresentFlag extent)
          rdts (seq (:RangeDateTimes extent))]
      (-> extent
          (update-in-each [:RangeDateTimes]
                          update-in [:EndingDateTime] (fn [x]
                                                        (when-not ends-at-present
                                                          x)))
          (assoc :EndsAtPresentFlag
                 (boolean (and rdts ends-at-present)))))))

(defn expected-iso-topic-categories
  "Update ISOTopicCategories values to a default value if it's not one of the specified values."
  [categories]
  (->> categories
       (map iso-topic-categories/umm->xml-iso-topic-category-map)
       (map iso-topic-categories/xml->umm-iso-topic-category-map)
       (remove nil?)
       seq))
