(ns clojure-basics.exceptions)

(defn some-exception-thrown
  []
  (throw (ex-info "Some exception with a map" {:name "Guilherme" :lastName "Barbosa" :error "Limit exceeded"})))

(defn test-exception []
  (try (some-exception-thrown)
       (catch Exception e (prn "caught" e))))


(test-exception)
