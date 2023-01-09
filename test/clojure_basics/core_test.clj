(ns clojure-basics.core-test
  (:require [clojure-basics.money :refer :all :as money]
            [clojure-basics.functions :refer :all :as f]
            [clojure.test :refer :all :as t]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as p]
            [schema.test :as st]))

(use-fixtures :once st/validate-schemas)

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (println "end of a-test")))



(deftest b-test
  (testing "SUCCESS"
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (is (= 1 1))
    (println "end of b-test")))



(deftest c-test
  (testing "SUCCESS"
    (is (= 4 4))
    (is (= 4 4))
    (is (= 4 4))
    (is (= 4 4))
    (is (= 4 4))
    (is (= 4 4))
    (is (= 4 4))
    (is (= 4 4))
    (println "end of c-test")))



(deftest money-test
  (testing "My first test"
    (is (= (:amount (money/make-money 100)) 100))
    (is (= (:amount (money/make-money 100)) 100))
    (is (= (:amount (money/make-money 102)) 102))
    (println "My first test")))


(deftest convert-celsius-to-fahrenheit-test
  "Convert Celsius to Fahrenheit"
  (testing "First test c -> f"
    (is (= 32 (f/convert-celsius-to-fahrenheit 0)))))

(defn sum
  [& numbers]
  (apply + numbers))

(deftest sum-test
  (let [input [1 2 3 4 5 6 7 8 9 10 11 12 13 14 15]]
    (is (= (apply sum input)
           (apply sum (reverse input))))))

(deftest sum-improved-test
  (testing
    (let [ints (gen/generate (gen/vector gen/small-integer))]
      (println "\n*************************")
      (println ints)
      (println "\n*************************")
      (is (= (apply sum ints)
             (apply sum (reverse ints)))))))


(defspec addition-is-cumulative
  (p/for-all [ints (gen/vector gen/small-integer)]
    (println "\n*************************")
    (println ints)
    (println "\n*************************")
    (is (= (apply sum ints)
           (apply sum (reverse ints))))))


(meta addition-is-cumulative)

(addition-is-cumulative)

;; run all tests
(t/run-tests)
(t/run-all-tests)

;; run a specific set of tests
(t/test-vars [#'sum-improved-test])

(gen/generate (gen/vector gen/small-integer))

(gen/generate (gen/fmap #(conj % 1) (gen/vector gen/large-integer)))
