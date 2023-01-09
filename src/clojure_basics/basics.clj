(ns clojure-basics.basics
  (:require [clojure.string :as str]))

;;variables
(def greeting "Hello")
(def numDouble 12.123456)
(def numLong 123456)

;;string format functions
(format "This is string %s" greeting)
(format "5 spaces and %5d" numLong)
(format "Leading zeros %012d" numLong)
(format "%-6d left justified" numLong)
(format "3 decimal plates %.3f" numDouble)

;;strings functions
(def str1 "The big frog jumping on the fox")
(str/blank? str1)
(str/ends-with? str1 "fox")
(str/capitalize str1)
(str/upper-case str1)
(str/lower-case str1)
(str/includes? str1 "frog")
(str/split str1 #" ")

;; watchers
(defn atom-x [x]
  (let [atomX (atom x)]
    (add-watch atomX :watcher
               (fn [_ _ old-state new-state]
                 (println "atom changed from " old-state " to " new-state)))
    (println "first x = " @atomX)
    (reset! atomX 10)
    (println "second x = " @atomX)
    (swap! atomX inc)
    (println "increment x" @atomX)))

(atom-x 5)

;;math
(defn math-functions []
  (let [sum (+' 1 2)]
    (println "sum is " sum)
    (println (- 10 7))
    (println (* 2 2))
    (println (/ 10 3))
    (println (mod 5 2))
    (println (inc 7))                                       ;; increment
    (println (dec 7))                                       ;; decrement
    (println (Math/abs (int -10)))
    (println (Math/cbrt 8))
    (println (Math/sqrt 81))
    (println (Math/ceil 0.5))
    (println (Math/floor 0.5))
    (println (Math/exp 1))
    (println (Math/hypot 3 3))
    (println (Math/log 10))
    (println (Math/log10 1000))
    (println (Math/max (int 1) (int 5)))
    (println (Math/min (int 1) (int 5)))
    (println (Math/pow (int 2) (int 3)))
    (println (rand-int 20))
    (println (reduce + [1 2 3]))
    (println (Math/PI))))

(math-functions)

;; loops (iterations)
(defn one-to-x
  [x]
  (let [i (atom 1)]
    (while (<= @i x)
      (do
        (println @i)
        (swap! i inc)))))

(one-to-x 5)

(defn dbl-to-x
  [x]
  (dotimes [i x]
    (println (* i 2))))

(dbl-to-x 10)
(dbl-to-x 1)
(dbl-to-x 3)

(defn triple-to-x
  [x y]
  (loop [i x]                                             ;; `i` starts with 1
    (when (<= i y)                                        ;; until i <= y
      (println (* i 3))                                   ;; triple the value of i
      (recur (+ i 1)))))                                   ;; increment i


(triple-to-x 1 5)

(defn print-list
  [& numbers]
  (doseq [x numbers]
    (println x)))

(print-list 1 2 3 4 5)


;; destructuring
(defn destruct
  []
  (let [vectorVals [1 2 3 4 5]
        [one two three & the-rest] vectorVals]
    (println one two three the-rest)))

(destruct)

(defstruct Customer :Name :Phone)

;; structs
(defn struct-map-ex
  []
  (let [customer1 (struct Customer "Guilherme" "+5531999991234")
        customer2 (struct-map Customer :Name "Mariana" :Phone "+552112312313123")]
    (println customer1)
    (println customer2)
    (println (:Name customer1))
    (println (:Name customer2))))

(struct-map-ex)

;; macros
(defmacro discount
  ([cond cmd1 cmd2]
   (list `if cond cmd1 cmd2)))

(discount (> 65 60) (println "discount") (println "full price"))

(defmacro regular-math
  [calc]
  (list (second calc) (first calc) (nth calc 2)))

(regular-math (10 mod 3))

(defmacro do-more
  [cond & body]
  (list `if cond (cons 'do body)))

(do-more (< 1 2) (println "first") (println "second") (println "third"))
