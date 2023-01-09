(ns clojure-basics.datomic.model
  (:require [schema.core :as s])
  (:import (java.util UUID)
           (java.time LocalDate)))

(defn uuid [] (UUID/randomUUID))

(def Variant
  {:variant/id    UUID
   :variant/name  s/Str
   :variant/price Long})

(def Category
  {(s/optional-key :category/id)          UUID
   (s/optional-key :category/name)        s/Str
   (s/optional-key :category/description) s/Str})

(def Product
  {:product/id                        UUID
   (s/optional-key :product/name)     s/Str
   (s/optional-key :product/slug)     s/Str
   (s/optional-key :product/price)    BigDecimal
   (s/optional-key :product/keyword)  [s/Str]
   (s/optional-key :product/stock)    s/Int
   (s/optional-key :product/digital)  s/Bool
   (s/optional-key :product/variants) [Variant]
   (s/optional-key :product/views)    Long
   (s/optional-key :product/category) Category})


(s/defn new-product :- Product
  ([name slug price digital]
   (new-product (uuid) name slug price digital))
  ([uuid name slug price digital]
   (new-product uuid name slug price digital 0))
  ([uuid name slug price digital stock]
   {:product/id      uuid
    :product/name    name
    :product/slug    slug
    :product/price   price
    :product/stock   stock
    :product/digital digital}))


(s/defn new-category :- Category
  ([name description]
   (new-category (uuid) name description))
  ([uuid name description]
   {:category/id          uuid
    :category/name        name
    :category/description description}))


(def model1 {:id     (uuid)
             :name   "Mariana"
             :movies [{:id           (uuid)
                       :title        "Movie 1"
                       :release-date (LocalDate/of 1999 2 8)
                       :price 10M}
                      {:id           (uuid)
                       :title        "Movie 2"
                       :release-date (LocalDate/of 1979 7 23)
                       :price 20M}
                      {:id           (uuid)
                       :title        "Movie 3"
                       :release-date (LocalDate/of 1988 4 22)
                       :price 30M}]})

(def model2 {:id     (uuid)
             :name   "Luiza"
             :movies [{:id           (uuid)
                       :title        "Movie 4"
                       :release-date (LocalDate/of 1999 2 8)
                       :price 140M}
                      {:id           (uuid)
                       :title        "Movie 5"
                       :release-date (LocalDate/of 1988 4 22)
                       :price 50M}]})

(def model3 {:id     (uuid)
             :name   "Bruna"
             :movies [{:id           (uuid)
                       :title        "Movie 6"
                       :release-date (LocalDate/of 1999 2 8)
                       :price 260M}]})

(let [artists [model1 model2 model3]]
  artists)
