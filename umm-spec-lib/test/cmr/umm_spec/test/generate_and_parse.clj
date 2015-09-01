(ns cmr.umm-spec.test.generate-and-parse
  "Tests roundtrip XML generation from a Clojure record and parsing it. Ensures that the same data
  is returned."
  (:require [clojure.test :refer :all]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test :refer [for-all]]
            [cmr.common.util :refer [update-in-each]]
            [cmr.common.test.test-check-ext :as ext :refer [defspec]]
            [cmr.umm-spec.test.expected-conversion :as expected-conversion]
            [cmr.umm-spec.core :as core]
            [cmr.common.util :refer [are2]]
            [cmr.umm-spec.test.umm-generators :as umm-gen]))

(defn xml-round-trip
  "Returns record after being converted to XML and back to UMM through
  the given to-xml and to-umm mappings."
  [record format]
  (core/parse-metadata :collection format (core/generate-metadata :collection format record)))

(deftest roundtrip-gen-parse
  (are2 [metadata-format]
    (= (expected-conversion/convert expected-conversion/example-record metadata-format)
       (xml-round-trip expected-conversion/example-record metadata-format))

    "echo10"
    :echo10

    "dif9"
    :dif

    "dif10"
    :dif10

    "iso-smap"
    :iso-smap

    "ISO19115-2"
    :iso19115))

(deftest generate-valid-xml
  (testing "valid XML is generated for each format"
    (are [fmt]
        (->> expected-conversion/example-record
             (core/generate-metadata :collection fmt)
             (core/validate-xml :collection fmt)
             empty?)
      :echo10
      :dif
      :dif10
      :iso-smap
      :iso19115)))

(defn fixup-generated-collection
  [umm-coll]
  (-> umm-coll
      ;; TODO: right now, the TemporalExtents roundtrip conversion does not work with the generator
      ;; generated umm record. We exclude it from the comparison for now. This should be addressed
      ;; within CMR-1933.
      (assoc :TemporalExtents nil)
      ;; TODO: Platforms/Instruments is not ready yet, but it is generated.
      (update-in-each [:Platforms] assoc :Instruments nil)))

(defspec roundtrip-generator-gen-parse 100
  (for-all [umm-record umm-gen/umm-c-generator
            metadata-format (gen/elements [:echo10 :dif :dif10 :iso-smap :iso19115])]
    (let [expected (fixup-generated-collection (expected-conversion/convert umm-record metadata-format))
          actual   (fixup-generated-collection (xml-round-trip umm-record metadata-format))]
      (is (= expected actual)))))
