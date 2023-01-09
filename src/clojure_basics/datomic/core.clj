(ns clojure-basics.datomic.core
  (:require [clojure-basics.datomic.db :as db]))

(defn -main
  "Just a clojure playground for learning"
  [& args]
  (doall
    (println "Just a clojure playground for learning")
    (println "Args: " args)))

(-main "parameter1" "parameter2" 10)

; setup
(def db-uri "datomic:dev://localhost:4334/ecommerce")
(db/delete-database db-uri)
(db/create-database db-uri)

; schema
(db/initialize-schema db-uri db/product-schema)
(db/initialize-schema db-uri db/category-schema)
(db/initialize-schema db-uri db/variant-schema)
(db/load-categories db-uri)
(db/load-products db-uri)

(db/associate-category-to-product db-uri "Category A" "Product A")
(db/associate-category-to-product db-uri "Category B" "Product B")
(db/associate-category-to-product db-uri "Category A" "Product C")

; queries
(db/find-all-categories db-uri)
(db/find-all-products db-uri)
(db/find-category-id-by-name db-uri "Category A")
(db/find-products-by-category db-uri "Category A")
(db/find-products-by-category-backward-navigation db-uri "Category A")

(db/find-all-products-entities db-uri)
(def products (db/find-all-products-entities db-uri))
(println products)
(def aProduct (first products))
(println aProduct)
(println (get (:product/variants aProduct) 2))
(println (:product/id aProduct))

(db/find-all-products-in-categories db-uri ["Category A"])
(db/find-all-products-in-categories db-uri ["Category A" "Category B"])
(db/find-all-products-in-categories db-uri ["Category A" "Category B" "Category C"])
(db/find-all-products-in-categories db-uri ["Category C"])

(db/find-all-products-in-categories-if-digital db-uri ["Category B"] false)
(db/find-all-products-in-categories-if-digital db-uri ["Category B"] true)

(db/update-product! db-uri (:product/id aProduct) 101M 102M)
(db/create-new-product-with-few-attributes! db-uri "simple" "/simple_slug" 333M)

(db/add-variant! db-uri (:product/id aProduct) "Macbook Pro 16' SSD 512 GB" 339)
(db/add-variant! db-uri (:product/id aProduct) "Macbook Pro 22' SSD 1 TB" 439)

(db/count-all-products db-uri)
(db/find-all-variants db-uri)
(db/remove-entity! db-uri (:product/id aProduct))

(db/find-views-by-product-id db-uri (:product/id aProduct))
(dotimes [_ 10] (db/increment-views-by-product-id! db-uri (:product/id aProduct)))
(db/increment-views-by-product-id! db-uri (:product/id aProduct))

(db/find-all-products-entities db-uri)

(def product-uuid (parse-uuid "ae1d7ef5-6ff6-4b21-afc1-9b7a6f95d670"))
#_(def product-uuid-2 (parse-uuid "ae1d7ef5-6ff6-4b21-afc1-9b7a6f95d671"))
(def product-c (db/find-product-by-id product-uuid db-uri))
(:product/id product-c)
(def conn (db/open-connection db-uri))

(db/find-product-by-id product-uuid db-uri)

; transactions
(defn update-price []
  (println "Updating price...")
  (let [product {:product/id product-uuid :product/price 8888M}]
    (Thread/sleep 3000)
    (db/upsert-products! conn [product])))

(update-price)

(defn update-slug []
  (println "Updating slug...")
  ; this is updating datom by datom (which makes sense for datomic)
  (let [product {:product/id product-uuid :product/slug "/soccer-ball-3"}]
    (Thread/sleep 3000)
    (db/upsert-products! conn [product])))

(update-slug)

(defn schedule-jobs [jobs]
  (let [futures (mapv #(future (%)) jobs)]
    (println (map deref futures))
    (println "scheduler done!")
    (println (db/find-product-by-id product-uuid db-uri))))

(schedule-jobs [update-price update-slug])
