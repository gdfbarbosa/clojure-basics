(ns clojure-basics.schemas
  (:require [schema.core :as st])

  (:import (java.time LocalDateTime)))

; validate schema always
(st/set-fn-validation! true)

(st/defn validateLong
  [number :- Long]
  (println number))

(validateLong 1111)

(defrecord Employee [ first-name
                     last-name
                     business-unit
                     employee-id])

(let [maria-employee (->Employee "Maria"
                                 "da Silva"
                                 :ctp
                                 12345)]
  (println maria-employee))

(let [maria-employee (map->Employee {
                                     :first-name    "Maria"
                                     :last-name     "da Silva"
                                     :business-unit :ctp
                                     :employee-id   12345})]
  (println maria-employee))

(st/defschema Transaction
  {:amount   Number
   :dateTime LocalDateTime
   :description String})

(st/validate
  Transaction
  {:amount 100.0
   :dateTime (LocalDateTime/now)
   :description "Sample transaction"})

(st/validate
  Transaction
  {:amount   "100.0" ;; validation error
   :description "99.99"
   :dateTime (LocalDateTime/now)})
