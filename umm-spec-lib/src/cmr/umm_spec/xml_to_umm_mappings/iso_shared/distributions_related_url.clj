(ns cmr.umm-spec.xml-to-umm-mappings.iso-shared.distributions-related-url
  "Functions for parsing UMM related-url records out of ISO XML documents."
  (:require
   [clojure.string :as str]
   [cmr.common.xml.parse :refer :all]
   [cmr.common.xml.simple-xpath :refer :all]
   [cmr.umm-spec.iso19115-2-util :refer :all]
   [cmr.umm-spec.url :as url]))

(defn get-index-or-nil
 "Get the index of the key in the description. Return nil if the key does not
 exist in the description"
 [description key]
 (let [index (.indexOf description key)]
  (when (>= index 0)
   index)))

(defn parse-url-types-from-description
 "In ISO, since there are not separate fields for the types, they are put in the
 description in the format 'Description: X URLContentType: Y Type: Z Subtype: A'
 Parse all available elements out from the description string."
 [description]
 (when description
  (let [description-index (get-index-or-nil description "Description:")
        url-content-type-index (get-index-or-nil description "URLContentType:")
        type-index (get-index-or-nil description " Type:")
        subtype-index (get-index-or-nil description "Subtype:")]
   (if (and (nil? description-index)(nil? url-content-type-index)
            (nil? type-index) (nil? subtype-index))
    {:Description description} ; Description not formatted like above, so just description
    {:Description (when description-index
                   (let [desc (subs description
                               description-index
                               (or url-content-type-index type-index subtype-index (count description)))]
                    (str/trim (subs desc (inc (.indexOf desc ":"))))))
     :URLContentType (when url-content-type-index
                      (let [content-type (subs description
                                          url-content-type-index
                                          (or type-index subtype-index (count description)))]
                        (str/trim (subs content-type (inc (.indexOf content-type ":"))))))
     :Type (when type-index
            (let [type (subs description
                        type-index
                        (or subtype-index (count description)))]
              (str/trim (subs type (inc (.indexOf type ":"))))))
     :Subtype (when subtype-index
               (let [subtype (subs description subtype-index)]
                 (str/trim (subs subtype (inc (.indexOf subtype ":"))))))}))))

(defn parse-service-urls
 "Parse service URLs from service location. These are most likely dups of the
 distribution urls, but may contain additional type info."
 [doc sanitize? service-url-path]
 (for [service (select doc service-url-path)
       :let [local-name (value-of service "srv:serviceType/gco:LocalName")]
       :when (str/includes? local-name "RelatedURL")
       :let [url-types (parse-url-types-from-description local-name)
             url (first (select service
                          (str "srv:containsOperations/srv:SV_OperationMetadata/"
                               "srv:connectPoint/gmd:CI_OnlineResource")))
             url-link (value-of url "gmd:linkage/gmd:URL")]]
   (merge url-types
     {:URL (when url-link (url/format-url url-link sanitize?))
      :Description (char-string-value url "gmd:description")})))

(defn parse-online-urls
  "Parse ISO online resource urls"
  [doc sanitize? service-urls distributor-online-url-xpath]
  (for [url (select doc distributor-online-url-xpath)
        :let [name (char-string-value url "gmd:name")
              code (value-of url "gmd:function/gmd:CI_OnlineFunctionCode")
              url-link (value-of url "gmd:linkage/gmd:URL")
              url-link (when url-link (url/format-url url-link sanitize?))
              opendap-type (when (= code "GET DATA : OPENDAP DATA (DODS)")
                            "GET SERVICE")
              types-and-desc (parse-url-types-from-description
                              (char-string-value url "gmd:description"))
              service-url (some #(= url-link (:URL %)) service-urls)]]
   {:URL url-link
    :URLContentType "DistributionURL"
    :Type (or opendap-type (:Type types-and-desc) (:Type service-url) "GET DATA")
    :Subtype (if opendap-type
              "OPENDAP DATA (DODS)"
              (or (:Subtype types-and-desc) (:Subtype service-url)))
    :Description (:Description types-and-desc)}))

(defn parse-online-and-service-urls
 "Parse online and service urls. Service urls may be a dup of distribution urls,
 but may contain additional needed type information. Filter out dup service URLs."
 [doc sanitize? service-url-path distributor-online-url-xpath ]
 (let [service-urls (parse-service-urls doc sanitize? service-url-path)
       online-urls (parse-online-urls doc sanitize? service-urls distributor-online-url-xpath)
       online-url-urls (set (map :URL online-urls))
       service-urls (seq (remove #(contains? online-url-urls (:URL %)) service-urls))]
  (concat
   online-urls
   service-urls)))

(defn parse-browse-graphics
  "Parse browse graphic urls"
  [doc sanitize? browse-graphic-xpath]
  (for [url (select doc browse-graphic-xpath)
        ;; We retrieve browse url from two different places. This might change depending on the
        ;; outcome of ECSE-129.
        :let [browse-url (or (value-of url "gmd:fileName/gmx:FileName/@src")
                             (value-of url "gmd:fileName/gco:CharacterString"))
              types-and-desc (parse-url-types-from-description
                              (char-string-value url "gmd:fileDescription"))]]
     {:URL (when browse-url (url/format-url browse-url sanitize?))
      :Description (:Description types-and-desc)
      :URLContentType "VisualizationURL"
      :Type "GET RELATED VISUALIZATION"
      :Subtype (:Subtype types-and-desc)}))

