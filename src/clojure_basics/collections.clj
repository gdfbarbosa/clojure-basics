(ns clojure-basics.collections)

;; lists in clojure, 0-indexed
(let [aList (list 1 2 3)]
  (println aList)
  (println (nth aList 0))
  (println (cons 97 aList))
  (println (cons 98 aList))
  (println (cons 99 aList)))

;; sets (no index, get refers to the element)
(let [aSet (set '(1 2 2 3 3 3 4 4 4 4 4 4 4 5 5 5 5))
      newSet (set '(3 99 102))]
  (println "aSet" aSet)
  (println "newSet" newSet)
  (println (contains? aSet 99))
  (println (conj aSet 99))
  (println (disj aSet newSet))
  (println (disj newSet 99 102 3))
  (println (get (set '(3 99 102)) 1)))

;; vectors (like an array, 0-indexed)
(let [aVector (vector 10 20 30 40 "other" 11.22)]
  (println (get aVector 5))
  (println (subvec aVector 2 4)))                           ;; excludes end index

;; maps
(let [aMap (hash-map 1 2 3 4)
      aSortedMap (sorted-map 9 "guilherme" 5 "luiza" 2 "lara")]
  (println aMap)
  (println aSortedMap)
  (println (get aSortedMap 5))
  (contains? aSortedMap 8)
  (find aSortedMap 5)
  (merge-with + aMap aSortedMap)
  ;; more common usage
  (def barbosa {:first-name    "Guilherme"
                :last-name     "Barbosa"
                :business-unit "Platform Team"
                :employee-id   12345})
  ;; both lookups work
  (println (barbosa :first-name))
  (println (:first-name barbosa)))

;; filtering lists
(take 2 [1 2 3 4 5])                                      ;; take first two elements
(drop 3 (range 1 10))                                     ;; drop first three elements
(take-while #(< % 10) (range 1 20))                       ;; interesting function with take-while
(drop-while #(< % 10) (range 1 20))                       ;; function with drop-while version
(filter #(< % 0) (range -20 20))                          ;; filter with condition

(seq '(1))                                                  ;;=> (1)
(seq [1 2])                                                 ;;=> (1 2)
(seq "abc")                                                 ;;=> (\a \b \c)

;; Corner cases
(seq nil)                                                   ;;=> nil
(seq '())                                                   ;;=> nil
(seq [])                                                    ;;=> nil
(seq "")                                                    ;;=> nil

(every? seq ["" '() []])

(defn filter1 [x]
  (println "filter1")
  x)
(defn filter2 [x]
  (println "filter2")
  x)

; mapv is eager
(->> [1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10]
     (mapv filter1)
     (mapv filter2)
     println)

; map is lazy
(->> '(1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10)
     (map filter1)
     (map filter2)
     println)
