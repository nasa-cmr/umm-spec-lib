(ns cmr.umm-spec.record-generator
  "Defines functions for generating clojure records that represent the UMM."
  (:require [cmr.umm-spec.json-schema :as js]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [cmr.umm-spec.util :as spec-util]
            [cmr.umm-spec.models.collection]
            [cmr.umm-spec.models.common]))

;; Improvements
;; - generate records with fields in the same order as they are defined in the file.
;; - generate documentation list the type and restrictions


(def schema-name->namespace
  "A map of schema names to the namespace they should be placed in"
  {"umm-cmn-json-schema.json" 'cmr.umm-spec.models.common
   "umm-c-json-schema.json" 'cmr.umm-spec.models.collection})

(defn schema-type-constructor
  "Returns the map->RecordName function that can be used to construct a type defined in the JSON
  schema"
  [schema type-name]
  (let [record-ns (schema-name->namespace (:schema-name schema))]
    (-> (str (name record-ns) "/map->" (name type-name))
        symbol
        find-var
        var-get)))


(def ^:private MAX_LINE_SIZE
  "Defines the maximum line size for Clojure files."
  100)

(defn wrap-line
  "Wraps lines so they are at most line-size characters long. Returns a list of lines"
  [line-size text]
  (loop [line nil
         lines []
         [word & words] (str/split text #"\s+")]
    (cond
      ;; No more words means we are finished
      (nil? word)
      (if line
        (conj lines line)
        ;; text was empty
        lines)

      ;; First word of the line
      (nil? line)
      (recur word lines words)

      ;; Word can fit on the current line
      (<= (+ (count line) 1 (count word)) line-size)
      (recur (str line " " word) lines words)

      ;; Word can't fit on the current line
      :else
      (recur word (conj lines line) words))))

(defn- generate-comment
  "Generates a clojure comment string with indentation."
  [indent-size text]
  (let [indent+comment (str (str/join (repeat indent-size " ")) ";; ")
        max-comment-line-size (- MAX_LINE_SIZE (count indent+comment))]
    (str indent+comment
         (str/join (str "\n" indent+comment) (wrap-line max-comment-line-size text)))))

(defn- generate-doc-string
  "Generates a Clojure doc string."
  [text]
  (let [indent "  "
        max-doc-line-size (- MAX_LINE_SIZE (count indent))]
    (str indent "\""
         (->> (str/replace text "\"" "\\\"")
              (wrap-line max-doc-line-size)
              (str/join (str "\n" indent)))
         "\"")))

(def RECORD_COMMENT_INDENTATION
  "The number of spaces that should be indented before a record comment."
  3)

(defn- generate-record-field
  "Generates a single field for a clojure record"
  [{:keys [field-name description]}]
  (let [description-str (when description (generate-comment RECORD_COMMENT_INDENTATION description))
        field-str (str "   " field-name)]
    (if description-str
      (str description-str "\n" field-str)
      field-str)))

(defn- generate-record
  "Generates Clojure record"
  [{:keys [record-name fields description]}]
  (str/join
    "\n"
    (concat
      (when description
        [(generate-comment 0 description)])
      [(str "(defrecord " record-name)
       "  ["]
      [(str/join "\n\n" (map generate-record-field fields))]
      ["  ])"
       (str "(record-pretty-printer/enable-record-pretty-printing " record-name ")")])))

(defn- definition->record
  "Converts a JSON Schema definition into a record description if it's appropriate to have a record
  for it. Returns nil otherwise."
  [type-name type-def]
  (when (= "object" (:type type-def))
    (let [merged-properties (apply merge (:properties type-def) (map :properties (:oneOf type-def)))]
      {:record-name (name type-name)
       :description (:description type-def)
       :fields (for [[property-name prop-def] merged-properties]
                 {:field-name (name property-name)
                  :description (:description prop-def)})})))

(defn- generate-clojure-records
  "Generates a string containing clojure record definitions from the given schema."
  [schema]
  (let [definitions (:definitions schema)
        definitions (if (:title schema)
                      ;; The schema itself can define a top level object
                      (cons [(keyword (:title schema)) (dissoc schema :definitions)]
                            definitions)
                      definitions)
        records-strings (->> definitions
                             (map #(apply definition->record %))
                             (remove nil?)
                             (map generate-record))]
    (str/join "\n\n" records-strings)))

(defn- generate-ns-declaration
  "Generates a namespace declaration for a namespace file containing UMM records."
  [{:keys [the-ns description]}]
  (format "(ns %s\n %s\n (:require [cmr.common.dev.record-pretty-printer :as record-pretty-printer]))"
          (name the-ns)
          (generate-doc-string description)))

(defn- generated-file-warning
  "A comment placed at the top of a generated file to warn it was generated."
  [source-resource]
  (let [file-name (last (str/split (str source-resource) #"/"))]
    (format ";; WARNING: This file was generated from %s. Do not manually modify."
            file-name)))

(defn generate-clojure-records-file
  "Generates a file containing clojure records for the types defined in the UMM JSON schema."
  [{:keys [the-ns schema-resource] :as ns-def}]
  (let [schema (spec-util/load-json-resource schema-resource)
        file-name (str "src/"
                       (-> the-ns
                           name
                           (str/replace "." "/")
                           (str/replace "-" "_"))
                       ".clj")
        file-contents (str (generated-file-warning schema-resource)
                           "\n"
                           (generate-ns-declaration ns-def)
                           "\n\n"
                           (generate-clojure-records schema))]

    (.. (io/file file-name) getParentFile mkdirs)
    (spit file-name file-contents)))

(defn generate-umm-records
  "Generates all the UMM records"
  []
  (generate-clojure-records-file {:the-ns 'cmr.umm-spec.models.common
                                  :description "Defines UMM Common clojure records."
                                  :schema-resource js/umm-cmn-schema-file})

  (generate-clojure-records-file {:the-ns 'cmr.umm-spec.models.collection
                                  :description "Defines UMM-C clojure records."
                                  :schema-resource js/umm-c-schema-file}))

(comment

  (generate-umm-records)

  (generated-file-warning js/umm-c-schema-file)

  )


