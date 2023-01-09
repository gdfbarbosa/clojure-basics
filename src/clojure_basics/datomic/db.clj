(ns clojure-basics.datomic.db
  (:require [clojure-basics.datomic.model :as model]
            [clojure.core]
            [clojure.walk :as walk]
            [datomic.api :as d]
            [schema.core :as s])
  (:import (java.util UUID)))

(def product-schema [
                     ; products
                     {:db/ident       :product/id
                      :db/valueType   :db.type/uuid
                      :db/unique      :db.unique/identity
                      :db/cardinality :db.cardinality/one}
                     {:db/ident       :product/name
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Name of the product in catalog"}
                     {:db/ident       :product/slug
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Product slug"}
                     {:db/ident       :product/price
                      :db/valueType   :db.type/bigdec
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Price of a product"}
                     {:db/ident       :product/category
                      :db/valueType   :db.type/ref
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Category of a product"}
                     {:db/ident       :product/stock
                      :db/valueType   :db.type/long
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Stock of a product"}
                     {:db/ident       :product/digital
                      :db/valueType   :db.type/boolean
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Specify if is digital product"}
                     {:db/ident       :product/variants
                      :db/valueType   :db.type/ref
                      :db/isComponent true
                      :db/cardinality :db.cardinality/many
                      :db/doc         "Variants of a product"}
                     {:db/ident       :product/views
                      :db/valueType   :db.type/long
                      :db/cardinality :db.cardinality/one
                      :db/noHistory   true
                      :db/doc         "Number of views (sharded counter)?"}])


(def category-schema [
                      ;category
                      {:db/ident       :category/id
                       :db/valueType   :db.type/uuid
                       :db/unique      :db.unique/identity
                       :db/cardinality :db.cardinality/one}
                      {:db/ident       :category/name
                       :db/valueType   :db.type/string
                       :db/cardinality :db.cardinality/one}
                      {:db/ident       :category/description
                       :db/valueType   :db.type/string
                       :db/cardinality :db.cardinality/one}])


(def variant-schema [
                     ;variant
                     {:db/ident       :variant/id
                      :db/valueType   :db.type/uuid
                      :db/unique      :db.unique/identity
                      :db/cardinality :db.cardinality/one}
                     {:db/ident       :variant/name
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one}
                     {:db/ident       :variant/price
                      :db/valueType   :db.type/long
                      :db/cardinality :db.cardinality/one}])


(def categories [(model/new-category "Category A" "Description A")
                 (model/new-category "Category B" "Description B")])

(def products [(model/new-product "Product A" "/product_a" 100.0M false)
               (model/new-product "Product B" "/product_b" 200.0M true)
               (model/new-product "Product C" "/product_c" 300.0M false)])

; datalog rules
(def rules
  '[
    [(products-in-categories ?category-name ?product)
     [?category :category/name ?category-name]
     [?product :product/category ?category]]])


; global properties
; local/dev
;(def db-uri {:server-type :dev-local :system "dev"})
;(def datomicClient (d/client db-uri))
;(defn list-databases []  (d/list-databases datomicClient db-uri))

; functions
(defn create-database [db-uri] (d/create-database db-uri))
(defn delete-database [db-uri] (d/delete-database db-uri))
(defn open-connection [db-uri] (d/connect db-uri))

; initialize schema
(defn initialize-schema [db-uri schema]
  (d/transact (open-connection db-uri) schema))

(defn load-categories [db-uri]
  (d/transact (open-connection db-uri) categories))

(defn load-products [db-uri]
  (d/transact (open-connection db-uri) products))

; utils
(defn dissoc-db-id [entity]
  (if (map? entity)
    (dissoc entity :db/id)
    entity))

(defn datomic-to-entity [entities]
  (walk/prewalk dissoc-db-id entities))

; queries
(defn find-all-categories [db-uri] (d/q '[:find [(pull ?e [*]) ...]
                                           :where [?e :category/name ?category]] (d/db (open-connection db-uri))))
(defn find-all-products [db-uri] (d/q '[:find [(pull ?e [*]) ...]
                                         :where [?e :product/name ?product]] (d/db (open-connection db-uri))))
(defn find-products-by-category [db-uri category-name]
  (d/q '[:find [(pull ?product [:product/name :product/slug {:product/category [:category/name]}]) ...]
         :in $ ?category-name
         :where [?category :category/name ?category-name]
         [?product :product/category ?category]] (d/db (open-connection db-uri)) category-name))


(defn find-products-by-category-backward-navigation [db-uri category-name]
  ;; backwards navigation
  (d/q '[:find [(pull ?category [:category/name {:product/_category [:product/name]}]) ...]
         :in $ ?name
         :where [?category :category/name ?name]] (d/db (open-connection db-uri)) category-name))

(s/defn find-product-by-id [id :- s/Uuid db-uri :- s/Str] :- model/Product
  (d/q '[:find [pull ?product [*]] .
         :in $ ?id
         :where [?product :product/id ?id]] (d/db (open-connection db-uri)) id))



(defn find-category-id-by-name [db-uri category-name]
  (first (d/q '[:find [(pull ?category [*]) ...]
                :in $ ?category-name
                :where [?category :category/name ?category-name]] (d/db (open-connection db-uri)) category-name)))

(defn find-product-id-by-name [db-uri product-name]
  (first (d/q '[:find [(pull ?product [*]) ...]
                :in $ ?product-name
                :where [?product :product/name ?product-name]] (d/db (open-connection db-uri)) product-name)))

(s/defn find-all-products-entities :- [model/Product]
  [db-uri]
  (datomic-to-entity (d/q '[:find [(pull ?e [* {:product/category [*]}]) ...]
                            :where [?e :product/name ?product]] (d/db (open-connection db-uri)))))



(s/defn find-all-products-in-categories :- [model/Product]
  [db-uri, categories :- [s/Str]]
  (datomic-to-entity
    (d/q '[:find [(pull ?product [* {:product/category [*]}]) ...]
           :in $ % [?category-name ...]
           :where
           (products-in-categories ?category-name ?product)] (d/db (open-connection db-uri)) rules categories)))

(s/defn find-all-products-in-categories-if-digital :- [model/Product]
  [db-uri :- s/Str categories :- [s/Str] digital :- s/Bool]
  (datomic-to-entity
    (d/q '[:find [(pull ?product [* {:product/category [*]}]) ...]
           :in $ % [?category-name ...] ?digital
           :where
           (products-in-categories ?category-name ?product)
           [?product :product/digital ?digital]] (d/db (open-connection db-uri)) rules categories digital)))

; updates
(defn associate-category-to-product [db-uri category-name product-name]
  (let [category (find-category-id-by-name db-uri category-name)
        product (find-product-id-by-name db-uri product-name)]
    (d/transact (open-connection db-uri) [
                                           [:db/add [:product/id (:product/id product)] ; lookup-ref
                                            :product/category
                                            [:category/id (:category/id category)]]]))) ; lookup-ref



(s/defn upsert-products!
  [conn, products]
  (d/transact conn products))

(s/defn update-product!
  [db-uri :- s/Str productId :- UUID oldValue :- s/Int newValue :- s/Int]
  (d/transact (open-connection db-uri)
              [
               [:db/cas [:product/id productId] :product/price oldValue newValue]]))



(s/defn create-new-product-with-few-attributes!
  [db-uri :- s/Str name :- s/Str slug :- s/Str price :- s/Num]
  (d/transact (open-connection db-uri) [
                                         [:db/add "tempid" :product/name name]
                                         [:db/add "tempid" :product/slug slug]
                                         [:db/add "tempid" :product/price price]]))


(s/defn add-variant! [db-uri :- s/Str productId :- UUID name :- s/Str price]
  (d/transact (open-connection db-uri) [
                                         {:variant/id    (model/uuid)
                                          :variant/name  name
                                          :variant/price price
                                          :db/id         "variant-id"}
                                         {:product/id       productId
                                          :product/variants "variant-id"}]))



(defn count-all-products [db-uri]
  (first (first (d/q '[:find (count ?productName)
                       :where [?product :product/name ?productName]] (d/db (open-connection db-uri))))))

(s/defn remove-entity! [db-uri :- s/Str product-id :- UUID]
  (d/transact (open-connection db-uri) [[:db/retractEntity [:product/id product-id]]]))

(defn find-all-variants [db-uri]
  (d/q '[:find [(pull ?variant [*]) ...]
         :where [?variant :variant/name ?variantName]] (d/db (open-connection db-uri))))


(s/defn find-views-by-product-id [db-uri :- s/Str product-id :- UUID]
  (or (first (first (d/q '[:find ?views
                           :in $ ?id
                           :where [?product :product/id ?id]
                           [?product :product/views ?views]] (d/db (open-connection db-uri)) product-id))) 0))

(s/defn increment-views-by-product-id! [db-uri :- s/Str product-id :- UUID]
  (let [current-views (find-views-by-product-id db-uri product-id)
        updated-views (inc current-views)]
    (d/transact (open-connection db-uri)
                [
                 {:product/id    product-id
                  :product/views updated-views}])))
