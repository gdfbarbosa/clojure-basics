(ns clojure-basics.io
  (:require
    [clojure.java.io :as io]
    [schema.core :as s]))

;; I/O
(s/defn write-to-file
        [file :- s/Str
         text :- s/Str]
        (io/delete-file file)
        (with-open [writer (io/writer file)]
          (.write writer (str text))))

(defn read-from-file
  [file]
  (try
    (println (slurp file))
    (catch Exception e (println "Error: " (.getMessage e)))))

(defn append-to-file
  [file text]
  (with-open [writer (io/writer file :append true)]
    (.write writer (str text))))

(defn read-line-from-file
  [file]
  (with-open [rdr (io/reader file)]
    (doseq [line (line-seq rdr)]
      (println line))))

(def fileName "/Users/guilherme.figueiredo/temp/test.txt")
(write-to-file fileName "some text")
(read-from-file fileName)
(append-to-file fileName "\n\n another text \n")
(read-line-from-file fileName)
