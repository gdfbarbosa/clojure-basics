(ns clojure-basics.money
  (:require [clojure.spec.alpha :as s]))

(use 'clojure.java.io)

;; domain-model finance
(def currencies {
                 :usd { :divisor 100 :code "USD" :sign "$"
                       :desc "US Dollars"}
                 :brl { :divisor 100 :code "BRL" :sign "R$"
                       :desc "Brazilian Real"}
                 :ukg { :divisor (* 17 29) :code "UKG" :sign "ʛ"
                       :desc "Galleons of the United Kingdom"}})

(def default-currency (:brl currencies))

(defn make-money
  "takes an amount and a currency, creating a Money entity"
  ([]                   {:amount 0
                         :currency default-currency})
  ([amount]             {:amount amount
                         :currency default-currency})
  ([amount currency] {:amount amount
                      :currency currency}))

(defn fetch-account
  []
  {:account/owners {:person/first-name "Guilherme" :person/last-name "Figueiredo"}})

(fetch-account)

;; validations
(s/def :money/amount int?)
(s/def :currency/divisor int?)
(s/def :currency/sign string?)
(s/def :currency/desc string?)
(s/def :currency/code (and string? #{"USD" "BRL"}))

(s/def :currency/sign (s/nilable string?))
(s/def :currency/desc (s/nilable string?))

(s/def :finance/currency (s/keys :req-un [:currency/divisor
                                          :currency/code]
                                 :opt-un [:currency/sign
                                          :currency/desc]))

(s/def :finance/money (s/keys :req-un [:money/amount
                                       :finance/currency]))

(s/valid? :finance/currency (:usd currencies))

(s/valid? :finance/money (make-money 1234 (:brl currencies)))

;; error
;;(s/explain :finance/money {:amount "a", :currency (:brl currencies)})
