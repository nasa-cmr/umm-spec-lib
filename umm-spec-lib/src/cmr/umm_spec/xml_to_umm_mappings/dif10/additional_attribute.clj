(ns cmr.umm-spec.xml-to-umm-mappings.dif10.additional-attribute
  (:require [cmr.common.xml.simple-xpath :refer [select]]
            [cmr.common.xml.parse :refer :all]
            [cmr.umm-spec.models.common :as cmn]
            [cmr.common.util :as util]
            [cmr.umm-spec.xml-to-umm-mappings.dif9.additional-attribute :as d9-aa]))

(defn xml-elem->AdditionalAttribute
  [aa-elem]
  (let [attribs {:Name (value-of aa-elem "Name")
                 :Description (value-of aa-elem "Description")
                 :DataType (value-of aa-elem "DataType")
                 :ParameterRangeBegin (value-of aa-elem "ParameterRangeBegin")
                 :ParameterRangeEnd (value-of aa-elem "ParameterRangeEnd")
                 :Value (value-of aa-elem "Value")}]
    (-> attribs
        util/remove-nil-keys
        cmn/map->AdditionalAttributeType)))

(defn xml-elem->AdditionalAttributes
  "Extracts Additional_Attributes and Extended_Metadata from DIF10 XML and includes both
  concatenated together as UMM AdditionalAttributes"
  [doc]
  (let [additional-attributes (mapv xml-elem->AdditionalAttribute
                                    (select doc "/DIF/Additional_Attributes"))
        extended-metadata (d9-aa/xml-elem->AdditionalAttributes doc)]
    (seq (into additional-attributes extended-metadata))))

