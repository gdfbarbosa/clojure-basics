(ns clojure-basics.functions
  (:require [schema.core :as s :include-macros true]))

(s/defn convert-celsius-to-fahrenheit
  [c :- s/Int]
  (+ 32 (* c (/ 9 5))))

(convert-celsius-to-fahrenheit 0.0)

;; functions
(defn say-hello
  "Say hello to someone by their name"
  [name]
  (println "Hello again" name))

(say-hello "Gui")

(defn get-sum
  "Calculate the sum between x and y"
  [x y]
  (+ x y))

(defn get-sum-more
  "Calculate the sum between x and y (optionally z also)"
  ([x y z]
   (+ x y z))
  ([x y]
   (+ x y)))

(defn hello-to-you
  [name]
  (str "Hello " name))

(defn hello-all
  [& names]
  (map hello-to-you names))

(get-sum 2 3)
(get-sum-more 2 3 4)
(get-sum-more 2 3)
(hello-all "gui" "barbosa" "figueiredo")
;
;; flow control
(defn can-vote
  "Check if a person can vote"
  [age]
  (if (>= age 16)
    (println "you can vote")
    (println "sorry, you can not vote underage")))

(can-vote 15)
(can-vote 17)
(can-vote 16)

(defn can-do-more
  [age]
  (if (>= age 16)
    (do (println "you can drive")
        (println "you can vote"))))

(can-do-more 16)

(defn when-x
  [tof]
  (if tof
    (println "1st thing")
    (println "2nd thing")))
;
(when-x (>= 11 15))

(defn what-grade
  [age]
  (cond
    (< age 1) (println "nursery")
    (and (>= age 1) (<= age 5)) (println "childish")
    (and (>= age 6) (<= age 15)) (println "fundamental")
    (and (>= age 16) (<= age 18)) (println "go to grade" (- age 15))
    :else (println "go to college")))

(what-grade 0.5)
(what-grade 2)
(what-grade 4)
(what-grade 6)
(what-grade 16)
(what-grade 17)
(what-grade 18)
(what-grade 19)

;; anonymous function
(map (fn [x] (* x x)) (range 1 10))                       ;; extensive declaration
(map #(* % 3) (range 1 10))                               ;; compact declaration

(#(* %1 %2 %3) 2 3 4)                                     ;; strange, but it's okay

(defn custom-multiplier
  [multiply-by]
  #(* % multiply-by))

(def multiply-by-3
  (custom-multiplier 3))

(multiply-by-3 5)

(defn fibonacci [x]
  (if (or (= x 0) (= x 1))
    x
    (+ (fibonacci (- x 1)) (fibonacci (- x 2)))))

(map fibonacci (range 14))

; sample recursive function
(loop [sum 0
       cnt 2]
  ; If count reaches 0 then exit the loop and return sum
  (if (= cnt 0)
    sum
    ; Otherwise add count to sum, decrease count and
    ; use recur to feed the new values back into the loop
    (recur (+ cnt sum) (dec cnt))))

(defn my-counting
  ([elements]
   (my-counting 0 elements))
  ([total elements]
   (if (seq elements)
     (my-counting (inc total) (next elements))
     total)))

; for-loop
(defn my-counting2
  [list]
  (loop [total 0
         elements list]
    (if (seq elements)
      (recur (inc total) (next elements))
      total)))

(my-counting ["a" "b" "c" "d" "e"])
(my-counting [1 2 3])

(my-counting2 ["a" "b" "c" "d" "e"])
(my-counting2 [1 2 3])

(for [x [0 1 2 3 4 5]
      :let [y (* x 3)]
      :when (odd? y)]
  y)

(def digits [1 5 8])

(for [x1 digits
      x2 digits]
  (* x1 x2))
